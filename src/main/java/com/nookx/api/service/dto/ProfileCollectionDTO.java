package com.nookx.api.service.dto;

import com.nookx.api.client.dto.ClientInterestDTO;
import com.nookx.api.domain.ProfileCollection;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link ProfileCollection} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class ProfileCollectionDTO implements Serializable {

    private Long id;

    private String title;

    private String description;

    private Boolean isPublic;

    private ProfileDTO profile;

    private ClientInterestDTO interest;

    private ProfileCollectionImageDTO image;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileCollectionDTO)) {
            return false;
        }

        ProfileCollectionDTO profileCollectionDTO = (ProfileCollectionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, profileCollectionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfileCollectionDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", isPublic='" + getIsPublic() + "'" +
            ", profile=" + getProfile() +
            ", image=" + getImage() +
            "}";
    }
}
