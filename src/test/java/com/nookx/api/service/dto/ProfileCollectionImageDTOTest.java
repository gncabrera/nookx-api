package com.nookx.api.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nookx.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfileCollectionImageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfileCollectionImageDTO.class);
        ProfileCollectionImageDTO profileCollectionImageDTO1 = new ProfileCollectionImageDTO();
        profileCollectionImageDTO1.setId(1L);
        ProfileCollectionImageDTO profileCollectionImageDTO2 = new ProfileCollectionImageDTO();
        assertThat(profileCollectionImageDTO1).isNotEqualTo(profileCollectionImageDTO2);
        profileCollectionImageDTO2.setId(profileCollectionImageDTO1.getId());
        assertThat(profileCollectionImageDTO1).isEqualTo(profileCollectionImageDTO2);
        profileCollectionImageDTO2.setId(2L);
        assertThat(profileCollectionImageDTO1).isNotEqualTo(profileCollectionImageDTO2);
        profileCollectionImageDTO1.setId(null);
        assertThat(profileCollectionImageDTO1).isNotEqualTo(profileCollectionImageDTO2);
    }
}
