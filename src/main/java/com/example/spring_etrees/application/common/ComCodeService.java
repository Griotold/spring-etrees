package com.example.spring_etrees.application.common;

import com.example.spring_etrees.application.common.required.ComCodeRepository;
import com.example.spring_etrees.domain.comcode.ComCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ComCodeService {

    private final ComCodeRepository comCodeRepository;

    /**
     * 메뉴(게시판 타입) 코드 목록 조회
     */
    public List<ComCode> getMenuCodes() {
        return comCodeRepository.findByCodeTypeOrderByCodeId("menu");
    }

    /**
     * 특정 메뉴 코드 조회
     */
    public Optional<ComCode> getMenuCode(String codeId) {
        return comCodeRepository.findByCodeTypeAndCodeId("menu", codeId);
    }

    /**
     * 코드 ID로 코드명 조회
     */
    public String getCodeName(String codeType, String codeId) {
        return comCodeRepository.findByCodeTypeAndCodeId(codeType, codeId)
                .map(ComCode::getCodeName)
                .orElse("알 수 없음");
    }
}
