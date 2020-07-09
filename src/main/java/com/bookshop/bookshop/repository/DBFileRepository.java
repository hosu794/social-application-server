package com.bookshop.bookshop.repository;

import com.bookshop.bookshop.model.DBFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DBFileRepository extends JpaRepository<DBFile, Long> {

    boolean existsByFilename(String filename);

    Optional<DBFile> findByFilename(String filename);

}
