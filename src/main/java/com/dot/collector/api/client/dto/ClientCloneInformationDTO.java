package com.dot.collector.api.client.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ClientCloneInformationDTO {

    private boolean cloned;
    private Long sourceCollectionId;
    private Long sourceUserId;
    private String sourceUsername;
    private String sourceUserImageUrl;
}
