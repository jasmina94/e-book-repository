package com.ftn.repository;

import com.ftn.model.EBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jasmina on 16/06/2018.
 */
@Repository
public interface EBookDao extends JpaRepository<EBook, Long> {

    List<EBook> findByCategoryId(Long categoryId);
}
