package com.nookx.api.web.rest;

import com.nookx.api.domain.User;
import com.nookx.api.repository.MegaAssetRepository;
import com.nookx.api.service.MegaAssetService;
import com.nookx.api.service.UserService;
import com.nookx.api.service.dto.MegaAssetDTO;
import com.nookx.api.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link MegaAsset}.
 */
@RestController
@RequestMapping("/api/client/assets")
public class MegaAssetResource {

    private static final Logger LOG = LoggerFactory.getLogger(MegaAssetResource.class);

    private static final String ENTITY_NAME = "megaAsset";

    @Value("${jhipster.clientApp.name:nookx}")
    private String applicationName;

    private final MegaAssetService megaAssetService;

    private final MegaAssetRepository megaAssetRepository;

    private final UserService userService;

    public MegaAssetResource(MegaAssetService megaAssetService, MegaAssetRepository megaAssetRepository, UserService userService) {
        this.megaAssetService = megaAssetService;
        this.megaAssetRepository = megaAssetRepository;
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<MegaAssetDTO> createMegaAsset(@Valid @RequestBody MegaAssetDTO megaAssetDTO) throws URISyntaxException {
        LOG.debug("REST request to save MegaAsset : {}", megaAssetDTO);
        if (megaAssetDTO.getId() != null) {
            throw new BadRequestAlertException("A new megaAsset cannot already have an ID", ENTITY_NAME, "idexists");
        }
        megaAssetDTO = megaAssetService.save(megaAssetDTO);
        return ResponseEntity.created(new URI("/api/assets/" + megaAssetDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, megaAssetDTO.getId().toString()))
            .body(megaAssetDTO);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MegaAssetDTO> uploadMegaAsset(
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "description", required = false) String description,
        @RequestParam(value = "isPublic", required = false, defaultValue = "false") boolean isPublic
    ) throws URISyntaxException {
        LOG.debug("REST request to upload MegaAsset file");

        MegaAssetDTO megaAssetDTO = megaAssetService.upload(file, description, isPublic);
        return ResponseEntity.created(new URI("/api/assets/" + megaAssetDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, megaAssetDTO.getId().toString()))
            .body(megaAssetDTO);
    }

    @GetMapping("/dl/{uuid}")
    public ResponseEntity<Resource> downloadMegaAsset(@PathVariable("uuid") String uuid) {
        LOG.debug("REST request to download MegaAsset file : {}", uuid);
        return megaAssetService
            .findFileForDownload(uuid)
            .map(dl -> {
                MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
                if (StringUtils.hasText(dl.asset().getContentType())) {
                    try {
                        mediaType = MediaType.parseMediaType(dl.asset().getContentType());
                    } catch (IllegalArgumentException ex) {
                        LOG.debug("Ignoring invalid content type for asset {}: {}", uuid, dl.asset().getContentType());
                    }
                }
                String filename = StringUtils.hasText(dl.asset().getName()) ? dl.asset().getName() : "download";
                ContentDisposition disposition = ContentDisposition.attachment().filename(filename, StandardCharsets.UTF_8).build();
                return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                    .body(dl.resource());
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMegaAsset(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MegaAsset : {}", id);
        megaAssetService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
