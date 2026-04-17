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
import org.springframework.security.access.AccessDeniedException;
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

        Interest existing = interestRepository
            .findById(interestDTO.getId())
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", "interest", "idnotfound"));

        assertInterestUpdatableByCurrentProfile(existing);
        assertCurrentUserIsOwner(existing);

        existing.setName(interestDTO.getName());
        existing.setDescription(interestDTO.getDescription());
        existing.setPublic(interestDTO.isPublic());

        existing = interestRepository.save(existing);
        return interestMapper.toDto(existing);
    }

    private void assertInterestUpdatableByCurrentProfile(Interest interest) {
        if (interest.isSystem()) {
            throw new BadRequestAlertException("System interests cannot be updated", "interest", "systeminterest");
        }
    }

    private void assertCurrentUserIsOwner(Interest interest) {
        Profile currentProfile = profileService.getCurrentProfile();
        if (!profileInterestRepository.existsByProfile_IdAndInterest_Id(currentProfile.getId(), interest.getId())) {
            throw new AccessDeniedException("Current profile is not linked to this interest");
        }
    }

    @Transactional(readOnly = true)
    public List<InterestDTO> findAll() {
        LOG.debug("Request to get Interests for current profile and system catalog");
        Long profileId = profileService.getCurrentProfile().getId();
        return interestRepository
            .findAllLinkedToProfileOrSystem(profileId)
            .stream()
            .map(interestMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public Optional<InterestDTO> findOne(Long id) {
        LOG.debug("Request to get Interest : {} for current profile", id);
        Long profileId = profileService.getCurrentProfile().getId();
        return interestRepository.findByIdLinkedToProfileOrSystem(id, profileId).map(interestMapper::toDto);
    }

    public void delete(Long id) {
        LOG.debug("Request to delete Interest : {}", id);
        Interest existing = interestRepository
            .findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", "interest", "idnotfound"));

        if (existing.isSystem()) throw new AccessDeniedException("Current profile is not linked to this interest");
        assertCurrentUserIsOwner(existing);
        interestRepository.deleteById(id);
    }
}
