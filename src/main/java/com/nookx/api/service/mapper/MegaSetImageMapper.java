package com.nookx.api.service.mapper;

import com.nookx.api.domain.MegaAsset;
import com.nookx.api.domain.MegaSet;
import com.nookx.api.domain.MegaSetImage;
import com.nookx.api.service.dto.MegaAssetDTO;
import com.nookx.api.service.dto.MegaSetDTO;
import com.nookx.api.service.dto.MegaSetImageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MegaSetImage} and its DTO {@link MegaSetImageDTO}.
 */
@Mapper(componentModel = "spring", uses = { MegaAssetMapper.class })
public interface MegaSetImageMapper extends EntityMapper<MegaSetImageDTO, MegaSetImage> {
    @Mapping(target = "megaSet", source = "megaSet", qualifiedByName = "megaSetId")
    @Mapping(target = "asset", source = "asset", qualifiedByName = "megaAssetId")
    MegaSetImageDTO toDto(MegaSetImage entity);

    @Override
    @Mapping(target = "megaSet", source = "megaSet", qualifiedByName = "megaSetFromDtoId")
    @Mapping(target = "asset", source = "asset", qualifiedByName = "megaAssetFromDtoId")
    MegaSetImage toEntity(MegaSetImageDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "megaSet", source = "megaSet", qualifiedByName = "megaSetFromDtoId")
    @Mapping(target = "asset", source = "asset", qualifiedByName = "megaAssetFromDtoId")
    void partialUpdate(@MappingTarget MegaSetImage entity, MegaSetImageDTO dto);

    @Named("megaSetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MegaSetDTO toDtoMegaSetId(MegaSet megaSet);

    @Named("megaAssetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MegaAssetDTO toDtoMegaAssetId(MegaAsset asset);

    @Named("megaSetFromDtoId")
    default MegaSet megaSetFromDtoId(MegaSetDTO dto) {
        if (dto == null) {
            return null;
        }
        MegaSet megaSet = new MegaSet();
        megaSet.setId(dto.getId());
        return megaSet;
    }
}
