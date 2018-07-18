package com.ftn.service;

import com.ftn.model.dto.CategoryDTO;

import java.util.List;

/**
 * Created by Jasmina on 16/06/2018.
 */
public interface CategoryService {

    List<CategoryDTO> readAll();

    CategoryDTO create(CategoryDTO categoryDTO);

    CategoryDTO update(Long id, CategoryDTO categoryDTO);

    boolean delete(Long id);
}
