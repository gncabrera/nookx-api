package com.nookx.api.domain;

import static com.nookx.api.domain.MegaAssetTestSamples.*;
import static com.nookx.api.domain.MegaPartImageTestSamples.*;
import static com.nookx.api.domain.MegaPartTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nookx.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MegaPartImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MegaPartImage.class);
        MegaPartImage megaPartImage1 = getMegaPartImageSample1();
        MegaPartImage megaPartImage2 = new MegaPartImage();
        assertThat(megaPartImage1).isNotEqualTo(megaPartImage2);

        megaPartImage2.setId(megaPartImage1.getId());
        assertThat(megaPartImage1).isEqualTo(megaPartImage2);

        megaPartImage2 = getMegaPartImageSample2();
        assertThat(megaPartImage1).isNotEqualTo(megaPartImage2);
    }

    @Test
    void partTest() {
        MegaPartImage megaPartImage = getMegaPartImageRandomSampleGenerator();
        MegaPart megaPartBack = getMegaPartRandomSampleGenerator();

        megaPartImage.setPart(megaPartBack);
        assertThat(megaPartImage.getPart()).isEqualTo(megaPartBack);

        megaPartImage.part(null);
        assertThat(megaPartImage.getPart()).isNull();
    }

    @Test
    void assetTest() {
        MegaPartImage megaPartImage = getMegaPartImageRandomSampleGenerator();
        MegaAsset megaAssetBack = getMegaAssetRandomSampleGenerator();

        megaPartImage.setAsset(megaAssetBack);
        assertThat(megaPartImage.getAsset()).isEqualTo(megaAssetBack);

        megaPartImage.asset(null);
        assertThat(megaPartImage.getAsset()).isNull();
    }
}
