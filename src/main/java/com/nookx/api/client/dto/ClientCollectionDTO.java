package com.nookx.api.client.dto;

import com.nookx.api.domain.enumeration.ProfileCollectionType;
import java.util.List;
import lombok.Data;

@Data
public class ClientCollectionDTO {

    private Long id;
    private String title;
    private String description;
    private ProfileCollectionType collectionType;
    private List<String> sets;
    private String createdBy;
    private ClientCollectionCommunityDTO community;
    private ClientCollectionSettingsDTO settings;
    private ClientImageDTO image;
    private ClientInterestDTO interest;
}
