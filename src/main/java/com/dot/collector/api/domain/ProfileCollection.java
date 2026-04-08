package com.dot.collector.api.domain;

import com.dot.collector.api.domain.enumeration.AttributeType;
import com.dot.collector.api.domain.enumeration.ProfileCollectionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * A ProfileCollection.
 */
@Entity
@Table(name = "profile_collection")
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class ProfileCollection implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "show_price", nullable = false)
    private boolean showPrice;

    @Column(name = "show_checkbox", nullable = false)
    private boolean showCheckbox;

    @Column(name = "show_comment", nullable = false)
    private boolean showComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(allowSetters = true)
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Profile profile;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private CloneInformation cloneInformation;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ProfileCollectionType type;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ProfileCollection id(Long id) {
        this.setId(id);
        return this;
    }

    public ProfileCollection title(String title) {
        this.setTitle(title);
        return this;
    }

    public ProfileCollection description(String description) {
        this.setDescription(description);
        return this;
    }

    public ProfileCollection isPublic(Boolean isPublic) {
        this.setIsPublic(isPublic);
        return this;
    }

    public ProfileCollection showPrice(boolean showPrice) {
        this.setShowPrice(showPrice);
        return this;
    }

    public ProfileCollection showCheckbox(boolean showCheckbox) {
        this.setShowCheckbox(showCheckbox);
        return this;
    }

    public ProfileCollection showComment(boolean showComment) {
        this.setShowComment(showComment);
        return this;
    }

    public ProfileCollection currency(Currency currency) {
        this.setCurrency(currency);
        return this;
    }

    public ProfileCollection profile(Profile profile) {
        this.setProfile(profile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileCollection)) {
            return false;
        }
        return getId() != null && getId().equals(((ProfileCollection) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfileCollection{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", isPublic='" + getIsPublic() + "'" +
            ", showPrice=" + isShowPrice() +
            ", showCheckbox=" + isShowCheckbox() +
            ", showComment=" + isShowComment() +
            ", currency=" + getCurrency() +
            "}";
    }
}
