package com.example.spring_etrees.application.reply.required;

import com.example.spring_etrees.domain.reply.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
