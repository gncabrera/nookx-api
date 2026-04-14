package com.nookx.api.service.dto;

import com.nookx.api.domain.enumeration.AssetType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link MegaAsset} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class MegaAssetDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private String path;

    private AssetType type;

    private String contentType;

    private Long sizeBytes;

    private Long uploadedById;

    private boolean isPublic;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MegaAssetDTO)) {
            return false;
        }

        MegaAssetDTO megaAssetDTO = (MegaAssetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, megaAssetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MegaAssetDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", path='" + getPath() + "'" +
            ", type='" + getType() + "'" +
            ", contentType='" + getContentType() + "'" +
            ", sizeBytes=" + getSizeBytes() +
            ", uploadedById=" + getUploadedById() +
            ", isPublic=" + isPublic() +
            "}";
    }
}
