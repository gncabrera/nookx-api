package com.nookx.api.service.mapper;

import com.nookx.api.domain.MegaAsset;
import com.nookx.api.domain.MegaPart;
import com.nookx.api.domain.MegaPartImage;
import com.nookx.api.service.dto.MegaAssetDTO;
import com.nookx.api.service.dto.MegaPartDTO;
import com.nookx.api.service.dto.MegaPartImageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MegaPartImage} and its DTO {@link MegaPartImageDTO}.
 */
@Mapper(componentModel = "spring", uses = { MegaAssetMapper.class })
public interface MegaPartImageMapper extends EntityMapper<MegaPartImageDTO, MegaPartImage> {
    @Mapping(target = "part", source = "part", qualifiedByName = "megaPartId")
    @Mapping(target = "asset", source = "asset", qualifiedByName = "megaAssetId")
    MegaPartImageDTO toDto(MegaPartImage entity);

    @Override
    @Mapping(target = "part", source = "part", qualifiedByName = "megaPartFromDtoId")
    @Mapping(target = "asset", source = "asset", qualifiedByName = "megaAssetFromDtoId")
    MegaPartImage toEntity(MegaPartImageDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "part", source = "part", qualifiedByName = "megaPartFromDtoId")
    @Mapping(target = "asset", source = "asset", qualifiedByName = "megaAssetFromDtoId")
    void partialUpdate(@MappingTarget MegaPartImage entity, MegaPartImageDTO dto);

    @Named("megaPartId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MegaPartDTO toDtoMegaPartId(MegaPart part);

    @Named("megaAssetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MegaAssetDTO toDtoMegaAssetId(MegaAsset asset);

    @Named("megaPartFromDtoId")
    default MegaPart megaPartFromDtoId(MegaPartDTO dto) {
        if (dto == null) {
            return null;
        }
        MegaPart megaPart = new MegaPart();
        megaPart.setId(dto.getId());
        return megaPart;
    }
}
