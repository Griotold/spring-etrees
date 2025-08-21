package com.example.spring_etrees.application.file.provided;

import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.file.File;

import java.util.List;

public interface FileFinder {

    /**
     * 게시글의 첨부파일 목록 조회
     * @param board 게시글
     * @return 첨부파일 목록
     */
    List<File> getFilesByBoard(Board board);
}
