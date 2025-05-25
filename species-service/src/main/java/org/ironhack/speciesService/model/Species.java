package org.ironhack.speciesService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Species {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long speciesId;

    @NotBlank(message = "Common name must not be empty")
    @Size(max = 100, message = "Common name must not exceed 100 characters")
    private String commonName;

    @NotBlank(message = "Scientific name must not be empty")
    @Size(max = 150, message = "Scientific name must not exceed 150 characters")
    private String scientificName;

    @NotBlank(message = "Conservation status must not be empty")
    @Size(max = 50, message = "Conservation status must not exceed 50 characters")
    private String conservationStatus;

}
