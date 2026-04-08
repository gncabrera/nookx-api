package com.dot.collector.api.client.dto;

import com.dot.collector.api.service.dto.CurrencyDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ClientCollectionSettingsDTO {

    private boolean isPublic;
    private boolean showPrice;
    private boolean showCheckbox;
    private boolean showComment;
    private CurrencyDTO currency;
}
