package com.dot.collector.api.service;

import com.dot.collector.api.domain.Profile;
import com.dot.collector.api.domain.User;
import com.dot.collector.api.repository.ProfileRepository;
import com.dot.collector.api.service.dto.ProfileDTO;
import com.dot.collector.api.service.mapper.ProfileMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dot.collector.api.domain.Profile}.
 */
@Service
@Transactional
public class ProfileService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileService.class);

    private final ProfileRepository profileRepository;

    private final ProfileMapper profileMapper;

    private final UserService userService;

    public ProfileService(ProfileRepository profileRepository, ProfileMapper profileMapper, UserService userService) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
        this.userService = userService;
    }

    /**
     * Save a profile.
     *
     * @param profileDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfileDTO save(ProfileDTO profileDTO) {
        LOG.debug("Request to save Profile : {}", profileDTO);
        Profile profile = profileMapper.toEntity(profileDTO);
        profile = profileRepository.save(profile);
        return profileMapper.toDto(profile);
    }

    /**
     * Update a profile.
     *
     * @param profileDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfileDTO update(ProfileDTO profileDTO) {
        LOG.debug("Request to update Profile : {}", profileDTO);
        Profile profile = profileMapper.toEntity(profileDTO);
        profile = profileRepository.save(profile);
        return profileMapper.toDto(profile);
    }

    /**
     * Partially update a profile.
     *
     * @param profileDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProfileDTO> partialUpdate(ProfileDTO profileDTO) {
        LOG.debug("Request to partially update Profile : {}", profileDTO);

        return profileRepository
            .findById(profileDTO.getId())
            .map(existingProfile -> {
                profileMapper.partialUpdate(existingProfile, profileDTO);

                return existingProfile;
            })
            .map(profileRepository::save)
            .map(profileMapper::toDto);
    }

    /**
     * Get all the profiles.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProfileDTO> findAll() {
        LOG.debug("Request to get all Profiles");
        return profileRepository.findAll().stream().map(profileMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one profile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfileDTO> findOne(Long id) {
        LOG.debug("Request to get Profile : {}", id);
        return profileRepository.findById(id).map(profileMapper::toDto);
    }

    /**
     * Delete the profile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Profile : {}", id);
        profileRepository.deleteById(id);
    }

    public Profile getCurrentProfile() {
        return userService
            .getUserWithAuthorities()
            .map(user ->
                profileRepository
                    .findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Profile for user " + user + " not found"))
            )
            .orElse(null);
    }
}
