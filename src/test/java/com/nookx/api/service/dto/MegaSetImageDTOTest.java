package com.nookx.api.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nookx.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MegaSetImageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MegaSetImageDTO.class);
        MegaSetImageDTO megaSetImageDTO1 = new MegaSetImageDTO();
        megaSetImageDTO1.setId(1L);
        MegaSetImageDTO megaSetImageDTO2 = new MegaSetImageDTO();
        assertThat(megaSetImageDTO1).isNotEqualTo(megaSetImageDTO2);
        megaSetImageDTO2.setId(megaSetImageDTO1.getId());
        assertThat(megaSetImageDTO1).isEqualTo(megaSetImageDTO2);
        megaSetImageDTO2.setId(2L);
        assertThat(megaSetImageDTO1).isNotEqualTo(megaSetImageDTO2);
        megaSetImageDTO1.setId(null);
        assertThat(megaSetImageDTO1).isNotEqualTo(megaSetImageDTO2);
    }
}
