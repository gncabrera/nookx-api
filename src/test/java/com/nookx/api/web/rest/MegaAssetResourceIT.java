package com.nookx.api.web.rest;

import static com.nookx.api.domain.MegaAssetAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nookx.api.IntegrationTest;
import com.nookx.api.domain.MegaAsset;
import com.nookx.api.domain.enumeration.AssetType;
import com.nookx.api.repository.MegaAssetRepository;
import com.nookx.api.repository.UserRepository;
import com.nookx.api.service.dto.MegaAssetDTO;
import com.nookx.api.service.mapper.MegaAssetMapper;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MegaAssetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MegaAssetResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final AssetType DEFAULT_TYPE = AssetType.IMAGE;
    private static final AssetType UPDATED_TYPE = AssetType.FILE;

    private static final String ENTITY_API_URL = "/api/client/assets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_API_URL_DL = ENTITY_API_URL + "/dl/{uuid}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MegaAssetRepository megaAssetRepository;

    @Autowired
    private MegaAssetMapper megaAssetMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc restMegaAssetMockMvc;

    private MegaAsset megaAsset;

    private MegaAsset insertedMegaAsset;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MegaAsset createEntity() {
        return new MegaAsset().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).path(DEFAULT_PATH).type(DEFAULT_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MegaAsset createUpdatedEntity() {
        return new MegaAsset().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).path(UPDATED_PATH).type(UPDATED_TYPE);
    }

    @BeforeEach
    void initTest() {
        megaAsset = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMegaAsset != null) {
            megaAssetRepository.delete(insertedMegaAsset);
            insertedMegaAsset = null;
        }
    }

    @Test
    @Transactional
    void createMegaAsset() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MegaAsset
        MegaAssetDTO megaAssetDTO = megaAssetMapper.toDto(megaAsset);
        var returnedMegaAssetDTO = om.readValue(
            restMegaAssetMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(megaAssetDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MegaAssetDTO.class
        );

        // Validate the MegaAsset in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMegaAsset = megaAssetMapper.toEntity(returnedMegaAssetDTO);
        assertMegaAssetUpdatableFieldsEquals(returnedMegaAsset, getPersistedMegaAsset(returnedMegaAsset));

        insertedMegaAsset = returnedMegaAsset;
    }

    @Test
    @Transactional
    void uploadMegaAsset() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "hello".getBytes());
        long userId = userRepository.findOneByLogin("user").orElseThrow().getId();
        MegaAssetDTO returnedMegaAssetDTO = om.readValue(
            restMegaAssetMockMvc
                .perform(multipart(ENTITY_API_URL + "/upload").file(file).param("description", "my doc").param("isPublic", "true"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MegaAssetDTO.class
        );

        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertThat(returnedMegaAssetDTO.getName()).isEqualTo("hello.txt");
        assertThat(returnedMegaAssetDTO.getDescription()).isEqualTo("my doc");
        assertThat(returnedMegaAssetDTO.getType()).isEqualTo(AssetType.FILE);
        assertThat(returnedMegaAssetDTO.getContentType()).isEqualTo(MediaType.TEXT_PLAIN_VALUE);
        assertThat(returnedMegaAssetDTO.getSizeBytes()).isEqualTo(5L);
        assertThat(returnedMegaAssetDTO.getPath()).endsWith(".txt");
        assertThat(returnedMegaAssetDTO.getUploadedById()).isEqualTo(userId);
        assertThat(returnedMegaAssetDTO.isPublic()).isTrue();

        insertedMegaAsset = megaAssetMapper.toEntity(returnedMegaAssetDTO);
    }

    @Test
    @Transactional
    void uploadMegaAssetIsPublicDefaultsToFalse() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "x.txt", MediaType.TEXT_PLAIN_VALUE, "x".getBytes());
        MegaAssetDTO dto = om.readValue(
            restMegaAssetMockMvc
                .perform(multipart(ENTITY_API_URL + "/upload").file(file))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MegaAssetDTO.class
        );
        assertThat(dto.isPublic()).isFalse();
        insertedMegaAsset = megaAssetMapper.toEntity(dto);
    }

    @Test
    @Transactional
    void uploadMegaAssetEmptyFile() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        MockMultipartFile file = new MockMultipartFile("file", "empty.txt", MediaType.TEXT_PLAIN_VALUE, new byte[0]);
        restMegaAssetMockMvc.perform(multipart(ENTITY_API_URL + "/upload").file(file)).andExpect(status().isBadRequest());
        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void createMegaAssetWithExistingId() throws Exception {
        // Create the MegaAsset with an existing ID
        megaAsset.setId(1L);
        MegaAssetDTO megaAssetDTO = megaAssetMapper.toDto(megaAsset);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMegaAssetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(megaAssetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MegaAsset in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        megaAsset.setName(null);

        // Create the MegaAsset, which fails.
        MegaAssetDTO megaAssetDTO = megaAssetMapper.toDto(megaAsset);

        restMegaAssetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(megaAssetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPathIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        megaAsset.setPath(null);

        // Create the MegaAsset, which fails.
        MegaAssetDTO megaAssetDTO = megaAssetMapper.toDto(megaAsset);

        restMegaAssetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(megaAssetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMegaAssets() throws Exception {
        // List endpoint is not exposed under /api/client/assets
        restMegaAssetMockMvc.perform(get(ENTITY_API_URL + "?sort=id,desc")).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void downloadMegaAsset() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "hello".getBytes());
        MegaAssetDTO uploaded = om.readValue(
            restMegaAssetMockMvc
                .perform(multipart(ENTITY_API_URL + "/upload").file(file))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MegaAssetDTO.class
        );
        insertedMegaAsset = megaAssetMapper.toEntity(uploaded);

        restMegaAssetMockMvc
            .perform(get(ENTITY_API_URL_DL, uploaded.getPath()))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
            .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, containsString("hello.txt")))
            .andExpect(content().bytes("hello".getBytes()));
    }

    @Test
    @Transactional
    void downloadMegaAssetNotFoundWhenFileMissing() throws Exception {
        insertedMegaAsset = megaAssetRepository.saveAndFlush(megaAsset);
        restMegaAssetMockMvc.perform(get(ENTITY_API_URL_DL, megaAsset.getPath())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void getNonExistingMegaAsset() throws Exception {
        // Get the megaAsset
        restMegaAssetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMegaAsset() throws Exception {
        insertedMegaAsset = megaAssetRepository.saveAndFlush(megaAsset);
        long databaseSizeBeforeUpdate = getRepositoryCount();
        MegaAssetDTO megaAssetDTO = megaAssetMapper.toDto(megaAsset);
        restMegaAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, megaAssetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(megaAssetDTO))
            )
            .andExpect(status().isMethodNotAllowed());
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putNonExistingMegaAsset() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        megaAsset.setId(longCount.incrementAndGet());

        // Create the MegaAsset
        MegaAssetDTO megaAssetDTO = megaAssetMapper.toDto(megaAsset);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMegaAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, megaAssetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(megaAssetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMegaAsset() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        megaAsset.setId(longCount.incrementAndGet());

        // Create the MegaAsset
        MegaAssetDTO megaAssetDTO = megaAssetMapper.toDto(megaAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMegaAssetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(megaAssetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMegaAsset() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        megaAsset.setId(longCount.incrementAndGet());

        // Create the MegaAsset
        MegaAssetDTO megaAssetDTO = megaAssetMapper.toDto(megaAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMegaAssetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(megaAssetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MegaAsset in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMegaAssetWithPatch() throws Exception {
        // Initialize the database
        insertedMegaAsset = megaAssetRepository.saveAndFlush(megaAsset);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the megaAsset using partial update
        MegaAsset partialUpdatedMegaAsset = new MegaAsset();
        partialUpdatedMegaAsset.setId(megaAsset.getId());

        partialUpdatedMegaAsset.name(UPDATED_NAME).path(UPDATED_PATH).type(UPDATED_TYPE);

        restMegaAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMegaAsset.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMegaAsset))
            )
            .andExpect(status().isMethodNotAllowed());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void fullUpdateMegaAssetWithPatch() throws Exception {
        // Initialize the database
        insertedMegaAsset = megaAssetRepository.saveAndFlush(megaAsset);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the megaAsset using partial update
        MegaAsset partialUpdatedMegaAsset = new MegaAsset();
        partialUpdatedMegaAsset.setId(megaAsset.getId());

        partialUpdatedMegaAsset.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).path(UPDATED_PATH).type(UPDATED_TYPE);

        restMegaAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMegaAsset.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMegaAsset))
            )
            .andExpect(status().isMethodNotAllowed());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchNonExistingMegaAsset() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        megaAsset.setId(longCount.incrementAndGet());

        // Create the MegaAsset
        MegaAssetDTO megaAssetDTO = megaAssetMapper.toDto(megaAsset);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMegaAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, megaAssetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(megaAssetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMegaAsset() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        megaAsset.setId(longCount.incrementAndGet());

        // Create the MegaAsset
        MegaAssetDTO megaAssetDTO = megaAssetMapper.toDto(megaAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMegaAssetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(megaAssetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMegaAsset() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        megaAsset.setId(longCount.incrementAndGet());

        // Create the MegaAsset
        MegaAssetDTO megaAssetDTO = megaAssetMapper.toDto(megaAsset);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMegaAssetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(megaAssetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MegaAsset in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMegaAsset() throws Exception {
        // Initialize the database
        insertedMegaAsset = megaAssetRepository.saveAndFlush(megaAsset);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the megaAsset
        restMegaAssetMockMvc
            .perform(delete(ENTITY_API_URL_ID, megaAsset.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return megaAssetRepository.count();
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

    protected MegaAsset getPersistedMegaAsset(MegaAsset megaAsset) {
        return megaAssetRepository.findById(megaAsset.getId()).orElseThrow();
    }

    protected void assertPersistedMegaAssetToMatchAllProperties(MegaAsset expectedMegaAsset) {
        assertMegaAssetAllPropertiesEquals(expectedMegaAsset, getPersistedMegaAsset(expectedMegaAsset));
    }

    protected void assertPersistedMegaAssetToMatchUpdatableProperties(MegaAsset expectedMegaAsset) {
        assertMegaAssetAllUpdatablePropertiesEquals(expectedMegaAsset, getPersistedMegaAsset(expectedMegaAsset));
    }
}
