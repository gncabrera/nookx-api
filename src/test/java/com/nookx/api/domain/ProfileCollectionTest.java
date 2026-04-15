package com.nookx.api.domain;

import static com.nookx.api.domain.ProfileCollectionImageTestSamples.*;
import static com.nookx.api.domain.ProfileCollectionTestSamples.*;
import static com.nookx.api.domain.ProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nookx.api.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfileCollectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfileCollection.class);
        ProfileCollection profileCollection1 = getProfileCollectionSample1();
        ProfileCollection profileCollection2 = new ProfileCollection();
        assertThat(profileCollection1).isNotEqualTo(profileCollection2);

        profileCollection2.setId(profileCollection1.getId());
        assertThat(profileCollection1).isEqualTo(profileCollection2);

        profileCollection2 = getProfileCollectionSample2();
        assertThat(profileCollection1).isNotEqualTo(profileCollection2);
    }

    @Test
    void profileTest() {
        ProfileCollection profileCollection = getProfileCollectionRandomSampleGenerator();
        Profile profileBack = getProfileRandomSampleGenerator();

        profileCollection.setProfile(profileBack);
        assertThat(profileCollection.getProfile()).isEqualTo(profileBack);

        profileCollection.profile(null);
        assertThat(profileCollection.getProfile()).isNull();
    }

    @Test
    void imageTest() {
        ProfileCollection profileCollection = getProfileCollectionRandomSampleGenerator();
        ProfileCollectionImage imageBack = getProfileCollectionImageRandomSampleGenerator();

        profileCollection.setImage(imageBack);
        assertThat(profileCollection.getImage()).isEqualTo(imageBack);
        assertThat(imageBack.getProfileCollection()).isEqualTo(profileCollection);

        profileCollection.setImage(null);
        assertThat(profileCollection.getImage()).isNull();
    }
}
