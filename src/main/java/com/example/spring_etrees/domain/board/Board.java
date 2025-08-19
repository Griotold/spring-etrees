package com.example.spring_etrees.domain.board;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "BOARD")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_NUM")
    private Long boardNum;

    @Column(name = "BOARD_TYPE", length = 10)
    private String boardType;

    @Column(name = "BOARD_TITLE", length = 200, nullable = false)
    private String boardTitle;

    @Lob
    @Column(name = "BOARD_COMMENT", nullable = false)
    private String boardComment;

    @Column(name = "CREATOR", length = 50)
    private String creator;

    @CreatedDate
    @Column(name = "CREATE_TIME", updatable = false)
    private LocalDateTime createTime;

    @Column(name = "MODIFIER", length = 50)
    private String modifier;

    @LastModifiedDate
    @Column(name = "MODIFIED_TIME")
    private LocalDateTime modifiedTime;

    public static Board create(String boardTitle, String boardComment) {
        Board board = new Board();

        board.boardType = "1";  // 기본 게시판 타입
        board.boardTitle = boardTitle;
        board.boardComment = boardComment;
        board.creator = "SYSTEM";  // 현재는 고정값, 추후 로그인 기능 추가시 변경
        board.modifier = "SYSTEM";

        return board;
    }

    public void update(String boardTitle, String boardComment) {
        this.boardTitle = boardTitle;
        this.boardComment = boardComment;
        this.modifier = "SYSTEM";  // 현재는 고정값, 추후 로그인 기능 추가시 변경
    }

}
