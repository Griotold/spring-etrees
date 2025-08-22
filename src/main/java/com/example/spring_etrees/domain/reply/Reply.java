package com.example.spring_etrees.domain.reply;

import com.example.spring_etrees.domain.board.Board;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "REPLY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPLY_NUM")
    private Long replyNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_NUM", nullable = false,
            foreignKey = @ForeignKey(name = "FK_REPLY_BOARD",
                    foreignKeyDefinition = "FOREIGN KEY (BOARD_NUM) REFERENCES BOARD(BOARD_NUM) ON DELETE CASCADE"))
    private Board board;

    @Lob
    @Column(name = "REPLY_CONTENT", nullable = false)
    private String replyContent;

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

    public static Reply create(Board board, String replyContent, String creator) {
        Reply reply = new Reply();

        reply.board = board;
        reply.replyContent = replyContent;
        reply.creator = creator;
        reply.modifier = creator;

        return reply;
    }

    public void update(String replyContent, String modifier) {
        this.replyContent = replyContent;
        this.modifier = modifier;
    }
}