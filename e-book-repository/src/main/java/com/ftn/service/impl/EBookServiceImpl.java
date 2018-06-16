package com.ftn.service.impl;

import com.ftn.exception.NotFoundException;
import com.ftn.model.EBook;
import com.ftn.model.dto.EBookDTO;
import com.ftn.repository.EBookDao;
import com.ftn.service.EBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.lang.model.element.ElementVisitor;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jasmina on 16/06/2018.
 */
@Service
public class EBookServiceImpl implements EBookService {

    @Autowired
    private EBookDao eBookDao;

    @Override
    public List<EBookDTO> readAll() {
        return eBookDao.findAll().stream().map(eBook -> new EBookDTO(eBook)).collect(Collectors.toList());
    }

    @Override
    public List<EBookDTO> readByCategory(Long categoryId) {
        return eBookDao.findByCategoryId(categoryId).stream().map(eBook -> new EBookDTO(eBook)).collect(Collectors.toList());
    }

    @Override
    public EBookDTO create(EBookDTO eBookDTO) {
        EBook eBook = new EBook();
        eBook.merge(eBookDTO);
        eBook = eBookDao.save(eBook);
        return new EBookDTO(eBook);
    }

    @Override
    public EBookDTO update(Long id, EBookDTO eBookDTO) {
        EBook eBook = eBookDao.findById(id).orElseThrow(NotFoundException::new);
        eBook.merge(eBookDTO);
        eBook = eBookDao.save(eBook);
        return new EBookDTO(eBook);
    }

    @Override
    public void delete(Long id) {
        final EBook eBook = eBookDao.findById(id).orElseThrow(NotFoundException::new);
        eBookDao.delete(eBook);
    }
}
