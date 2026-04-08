package com.dot.collector.api.client.rest;

import com.dot.collector.api.client.dto.ClientCollectionDTO;
import com.dot.collector.api.client.dto.ClientCollectionLiteDTO;
import com.dot.collector.api.client.service.ClientCollectionService;
import com.dot.collector.api.domain.enumeration.ProfileCollectionType;
import com.dot.collector.api.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for client {@link com.dot.collector.api.client.dto.ClientCollectionDTO} operations.
 */
@RestController
@RequestMapping("/api/client/collections")
public class ClientCollectionResource {

    private static final Logger LOG = LoggerFactory.getLogger(ClientCollectionResource.class);

    private static final String ENTITY_NAME = "clientCollection";

    @Value("${jhipster.clientApp.name:dotCollector}")
    private String applicationName;

    private final ClientCollectionService clientCollectionService;

    public ClientCollectionResource(ClientCollectionService clientCollectionService) {
        this.clientCollectionService = clientCollectionService;
    }

    /**
     * {@code GET  /client-collections} : get collections for the current user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list in body.
     */
    @GetMapping("")
    public List<ClientCollectionLiteDTO> getUserCollections() {
        LOG.debug("REST request to get user ClientCollections");
        return clientCollectionService.getUserCollections();
    }

    /**
     * {@code GET  /collections/types} : get all {@link ProfileCollectionType} values as strings.
     *
     * @return the list of enum constant names.
     */
    @GetMapping("/types")
    public List<String> getCollectionTypes() {
        LOG.debug("REST request to get ProfileCollectionType values");
        return Arrays.stream(ProfileCollectionType.values()).map(Enum::name).toList();
    }

    /**
     * {@code GET  /client-collections/:id} : get the collection by id.
     *
     * @param id the id of the collection to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the DTO, or {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientCollectionDTO> getCollectionById(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ClientCollection : {}", id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(clientCollectionService.getCollectionById(id)));
    }

    /**
     * {@code POST  /client-collections} : create a new client collection.
     *
     * @param dto the collection to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new DTO,
     * or with status {@code 400 (Bad Request)} if the collection already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ClientCollectionDTO> create(@Valid @RequestBody ClientCollectionDTO dto) throws URISyntaxException {
        LOG.debug("REST request to save ClientCollection : {}", dto);
        if (dto.getId() != null) {
            throw new BadRequestAlertException("A new clientCollection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClientCollectionDTO result = clientCollectionService.create(dto);
        return ResponseEntity.created(new URI("/api/client/collections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /client-collections/:sourceCollectionId/clone} : clone a collection.
     *
     * @param sourceCollectionId id of the collection to clone from.
     * @param dto clone payload.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new DTO,
     * or with status {@code 400 (Bad Request)} if the DTO already has an ID,
     * or {@code 404 (Not Found)} if the clone could not be produced.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/{sourceCollectionId}/clone")
    public ResponseEntity<ClientCollectionDTO> cloneCollection(
        @PathVariable("sourceCollectionId") Long sourceCollectionId,
        @Valid @RequestBody ClientCollectionDTO dto
    ) throws URISyntaxException {
        LOG.debug("REST request to clone ClientCollection from {} : {}", sourceCollectionId, dto);
        ClientCollectionDTO result = clientCollectionService.cloneCollection(sourceCollectionId, dto);
        if (result == null || result.getId() == null) {
            return ResponseUtil.wrapOrNotFound(Optional.empty());
        }
        return ResponseEntity.created(new URI("/api/client/collections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
