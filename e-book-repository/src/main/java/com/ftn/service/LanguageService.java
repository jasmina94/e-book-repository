package com.ftn.service;

import com.ftn.model.dto.LanguageDTO;

import java.util.List;

/**
 * Created by Jasmina on 16/06/2018.
 */
public interface LanguageService {

    List<LanguageDTO> readAll();

    LanguageDTO create(LanguageDTO languageDTO);

    LanguageDTO update(Long id, LanguageDTO languageDTO);

    void delete(Long id);
}
