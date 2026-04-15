package com.nookx.api.service.dto;

import com.nookx.api.domain.ProfileCollectionImage;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link ProfileCollectionImage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class ProfileCollectionImageDTO implements Serializable {

    private Long id;

    private ProfileCollectionDTO profileCollection;

    private MegaAssetDTO asset;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileCollectionImageDTO)) {
            return false;
        }

        ProfileCollectionImageDTO profileCollectionImageDTO = (ProfileCollectionImageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, profileCollectionImageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfileCollectionImageDTO{" + "id=" + getId() + ", profileCollection=" + getProfileCollection() + ", asset=" + getAsset() + "}";
    }
}
