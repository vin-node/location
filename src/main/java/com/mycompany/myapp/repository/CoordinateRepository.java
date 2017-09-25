package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Coordinate;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Coordinate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {

}
