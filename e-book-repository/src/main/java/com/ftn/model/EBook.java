package com.ftn.model;

import com.ftn.model.dto.EBookDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by Jasmina on 16/06/2018.
 */
@Entity
@Data
@NoArgsConstructor
public class EBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String author;

    @Column
    private String keywords;

    @Column
    private int publicationYear;

    @Column(nullable = false)
    private String filename;

    @Column
    private String MIME;

    @ManyToOne(optional = false)
    private Language language;

    @ManyToOne(optional = false)
    private Category category;

    @ManyToOne(optional = false)
    private User cataloguer;


    public void merge(EBookDTO eBookDTO){
        this.title = eBookDTO.getTitle();
        this.author = eBookDTO.getAuthor();
        this.keywords = eBookDTO.getKeywords();
        this.publicationYear = eBookDTO.getPublicationYear();
        this.filename = eBookDTO.getFilename();
        this.MIME = eBookDTO.getMIME();
        this.language = eBookDTO.getLanguage().construct();
        this.category = eBookDTO.getCategory().construct();
        this.cataloguer = eBookDTO.getCataloguer().construct();
    }
}
