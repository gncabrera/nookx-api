package com.dot.collector.api.client.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ClientCollectionCommunityDTO {

    private int totalStars;
    private int totalComments;
    private int totalCloned;
    private ClientCloneInformationDTO clone;
}
