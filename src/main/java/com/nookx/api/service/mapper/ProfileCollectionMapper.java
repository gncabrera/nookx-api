package com.nookx.api.service.mapper;

import com.nookx.api.domain.MegaAsset;
import com.nookx.api.domain.Profile;
import com.nookx.api.domain.ProfileCollection;
import com.nookx.api.service.dto.MegaAssetDTO;
import com.nookx.api.service.dto.ProfileCollectionDTO;
import com.nookx.api.service.dto.ProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfileCollection} and its DTO {@link ProfileCollectionDTO}.
 */
@Mapper(componentModel = "spring", uses = { MegaAssetMapper.class })
public interface ProfileCollectionMapper extends EntityMapper<ProfileCollectionDTO, ProfileCollection> {
    @Mapping(target = "profile", source = "profile", qualifiedByName = "profileId")
    @Mapping(target = "image", source = "image", qualifiedByName = "megaAssetId")
    ProfileCollectionDTO toDto(ProfileCollection s);

    @Override
    @Mapping(target = "image", source = "image", qualifiedByName = "megaAssetFromDtoId")
    ProfileCollection toEntity(ProfileCollectionDTO profileCollectionDTO);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "image", source = "image", qualifiedByName = "megaAssetFromDtoId")
    void partialUpdate(@MappingTarget ProfileCollection entity, ProfileCollectionDTO dto);

    @Named("profileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfileDTO toDtoProfileId(Profile profile);

    @Named("megaAssetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MegaAssetDTO toDtoMegaAssetId(MegaAsset megaAsset);
}
