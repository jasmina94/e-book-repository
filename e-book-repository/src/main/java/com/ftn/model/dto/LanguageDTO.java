package com.ftn.model.dto;

import com.ftn.model.Language;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Created by Jasmina on 16/06/2018.
 */
@Data
public class LanguageDTO {

    private long id;

    @NotNull
    private String name;

    public LanguageDTO(){}

    public LanguageDTO(Language language){
        this.id = language.getId();
        this.name = language.getName();
    }

    public Language construct(){
        final Language language = new Language();
        language.setId(this.id);
        language.setName(this.name);
        return language;
    }
}
