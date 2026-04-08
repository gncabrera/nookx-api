package com.dot.collector.api.repository;

import com.dot.collector.api.domain.CloneInformation;
import com.dot.collector.api.domain.MegaAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MegaAttribute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CloneInformationRepository extends JpaRepository<CloneInformation, Long> {}
