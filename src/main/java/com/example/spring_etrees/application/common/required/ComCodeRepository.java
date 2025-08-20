package com.example.spring_etrees.application.common.required;

import com.example.spring_etrees.domain.comcode.ComCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ComCodeRepository extends JpaRepository<ComCode, Long> {

    /**
     * 코드 타입별 코드 목록 조회
     * @param codeType 코드 타입 (예: "menu")
     * @return 코드 목록
     */
    List<ComCode> findByCodeTypeOrderByCodeId(String codeType);

    /**
     * 특정 코드 조회
     * @param codeType 코드 타입
     * @param codeId 코드 ID
     * @return 코드 정보
     */
    Optional<ComCode> findByCodeTypeAndCodeId(String codeType, String codeId);
}