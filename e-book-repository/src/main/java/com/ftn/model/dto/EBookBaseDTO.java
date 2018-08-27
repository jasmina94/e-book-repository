package com.ftn.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Jasmina on 17/08/2018.
 *
 * This class represent data returning on form for adding new EBook.
 * After admin uploaded the file (pdf) this values are required to be shown.
 */
@Data
@NoArgsConstructor
public class EBookBaseDTO {

    private String title;

    private String author;

    private String keywords;

    private String filename;
}
