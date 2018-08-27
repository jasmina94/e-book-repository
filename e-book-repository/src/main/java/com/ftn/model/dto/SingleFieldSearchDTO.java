package com.ftn.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by Jasmina on 25/08/2018.
 */
@Data
@NoArgsConstructor
public class SingleFieldSearchDTO {

    @NotNull
    private String fieldName;

    @NotNull
    private String fieldValue;

    @NotNull
    @Pattern(regexp = "^(STANDARD|PHRASE|FUZZY)$")
    private String queryType;
}
