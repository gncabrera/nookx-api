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
 * A DTO for the {@link com.dot.collector.api.domain.MegaPart} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class MegaPartDTO implements Serializable {

    private Long id;

    private LocalDate releaseDate;

    @NotNull
    private String partNumber;

    @NotNull
    private String nameEN;

    private String nameES;

    private String nameDE;

    private String nameFR;

    private String description;

    private String notes;

    private JsonNode attributes;

    private String attributesContentType;

    private MegaPartTypeDTO type;

    private PartCategoryDTO partCategory;

    private Set<PartSubCategoryDTO> partSubCategories = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MegaPartDTO)) {
            return false;
        }

        MegaPartDTO megaPartDTO = (MegaPartDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, megaPartDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MegaPartDTO{" +
            "id=" + getId() +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", partNumber='" + getPartNumber() + "'" +
            ", nameEN='" + getNameEN() + "'" +
            ", nameES='" + getNameES() + "'" +
            ", nameDE='" + getNameDE() + "'" +
            ", nameFR='" + getNameFR() + "'" +
            ", description='" + getDescription() + "'" +
            ", notes='" + getNotes() + "'" +
            ", attributes='" + getAttributes() + "'" +
            ", type=" + getType() +
            ", partCategory=" + getPartCategory() +
            ", partSubCategories=" + getPartSubCategories() +
            "}";
    }
}
