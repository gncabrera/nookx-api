package com.nookx.api.web.rest;

import static com.nookx.api.domain.MegaSetAsserts.*;
import static com.nookx.api.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.nookx.api.IntegrationTest;
import com.nookx.api.domain.MegaSet;
import com.nookx.api.repository.MegaSetRepository;
import com.nookx.api.service.dto.MegaSetDTO;
import com.nookx.api.service.mapper.MegaSetMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MegaSetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MegaSetResourceIT {

    private static final String DEFAULT_SET_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SET_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_RELEASE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RELEASE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_EN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_EN = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_ES = "AAAAAAAAAA";
    private static final String UPDATED_NAME_ES = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_DE = "AAAAAAAAAA";
    private static final String UPDATED_NAME_DE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_FR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_FR = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_EN = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_EN = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_ES = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_ES = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_DE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_DE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_FR = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_FR = "BBBBBBBBBB";

    private static final JsonNode DEFAULT_ATTRIBUTES = JsonNodeFactory.instance.objectNode().put("v", "0");
    private static final JsonNode UPDATED_ATTRIBUTES = JsonNodeFactory.instance.objectNode().put("v", "1");
    private static final String DEFAULT_ATTRIBUTES_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ATTRIBUTES_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/mega-sets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MegaSetRepository megaSetRepository;

    @Autowired
    private MegaSetMapper megaSetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMegaSetMockMvc;

    private MegaSet megaSet;

    private MegaSet insertedMegaSet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MegaSet createEntity() {
        return new MegaSet()
            .setNumber(DEFAULT_SET_NUMBER)
            .releaseDate(DEFAULT_RELEASE_DATE)
            .notes(DEFAULT_NOTES)
            .nameEN(DEFAULT_NAME_EN)
            .nameES(DEFAULT_NAME_ES)
            .nameDE(DEFAULT_NAME_DE)
            .nameFR(DEFAULT_NAME_FR)
            .descriptionEN(DEFAULT_DESCRIPTION_EN)
            .descriptionES(DEFAULT_DESCRIPTION_ES)
            .descriptionDE(DEFAULT_DESCRIPTION_DE)
            .descriptionFR(DEFAULT_DESCRIPTION_FR)
            .attributes(DEFAULT_ATTRIBUTES)
            .attributesContentType(DEFAULT_ATTRIBUTES_CONTENT_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MegaSet createUpdatedEntity() {
        return new MegaSet()
            .setNumber(UPDATED_SET_NUMBER)
            .releaseDate(UPDATED_RELEASE_DATE)
            .notes(UPDATED_NOTES)
            .nameEN(UPDATED_NAME_EN)
            .nameES(UPDATED_NAME_ES)
            .nameDE(UPDATED_NAME_DE)
            .nameFR(UPDATED_NAME_FR)
            .descriptionEN(UPDATED_DESCRIPTION_EN)
            .descriptionES(UPDATED_DESCRIPTION_ES)
            .descriptionDE(UPDATED_DESCRIPTION_DE)
            .descriptionFR(UPDATED_DESCRIPTION_FR)
            .attributes(UPDATED_ATTRIBUTES)
            .attributesContentType(UPDATED_ATTRIBUTES_CONTENT_TYPE);
    }

    @BeforeEach
    void initTest() {
        megaSet = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMegaSet != null) {
            megaSetRepository.delete(insertedMegaSet);
            insertedMegaSet = null;
        }
    }

    @Test
    @Transactional
    void createMegaSet() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MegaSet
        MegaSetDTO megaSetDTO = megaSetMapper.toDto(megaSet);
        var returnedMegaSetDTO = om.readValue(
            restMegaSetMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(megaSetDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MegaSetDTO.class
        );

        // Validate the MegaSet in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMegaSet = megaSetMapper.toEntity(returnedMegaSetDTO);
        assertMegaSetUpdatableFieldsEquals(returnedMegaSet, getPersistedMegaSet(returnedMegaSet));

        insertedMegaSet = returnedMegaSet;
    }

    @Test
    @Transactional
    void createMegaSetWithExistingId() throws Exception {
        // Create the MegaSet with an existing ID
        megaSet.setId(1L);
        MegaSetDTO megaSetDTO = megaSetMapper.toDto(megaSet);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMegaSetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(megaSetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MegaSet in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSetNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        megaSet.setSetNumber(null);

        // Create the MegaSet, which fails.
        MegaSetDTO megaSetDTO = megaSetMapper.toDto(megaSet);

        restMegaSetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(megaSetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameENIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        megaSet.setNameEN(null);

        // Create the MegaSet, which fails.
        MegaSetDTO megaSetDTO = megaSetMapper.toDto(megaSet);

        restMegaSetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(megaSetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionENIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        megaSet.setDescriptionEN(null);

        // Create the MegaSet, which fails.
        MegaSetDTO megaSetDTO = megaSetMapper.toDto(megaSet);

        restMegaSetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(megaSetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMegaSets() throws Exception {
        // Initialize the database
        insertedMegaSet = megaSetRepository.saveAndFlush(megaSet);

        // Get all the megaSetList
        restMegaSetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(megaSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].setNumber").value(hasItem(DEFAULT_SET_NUMBER)))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].nameEN").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameES").value(hasItem(DEFAULT_NAME_ES)))
            .andExpect(jsonPath("$.[*].nameDE").value(hasItem(DEFAULT_NAME_DE)))
            .andExpect(jsonPath("$.[*].nameFR").value(hasItem(DEFAULT_NAME_FR)))
            .andExpect(jsonPath("$.[*].descriptionEN").value(hasItem(DEFAULT_DESCRIPTION_EN)))
            .andExpect(jsonPath("$.[*].descriptionES").value(hasItem(DEFAULT_DESCRIPTION_ES)))
            .andExpect(jsonPath("$.[*].descriptionDE").value(hasItem(DEFAULT_DESCRIPTION_DE)))
            .andExpect(jsonPath("$.[*].descriptionFR").value(hasItem(DEFAULT_DESCRIPTION_FR)))
            .andExpect(jsonPath("$.[*].attributesContentType").value(hasItem(DEFAULT_ATTRIBUTES_CONTENT_TYPE)));
    }

    @Test
    @Transactional
    void getMegaSet() throws Exception {
        // Initialize the database
        insertedMegaSet = megaSetRepository.saveAndFlush(megaSet);

        // Get the megaSet
        restMegaSetMockMvc
            .perform(get(ENTITY_API_URL_ID, megaSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(megaSet.getId().intValue()))
            .andExpect(jsonPath("$.setNumber").value(DEFAULT_SET_NUMBER))
            .andExpect(jsonPath("$.releaseDate").value(DEFAULT_RELEASE_DATE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.nameEN").value(DEFAULT_NAME_EN))
            .andExpect(jsonPath("$.nameES").value(DEFAULT_NAME_ES))
            .andExpect(jsonPath("$.nameDE").value(DEFAULT_NAME_DE))
            .andExpect(jsonPath("$.nameFR").value(DEFAULT_NAME_FR))
            .andExpect(jsonPath("$.descriptionEN").value(DEFAULT_DESCRIPTION_EN))
            .andExpect(jsonPath("$.descriptionES").value(DEFAULT_DESCRIPTION_ES))
            .andExpect(jsonPath("$.descriptionDE").value(DEFAULT_DESCRIPTION_DE))
            .andExpect(jsonPath("$.descriptionFR").value(DEFAULT_DESCRIPTION_FR))
            .andExpect(jsonPath("$.attributesContentType").value(DEFAULT_ATTRIBUTES_CONTENT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingMegaSet() throws Exception {
        // Get the megaSet
        restMegaSetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMegaSet() throws Exception {
        // Initialize the database
        insertedMegaSet = megaSetRepository.saveAndFlush(megaSet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the megaSet
        MegaSet updatedMegaSet = megaSetRepository.findById(megaSet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMegaSet are not directly saved in db
        em.detach(updatedMegaSet);
        updatedMegaSet
            .setNumber(UPDATED_SET_NUMBER)
            .releaseDate(UPDATED_RELEASE_DATE)
            .notes(UPDATED_NOTES)
            .nameEN(UPDATED_NAME_EN)
            .nameES(UPDATED_NAME_ES)
            .nameDE(UPDATED_NAME_DE)
            .nameFR(UPDATED_NAME_FR)
            .descriptionEN(UPDATED_DESCRIPTION_EN)
            .descriptionES(UPDATED_DESCRIPTION_ES)
            .descriptionDE(UPDATED_DESCRIPTION_DE)
            .descriptionFR(UPDATED_DESCRIPTION_FR)
            .attributes(UPDATED_ATTRIBUTES)
            .attributesContentType(UPDATED_ATTRIBUTES_CONTENT_TYPE);
        MegaSetDTO megaSetDTO = megaSetMapper.toDto(updatedMegaSet);

        restMegaSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, megaSetDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(megaSetDTO))
            )
            .andExpect(status().isOk());

        // Validate the MegaSet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMegaSetToMatchAllProperties(updatedMegaSet);
    }

    @Test
    @Transactional
    void putNonExistingMegaSet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        megaSet.setId(longCount.incrementAndGet());

        // Create the MegaSet
        MegaSetDTO megaSetDTO = megaSetMapper.toDto(megaSet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMegaSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, megaSetDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(megaSetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MegaSet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMegaSet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        megaSet.setId(longCount.incrementAndGet());

        // Create the MegaSet
        MegaSetDTO megaSetDTO = megaSetMapper.toDto(megaSet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMegaSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(megaSetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MegaSet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMegaSet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        megaSet.setId(longCount.incrementAndGet());

        // Create the MegaSet
        MegaSetDTO megaSetDTO = megaSetMapper.toDto(megaSet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMegaSetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(megaSetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MegaSet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMegaSetWithPatch() throws Exception {
        // Initialize the database
        insertedMegaSet = megaSetRepository.saveAndFlush(megaSet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the megaSet using partial update
        MegaSet partialUpdatedMegaSet = new MegaSet();
        partialUpdatedMegaSet.setId(megaSet.getId());

        partialUpdatedMegaSet
            .setNumber(UPDATED_SET_NUMBER)
            .releaseDate(UPDATED_RELEASE_DATE)
            .notes(UPDATED_NOTES)
            .nameEN(UPDATED_NAME_EN)
            .nameDE(UPDATED_NAME_DE)
            .descriptionFR(UPDATED_DESCRIPTION_FR)
            .attributes(UPDATED_ATTRIBUTES)
            .attributesContentType(UPDATED_ATTRIBUTES_CONTENT_TYPE);

        restMegaSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMegaSet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMegaSet))
            )
            .andExpect(status().isOk());

        // Validate the MegaSet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMegaSetUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMegaSet, megaSet), getPersistedMegaSet(megaSet));
    }

    @Test
    @Transactional
    void fullUpdateMegaSetWithPatch() throws Exception {
        // Initialize the database
        insertedMegaSet = megaSetRepository.saveAndFlush(megaSet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the megaSet using partial update
        MegaSet partialUpdatedMegaSet = new MegaSet();
        partialUpdatedMegaSet.setId(megaSet.getId());

        partialUpdatedMegaSet
            .setNumber(UPDATED_SET_NUMBER)
            .releaseDate(UPDATED_RELEASE_DATE)
            .notes(UPDATED_NOTES)
            .nameEN(UPDATED_NAME_EN)
            .nameES(UPDATED_NAME_ES)
            .nameDE(UPDATED_NAME_DE)
            .nameFR(UPDATED_NAME_FR)
            .descriptionEN(UPDATED_DESCRIPTION_EN)
            .descriptionES(UPDATED_DESCRIPTION_ES)
            .descriptionDE(UPDATED_DESCRIPTION_DE)
            .descriptionFR(UPDATED_DESCRIPTION_FR)
            .attributes(UPDATED_ATTRIBUTES)
            .attributesContentType(UPDATED_ATTRIBUTES_CONTENT_TYPE);

        restMegaSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMegaSet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMegaSet))
            )
            .andExpect(status().isOk());

        // Validate the MegaSet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMegaSetUpdatableFieldsEquals(partialUpdatedMegaSet, getPersistedMegaSet(partialUpdatedMegaSet));
    }

    @Test
    @Transactional
    void patchNonExistingMegaSet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        megaSet.setId(longCount.incrementAndGet());

        // Create the MegaSet
        MegaSetDTO megaSetDTO = megaSetMapper.toDto(megaSet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMegaSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, megaSetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(megaSetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MegaSet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMegaSet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        megaSet.setId(longCount.incrementAndGet());

        // Create the MegaSet
        MegaSetDTO megaSetDTO = megaSetMapper.toDto(megaSet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMegaSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(megaSetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MegaSet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMegaSet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        megaSet.setId(longCount.incrementAndGet());

        // Create the MegaSet
        MegaSetDTO megaSetDTO = megaSetMapper.toDto(megaSet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMegaSetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(megaSetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MegaSet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMegaSet() throws Exception {
        // Initialize the database
        insertedMegaSet = megaSetRepository.saveAndFlush(megaSet);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the megaSet
        restMegaSetMockMvc
            .perform(delete(ENTITY_API_URL_ID, megaSet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return megaSetRepository.count();
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

    protected MegaSet getPersistedMegaSet(MegaSet megaSet) {
        return megaSetRepository.findById(megaSet.getId()).orElseThrow();
    }

    protected void assertPersistedMegaSetToMatchAllProperties(MegaSet expectedMegaSet) {
        assertMegaSetAllPropertiesEquals(expectedMegaSet, getPersistedMegaSet(expectedMegaSet));
    }

    protected void assertPersistedMegaSetToMatchUpdatableProperties(MegaSet expectedMegaSet) {
        assertMegaSetAllUpdatablePropertiesEquals(expectedMegaSet, getPersistedMegaSet(expectedMegaSet));
    }
}
