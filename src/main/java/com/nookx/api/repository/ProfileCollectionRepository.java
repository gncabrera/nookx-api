package com.nookx.api.repository;

import com.nookx.api.domain.ProfileCollection;
import com.nookx.api.domain.enumeration.ProfileCollectionType;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProfileCollection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfileCollectionRepository extends JpaRepository<ProfileCollection, Long> {
    long countByProfile_IdAndType(Long profileId, ProfileCollectionType type);

    List<ProfileCollection> findByProfile_Id(Long profileId);
}
