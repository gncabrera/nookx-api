package com.nookx.api.domain;

import static com.nookx.api.domain.MegaAssetTestSamples.*;
import static com.nookx.api.domain.MegaSetImageTestSamples.*;
import static com.nookx.api.domain.MegaSetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nookx.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MegaSetImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MegaSetImage.class);
        MegaSetImage megaSetImage1 = getMegaSetImageSample1();
        MegaSetImage megaSetImage2 = new MegaSetImage();
        assertThat(megaSetImage1).isNotEqualTo(megaSetImage2);

        megaSetImage2.setId(megaSetImage1.getId());
        assertThat(megaSetImage1).isEqualTo(megaSetImage2);

        megaSetImage2 = getMegaSetImageSample2();
        assertThat(megaSetImage1).isNotEqualTo(megaSetImage2);
    }

    @Test
    void megaSetTest() {
        MegaSetImage megaSetImage = getMegaSetImageRandomSampleGenerator();
        MegaSet megaSetBack = getMegaSetRandomSampleGenerator();

        megaSetImage.setMegaSet(megaSetBack);
        assertThat(megaSetImage.getMegaSet()).isEqualTo(megaSetBack);

        megaSetImage.megaSet(null);
        assertThat(megaSetImage.getMegaSet()).isNull();
    }

    @Test
    void assetTest() {
        MegaSetImage megaSetImage = getMegaSetImageRandomSampleGenerator();
        MegaAsset megaAssetBack = getMegaAssetRandomSampleGenerator();

        megaSetImage.setAsset(megaAssetBack);
        assertThat(megaSetImage.getAsset()).isEqualTo(megaAssetBack);

        megaSetImage.asset(null);
        assertThat(megaSetImage.getAsset()).isNull();
    }
}
