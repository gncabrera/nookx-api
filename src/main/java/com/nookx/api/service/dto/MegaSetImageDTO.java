package com.nookx.api.service.dto;

import com.nookx.api.domain.MegaSetImage;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link MegaSetImage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class MegaSetImageDTO implements Serializable {

    private Long id;

    private MegaSetDTO megaSet;

    private MegaAssetDTO asset;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MegaSetImageDTO)) {
            return false;
        }

        MegaSetImageDTO megaSetImageDTO = (MegaSetImageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, megaSetImageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MegaSetImageDTO{" + "id=" + getId() + ", megaSet=" + getMegaSet() + ", asset=" + getAsset() + "}";
    }
}
