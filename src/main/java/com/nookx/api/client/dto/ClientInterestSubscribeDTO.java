package com.nookx.api.client.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;

@Data
public class ClientInterestSubscribeDTO {

    @NotEmpty
    private List<Long> interestIds;
}
