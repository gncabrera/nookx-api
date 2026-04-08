package com.dot.collector.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * A MegaSet.
 */
@Entity
@Table(name = "mega_set")
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class MegaSet implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "set_number", nullable = false, unique = true)
    private String setNumber;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "notes")
    private String notes;

    @NotNull
    @Column(name = "name_en", nullable = false)
    private String nameEN;

    @Column(name = "name_es")
    private String nameES;

    @Column(name = "name_de")
    private String nameDE;

    @Column(name = "name_fr")
    private String nameFR;

    @NotNull
    @Column(name = "description_en", nullable = false)
    private String descriptionEN;

    @Column(name = "description_es")
    private String descriptionES;

    @Column(name = "description_de")
    private String descriptionDE;

    @Column(name = "description_fr")
    private String descriptionFR;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes", columnDefinition = "jsonb")
    private JsonNode attributes;

    @Column(name = "attributes_content_type")
    private String attributesContentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "attributes" }, allowSetters = true)
    private MegaSetType type;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "sets")
    @JsonIgnoreProperties(value = { "collection", "sets" }, allowSetters = true)
    @Setter(AccessLevel.NONE)
    private Set<ProfileCollectionSet> profileCollectionSets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public MegaSet id(Long id) {
        this.setId(id);
        return this;
    }

    public MegaSet setNumber(String setNumber) {
        this.setSetNumber(setNumber);
        return this;
    }

    public MegaSet releaseDate(LocalDate releaseDate) {
        this.setReleaseDate(releaseDate);
        return this;
    }

    public MegaSet notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public MegaSet nameEN(String nameEN) {
        this.setNameEN(nameEN);
        return this;
    }

    public MegaSet nameES(String nameES) {
        this.setNameES(nameES);
        return this;
    }

    public MegaSet nameDE(String nameDE) {
        this.setNameDE(nameDE);
        return this;
    }

    public MegaSet nameFR(String nameFR) {
        this.setNameFR(nameFR);
        return this;
    }

    public MegaSet descriptionEN(String descriptionEN) {
        this.setDescriptionEN(descriptionEN);
        return this;
    }

    public MegaSet descriptionES(String descriptionES) {
        this.setDescriptionES(descriptionES);
        return this;
    }

    public MegaSet descriptionDE(String descriptionDE) {
        this.setDescriptionDE(descriptionDE);
        return this;
    }

    public MegaSet descriptionFR(String descriptionFR) {
        this.setDescriptionFR(descriptionFR);
        return this;
    }

    public MegaSet attributes(JsonNode attributes) {
        this.setAttributes(attributes);
        return this;
    }

    public MegaSet attributesContentType(String attributesContentType) {
        this.attributesContentType = attributesContentType;
        return this;
    }

    public MegaSet type(MegaSetType megaSetType) {
        this.setType(megaSetType);
        return this;
    }

    public void setProfileCollectionSets(Set<ProfileCollectionSet> profileCollectionSets) {
        if (this.profileCollectionSets != null) {
            this.profileCollectionSets.forEach(i -> i.removeSet(this));
        }
        if (profileCollectionSets != null) {
            profileCollectionSets.forEach(i -> i.addSet(this));
        }
        this.profileCollectionSets = profileCollectionSets;
    }

    public MegaSet profileCollectionSets(Set<ProfileCollectionSet> profileCollectionSets) {
        this.setProfileCollectionSets(profileCollectionSets);
        return this;
    }

    public MegaSet addProfileCollectionSet(ProfileCollectionSet profileCollectionSet) {
        this.profileCollectionSets.add(profileCollectionSet);
        profileCollectionSet.getSets().add(this);
        return this;
    }

    public MegaSet removeProfileCollectionSet(ProfileCollectionSet profileCollectionSet) {
        this.profileCollectionSets.remove(profileCollectionSet);
        profileCollectionSet.getSets().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MegaSet)) {
            return false;
        }
        return getId() != null && getId().equals(((MegaSet) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MegaSet{" +
            "id=" + getId() +
            ", setNumber='" + getSetNumber() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", notes='" + getNotes() + "'" +
            ", nameEN='" + getNameEN() + "'" +
            ", nameES='" + getNameES() + "'" +
            ", nameDE='" + getNameDE() + "'" +
            ", nameFR='" + getNameFR() + "'" +
            ", descriptionEN='" + getDescriptionEN() + "'" +
            ", descriptionES='" + getDescriptionES() + "'" +
            ", descriptionDE='" + getDescriptionDE() + "'" +
            ", descriptionFR='" + getDescriptionFR() + "'" +
            ", attributes='" + getAttributes() + "'" +
            ", attributesContentType='" + getAttributesContentType() + "'" +
            "}";
    }
}
