package com.nookx.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * Join between {@link ProfileCollection} and {@link MegaAsset} (at most one per collection).
 */
@Entity
@Table(name = "profile_collection_image")
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class ProfileCollectionImage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_collection_id", unique = true, nullable = false)
    @JsonIgnore
    private ProfileCollection profileCollection;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    @JsonIgnoreProperties(value = { "uploadedBy" }, allowSetters = true)
    private MegaAsset asset;

    public ProfileCollectionImage id(Long id) {
        this.setId(id);
        return this;
    }

    public ProfileCollectionImage profileCollection(ProfileCollection profileCollection) {
        this.setProfileCollection(profileCollection);
        return this;
    }

    public ProfileCollectionImage asset(MegaAsset asset) {
        this.setAsset(asset);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileCollectionImage)) {
            return false;
        }
        return getId() != null && getId().equals(((ProfileCollectionImage) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfileCollectionImage{" + "id=" + getId() + "}";
    }
}
