package com.nookx.api.service;

import com.nookx.api.domain.Interest;
import com.nookx.api.domain.Profile;
import com.nookx.api.domain.ProfileInterest;
import com.nookx.api.repository.InterestRepository;
import com.nookx.api.repository.ProfileInterestRepository;
import com.nookx.api.service.dto.InterestDTO;
import com.nookx.api.service.mapper.InterestMapper;
import com.nookx.api.web.rest.errors.BadRequestAlertException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Interest}.
 */
@Service
@Transactional
public class InterestService {

    private static final Logger LOG = LoggerFactory.getLogger(InterestService.class);

    private final InterestRepository interestRepository;

    private final InterestMapper interestMapper;

    private final ProfileService profileService;

    private final ProfileInterestRepository profileInterestRepository;

    public InterestService(
        InterestRepository interestRepository,
        InterestMapper interestMapper,
        ProfileService profileService,
        ProfileInterestRepository profileInterestRepository
    ) {
        this.interestRepository = interestRepository;
        this.interestMapper = interestMapper;
        this.profileService = profileService;
        this.profileInterestRepository = profileInterestRepository;
    }

    @Transactional
    public InterestDTO save(InterestDTO interestDTO) {
        LOG.debug("Request to save Interest : {}", interestDTO);
        Interest interest = interestMapper.toEntity(interestDTO);
        interest.setSystem(false);
        interest.setPublic(false);
        interest = interestRepository.save(interest);

        Profile currentProfile = profileService.getCurrentProfile();
        subscribeToInterest(interest, currentProfile);

        return interestMapper.toDto(interest);
    }

    private void subscribeToInterest(Interest interest, Profile currentProfile) {
        ProfileInterest profileInterest = new ProfileInterest();
        profileInterest.setInterest(interest);
        profileInterest.setProfile(currentProfile);
        profileInterestRepository.save(profileInterest);
    }

    /**
     * Associates existing interests with the current profile (skips pairs already linked).
     */
    public void subscribeCurrentProfileToInterests(List<Long> interestIds) {
        LOG.debug("Request to subscribe current profile to interests : {}", interestIds);
        Profile currentProfile = profileService.getCurrentProfile();
        Long profileId = currentProfile.getId();
        List<Long> distinctIds = interestIds.stream().distinct().toList();
        for (Long interestId : distinctIds) {
            Interest interest = interestRepository
                .findById(interestId)
                .orElseThrow(() -> new BadRequestAlertException("Interest not found", "interest", "idnotfound"));
            if (profileInterestRepository.existsByProfile_IdAndInterest_Id(profileId, interestId)) {
                continue;
            }
            subscribeToInterest(interest, currentProfile);
        }
    }

    /**
     * Removes links between the current profile and the given interests (ids not linked are ignored).
     */
    public void unsubscribeCurrentProfileFromInterests(List<Long> interestIds) {
        LOG.debug("Request to unsubscribe current profile from interests : {}", interestIds);
        Profile currentProfile = profileService.getCurrentProfile();
        Long profileId = currentProfile.getId();
        List<Long> distinctIds = interestIds.stream().filter(Objects::nonNull).distinct().toList();
        if (distinctIds.isEmpty()) {
            return;
        }
        profileInterestRepository.deleteByProfile_IdAndInterest_IdIn(profileId, distinctIds);
    }

    public InterestDTO update(InterestDTO interestDTO) {
        LOG.debug("Request to update Interest : {}", interestDTO);
        Interest interest = interestMapper.toEntity(interestDTO);
        interest = interestRepository.save(interest);
        return interestMapper.toDto(interest);
    }

    public Optional<InterestDTO> partialUpdate(InterestDTO interestDTO) {
        LOG.debug("Request to partially update Interest : {}", interestDTO);

        return interestRepository
            .findById(interestDTO.getId())
            .map(existingInterest -> {
                interestMapper.partialUpdate(existingInterest, interestDTO);

                return existingInterest;
            })
            .map(interestRepository::save)
            .map(interestMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<InterestDTO> findAll() {
        LOG.debug("Request to get all Interests");
        return interestRepository.findAll().stream().map(interestMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public Optional<InterestDTO> findOne(Long id) {
        LOG.debug("Request to get Interest : {}", id);
        return interestRepository.findById(id).map(interestMapper::toDto);
    }

    public void delete(Long id) {
        LOG.debug("Request to delete Interest : {}", id);
        interestRepository.deleteById(id);
    }
}
