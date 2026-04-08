package com.dot.collector.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "clone_information")
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class CloneInformation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "cloned")
    private Boolean cloned;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "cloneInformation")
    private ProfileCollection collection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "profile", "cloneInformation" }, allowSetters = true)
    private ProfileCollection sourceCollection;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CloneInformation)) {
            return false;
        }
        return getId() != null && getId().equals(((CloneInformation) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Currency{" +
            "id=" + getId() +
            ", cloned='" + getCloned() + "'" +
            "}";
    }
}
