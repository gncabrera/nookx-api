package com.dot.collector.api.web.rest;

import static com.dot.collector.api.domain.MegaPartAsserts.*;
import static com.dot.collector.api.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dot.collector.api.IntegrationTest;
import com.dot.collector.api.domain.MegaPart;
import com.dot.collector.api.repository.MegaPartRepository;
import com.dot.collector.api.service.MegaPartService;
import com.dot.collector.api.service.dto.MegaPartDTO;
import com.dot.collector.api.service.mapper.MegaPartMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MegaPartResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MegaPartResourceIT {

    private static final LocalDate DEFAULT_RELEASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RELEASE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PART_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PART_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_EN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_EN = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_ES = "AAAAAAAAAA";
    private static final String UPDATED_NAME_ES = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_DE = "AAAAAAAAAA";
    private static final String UPDATED_NAME_DE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_FR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_FR = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final JsonNode DEFAULT_ATTRIBUTES = JsonNodeFactory.instance.objectNode().put("v", "0");
    private static final JsonNode UPDATED_ATTRIBUTES = JsonNodeFactory.instance.objectNode().put("v", "1");
    private static final String DEFAULT_ATTRIBUTES_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ATTRIBUTES_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/mega-parts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MegaPartRepository megaPartRepository;

    @Mock
    private MegaPartRepository megaPartRepositoryMock;

    @Autowired
    private MegaPartMapper megaPartMapper;

    @Mock
    private MegaPartService megaPartServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMegaPartMockMvc;

    private MegaPart megaPart;

    private MegaPart insertedMegaPart;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MegaPart createEntity() {
        return new MegaPart()
            .releaseDate(DEFAULT_RELEASE_DATE)
            .partNumber(DEFAULT_PART_NUMBER)
            .nameEN(DEFAULT_NAME_EN)
            .nameES(DEFAULT_NAME_ES)
            .nameDE(DEFAULT_NAME_DE)
            .nameFR(DEFAULT_NAME_FR)
            .description(DEFAULT_DESCRIPTION)
            .notes(DEFAULT_NOTES)
            .attributes(DEFAULT_ATTRIBUTES)
            .attributesContentType(DEFAULT_ATTRIBUTES_CONTENT_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MegaPart createUpdatedEntity() {
        return new MegaPart()
            .releaseDate(UPDATED_RELEASE_DATE)
            .partNumber(UPDATED_PART_NUMBER)
            .nameEN(UPDATED_NAME_EN)
            .nameES(UPDATED_NAME_ES)
            .nameDE(UPDATED_NAME_DE)
            .nameFR(UPDATED_NAME_FR)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .attributes(UPDATED_ATTRIBUTES)
            .attributesContentType(UPDATED_ATTRIBUTES_CONTENT_TYPE);
    }

    @BeforeEach
    void initTest() {
        megaPart = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMegaPart != null) {
            megaPartRepository.delete(insertedMegaPart);
            insertedMegaPart = null;
        }
    }

    @Test
    @Transactional
    void createMegaPart() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MegaPart
        MegaPartDTO megaPartDTO = megaPartMapper.toDto(megaPart);
        var returnedMegaPartDTO = om.readValue(
            restMegaPartMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(megaPartDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MegaPartDTO.class
        );

        // Validate the MegaPart in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMegaPart = megaPartMapper.toEntity(returnedMegaPartDTO);
        assertMegaPartUpdatableFieldsEquals(returnedMegaPart, getPersistedMegaPart(returnedMegaPart));

        insertedMegaPart = returnedMegaPart;
    }

    @Test
    @Transactional
    void createMegaPartWithExistingId() throws Exception {
        // Create the MegaPart with an existing ID
        megaPart.setId(1L);
        MegaPartDTO megaPartDTO = megaPartMapper.toDto(megaPart);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMegaPartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(megaPartDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MegaPart in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPartNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        megaPart.setPartNumber(null);

        // Create the MegaPart, which fails.
        MegaPartDTO megaPartDTO = megaPartMapper.toDto(megaPart);

        restMegaPartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(megaPartDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameENIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        megaPart.setNameEN(null);

        // Create the MegaPart, which fails.
        MegaPartDTO megaPartDTO = megaPartMapper.toDto(megaPart);

        restMegaPartMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(megaPartDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMegaParts() throws Exception {
        // Initialize the database
        insertedMegaPart = megaPartRepository.saveAndFlush(megaPart);

        // Get all the megaPartList
        restMegaPartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(megaPart.getId().intValue())))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].partNumber").value(hasItem(DEFAULT_PART_NUMBER)))
            .andExpect(jsonPath("$.[*].nameEN").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameES").value(hasItem(DEFAULT_NAME_ES)))
            .andExpect(jsonPath("$.[*].nameDE").value(hasItem(DEFAULT_NAME_DE)))
            .andExpect(jsonPath("$.[*].nameFR").value(hasItem(DEFAULT_NAME_FR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].attributesContentType").value(hasItem(DEFAULT_ATTRIBUTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].attributes").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_ATTRIBUTES))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMegaPartsWithEagerRelationshipsIsEnabled() throws Exception {
        when(megaPartServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMegaPartMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(megaPartServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMegaPartsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(megaPartServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMegaPartMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(megaPartRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMegaPart() throws Exception {
        // Initialize the database
        insertedMegaPart = megaPartRepository.saveAndFlush(megaPart);

        // Get the megaPart
        restMegaPartMockMvc
            .perform(get(ENTITY_API_URL_ID, megaPart.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(megaPart.getId().intValue()))
            .andExpect(jsonPath("$.releaseDate").value(DEFAULT_RELEASE_DATE.toString()))
            .andExpect(jsonPath("$.partNumber").value(DEFAULT_PART_NUMBER))
            .andExpect(jsonPath("$.nameEN").value(DEFAULT_NAME_EN))
            .andExpect(jsonPath("$.nameES").value(DEFAULT_NAME_ES))
            .andExpect(jsonPath("$.nameDE").value(DEFAULT_NAME_DE))
            .andExpect(jsonPath("$.nameFR").value(DEFAULT_NAME_FR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.attributesContentType").value(DEFAULT_ATTRIBUTES_CONTENT_TYPE))
            .andExpect(jsonPath("$.attributes").value(Base64.getEncoder().encodeToString(DEFAULT_ATTRIBUTES)));
    }

    @Test
    @Transactional
    void getNonExistingMegaPart() throws Exception {
        // Get the megaPart
        restMegaPartMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMegaPart() throws Exception {
        // Initialize the database
        insertedMegaPart = megaPartRepository.saveAndFlush(megaPart);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the megaPart
        MegaPart updatedMegaPart = megaPartRepository.findById(megaPart.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMegaPart are not directly saved in db
        em.detach(updatedMegaPart);
        updatedMegaPart
            .releaseDate(UPDATED_RELEASE_DATE)
            .partNumber(UPDATED_PART_NUMBER)
            .nameEN(UPDATED_NAME_EN)
            .nameES(UPDATED_NAME_ES)
            .nameDE(UPDATED_NAME_DE)
            .nameFR(UPDATED_NAME_FR)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .attributes(UPDATED_ATTRIBUTES)
            .attributesContentType(UPDATED_ATTRIBUTES_CONTENT_TYPE);
        MegaPartDTO megaPartDTO = megaPartMapper.toDto(updatedMegaPart);

        restMegaPartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, megaPartDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(megaPartDTO))
            )
            .andExpect(status().isOk());

        // Validate the MegaPart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMegaPartToMatchAllProperties(updatedMegaPart);
    }

    @Test
    @Transactional
    void putNonExistingMegaPart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        megaPart.setId(longCount.incrementAndGet());

        // Create the MegaPart
        MegaPartDTO megaPartDTO = megaPartMapper.toDto(megaPart);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMegaPartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, megaPartDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(megaPartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MegaPart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMegaPart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        megaPart.setId(longCount.incrementAndGet());

        // Create the MegaPart
        MegaPartDTO megaPartDTO = megaPartMapper.toDto(megaPart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMegaPartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(megaPartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MegaPart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMegaPart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        megaPart.setId(longCount.incrementAndGet());

        // Create the MegaPart
        MegaPartDTO megaPartDTO = megaPartMapper.toDto(megaPart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMegaPartMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(megaPartDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MegaPart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMegaPartWithPatch() throws Exception {
        // Initialize the database
        insertedMegaPart = megaPartRepository.saveAndFlush(megaPart);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the megaPart using partial update
        MegaPart partialUpdatedMegaPart = new MegaPart();
        partialUpdatedMegaPart.setId(megaPart.getId());

        partialUpdatedMegaPart
            .partNumber(UPDATED_PART_NUMBER)
            .nameEN(UPDATED_NAME_EN)
            .description(UPDATED_DESCRIPTION)
            .attributes(UPDATED_ATTRIBUTES)
            .attributesContentType(UPDATED_ATTRIBUTES_CONTENT_TYPE);

        restMegaPartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMegaPart.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMegaPart))
            )
            .andExpect(status().isOk());

        // Validate the MegaPart in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMegaPartUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMegaPart, megaPart), getPersistedMegaPart(megaPart));
    }

    @Test
    @Transactional
    void fullUpdateMegaPartWithPatch() throws Exception {
        // Initialize the database
        insertedMegaPart = megaPartRepository.saveAndFlush(megaPart);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the megaPart using partial update
        MegaPart partialUpdatedMegaPart = new MegaPart();
        partialUpdatedMegaPart.setId(megaPart.getId());

        partialUpdatedMegaPart
            .releaseDate(UPDATED_RELEASE_DATE)
            .partNumber(UPDATED_PART_NUMBER)
            .nameEN(UPDATED_NAME_EN)
            .nameES(UPDATED_NAME_ES)
            .nameDE(UPDATED_NAME_DE)
            .nameFR(UPDATED_NAME_FR)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .attributes(UPDATED_ATTRIBUTES)
            .attributesContentType(UPDATED_ATTRIBUTES_CONTENT_TYPE);

        restMegaPartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMegaPart.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMegaPart))
            )
            .andExpect(status().isOk());

        // Validate the MegaPart in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMegaPartUpdatableFieldsEquals(partialUpdatedMegaPart, getPersistedMegaPart(partialUpdatedMegaPart));
    }

    @Test
    @Transactional
    void patchNonExistingMegaPart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        megaPart.setId(longCount.incrementAndGet());

        // Create the MegaPart
        MegaPartDTO megaPartDTO = megaPartMapper.toDto(megaPart);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMegaPartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, megaPartDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(megaPartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MegaPart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMegaPart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        megaPart.setId(longCount.incrementAndGet());

        // Create the MegaPart
        MegaPartDTO megaPartDTO = megaPartMapper.toDto(megaPart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMegaPartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(megaPartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MegaPart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMegaPart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        megaPart.setId(longCount.incrementAndGet());

        // Create the MegaPart
        MegaPartDTO megaPartDTO = megaPartMapper.toDto(megaPart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMegaPartMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(megaPartDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MegaPart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMegaPart() throws Exception {
        // Initialize the database
        insertedMegaPart = megaPartRepository.saveAndFlush(megaPart);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the megaPart
        restMegaPartMockMvc
            .perform(delete(ENTITY_API_URL_ID, megaPart.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return megaPartRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected MegaPart getPersistedMegaPart(MegaPart megaPart) {
        return megaPartRepository.findById(megaPart.getId()).orElseThrow();
    }

    protected void assertPersistedMegaPartToMatchAllProperties(MegaPart expectedMegaPart) {
        assertMegaPartAllPropertiesEquals(expectedMegaPart, getPersistedMegaPart(expectedMegaPart));
    }

    protected void assertPersistedMegaPartToMatchUpdatableProperties(MegaPart expectedMegaPart) {
        assertMegaPartAllUpdatablePropertiesEquals(expectedMegaPart, getPersistedMegaPart(expectedMegaPart));
    }
}
