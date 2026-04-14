package com.nookx.api.service;

import com.nookx.api.config.ApplicationProperties;
import com.nookx.api.domain.MegaAsset;
import com.nookx.api.domain.User;
import com.nookx.api.domain.enumeration.AssetType;
import com.nookx.api.repository.MegaAssetRepository;
import com.nookx.api.security.SecurityUtils;
import com.nookx.api.service.dto.MegaAssetDTO;
import com.nookx.api.service.mapper.MegaAssetMapper;
import com.nookx.api.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Implementation for managing {@link MegaAsset}.
 */
@Service
@Transactional
public class MegaAssetService {

    private static final Logger LOG = LoggerFactory.getLogger(MegaAssetService.class);

    private static final String ENTITY_NAME = "megaAsset";

    private final MegaAssetRepository megaAssetRepository;

    private final MegaAssetMapper megaAssetMapper;

    private final ApplicationProperties applicationProperties;

    private final UserService userService;

    public MegaAssetService(
        MegaAssetRepository megaAssetRepository,
        MegaAssetMapper megaAssetMapper,
        ApplicationProperties applicationProperties,
        UserService userService
    ) {
        this.megaAssetRepository = megaAssetRepository;
        this.megaAssetMapper = megaAssetMapper;
        this.applicationProperties = applicationProperties;
        this.userService = userService;
    }

    public MegaAssetDTO save(MegaAssetDTO megaAssetDTO) {
        LOG.debug("Request to save MegaAsset : {}", megaAssetDTO);
        MegaAsset megaAsset = megaAssetMapper.toEntity(megaAssetDTO);
        megaAsset = megaAssetRepository.save(megaAsset);
        return megaAssetMapper.toDto(megaAsset);
    }

    public MegaAssetDTO upload(MultipartFile file, String description, boolean isPublic) {
        LOG.debug("Request to upload MegaAsset file");
        if (file == null || file.isEmpty()) {
            throw new BadRequestAlertException("A file is required", ENTITY_NAME, "filerequired");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "upload");
        String safeName = Paths.get(originalFilename).getFileName().toString();
        if (safeName.isBlank()) {
            safeName = "upload";
        }

        String extension = "";
        int dot = safeName.lastIndexOf('.');
        if (dot >= 0 && dot < safeName.length() - 1) {
            extension = safeName.substring(dot);
            if (extension.length() > 32) {
                extension = "";
            }
        }

        String storedFilename = UUID.randomUUID() + extension.toLowerCase();
        Path baseDir = Paths.get(applicationProperties.getMegaAsset().getUploadDirectory()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(baseDir);
            Path target = baseDir.resolve(storedFilename).normalize();
            if (!target.startsWith(baseDir)) {
                throw new BadRequestAlertException("Invalid file path", ENTITY_NAME, "invalidpath");
            }
            file.transferTo(target.toFile());
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to store uploaded file", e);
        }

        String displayName = safeName.length() > 255 ? safeName.substring(0, 255) : safeName;
        String relativePath = storedFilename;
        String contentType = file.getContentType(); //TODO: find content-type from file directly
        AssetType assetType =
            StringUtils.hasText(contentType) && contentType.toLowerCase().startsWith("image/") ? AssetType.IMAGE : AssetType.FILE;

        User uploadedBy = userService
            .getUserWithAuthorities()
            .orElseThrow(() -> new BadRequestAlertException("Current user could not be resolved", ENTITY_NAME, "usernotfound"));

        MegaAsset megaAsset = new MegaAsset()
            .name(displayName)
            .description(description)
            .path(relativePath)
            .type(assetType)
            .contentType(contentType)
            .sizeBytes(file.getSize())
            .uploadedBy(uploadedBy);
        megaAsset.setPublic(isPublic);
        megaAsset = megaAssetRepository.save(megaAsset);
        return megaAssetMapper.toDto(megaAsset);
    }

    @Transactional(readOnly = true)
    public Optional<MegaAssetDTO> findOne(Long id) {
        LOG.debug("Request to get MegaAsset : {}", id);
        return megaAssetRepository.findById(id).map(megaAssetMapper::toDto);
    }

    /**
     * Resolves the stored file for download if the entity exists and the file is on disk.
     */
    @Transactional(readOnly = true)
    public Optional<MegaAssetFileDownload> findFileForDownload(String uuid) {
        LOG.debug("Request to get MegaAsset file stream : {}", uuid);
        Optional<MegaAsset> dtoOpt = megaAssetRepository.findByPath(uuid);
        if (dtoOpt.isEmpty()) {
            return Optional.empty();
        }

        MegaAsset entity = dtoOpt.get();

        boolean isAdmin = SecurityUtils.currentUserIsAdmin();
        if (!entity.isPublic() && !isAdmin) {
            Optional<User> currentUser = userService.getUserWithAuthorities();
            if (currentUser.isEmpty()) {
                return Optional.empty();
            }

            if (entity.getUploadedBy() != null && !Objects.equals(entity.getUploadedBy().getId(), currentUser.get().getId())) {
                return Optional.empty();
            }
        }
        if (!StringUtils.hasText(entity.getPath())) {
            return Optional.empty();
        }
        Path baseDir = Paths.get(applicationProperties.getMegaAsset().getUploadDirectory()).toAbsolutePath().normalize();
        Path filePath = baseDir.resolve(entity.getPath()).normalize();
        if (!filePath.startsWith(baseDir) || !Files.isRegularFile(filePath)) {
            LOG.warn("Asset id {} file not found at {}", uuid, filePath);
            return Optional.empty();
        }
        Resource resource = new FileSystemResource(filePath);
        return Optional.of(new MegaAssetFileDownload(megaAssetMapper.toDto(entity), resource));
    }

    public void delete(Long id) {
        LOG.debug("Request to delete MegaAsset : {}", id);
        megaAssetRepository.deleteById(id);
    }

    public record MegaAssetFileDownload(MegaAssetDTO asset, Resource resource) {}
}
