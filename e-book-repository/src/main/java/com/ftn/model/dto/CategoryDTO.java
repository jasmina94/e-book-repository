package com.ftn.model.dto;

import com.ftn.model.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Created by Jasmina on 16/06/2018.
 */
@Data
public class CategoryDTO {

    private long id;

    @NotNull
    private String name;

    public CategoryDTO(){}

    public CategoryDTO(Category category){
        this.id = category.getId();
        this.name = category.getName();
    }

    public Category construct(){
        final Category category = new Category();
        category.setId(this.id);
        category.setName(this.name);
        return category;
    }
}
