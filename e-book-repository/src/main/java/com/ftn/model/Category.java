package com.ftn.model;

import com.ftn.model.dto.CategoryDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by Jasmina on 16/06/2018.
 */
@Entity
@Data
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    public void merge(CategoryDTO categoryDTO){
        this.name = categoryDTO.getName();
    }
}
