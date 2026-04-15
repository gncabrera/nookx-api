package com.nookx.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * Join between {@link MegaSet} and {@link MegaAsset}.
 */
@Entity
@Table(
    name = "mega_set_image",
    uniqueConstraints = { @UniqueConstraint(name = "ux_mega_set_image__set_id_asset_id", columnNames = { "set_id", "asset_id" }) }
)
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class MegaSetImage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "set_id", nullable = false)
    @JsonIgnoreProperties(value = { "type", "profileCollectionSets" }, allowSetters = true)
    private MegaSet megaSet;

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

    public MegaSetImage id(Long id) {
        this.setId(id);
        return this;
    }

    public MegaSetImage megaSet(MegaSet megaSet) {
        this.setMegaSet(megaSet);
        return this;
    }

    public MegaSetImage asset(MegaAsset asset) {
        this.setAsset(asset);
        return this;
    }

    public MegaSetImage sortOrder(Integer sortOrder) {
        this.setSortOrder(sortOrder);
        return this;
    }

    public MegaSetImage label(String label) {
        this.setLabel(label);
        return this;
    }

    public MegaSetImage isPrimary(boolean isPrimary) {
        this.setPrimary(isPrimary);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MegaSetImage)) {
            return false;
        }
        return getId() != null && getId().equals(((MegaSetImage) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MegaSetImage{" +
            "id=" + getId() +
            ", sortOrder=" + getSortOrder() +
            ", label='" + getLabel() + "'" +
            ", isPrimary=" + isPrimary() +
            "}";
    }
}
