package com.example.spring_etrees.application.file;

import com.example.spring_etrees.application.file.provided.FileFinder;
import com.example.spring_etrees.application.file.required.FileRepository;
import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.file.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileQueryService implements FileFinder {

    private final FileRepository fileRepository;

    @Override
    public List<File> getFilesByBoard(Board board) {
        return fileRepository.findByBoard(board);
    }
}
