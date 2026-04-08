package com.dot.collector.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 * A MegaPartType.
 */
@Entity
@Table(
    name = "mega_part_type",
    uniqueConstraints = { @UniqueConstraint(name = "ux_mega_part_type__name_version", columnNames = { "name", "version" }) }
)
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class MegaPartType implements Serializable {

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

    @NotNull
    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "is_latest")
    private Boolean isLatest;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_mega_part_type__attribute",
        joinColumns = @JoinColumn(name = "mega_part_type_id"),
        inverseJoinColumns = @JoinColumn(name = "attribute_id")
    )
    @JsonIgnoreProperties(value = { "setTypes", "partTypes" }, allowSetters = true)
    private Set<MegaAttribute> attributes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public MegaPartType id(Long id) {
        this.setId(id);
        return this;
    }

    public MegaPartType name(String name) {
        this.setName(name);
        return this;
    }

    public MegaPartType version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public MegaPartType active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public MegaPartType isLatest(Boolean isLatest) {
        this.setIsLatest(isLatest);
        return this;
    }

    public MegaPartType attributes(Set<MegaAttribute> megaAttributes) {
        this.setAttributes(megaAttributes);
        return this;
    }

    public MegaPartType addAttribute(MegaAttribute megaAttribute) {
        this.attributes.add(megaAttribute);
        return this;
    }

    public MegaPartType removeAttribute(MegaAttribute megaAttribute) {
        this.attributes.remove(megaAttribute);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MegaPartType)) {
            return false;
        }
        return getId() != null && getId().equals(((MegaPartType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MegaPartType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", version=" + getVersion() +
            ", active='" + getActive() + "'" +
            ", isLatest='" + getIsLatest() + "'" +
            "}";
    }
}
