package com.ftn.lucene.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Jasmina on 17/08/2018.
 */
@Data
@NoArgsConstructor
public class ResultData {

    private Long id;

    private String title;

    private String author;

    private String keywords;

    private String language;

    private String category;

    private String fileName;

    private String highlights;

    private String year;
}
