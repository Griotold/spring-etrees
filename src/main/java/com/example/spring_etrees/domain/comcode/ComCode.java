package com.example.spring_etrees.domain.comcode;

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
}
