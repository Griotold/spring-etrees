package com.example.spring_etrees.domain.comcode;

import com.example.spring_etrees.domain.board.BoardType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "COM_CODE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ComCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CODE_TYPE", length = 20, nullable = false)
    private String codeType;

    @Column(name = "CODE_ID", length = 20, nullable = false)
    private String codeId;

    @Column(name = "CODE_NAME", length = 100, nullable = false)
    private String codeName;

    @Column(name = "CREATOR", length = 50)
    private String creator;

    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;

    @Column(name = "MODIFIER", length = 50)
    private String modifier;

    @Column(name = "MODIFIED_TIME")
    private LocalDateTime modifiedTime;

    /**
     * CodeId를 BoardType으로 변환
     */
    public BoardType getBoardType() {
        switch (this.codeId) {
            case "a01": return BoardType.GENERAL;
            case "a02": return BoardType.QNA;
            case "a03": return BoardType.ANONYMOUS;
            case "a04": return BoardType.FREE;
            default: return BoardType.GENERAL;
        }
    }

    /**
     * BoardType의 String 값 반환 (체크박스 value용)
     */
    public String getBoardTypeValue() {
        return getBoardType().name();
    }
}
