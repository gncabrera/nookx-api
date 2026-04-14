package com.nookx.api.domain;

import com.nookx.api.domain.enumeration.AssetType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * A MegaAsset.
 */
@Entity
@Table(name = "mega_asset")
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class MegaAsset implements Serializable {

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

    @NotNull
    @Column(name = "path", nullable = false)
    private String path;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AssetType type;

    @Column(name = "content_type", length = 255)
    private String contentType;

    @Column(name = "size_bytes")
    private Long sizeBytes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uploaded_by_id")
    private User uploadedBy;

    @NotNull
    @Column(name = "is_public", nullable = false)
    private boolean isPublic = false;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public MegaAsset id(Long id) {
        this.setId(id);
        return this;
    }

    public MegaAsset name(String name) {
        this.setName(name);
        return this;
    }

    public MegaAsset description(String description) {
        this.setDescription(description);
        return this;
    }

    public MegaAsset path(String path) {
        this.setPath(path);
        return this;
    }

    public MegaAsset type(AssetType type) {
        this.setType(type);
        return this;
    }

    public MegaAsset contentType(String contentType) {
        this.setContentType(contentType);
        return this;
    }

    public MegaAsset sizeBytes(Long sizeBytes) {
        this.setSizeBytes(sizeBytes);
        return this;
    }

    public MegaAsset uploadedBy(User uploadedBy) {
        this.setUploadedBy(uploadedBy);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MegaAsset)) {
            return false;
        }
        return getId() != null && getId().equals(((MegaAsset) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MegaAsset{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", path='" + getPath() + "'" +
            ", type='" + getType() + "'" +
            ", contentType='" + getContentType() + "'" +
            ", sizeBytes=" + getSizeBytes() +
            ", isPublic=" + isPublic() +
            "}";
    }
}
