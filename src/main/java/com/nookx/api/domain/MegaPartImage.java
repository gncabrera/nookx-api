package com.nookx.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * Join between {@link MegaPart} and {@link MegaAsset}.
 */
@Entity
@Table(
    name = "mega_part_image",
    uniqueConstraints = { @UniqueConstraint(name = "ux_mega_part_image__part_id_asset_id", columnNames = { "part_id", "asset_id" }) }
)
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class MegaPartImage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false)
    @JsonIgnoreProperties(value = { "type", "partCategory", "partSubCategories" }, allowSetters = true)
    private MegaPart part;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    @JsonIgnoreProperties(value = { "uploadedBy" }, allowSetters = true)
    private MegaAsset asset;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @Column(name = "label")
    private String label;

    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary = false;

    public MegaPartImage id(Long id) {
        this.setId(id);
        return this;
    }

    public MegaPartImage part(MegaPart part) {
        this.setPart(part);
        return this;
    }

    public MegaPartImage asset(MegaAsset asset) {
        this.setAsset(asset);
        return this;
    }

    public MegaPartImage sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public MegaPartImage label(String label) {
        this.setLabel(label);
        return this;
    }

    public MegaPartImage isPrimary(boolean isPrimary) {
        this.setPrimary(isPrimary);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MegaPartImage)) {
            return false;
        }
        return getId() != null && getId().equals(((MegaPartImage) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MegaPartImage{" +
            "id=" + getId() +
            ", sortOrder=" + getSortOrder() +
            ", label='" + getLabel() + "'" +
            ", isPrimary=" + isPrimary() +
            "}";
    }
}
