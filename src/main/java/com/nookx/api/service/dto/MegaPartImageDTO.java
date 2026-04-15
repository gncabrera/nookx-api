package com.nookx.api.service.dto;

import com.nookx.api.domain.MegaPartImage;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link MegaPartImage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class MegaPartImageDTO implements Serializable {

    private Long id;

    private MegaPartDTO part;

    private MegaAssetDTO asset;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MegaPartImageDTO)) {
            return false;
        }

        MegaPartImageDTO megaPartImageDTO = (MegaPartImageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, megaPartImageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MegaPartImageDTO{" + "id=" + getId() + ", part=" + getPart() + ", asset=" + getAsset() + "}";
    }
}
