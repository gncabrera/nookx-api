package com.nookx.api.client.dto;

import com.nookx.api.domain.Interest;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link Interest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class ClientInterestDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    private boolean isPublic;

    private boolean isSystem;

    private Integer order;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientInterestDTO)) {
            return false;
        }

        ClientInterestDTO interestDTO = (ClientInterestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, interestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InterestDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", isPublic=" + isPublic() +
            ", isSystem=" + isSystem() +
            ", order=" + getOrder() +
            "}";
    }
}
