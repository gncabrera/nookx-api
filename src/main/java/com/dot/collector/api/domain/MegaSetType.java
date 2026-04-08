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
 * A MegaSetType.
 */
@Entity
@Table(
    name = "mega_set_type",
    uniqueConstraints = { @UniqueConstraint(name = "ux_mega_set_type__name_version", columnNames = { "name", "version" }) }
)
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class MegaSetType implements Serializable {

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
        name = "rel_mega_set_type__attribute",
        joinColumns = @JoinColumn(name = "mega_set_type_id"),
        inverseJoinColumns = @JoinColumn(name = "attribute_id")
    )
    @JsonIgnoreProperties(value = { "setTypes", "partTypes" }, allowSetters = true)
    private Set<MegaAttribute> attributes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public MegaSetType id(Long id) {
        this.setId(id);
        return this;
    }

    public MegaSetType name(String name) {
        this.setName(name);
        return this;
    }

    public MegaSetType version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public MegaSetType active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public MegaSetType isLatest(Boolean isLatest) {
        this.setIsLatest(isLatest);
        return this;
    }

    public MegaSetType attributes(Set<MegaAttribute> megaAttributes) {
        this.setAttributes(megaAttributes);
        return this;
    }

    public MegaSetType addAttribute(MegaAttribute megaAttribute) {
        this.attributes.add(megaAttribute);
        return this;
    }

    public MegaSetType removeAttribute(MegaAttribute megaAttribute) {
        this.attributes.remove(megaAttribute);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MegaSetType)) {
            return false;
        }
        return getId() != null && getId().equals(((MegaSetType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MegaSetType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", version=" + getVersion() +
            ", active='" + getActive() + "'" +
            ", isLatest='" + getIsLatest() + "'" +
            "}";
    }
}
