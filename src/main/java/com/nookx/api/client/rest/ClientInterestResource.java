package com.nookx.api.client.rest;

import com.nookx.api.client.dto.ClientInterestDTO;
import com.nookx.api.client.dto.ClientInterestSubscribeDTO;
import com.nookx.api.repository.InterestRepository;
import com.nookx.api.service.InterestService;
import com.nookx.api.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nookx.api.domain.Interest}.
 */
@RestController
@RequestMapping("/api/client/interests")
public class ClientInterestResource {

    private static final Logger LOG = LoggerFactory.getLogger(ClientInterestResource.class);

    private static final String ENTITY_NAME = "interest";

    @Value("${jhipster.clientApp.name:nookx}")
    private String applicationName;

    private final InterestService interestService;

    private final InterestRepository interestRepository;

    public ClientInterestResource(InterestService interestService, InterestRepository interestRepository) {
        this.interestService = interestService;
        this.interestRepository = interestRepository;
    }

    @PostMapping("")
    public ResponseEntity<ClientInterestDTO> createInterest(@Valid @RequestBody ClientInterestDTO interestDTO) throws URISyntaxException {
        LOG.debug("REST request to save Interest : {}", interestDTO);
        if (interestDTO.getId() != null) {
            throw new BadRequestAlertException("A new interest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        interestDTO = interestService.save(interestDTO);
        return ResponseEntity.created(new URI("/api/interests/" + interestDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, interestDTO.getId().toString()))
            .body(interestDTO);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Void> subscribeToInterests(@Valid @RequestBody ClientInterestSubscribeDTO subscribeDTO) {
        LOG.debug("REST request to subscribe current profile to interests : {}", subscribeDTO.getInterestIds());
        interestService.subscribeCurrentProfileToInterests(subscribeDTO.getInterestIds());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribeFromInterests(@Valid @RequestBody ClientInterestSubscribeDTO subscribeDTO) {
        LOG.debug("REST request to unsubscribe current profile from interests : {}", subscribeDTO.getInterestIds());
        interestService.unsubscribeCurrentProfileFromInterests(subscribeDTO.getInterestIds());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientInterestDTO> updateInterest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClientInterestDTO interestDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Interest : {}, {}", id, interestDTO);
        if (interestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, interestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        interestDTO = interestService.update(interestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, interestDTO.getId().toString()))
            .body(interestDTO);
    }

    @GetMapping("")
    public List<ClientInterestDTO> getAllInterests() {
        LOG.debug("REST request to get all Interests");
        return interestService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientInterestDTO> getInterest(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Interest : {}", id);
        Optional<ClientInterestDTO> interestDTO = interestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(interestDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterest(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Interest : {}", id);
        interestService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
