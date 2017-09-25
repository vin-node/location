package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Coordinate;

import com.mycompany.myapp.repository.CoordinateRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Coordinate.
 */
@RestController
@RequestMapping("/api")
public class CoordinateResource {

    private final Logger log = LoggerFactory.getLogger(CoordinateResource.class);

    private static final String ENTITY_NAME = "coordinate";

    private final CoordinateRepository coordinateRepository;

    public CoordinateResource(CoordinateRepository coordinateRepository) {
        this.coordinateRepository = coordinateRepository;
    }

    /**
     * POST  /coordinates : Create a new coordinate.
     *
     * @param coordinate the coordinate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new coordinate, or with status 400 (Bad Request) if the coordinate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/coordinates")
    @Timed
    public ResponseEntity<Coordinate> createCoordinate(@RequestBody Coordinate coordinate) throws URISyntaxException {
        log.debug("REST request to save Coordinate : {}", coordinate);
        if (coordinate.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new coordinate cannot already have an ID")).body(null);
        }
        Coordinate result = coordinateRepository.save(coordinate);
        return ResponseEntity.created(new URI("/api/coordinates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /coordinates : Updates an existing coordinate.
     *
     * @param coordinate the coordinate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated coordinate,
     * or with status 400 (Bad Request) if the coordinate is not valid,
     * or with status 500 (Internal Server Error) if the coordinate couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/coordinates")
    @Timed
    public ResponseEntity<Coordinate> updateCoordinate(@RequestBody Coordinate coordinate) throws URISyntaxException {
        log.debug("REST request to update Coordinate : {}", coordinate);
        if (coordinate.getId() == null) {
            return createCoordinate(coordinate);
        }
        Coordinate result = coordinateRepository.save(coordinate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, coordinate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /coordinates : get all the coordinates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of coordinates in body
     */
    @GetMapping("/coordinates")
    @Timed
    public ResponseEntity<List<Coordinate>> getAllCoordinates(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Coordinates");
        Page<Coordinate> page = coordinateRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/coordinates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /coordinates/:id : get the "id" coordinate.
     *
     * @param id the id of the coordinate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the coordinate, or with status 404 (Not Found)
     */
    @GetMapping("/coordinates/{id}")
    @Timed
    public ResponseEntity<Coordinate> getCoordinate(@PathVariable Long id) {
        log.debug("REST request to get Coordinate : {}", id);
        Coordinate coordinate = coordinateRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(coordinate));
    }

    /**
     * DELETE  /coordinates/:id : delete the "id" coordinate.
     *
     * @param id the id of the coordinate to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/coordinates/{id}")
    @Timed
    public ResponseEntity<Void> deleteCoordinate(@PathVariable Long id) {
        log.debug("REST request to delete Coordinate : {}", id);
        coordinateRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
