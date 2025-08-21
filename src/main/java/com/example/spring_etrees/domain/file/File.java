package com.example.spring_etrees.domain.file;

import com.example.spring_etrees.domain.board.Board;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "FILE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_ID")
    private Long id;

    @Column(name = "ORIGINAL_FILE_NAME", length = 255, nullable = false)
    private String originalFileName; // 원본 파일명

    @Column(name = "STORED_FILE_NAME", length = 255, nullable = false)
    private String storedFileName;   // S3 저장 파일명

    @Column(name = "FILE_URL", length = 500, nullable = false)
    private String fileUrl;          // S3 URL

    @Column(name = "FILE_SIZE")
    private Long fileSize;           // 파일 크기

    @Column(name = "FILE_EXTENSION", length = 10)
    private String fileExtension;    // 확장자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_NUM")
    private Board board;

    @CreatedDate
    @Column(name = "UPLOAD_TIME", updatable = false)
    private LocalDateTime uploadTime;

    public static File create(String originalFileName, String storedFileName,
                              String fileUrl, Long fileSize, String fileExtension, Board board) {
        File file = new File();

        file.originalFileName = requireNonNull(originalFileName);
        file.storedFileName = requireNonNull(storedFileName);
        file.fileUrl = requireNonNull(fileUrl);
        file.fileSize = fileSize;
        file.fileExtension = fileExtension;
        file.board = requireNonNull(board);

        return file;
    }
}