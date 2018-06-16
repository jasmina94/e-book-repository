package com.ftn.repository;

import com.ftn.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Jasmina on 16/06/2018.
 */
@Repository
public interface LanguageDao extends JpaRepository<Language, Long> {
}
