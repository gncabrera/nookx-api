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
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * A MegaPart.
 */
@Entity
@Table(name = "mega_part")
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class MegaPart implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @NotNull
    @Column(name = "part_number", nullable = false, unique = true)
    private String partNumber;

    @NotNull
    @Column(name = "name_en", nullable = false)
    private String nameEN;

    @Column(name = "name_es")
    private String nameES;

    @Column(name = "name_de")
    private String nameDE;

    @Column(name = "name_fr")
    private String nameFR;

    @Column(name = "description")
    private String description;

    @Column(name = "notes")
    private String notes;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes", columnDefinition = "jsonb")
    private JsonNode attributes;

    @Column(name = "attributes_content_type")
    private String attributesContentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "attributes" }, allowSetters = true)
    private MegaPartType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private PartCategory partCategory;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_mega_part__part_sub_category",
        joinColumns = @JoinColumn(name = "mega_part_id"),
        inverseJoinColumns = @JoinColumn(name = "part_sub_category_id")
    )
    @JsonIgnoreProperties(value = { "megaParts" }, allowSetters = true)
    private Set<PartSubCategory> partSubCategories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public MegaPart id(Long id) {
        this.setId(id);
        return this;
    }

    public MegaPart releaseDate(LocalDate releaseDate) {
        this.setReleaseDate(releaseDate);
        return this;
    }

    public MegaPart partNumber(String partNumber) {
        this.setPartNumber(partNumber);
        return this;
    }

    public MegaPart nameEN(String nameEN) {
        this.setNameEN(nameEN);
        return this;
    }

    public MegaPart nameES(String nameES) {
        this.setNameES(nameES);
        return this;
    }

    public MegaPart nameDE(String nameDE) {
        this.setNameDE(nameDE);
        return this;
    }

    public MegaPart nameFR(String nameFR) {
        this.setNameFR(nameFR);
        return this;
    }

    public MegaPart description(String description) {
        this.setDescription(description);
        return this;
    }

    public MegaPart notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public MegaPart attributes(JsonNode attributes) {
        this.setAttributes(attributes);
        return this;
    }

    public MegaPart attributesContentType(String attributesContentType) {
        this.attributesContentType = attributesContentType;
        return this;
    }

    public MegaPart type(MegaPartType megaPartType) {
        this.setType(megaPartType);
        return this;
    }

    public MegaPart partCategory(PartCategory partCategory) {
        this.setPartCategory(partCategory);
        return this;
    }

    public MegaPart partSubCategories(Set<PartSubCategory> partSubCategories) {
        this.setPartSubCategories(partSubCategories);
        return this;
    }

    public MegaPart addPartSubCategory(PartSubCategory partSubCategory) {
        this.partSubCategories.add(partSubCategory);
        return this;
    }

    public MegaPart removePartSubCategory(PartSubCategory partSubCategory) {
        this.partSubCategories.remove(partSubCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MegaPart)) {
            return false;
        }
        return getId() != null && getId().equals(((MegaPart) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MegaPart{" +
            "id=" + getId() +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", partNumber='" + getPartNumber() + "'" +
            ", nameEN='" + getNameEN() + "'" +
            ", nameES='" + getNameES() + "'" +
            ", nameDE='" + getNameDE() + "'" +
            ", nameFR='" + getNameFR() + "'" +
            ", description='" + getDescription() + "'" +
            ", notes='" + getNotes() + "'" +
            ", attributes='" + getAttributes() + "'" +
            ", attributesContentType='" + getAttributesContentType() + "'" +
            "}";
    }
}
