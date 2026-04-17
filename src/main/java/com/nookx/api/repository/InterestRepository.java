package com.nookx.api.repository;

import com.nookx.api.domain.Interest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Interest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
    @Query(
        "SELECT i FROM Interest i WHERE i.isSystem = true OR EXISTS (" +
            "SELECT 1 FROM ProfileInterest pi WHERE pi.interest.id = i.id AND pi.profile.id = :profileId) ORDER BY i.order"
    )
    List<Interest> findAllLinkedToProfileOrSystem(@Param("profileId") Long profileId);

    @Query(
        "SELECT i FROM Interest i WHERE i.id = :id AND (i.isSystem = true OR EXISTS (" +
            "SELECT 1 FROM ProfileInterest pi WHERE pi.interest.id = i.id AND pi.profile.id = :profileId))"
    )
    Optional<Interest> findByIdLinkedToProfileOrSystem(@Param("id") Long id, @Param("profileId") Long profileId);
}
