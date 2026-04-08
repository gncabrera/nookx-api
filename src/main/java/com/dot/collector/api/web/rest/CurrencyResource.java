package com.dot.collector.api.web.rest;

import com.dot.collector.api.repository.CurrencyRepository;
import com.dot.collector.api.service.CurrencyService;
import com.dot.collector.api.service.dto.CurrencyDTO;
import com.dot.collector.api.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.dot.collector.api.domain.Currency}.
 */
@RestController
@RequestMapping("/api/currencies")
public class CurrencyResource {

    private static final Logger LOG = LoggerFactory.getLogger(CurrencyResource.class);

    private static final String ENTITY_NAME = "currency";

    @Value("${jhipster.clientApp.name:dotCollector}")
    private String applicationName;

    private final CurrencyService currencyService;

    private final CurrencyRepository currencyRepository;

    public CurrencyResource(CurrencyService currencyService, CurrencyRepository currencyRepository) {
        this.currencyService = currencyService;
        this.currencyRepository = currencyRepository;
    }

    @PostMapping("")
    public ResponseEntity<CurrencyDTO> createCurrency(@Valid @RequestBody CurrencyDTO currencyDTO) throws URISyntaxException {
        LOG.debug("REST request to save Currency : {}", currencyDTO);
        if (currencyDTO.getId() != null) {
            throw new BadRequestAlertException("A new currency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        currencyDTO = currencyService.save(currencyDTO);
        return ResponseEntity.created(new URI("/api/currencies/" + currencyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, currencyDTO.getId().toString()))
            .body(currencyDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CurrencyDTO> updateCurrency(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CurrencyDTO currencyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Currency : {}, {}", id, currencyDTO);
        if (currencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!currencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        currencyDTO = currencyService.update(currencyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, currencyDTO.getId().toString()))
            .body(currencyDTO);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CurrencyDTO> partialUpdateCurrency(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CurrencyDTO currencyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Currency partially : {}, {}", id, currencyDTO);
        if (currencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!currencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CurrencyDTO> result = currencyService.partialUpdate(currencyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, currencyDTO.getId().toString())
        );
    }

    @GetMapping("")
    public List<CurrencyDTO> getAllCurrencies() {
        LOG.debug("REST request to get all Currencies");
        return currencyService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyDTO> getCurrency(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Currency : {}", id);
        Optional<CurrencyDTO> currencyDTO = currencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(currencyDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Currency : {}", id);
        currencyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
