package com.example.spring_etrees.application.reply;

import com.example.spring_etrees.application.board.required.BoardRepository;
import com.example.spring_etrees.application.reply.provided.ReplyCreator;
import com.example.spring_etrees.application.reply.provided.ReplyModifier;
import com.example.spring_etrees.application.reply.required.ReplyRepository;
import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.reply.Reply;
import com.example.spring_etrees.domain.reply.ReplyCreateRequest;
import com.example.spring_etrees.domain.reply.ReplyUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyModifyService implements ReplyCreator, ReplyModifier {

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    @Override
    public Long createReply(ReplyCreateRequest request, String creator) {
        // 게시글 조회
        Board board = boardRepository.findById(request.boardNum())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + request.boardNum()));

        // 댓글 생성
        Reply reply = Reply.create(board, request.replyContent(), creator);

        // 저장 후 생성된 ID 반환
        Reply savedReply = replyRepository.save(reply);
        return savedReply.getReplyNum();
    }

    @Override
    public void updateReply(Long replyNum, ReplyUpdateRequest request, String modifier) {
        // 댓글 조회
        Reply reply = replyRepository.findById(replyNum)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + replyNum));

        // 도메인 메서드로 수정
        reply.update(request.replyContent(), modifier);

        // 명시적 save 호출
        replyRepository.save(reply);
    }

    @Override
    public void deleteReply(Long replyNum) {
        // 존재 여부 확인 후 삭제
        Reply reply = replyRepository.findById(replyNum)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + replyNum));

        replyRepository.delete(reply);
    }
}
