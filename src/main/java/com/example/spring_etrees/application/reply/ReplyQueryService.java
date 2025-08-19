package com.example.spring_etrees.application.reply;

import com.example.spring_etrees.application.reply.provided.ReplyFinder;
import com.example.spring_etrees.application.reply.required.ReplyRepository;
import com.example.spring_etrees.domain.reply.Reply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyQueryService implements ReplyFinder {

    private final ReplyRepository replyRepository;

    @Override
    public List<Reply> getRepliesByBoard(Long boardNum) {
        return replyRepository.findByBoardBoardNumOrderByCreateTimeAsc(boardNum);
    }

    @Override
    public Reply getReply(Long replyNum) {
        return replyRepository.findById(replyNum)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + replyNum));
    }
}
