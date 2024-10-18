package com.example.usermanagementservice.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.example.usermanagementservice.models.Address}
 */
@Data
public class AddressRequestDto implements Serializable {
    @NotEmpty(message = "name cannot be empty")
    private String name;

    @NotEmpty(message = "street can be empty")
    private String street;

    @NotEmpty(message = "city cannot be empty")
    private String city;

    @NotEmpty(message = "state cannot be empty")
    private String state;

    @NotEmpty(message = "zip cannot be empty")
    private String zip;

    @NotEmpty(message = "Country cannot be empty")
    private String country;
    private String extaNotes;
}