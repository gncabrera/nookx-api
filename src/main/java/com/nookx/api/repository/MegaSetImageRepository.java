package com.nookx.api.repository;

import com.nookx.api.domain.MegaSetImage;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MegaSetImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MegaSetImageRepository extends JpaRepository<MegaSetImage, Long> {
    List<MegaSetImage> findByMegaSet_IdOrderBySortOrderAsc(Long setId);
}
