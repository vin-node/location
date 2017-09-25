package com.mycompany.myapp.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Coordinate.
 */
@Entity
@Table(name = "coordinate")
public class Coordinate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "lati")
    private Double lati;

    @Column(name = "longi")
    private Double longi;

    @ManyToOne
    private Location location;

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLati() {
        return lati;
    }

    public Coordinate lati(Double lati) {
        this.lati = lati;
        return this;
    }

    public void setLati(Double lati) {
        this.lati = lati;
    }

    public Double getLongi() {
        return longi;
    }

    public Coordinate longi(Double longi) {
        this.longi = longi;
        return this;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }

    public Location getLocation() {
        return location;
    }

    public Coordinate location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coordinate coordinate = (Coordinate) o;
        if (coordinate.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), coordinate.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Coordinate{" +
            "id=" + getId() +
            ", lati='" + getLati() + "'" +
            ", longi='" + getLongi() + "'" +
            "}";
    }
}
