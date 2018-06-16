package com.ftn.service;

import com.ftn.model.dto.EBookDTO;

import java.util.List;

/**
 * Created by Jasmina on 16/06/2018.
 */
public interface EBookService {

    List<EBookDTO> readAll();

    List<EBookDTO> readByCategory(Long categoryId);

    EBookDTO create(EBookDTO eBookDTO);

    EBookDTO update(Long id, EBookDTO eBookDTO);

    void delete(Long id);
}
