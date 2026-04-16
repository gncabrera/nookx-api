package com.nookx.api.repository;

import com.nookx.api.domain.ProfileInterest;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProfileInterest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfileInterestRepository extends JpaRepository<ProfileInterest, Long> {
    boolean existsByProfile_IdAndInterest_Id(Long profileId, Long interestId);

    long deleteByProfile_IdAndInterest_IdIn(Long profileId, Collection<Long> interestIds);
}
