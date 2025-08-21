package com.example.spring_etrees.application.file.required;

import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.file.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByBoard(Board board);
}
