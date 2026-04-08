package com.dot.collector.api.domain;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * A Currency (ISO-like code, display name, symbol).
 */
@Entity
@Table(name = "currency")
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class Currency implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "symbol")
    private String symbol;

    public Currency id(Long id) {
        this.setId(id);
        return this;
    }

    public Currency code(String code) {
        this.setCode(code);
        return this;
    }

    public Currency name(String name) {
        this.setName(name);
        return this;
    }

    public Currency symbol(String symbol) {
        this.setSymbol(symbol);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Currency)) {
            return false;
        }
        return getId() != null && getId().equals(((Currency) o).getId());
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
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", symbol='" + getSymbol() + "'" +
            "}";
    }
}
