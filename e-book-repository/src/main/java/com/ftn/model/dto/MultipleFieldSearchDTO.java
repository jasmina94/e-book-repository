package com.ftn.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;

/**
 * Created by Jasmina on 25/08/2018.
 */
@Data
@NoArgsConstructor
public class MultipleFieldSearchDTO {

    private String title;

    private String author;

    private String keywords;

    private String content;

    private String language;

    @NotNull
    @Pattern(regexp = "^(STANDARD|PHRASE|FUZZY)$")
    private String queryType;

    @NotNull
    @Pattern(regexp = "^(OR|AND)$")
    private String queryOperator;
}
