package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartWriteOffResponseDTO {
    private long id;
    private String name;
    private String type;
    private String description;
    private long quantity;
    private String image;
}
