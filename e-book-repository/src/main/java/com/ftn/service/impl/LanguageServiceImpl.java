package com.ftn.service.impl;

import com.ftn.exception.NotFoundException;
import com.ftn.model.Language;
import com.ftn.model.dto.LanguageDTO;
import com.ftn.repository.LanguageDao;
import com.ftn.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jasmina on 16/06/2018.
 */
@Service
public class LanguageServiceImpl implements LanguageService {

    @Autowired
    private LanguageDao languageDao;

    @Override
    public List<LanguageDTO> readAll() {
        return languageDao.findAll().stream().map(language -> new LanguageDTO(language)).collect(Collectors.toList());
    }

    @Override
    public LanguageDTO create(LanguageDTO languageDTO) {
        Language language = new Language();
        language.merge(languageDTO);
        language = languageDao.save(language);
        return new LanguageDTO(language);
    }

    @Override
    public LanguageDTO update(Long id, LanguageDTO languageDTO) {
        Language language = languageDao.findById(id).orElseThrow(NotFoundException::new);
        language.merge(languageDTO);
        language = languageDao.save(language);
        return new LanguageDTO(language);
    }

    @Override
    public void delete(Long id) {
        final Language language = languageDao.findById(id).orElseThrow(NotFoundException::new);
        languageDao.delete(language);
    }
}
