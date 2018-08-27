package com.ftn.service;

import com.ftn.lucene.model.ResultData;
import com.ftn.model.EBook;
import com.ftn.model.dto.EBookBaseDTO;
import com.ftn.model.dto.EBookDTO;
import com.ftn.model.dto.MultipleFieldSearchDTO;
import com.ftn.model.dto.SingleFieldSearchDTO;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Jasmina on 16/06/2018.
 */
public interface EBookService {

    List<EBookDTO> readAll();

    List<EBookDTO> readByCategory(Long categoryId);

    EBookDTO readById(Long id);

    EBookDTO create(EBookDTO eBookDTO);

    EBookDTO saveAndIndex(EBookDTO eBookDTO);

    EBookDTO update(Long id, EBookDTO eBookDTO);

    boolean delete(Long id);

    File saveEBookFile(byte[] bytes) throws IOException;

    EBookBaseDTO prepareBase(File file);

    boolean indexEBook(EBook eBook);

    boolean deleteEBookIndex(EBook eBook);

    File getFile(EBook eBook);

    File getFile(Long id);

    String getFileName(Long id);

    boolean deleteFile(String filename);

    Resource loadFile(Long id);

    String loadFileString(Long id);

    List<ResultData> search(SingleFieldSearchDTO singleFieldSearchDTO);

    List<ResultData> search(MultipleFieldSearchDTO multipleFieldSearchDTO);
}
