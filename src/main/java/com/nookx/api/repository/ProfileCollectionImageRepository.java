package com.nookx.api.repository;

import com.nookx.api.domain.ProfileCollectionImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProfileCollectionImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfileCollectionImageRepository extends JpaRepository<ProfileCollectionImage, Long> {
    Optional<ProfileCollectionImage> findByProfileCollection_Id(Long profileCollectionId);
}
