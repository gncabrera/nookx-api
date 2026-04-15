package com.nookx.api.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nookx.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MegaPartImageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MegaPartImageDTO.class);
        MegaPartImageDTO megaPartImageDTO1 = new MegaPartImageDTO();
        megaPartImageDTO1.setId(1L);
        MegaPartImageDTO megaPartImageDTO2 = new MegaPartImageDTO();
        assertThat(megaPartImageDTO1).isNotEqualTo(megaPartImageDTO2);
        megaPartImageDTO2.setId(megaPartImageDTO1.getId());
        assertThat(megaPartImageDTO1).isEqualTo(megaPartImageDTO2);
        megaPartImageDTO2.setId(2L);
        assertThat(megaPartImageDTO1).isNotEqualTo(megaPartImageDTO2);
        megaPartImageDTO1.setId(null);
        assertThat(megaPartImageDTO1).isNotEqualTo(megaPartImageDTO2);
    }
}
