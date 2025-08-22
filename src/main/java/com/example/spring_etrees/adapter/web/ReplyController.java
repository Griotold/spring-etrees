package com.example.spring_etrees.adapter.web;

import com.example.spring_etrees.adapter.security.LoginUser;
import com.example.spring_etrees.application.reply.provided.ReplyCreator;
import com.example.spring_etrees.application.reply.provided.ReplyFinder;
import com.example.spring_etrees.application.reply.provided.ReplyModifier;
import com.example.spring_etrees.domain.reply.Reply;
import com.example.spring_etrees.domain.reply.ReplyCreateRequest;
import com.example.spring_etrees.domain.reply.ReplyUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyCreator replyCreator;
    private final ReplyFinder replyFinder;
    private final ReplyModifier replyModifier;

    /**
     * 댓글 작성 처리
     */
    @PostMapping("/{boardNum}")
    public String createReply(@PathVariable Long boardNum,
                              @Valid @ModelAttribute ReplyCreateRequest request,
                              @AuthenticationPrincipal LoginUser loginUser) {
        String creator = loginUser.getMember().getName();
        replyCreator.createReply(request, creator);
        return "redirect:/board/view/" + boardNum;
    }

    /**
     * 댓글 수정 처리
     */
    @PostMapping("/{replyNum}/edit")
    public String updateReply(@PathVariable Long replyNum,
                              @Valid @ModelAttribute ReplyUpdateRequest request,
                              @AuthenticationPrincipal LoginUser loginUser) {
        Reply reply = replyFinder.getReply(replyNum);
        Long boardNum = reply.getBoard().getBoardNum();
        String modifier = loginUser.getMember().getName();

        replyModifier.updateReply(replyNum, request, modifier);
        return "redirect:/board/view/" + boardNum;
    }

    /**
     * 댓글 삭제 처리
     */
    @PostMapping("/{replyNum}/delete")
    public String deleteReply(@PathVariable Long replyNum,
                              @AuthenticationPrincipal LoginUser loginUser) {
        Reply reply = replyFinder.getReply(replyNum);
        Long boardNum = reply.getBoard().getBoardNum();

        replyModifier.deleteReply(replyNum);
        return "redirect:/board/view/" + boardNum;
    }
}