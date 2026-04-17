package com.nookx.api.client.service;

import com.nookx.api.client.dto.*;
import com.nookx.api.config.ApplicationProperties;
import com.nookx.api.domain.CloneInformation;
import com.nookx.api.domain.Profile;
import com.nookx.api.domain.ProfileCollection;
import com.nookx.api.domain.ProfileCollectionImage;
import com.nookx.api.domain.enumeration.MegaAssetImageSize;
import com.nookx.api.repository.CloneInformationRepository;
import com.nookx.api.repository.CurrencyRepository;
import com.nookx.api.repository.InterestRepository;
import com.nookx.api.repository.ProfileCollectionImageRepository;
import com.nookx.api.repository.ProfileCollectionRepository;
import com.nookx.api.service.ProfileCollectionService;
import com.nookx.api.service.ProfileService;
import com.nookx.api.service.mapper.CurrencyMapper;
import com.nookx.api.service.mapper.InterestMapper;
import com.nookx.api.web.rest.errors.BadRequestAlertException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ClientCollectionService {

    private final ProfileService profileService;
    private final ProfileCollectionService profileCollectionService;
    private final ProfileCollectionRepository profileCollectionRepository;
    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;
    private final CloneInformationRepository cloneInformationRepository;
    private final ProfileCollectionImageRepository profileCollectionImageRepository;
    private final ApplicationProperties applicationProperties;
    private final InterestRepository interestRepository;
    private final InterestMapper interestMapper;

    public ClientCollectionService(
        ProfileService profileService,
        ProfileCollectionService profileCollectionService,
        ProfileCollectionRepository profileCollectionRepository,
        CurrencyRepository currencyRepository,
        CurrencyMapper currencyMapper,
        CloneInformationRepository cloneInformationRepository,
        ProfileCollectionImageRepository profileCollectionImageRepository,
        ApplicationProperties applicationProperties,
        InterestRepository interestRepository,
        InterestMapper interestMapper
    ) {
        this.profileService = profileService;
        this.profileCollectionService = profileCollectionService;
        this.profileCollectionRepository = profileCollectionRepository;
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
        this.cloneInformationRepository = cloneInformationRepository;
        this.profileCollectionImageRepository = profileCollectionImageRepository;
        this.applicationProperties = applicationProperties;
        this.interestRepository = interestRepository;
        this.interestMapper = interestMapper;
    }

    @Transactional(readOnly = true)
    public List<ClientCollectionLiteDTO> getUserCollections() {
        List<ClientCollectionLiteDTO> collections = profileCollectionRepository
            .findAll()
            .stream()
            .map(this::toClientCollectionLiteDTO)
            .toList();
        for (ClientCollectionLiteDTO dto : collections) {
            Long collectionId = dto.getId();
            ClientImageDTO clientImageDto = getClientImageDto(collectionId);
            dto.setImage(clientImageDto);
        }

        return collections;
    }

    private ClientImageDTO getClientImageDto(Long collectionId) {
        Optional<ProfileCollectionImage> byProfileCollectionId = profileCollectionImageRepository.findByProfileCollection_Id(collectionId);
        if (byProfileCollectionId.isPresent()) {
            ProfileCollectionImage profileCollectionImage = byProfileCollectionId.orElse(null);
            String url =
                applicationProperties.getBaseUrl() + "/api/client/assets/image/_SIZE_/" + profileCollectionImage.getAsset().getUuid();
            ClientImageDTO imageDTO = new ClientImageDTO();
            imageDTO.setOriginal(url.replace("_SIZE_", MegaAssetImageSize.ORIGINAL.suffix()));
            imageDTO.setThumb(url.replace("_SIZE_", MegaAssetImageSize.THUMB.suffix()));
            imageDTO.setMedium(url.replace("_SIZE_", MegaAssetImageSize.MEDIUM.suffix()));
            return imageDTO;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public ClientCollectionDTO getCollectionById(Long id) {
        Optional<ProfileCollection> result = profileCollectionRepository.findById(id);
        return result.map(this::toClientCollectionDTO).orElse(null);
    }

    @Transactional
    public ClientCollectionDTO cloneCollection(Long sourceCollectionId, ClientCollectionDTO dto) {
        return profileCollectionRepository
            .findById(sourceCollectionId)
            .map(sourceCollection -> {
                ClientCollectionDTO sourceDto = toClientCollectionDTO(sourceCollection);
                sourceDto.setTitle("Copy of " + sourceDto.getTitle());
                sourceDto.setId(null);
                ClientCollectionDTO clonedCollectionDto = create(sourceDto);

                ProfileCollection clonedCollection = profileCollectionRepository.findById(clonedCollectionDto.getId()).orElseThrow();

                CloneInformation info = new CloneInformation();
                info.setCloned(true);
                info.setSourceCollection(sourceCollection);
                info.setCollection(clonedCollection);
                cloneInformationRepository.save(info);

                clonedCollection.setCloneInformation(info);
                profileCollectionRepository.save(clonedCollection);

                return toClientCollectionDTO(clonedCollection);
            })
            .orElse(null);
    }

    @Transactional
    public ClientCollectionDTO create(ClientCollectionDTO dto) {
        Profile currentProfile = profileService.getCurrentProfile();

        ProfileCollection profileCollection = toProfileCollection(dto);
        profileCollection.setProfile(currentProfile);

        profileCollection = profileCollectionRepository.save(profileCollection);
        return toClientCollectionDTO(profileCollection);
    }

    private ClientCollectionDTO toClientCollectionDTO(ProfileCollection profileCollection) {
        ClientCollectionDTO dto = new ClientCollectionDTO();
        dto.setId(profileCollection.getId());
        dto.setTitle(profileCollection.getTitle());
        dto.setDescription(profileCollection.getDescription());
        dto.setCollectionType(profileCollection.getType());

        ClientCollectionSettingsDTO settings = new ClientCollectionSettingsDTO();
        if (profileCollection.getIsPublic() != null) {
            settings.setPublic(profileCollection.getIsPublic());
        }
        settings.setShowPrice(profileCollection.isShowPrice());
        settings.setShowCheckbox(profileCollection.isShowCheckbox());
        settings.setShowComment(profileCollection.isShowComment());
        settings.setCurrency(currencyMapper.toDto(profileCollection.getCurrency()));
        dto.setSettings(settings);

        if (profileCollection.getProfile() != null) {
            var profile = profileCollection.getProfile();
            if (profile.getUsername() != null) {
                dto.setCreatedBy(profile.getUsername());
            } else if (profile.getFullName() != null) {
                dto.setCreatedBy(profile.getFullName());
            }
        }

        ClientCollectionCommunityDTO communityDTO = new ClientCollectionCommunityDTO();
        communityDTO.setTotalCloned(77);
        communityDTO.setTotalComments(88);
        communityDTO.setTotalStars(99);

        CloneInformation cloneInformation = profileCollection.getCloneInformation();
        if (cloneInformation != null) {
            ClientCloneInformationDTO clone = getClientCloneInformationDTO(cloneInformation);
            communityDTO.setClone(clone);
        }
        dto.setCommunity(communityDTO);

        ClientImageDTO clientImageDto = getClientImageDto(profileCollection.getId());
        dto.setImage(clientImageDto);

        dto.setInterest(toClientInterestDto(profileCollection));

        return dto;
    }

    private ClientInterestDTO toClientInterestDto(ProfileCollection profileCollection) {
        if (profileCollection.getInterest() == null) {
            return null;
        }
        return interestMapper.toDto(profileCollection.getInterest());
    }

    private static ClientCloneInformationDTO getClientCloneInformationDTO(CloneInformation cloneInformation) {
        ClientCloneInformationDTO clone = new ClientCloneInformationDTO();
        clone.setCloned(cloneInformation.getCloned());
        clone.setSourceCollectionId(cloneInformation.getSourceCollection().getId());
        clone.setSourceUserId(cloneInformation.getSourceCollection().getProfile().getId());
        clone.setSourceUsername(cloneInformation.getSourceCollection().getProfile().getUsername());
        //TODO: Add Image here!
        clone.setSourceUserImageUrl("https://placehold.co/100x100");
        return clone;
    }

    private ClientCollectionLiteDTO toClientCollectionLiteDTO(ProfileCollection profileCollection) {
        ClientCollectionLiteDTO dto = new ClientCollectionLiteDTO();
        dto.setId(profileCollection.getId());
        dto.setTitle(profileCollection.getTitle());
        dto.setDescription(profileCollection.getDescription());
        dto.setCollectionType(profileCollection.getType());

        if (profileCollection.getProfile() != null) {
            var profile = profileCollection.getProfile();
            if (profile.getUsername() != null) {
                dto.setCreatedBy(profile.getUsername());
            } else if (profile.getFullName() != null) {
                dto.setCreatedBy(profile.getFullName());
            }
        }

        ClientCollectionCommunityDTO communityDTO = new ClientCollectionCommunityDTO();
        communityDTO.setTotalCloned(77);
        communityDTO.setTotalComments(88);
        communityDTO.setTotalStars(99);
        dto.setCommunity(communityDTO);

        dto.setInterest(toClientInterestDto(profileCollection));

        return dto;
    }

    private ProfileCollection toProfileCollection(ClientCollectionDTO dto) {
        ProfileCollection entity = new ProfileCollection();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setType(dto.getCollectionType());

        ClientCollectionSettingsDTO settings = dto.getSettings();
        if (settings != null) {
            entity.setIsPublic(settings.isPublic());
            entity.setShowPrice(settings.isShowPrice());
            entity.setShowCheckbox(settings.isShowCheckbox());
            entity.setShowComment(settings.isShowComment());
            if (settings.getCurrency() != null && settings.getCurrency().getId() != null) {
                currencyRepository.findById(settings.getCurrency().getId()).ifPresent(entity::setCurrency);
            }
        }

        ClientInterestDTO interestDto = dto.getInterest();
        if (interestDto != null && interestDto.getId() != null) {
            Long profileId = profileService.getCurrentProfile().getId();
            entity.setInterest(
                interestRepository
                    .findByIdLinkedToProfileOrSystem(interestDto.getId(), profileId)
                    .orElseThrow(() -> new BadRequestAlertException("Interest not found", "interest", "idnotfound"))
            );
        } else {
            entity.setInterest(null);
        }

        return entity;
    }
}
