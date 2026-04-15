package com.nookx.api.service.mapper;

import com.nookx.api.domain.MegaSet;
import com.nookx.api.domain.ProfileCollection;
import com.nookx.api.domain.ProfileCollectionSet;
import com.nookx.api.service.dto.MegaSetDTO;
import com.nookx.api.service.dto.ProfileCollectionDTO;
import com.nookx.api.service.dto.ProfileCollectionSetDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProfileCollectionSet} and its DTO {@link ProfileCollectionSetDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProfileCollectionMapper.class })
public interface ProfileCollectionSetMapper extends EntityMapper<ProfileCollectionSetDTO, ProfileCollectionSet> {
    @Mapping(target = "collection", source = "collection", qualifiedByName = "profileCollectionId")
    @Mapping(target = "sets", source = "sets", qualifiedByName = "megaSetIdSet")
    ProfileCollectionSetDTO toDto(ProfileCollectionSet s);

    @Mapping(target = "removeSet", ignore = true)
    ProfileCollectionSet toEntity(ProfileCollectionSetDTO profileCollectionSetDTO);

    @Named("profileCollectionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "image", source = "image", qualifiedByName = "megaAssetId")
    ProfileCollectionDTO toDtoProfileCollectionId(ProfileCollection profileCollection);

    @Named("megaSetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MegaSetDTO toDtoMegaSetId(MegaSet megaSet);

    @Named("megaSetIdSet")
    default Set<MegaSetDTO> toDtoMegaSetIdSet(Set<MegaSet> megaSet) {
        return megaSet.stream().map(this::toDtoMegaSetId).collect(Collectors.toSet());
    }
}
