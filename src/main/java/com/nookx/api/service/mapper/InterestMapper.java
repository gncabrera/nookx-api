package com.nookx.api.service.mapper;

import com.nookx.api.client.dto.ClientInterestDTO;
import com.nookx.api.domain.Interest;
import com.nookx.api.domain.ProfileInterest;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Interest} and its DTO {@link ClientInterestDTO}.
 */
@Mapper(componentModel = "spring")
public interface InterestMapper extends EntityMapper<ClientInterestDTO, Interest> {
    @Named("interestId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientInterestDTO toDtoInterestId(Interest interest);

    @Named("toEntityInterestId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Interest toEntityInterestId(ClientInterestDTO dto);

    @Named("toDtoFromProfileInterests")
    default Set<ClientInterestDTO> toDtoFromProfileInterests(Set<ProfileInterest> profileInterests) {
        if (profileInterests == null) {
            return new HashSet<>();
        }
        return profileInterests.stream().map(ProfileInterest::getInterest).map(this::toDto).collect(Collectors.toCollection(HashSet::new));
    }
}
