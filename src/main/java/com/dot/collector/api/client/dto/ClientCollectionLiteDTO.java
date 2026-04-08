package com.dot.collector.api.client.dto;

import com.dot.collector.api.domain.enumeration.ProfileCollectionType;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ClientCollectionLiteDTO {

    private Long id;
    private String title;
    private String description;
    private ProfileCollectionType collectionType;
    private List<String> sets;
    private String createdBy;
    private ClientCollectionCommunityDTO community;
}
