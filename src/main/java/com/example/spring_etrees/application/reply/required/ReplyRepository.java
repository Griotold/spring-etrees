package com.example.spring_etrees.application.reply.required;

import com.example.spring_etrees.domain.reply.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    // ReplyRepository에 추가
    List<Reply> findByBoardBoardNumOrderByCreateTimeAsc(Long boardNum);
}
