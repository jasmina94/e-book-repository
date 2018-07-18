package com.ftn.service.impl;

import com.ftn.exception.NotFoundException;
import com.ftn.model.Category;
import com.ftn.model.dto.CategoryDTO;
import com.ftn.model.dto.EBookDTO;
import com.ftn.model.dto.UserDTO;
import com.ftn.repository.CategoryDao;
import com.ftn.service.CategoryService;
import com.ftn.service.EBookService;
import com.ftn.service.UserService;
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

    @Autowired
    private UserService userService;

    @Autowired
    private EBookService eBookService;

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
    public boolean delete(Long id) {
        boolean retVal = true;
        final Category category = categoryDao.findById(id).orElseThrow(NotFoundException::new);
        List<UserDTO> subscribers = userService.readSubscribersTo(id);
        List<EBookDTO> eBooksInCategory = eBookService.readByCategory(id);
        if (eBooksInCategory.size() != 0) {
            retVal = false;
        } else {
            if (subscribers.size() != 0)
                subscribers.stream().forEach(subscriber -> userService.cancelSubscription(subscriber.getId()));
            retVal = true;
            categoryDao.delete(category);
        }
        return retVal;
    }
}
