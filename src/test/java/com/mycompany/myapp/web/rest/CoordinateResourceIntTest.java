package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AppApp;

import com.mycompany.myapp.domain.Coordinate;
import com.mycompany.myapp.repository.CoordinateRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CoordinateResource REST controller.
 *
 * @see CoordinateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppApp.class)
public class CoordinateResourceIntTest {

    private static final Double DEFAULT_LATI = 1D;
    private static final Double UPDATED_LATI = 2D;

    private static final Double DEFAULT_LONGI = 1D;
    private static final Double UPDATED_LONGI = 2D;

    @Autowired
    private CoordinateRepository coordinateRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCoordinateMockMvc;

    private Coordinate coordinate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CoordinateResource coordinateResource = new CoordinateResource(coordinateRepository);
        this.restCoordinateMockMvc = MockMvcBuilders.standaloneSetup(coordinateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coordinate createEntity(EntityManager em) {
        Coordinate coordinate = new Coordinate()
            .lati(DEFAULT_LATI)
            .longi(DEFAULT_LONGI);
        return coordinate;
    }

    @Before
    public void initTest() {
        coordinate = createEntity(em);
    }

    @Test
    @Transactional
    public void createCoordinate() throws Exception {
        int databaseSizeBeforeCreate = coordinateRepository.findAll().size();

        // Create the Coordinate
        restCoordinateMockMvc.perform(post("/api/coordinates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coordinate)))
            .andExpect(status().isCreated());

        // Validate the Coordinate in the database
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeCreate + 1);
        Coordinate testCoordinate = coordinateList.get(coordinateList.size() - 1);
        assertThat(testCoordinate.getLati()).isEqualTo(DEFAULT_LATI);
        assertThat(testCoordinate.getLongi()).isEqualTo(DEFAULT_LONGI);
    }

    @Test
    @Transactional
    public void createCoordinateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = coordinateRepository.findAll().size();

        // Create the Coordinate with an existing ID
        coordinate.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoordinateMockMvc.perform(post("/api/coordinates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coordinate)))
            .andExpect(status().isBadRequest());

        // Validate the Coordinate in the database
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCoordinates() throws Exception {
        // Initialize the database
        coordinateRepository.saveAndFlush(coordinate);

        // Get all the coordinateList
        restCoordinateMockMvc.perform(get("/api/coordinates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coordinate.getId().intValue())))
            .andExpect(jsonPath("$.[*].lati").value(hasItem(DEFAULT_LATI.doubleValue())))
            .andExpect(jsonPath("$.[*].longi").value(hasItem(DEFAULT_LONGI.doubleValue())));
    }

    @Test
    @Transactional
    public void getCoordinate() throws Exception {
        // Initialize the database
        coordinateRepository.saveAndFlush(coordinate);

        // Get the coordinate
        restCoordinateMockMvc.perform(get("/api/coordinates/{id}", coordinate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(coordinate.getId().intValue()))
            .andExpect(jsonPath("$.lati").value(DEFAULT_LATI.doubleValue()))
            .andExpect(jsonPath("$.longi").value(DEFAULT_LONGI.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCoordinate() throws Exception {
        // Get the coordinate
        restCoordinateMockMvc.perform(get("/api/coordinates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoordinate() throws Exception {
        // Initialize the database
        coordinateRepository.saveAndFlush(coordinate);
        int databaseSizeBeforeUpdate = coordinateRepository.findAll().size();

        // Update the coordinate
        Coordinate updatedCoordinate = coordinateRepository.findOne(coordinate.getId());
        updatedCoordinate
            .lati(UPDATED_LATI)
            .longi(UPDATED_LONGI);

        restCoordinateMockMvc.perform(put("/api/coordinates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCoordinate)))
            .andExpect(status().isOk());

        // Validate the Coordinate in the database
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeUpdate);
        Coordinate testCoordinate = coordinateList.get(coordinateList.size() - 1);
        assertThat(testCoordinate.getLati()).isEqualTo(UPDATED_LATI);
        assertThat(testCoordinate.getLongi()).isEqualTo(UPDATED_LONGI);
    }

    @Test
    @Transactional
    public void updateNonExistingCoordinate() throws Exception {
        int databaseSizeBeforeUpdate = coordinateRepository.findAll().size();

        // Create the Coordinate

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCoordinateMockMvc.perform(put("/api/coordinates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coordinate)))
            .andExpect(status().isCreated());

        // Validate the Coordinate in the database
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCoordinate() throws Exception {
        // Initialize the database
        coordinateRepository.saveAndFlush(coordinate);
        int databaseSizeBeforeDelete = coordinateRepository.findAll().size();

        // Get the coordinate
        restCoordinateMockMvc.perform(delete("/api/coordinates/{id}", coordinate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Coordinate> coordinateList = coordinateRepository.findAll();
        assertThat(coordinateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Coordinate.class);
        Coordinate coordinate1 = new Coordinate();
        coordinate1.setId(1L);
        Coordinate coordinate2 = new Coordinate();
        coordinate2.setId(coordinate1.getId());
        assertThat(coordinate1).isEqualTo(coordinate2);
        coordinate2.setId(2L);
        assertThat(coordinate1).isNotEqualTo(coordinate2);
        coordinate1.setId(null);
        assertThat(coordinate1).isNotEqualTo(coordinate2);
    }
}
