package com.nookx.api.domain;

import static com.nookx.api.domain.MegaAssetTestSamples.*;
import static com.nookx.api.domain.ProfileCollectionImageTestSamples.*;
import static com.nookx.api.domain.ProfileCollectionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nookx.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfileCollectionImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfileCollectionImage.class);
        ProfileCollectionImage profileCollectionImage1 = getProfileCollectionImageSample1();
        ProfileCollectionImage profileCollectionImage2 = new ProfileCollectionImage();
        assertThat(profileCollectionImage1).isNotEqualTo(profileCollectionImage2);

        profileCollectionImage2.setId(profileCollectionImage1.getId());
        assertThat(profileCollectionImage1).isEqualTo(profileCollectionImage2);

        profileCollectionImage2 = getProfileCollectionImageSample2();
        assertThat(profileCollectionImage1).isNotEqualTo(profileCollectionImage2);
    }

    @Test
    void profileCollectionTest() {
        ProfileCollectionImage profileCollectionImage = getProfileCollectionImageRandomSampleGenerator();
        ProfileCollection profileCollectionBack = getProfileCollectionRandomSampleGenerator();

        profileCollectionImage.setProfileCollection(profileCollectionBack);
        assertThat(profileCollectionImage.getProfileCollection()).isEqualTo(profileCollectionBack);

        profileCollectionImage.profileCollection(null);
        assertThat(profileCollectionImage.getProfileCollection()).isNull();
    }

    @Test
    void assetTest() {
        ProfileCollectionImage profileCollectionImage = getProfileCollectionImageRandomSampleGenerator();
        MegaAsset megaAssetBack = getMegaAssetRandomSampleGenerator();

        profileCollectionImage.setAsset(megaAssetBack);
        assertThat(profileCollectionImage.getAsset()).isEqualTo(megaAssetBack);

        profileCollectionImage.asset(null);
        assertThat(profileCollectionImage.getAsset()).isNull();
    }
}
