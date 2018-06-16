package com.ftn.service.impl;

import com.ftn.exception.NotFoundException;
import com.ftn.model.Category;
import com.ftn.model.dto.CategoryDTO;
import com.ftn.repository.CategoryDao;
import com.ftn.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jasmina on 16/06/2018.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public List<CategoryDTO> readAll() {
        return categoryDao.findAll().stream().map(category -> new CategoryDTO(category)).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO create(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.merge(categoryDTO);
        category = categoryDao.save(category);
        return new CategoryDTO(category);
    }

    @Override
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
        Category category = categoryDao.findById(id).orElseThrow(NotFoundException::new);
        category.merge(categoryDTO);
        category = categoryDao.save(category);
        return new CategoryDTO(category);
    }

    @Override
    public void delete(Long id) {
        final Category category = categoryDao.findById(id).orElseThrow(NotFoundException::new);
        categoryDao.delete(category);
    }
}
