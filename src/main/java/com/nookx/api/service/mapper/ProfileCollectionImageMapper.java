package com.nookx.api.service.mapper;

import com.nookx.api.domain.MegaAsset;
import com.nookx.api.domain.ProfileCollection;
import com.nookx.api.domain.ProfileCollectionImage;
import com.nookx.api.service.dto.MegaAssetDTO;
import com.nookx.api.service.dto.ProfileCollectionDTO;
import com.nookx.api.service.dto.ProfileCollectionImageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfileCollectionImage} and its DTO {@link ProfileCollectionImageDTO}.
 */
@Mapper(componentModel = "spring", uses = { MegaAssetMapper.class })
public interface ProfileCollectionImageMapper extends EntityMapper<ProfileCollectionImageDTO, ProfileCollectionImage> {
    @Mapping(target = "profileCollection", source = "profileCollection", qualifiedByName = "profileCollectionId")
    @Mapping(target = "asset", source = "asset", qualifiedByName = "megaAssetId")
    ProfileCollectionImageDTO toDto(ProfileCollectionImage entity);

    @Override
    @Mapping(target = "profileCollection", source = "profileCollection", qualifiedByName = "profileCollectionFromDtoId")
    @Mapping(target = "asset", source = "asset", qualifiedByName = "megaAssetFromDtoId")
    ProfileCollectionImage toEntity(ProfileCollectionImageDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "profileCollection", source = "profileCollection", qualifiedByName = "profileCollectionFromDtoId")
    @Mapping(target = "asset", source = "asset", qualifiedByName = "megaAssetFromDtoId")
    void partialUpdate(@MappingTarget ProfileCollectionImage entity, ProfileCollectionImageDTO dto);

    @Named("profileCollectionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfileCollectionDTO toDtoProfileCollectionId(ProfileCollection profileCollection);

    @Named("megaAssetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MegaAssetDTO toDtoMegaAssetId(MegaAsset asset);

    @Named("profileCollectionFromDtoId")
    default ProfileCollection profileCollectionFromDtoId(ProfileCollectionDTO dto) {
        if (dto == null) {
            return null;
        }
        ProfileCollection profileCollection = new ProfileCollection();
        profileCollection.setId(dto.getId());
        return profileCollection;
    }
}
