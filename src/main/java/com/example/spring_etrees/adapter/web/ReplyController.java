package com.example.spring_etrees.adapter.web;

import com.example.spring_etrees.adapter.security.LoginUser;
import com.example.spring_etrees.application.board.provided.BoardFinder;
import com.example.spring_etrees.application.reply.provided.ReplyCreator;
import com.example.spring_etrees.application.reply.provided.ReplyFinder;
import com.example.spring_etrees.application.reply.provided.ReplyModifier;
import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.reply.Reply;
import com.example.spring_etrees.domain.reply.ReplyCreateRequest;
import com.example.spring_etrees.domain.reply.ReplyUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyCreator replyCreator;
    private final ReplyFinder replyFinder;
    private final ReplyModifier replyModifier;
    private final BoardFinder boardFinder;

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
     * 댓글 수정 폼 페이지
     */
    @GetMapping("/{replyNum}/edit")
    public String replyEditForm(@PathVariable Long replyNum,
                                @AuthenticationPrincipal LoginUser loginUser,
                                Model model) {
        Reply reply = replyFinder.getReply(replyNum);
        Board board = reply.getBoard(); // Reply에서 Board 직접 가져오기
        String currentUser = loginUser != null ? loginUser.getMember().getName() : null;

        ReplyUpdateRequest replyUpdateRequest = new ReplyUpdateRequest(reply.getReplyContent());

        model.addAttribute("board", board);
        model.addAttribute("reply", reply);
        model.addAttribute("replyUpdateRequest", replyUpdateRequest);
        model.addAttribute("currentUser", currentUser);
        return "reply/edit";
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

        replyModifier.updateReply(replyNum, request);
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