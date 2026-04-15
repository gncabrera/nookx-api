package com.nookx.api.repository;

import com.nookx.api.domain.MegaPartImage;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MegaPartImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MegaPartImageRepository extends JpaRepository<MegaPartImage, Long> {
    List<MegaPartImage> findByPart_Id(Long partId);
}
