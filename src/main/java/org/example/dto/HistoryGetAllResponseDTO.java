package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HistoryGetAllResponseDTO {
    private long id;
    private long ownerId;
    private long partId;
    private String type;
    private String partName;
    private long partQty;
}
