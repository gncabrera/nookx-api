package com.nookx.api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * A catalog interest (e.g. theme or topic).
 */
@Entity
@Table(name = "interest")
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class Interest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @Column(name = "is_system", nullable = false)
    private boolean isSystem;

    @Column(name = "sort_order", nullable = false)
    private Integer order;

    public Interest id(Long id) {
        this.setId(id);
        return this;
    }

    public Interest name(String name) {
        this.setName(name);
        return this;
    }

    public Interest description(String description) {
        this.setDescription(description);
        return this;
    }

    public Interest order(Integer order) {
        this.setOrder(order);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Interest)) {
            return false;
        }
        return getId() != null && getId().equals(((Interest) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Interest{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", isPublic=" + isPublic() +
            ", isSystem=" + isSystem() +
            ", order=" + getOrder() +
            "}";
    }
}
