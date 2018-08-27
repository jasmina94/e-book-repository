package com.ftn.model.dto;

import com.ftn.model.EBook;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by Jasmina on 16/06/2018.
 */
@Data
public class EBookDTO {

    private Long id;

    @NotNull
    private String title;

    private String author;

    private String keywords;

    private Integer publicationYear;

    @NotNull
    private String filename;

    private String mime;

    @NotNull
    private LanguageDTO language;

    @NotNull
    private CategoryDTO category;

    @NotNull
    private UserDTO cataloguer;

    public EBookDTO(){}

    public EBookDTO(EBook eBook){
        this.id = eBook.getId();
        this.title = eBook.getTitle();
        this.author = eBook.getAuthor();
        this.keywords = eBook.getKeywords();
        this.publicationYear = eBook.getPublicationYear();
        this.filename = eBook.getFilename();
        this.mime = eBook.getMime();
        this.language = new LanguageDTO(eBook.getLanguage());
        this.category = new CategoryDTO(eBook.getCategory());
        this.cataloguer = new UserDTO(eBook.getCataloguer());
    }


    public EBook construct(){
        final EBook eBook = new EBook();
        eBook.setId(this.id);
        eBook.setTitle(this.title);
        eBook.setAuthor(this.author);
        eBook.setKeywords(this.keywords);
        eBook.setPublicationYear(this.publicationYear);
        eBook.setFilename(this.filename);
        eBook.setMime(this.mime);
        eBook.setLanguage(this.language.construct());
        eBook.setCategory(this.category.construct());
        eBook.setCataloguer(this.cataloguer.construct());
        return eBook;
    }
}
