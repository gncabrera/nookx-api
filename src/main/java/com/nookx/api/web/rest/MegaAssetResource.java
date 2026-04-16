package com.nookx.api.web.rest;

import com.nookx.api.domain.enumeration.AssetType;
import com.nookx.api.domain.enumeration.AttachmentType;
import com.nookx.api.domain.enumeration.MegaAssetImageSize;
import com.nookx.api.service.MegaAssetService;
import com.nookx.api.service.dto.MegaAssetDTO;
import com.nookx.api.service.upload.AssetUploadLinkContext;
import com.nookx.api.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Optional;
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
 * REST controller for managing {@link com.nookx.api.domain.MegaAsset}.
 */
@RestController
@RequestMapping("/api/client/assets")
public class MegaAssetResource {

    private static final Logger LOG = LoggerFactory.getLogger(MegaAssetResource.class);

    private static final String ENTITY_NAME = "megaAsset";

    @Value("${jhipster.clientApp.name:nookx}")
    private String applicationName;

    private final MegaAssetService megaAssetService;

    public MegaAssetResource(MegaAssetService megaAssetService) {
        this.megaAssetService = megaAssetService;
    }

    @PostMapping(value = "/upload/{type}/{entityId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MegaAssetDTO> uploadMegaAssetForTarget(
        @PathVariable("type") String type,
        @PathVariable("entityId") Long entityId,
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "description", required = false) String description,
        @RequestParam(value = "isPublic", required = false, defaultValue = "false") boolean isPublic,
        @RequestParam(value = "sortOrder", required = false) Integer sortOrder,
        @RequestParam(value = "label", required = false) String label,
        @RequestParam(value = "isPrimary", required = false) Boolean isPrimary
    ) throws URISyntaxException {
        LOG.debug("REST request to upload MegaAsset file for target type {} entityId {}", type, entityId);
        AttachmentType attachmentType;
        try {
            attachmentType = AttachmentType.valueOf(type.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new BadRequestAlertException("Unknown attachment type: " + type, ENTITY_NAME, "invalidattachmenttype");
        }
        AssetUploadLinkContext linkContext = new AssetUploadLinkContext(sortOrder, label, isPrimary);
        MegaAssetDTO megaAssetDTO = megaAssetService.uploadAndLinkToEntity(
            attachmentType,
            entityId,
            file,
            description,
            isPublic,
            linkContext
        );
        return ResponseEntity.created(new URI("/api/assets/" + megaAssetDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, megaAssetDTO.getId().toString()))
            .body(megaAssetDTO);
    }

    @GetMapping("/dl/{uuid}")
    public ResponseEntity<Resource> downloadMegaAsset(@PathVariable("uuid") String uuid) {
        LOG.debug("REST request to download MegaAsset file : {}", uuid);
        Optional<MegaAssetService.MegaAssetFileDownload> fileForDownload = megaAssetService.findFileForDownload(uuid);

        if (fileForDownload.isPresent()) {
            MegaAssetService.MegaAssetFileDownload dl = fileForDownload.orElse(null);
            if (dl.asset().getType() != AssetType.IMAGE) {
                return ResponseEntity.badRequest().build();
            }
            return downloadAsset(uuid, dl);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/image/{size}/{uuid}")
    public ResponseEntity<Resource> downloadMegaAssetImage(@PathVariable("size") String sizeParam, @PathVariable("uuid") String uuid) {
        LOG.debug("REST request to download MegaAsset image : size {} uuid {}", sizeParam, uuid);
        final MegaAssetImageSize size;
        try {
            size = MegaAssetImageSize.fromApiValue(sizeParam);
        } catch (IllegalArgumentException ex) {
            throw new BadRequestAlertException("Invalid image size: " + sizeParam, ENTITY_NAME, "invalidimagesize");
        }
        Optional<MegaAssetService.MegaAssetFileDownload> fileForDownload = megaAssetService.findImageFileForDownload(uuid, size);

        if (fileForDownload.isPresent()) {
            MegaAssetService.MegaAssetFileDownload dl = fileForDownload.orElse(null);
            return downloadAsset(uuid, dl);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private static ResponseEntity<Resource> downloadAsset(String uuid, MegaAssetService.MegaAssetFileDownload dl) {
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
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteMegaAsset(@PathVariable("uuid") String uuid) {
        LOG.debug("REST request to delete MegaAsset : {}", uuid);
        if (!megaAssetService.deleteByUuid(uuid)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, uuid)).build();
    }
}
