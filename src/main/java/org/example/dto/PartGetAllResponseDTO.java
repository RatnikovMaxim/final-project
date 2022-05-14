package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PartGetAllResponseDTO {
    private long id;
    private String name;
    private String description;
    private long quantity;
    private String image;
}
