package com.dot.collector.api.service.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link com.dot.collector.api.domain.MegaSet} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class MegaSetDTO implements Serializable {

    private Long id;

    @NotNull
    private String setNumber;

    private LocalDate releaseDate;

    private String notes;

    @NotNull
    private String nameEN;

    private String nameES;

    private String nameDE;

    private String nameFR;

    @NotNull
    private String descriptionEN;

    private String descriptionES;

    private String descriptionDE;

    private String descriptionFR;

    private JsonNode attributes;

    private String attributesContentType;

    private MegaSetTypeDTO type;

    private Set<ProfileCollectionSetDTO> profileCollectionSets = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MegaSetDTO)) {
            return false;
        }

        MegaSetDTO megaSetDTO = (MegaSetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, megaSetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MegaSetDTO{" +
            "id=" + getId() +
            ", setNumber='" + getSetNumber() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", notes='" + getNotes() + "'" +
            ", nameEN='" + getNameEN() + "'" +
            ", nameES='" + getNameES() + "'" +
            ", nameDE='" + getNameDE() + "'" +
            ", nameFR='" + getNameFR() + "'" +
            ", descriptionEN='" + getDescriptionEN() + "'" +
            ", descriptionES='" + getDescriptionES() + "'" +
            ", descriptionDE='" + getDescriptionDE() + "'" +
            ", descriptionFR='" + getDescriptionFR() + "'" +
            ", attributes='" + getAttributes() + "'" +
            ", type=" + getType() +
            ", profileCollectionSets=" + getProfileCollectionSets() +
            "}";
    }
}
