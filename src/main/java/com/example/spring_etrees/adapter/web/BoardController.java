package com.example.spring_etrees.adapter.web;

import com.example.spring_etrees.application.board.provided.BoardCreator;
import com.example.spring_etrees.application.board.provided.BoardFinder;
import com.example.spring_etrees.application.board.provided.BoardModifier;
import com.example.spring_etrees.application.common.ComCodeService;
import com.example.spring_etrees.application.reply.provided.ReplyCreator;
import com.example.spring_etrees.application.reply.provided.ReplyFinder;
import com.example.spring_etrees.application.reply.provided.ReplyModifier;
import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.board.BoardCreateRequest;
import com.example.spring_etrees.domain.board.BoardUpdateRequest;
import com.example.spring_etrees.domain.comcode.ComCode;
import com.example.spring_etrees.domain.reply.Reply;
import com.example.spring_etrees.domain.reply.ReplyCreateRequest;
import com.example.spring_etrees.domain.reply.ReplyUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardCreator boardCreator;
    private final BoardFinder boardFinder;
    private final BoardModifier boardModifier;
    private final ReplyCreator replyCreator;
    private final ReplyFinder replyFinder;
    private final ReplyModifier replyModifier;
    private final ComCodeService comCodeService;

    /**
     * 게시글 목록 페이지
     */
    @GetMapping("/list")
    public String boardList(@RequestParam(defaultValue = "all") String type,
                            @RequestParam(defaultValue = "1") int pageNo,
                            Model model) {
        // 페이지 번호는 1부터 시작하므로 -1 처리, 기본 5건씩 조회
        Pageable pageable = PageRequest.of(pageNo - 1, 5);
        Page<Board> boardPage = boardFinder.getBoardList(type, pageable);

        // 메뉴 코드 목록 추가
        List<ComCode> menuCodes = comCodeService.getMenuCodes();

        model.addAttribute("boardList", boardPage.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", boardPage.getTotalPages());
        model.addAttribute("totalElements", boardPage.getTotalElements());
        model.addAttribute("hasNext", boardPage.hasNext());
        model.addAttribute("hasPrevious", boardPage.hasPrevious());
        model.addAttribute("selectedType", type); // 선택된 타입 전달
        model.addAttribute("menuCodes", menuCodes); // 메뉴 코드 목록 추가

        return "board/list";
    }

    /**
     * 게시글 상세 페이지
     */
    @GetMapping("/view/{boardNum}")
    public String boardView(@PathVariable Long boardNum, Model model) {
        Board board = boardFinder.getBoard(boardNum);
        List<Reply> replies = replyFinder.getRepliesByBoard(boardNum);
        model.addAttribute("board", board);
        model.addAttribute("replies", replies);
        model.addAttribute("replyCreateRequest", new ReplyCreateRequest(boardNum, ""));
        return "board/view";
    }

    /**
     * 게시글 작성 폼 페이지
     */
    @GetMapping("/write")
    public String boardWriteForm(Model model) {
        // 메뉴 코드 목록 조회
        List<ComCode> menuCodes = comCodeService.getMenuCodes();
        
        // 빈 DTO 객체를 모델에 추가 (Thymeleaf 폼 바인딩용)
        model.addAttribute("boardCreateRequest", new BoardCreateRequest(null, "", ""));
        model.addAttribute("menuCodes", menuCodes);
        return "board/write";
    }

    /**
     * 게시글 작성 처리
     */
    @PostMapping("/write")
    public String create(@Valid @ModelAttribute BoardCreateRequest request) {
        Long boardNum = boardCreator.createBoard(request);
        return "redirect:/board/view/" + boardNum;
    }

    /**
     * 게시글 수정 처리
     * */
    @PostMapping("/edit/{boardNum}")
    public String update(@PathVariable Long boardNum,
                         @Valid @ModelAttribute BoardUpdateRequest request) {
        boardModifier.updateBoard(boardNum, request);
        return "redirect:/board/view/" + boardNum;
    }

    /**
     * 게시글 수정 폼 페이지
     */
    @GetMapping("/edit/{boardNum}")
    public String boardEditForm(@PathVariable Long boardNum, Model model) {
        Board board = boardFinder.getBoard(boardNum);

        // 기존 게시글 정보로 수정 요청 객체 생성
        BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest(
                board.getBoardTitle(),
                board.getBoardComment()
        );

        model.addAttribute("board", board);
        model.addAttribute("boardUpdateRequest", boardUpdateRequest);
        return "board/edit";  // 수정 폼 템플릿
    }

    /**
     * 게시글 삭제 처리
     */
    @PostMapping("/delete/{boardNum}")
    public String delete(@PathVariable Long boardNum) {
        boardModifier.deleteBoard(boardNum);
        return "redirect:/board/list";
    }

    /**
     * 댓글 작성 처리
     */
    @PostMapping("/view/{boardNum}/reply")
    public String createReply(@PathVariable Long boardNum,
                              @Valid @ModelAttribute ReplyCreateRequest request) {
        replyCreator.createReply(request);
        return "redirect:/board/view/" + boardNum;
    }

    // 댓글 수정 폼 페이지
    @GetMapping("/view/{boardNum}/reply/{replyNum}/edit")
    public String replyEditForm(@PathVariable Long boardNum,
                                @PathVariable Long replyNum,
                                Model model) {
        Board board = boardFinder.getBoard(boardNum);
        Reply reply = replyFinder.getReply(replyNum);

        ReplyUpdateRequest replyUpdateRequest = new ReplyUpdateRequest(reply.getReplyContent());

        model.addAttribute("board", board);
        model.addAttribute("reply", reply);
        model.addAttribute("replyUpdateRequest", replyUpdateRequest);
        return "reply/edit";
    }

    // 댓글 수정 처리
    @PostMapping("/view/{boardNum}/reply/{replyNum}/edit")
    public String updateReply(@PathVariable Long boardNum,
                              @PathVariable Long replyNum,
                              @Valid @ModelAttribute ReplyUpdateRequest request) {
        replyModifier.updateReply(replyNum, request);
        return "redirect:/board/view/" + boardNum;
    }

    // 댓글 삭제 처리
    @PostMapping("/view/{boardNum}/reply/{replyNum}/delete")
    public String deleteReply(@PathVariable Long boardNum,
                              @PathVariable Long replyNum) {
        replyModifier.deleteReply(replyNum);
        return "redirect:/board/view/" + boardNum;
    }
}
