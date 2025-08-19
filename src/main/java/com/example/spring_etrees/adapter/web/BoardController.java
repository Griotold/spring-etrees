package com.example.spring_etrees.adapter.web;

import com.example.spring_etrees.application.board.provided.BoardCreator;
import com.example.spring_etrees.application.board.provided.BoardFinder;
import com.example.spring_etrees.application.board.provided.BoardModifier;
import com.example.spring_etrees.domain.board.Board;
import com.example.spring_etrees.domain.board.BoardCreateRequest;
import com.example.spring_etrees.domain.board.BoardUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardCreator boardCreator;
    private final BoardFinder boardFinder;
    private final BoardModifier boardModifier;

    /**
     * 게시글 목록 페이지
     */
    @GetMapping("/list")
    public String boardList(@RequestParam(defaultValue = "1") int pageNo, Model model) {
        // 페이지 번호는 1부터 시작하므로 -1 처리, 기본 5건씩 조회
        Pageable pageable = PageRequest.of(pageNo - 1, 5);
        Page<Board> boardPage = boardFinder.getBoardList(pageable);

        model.addAttribute("boardList", boardPage.getContent());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", boardPage.getTotalPages());
        model.addAttribute("totalElements", boardPage.getTotalElements());
        model.addAttribute("hasNext", boardPage.hasNext());
        model.addAttribute("hasPrevious", boardPage.hasPrevious());

        return "board/list";
    }

    /**
     * 게시글 상세 페이지
     */
    @GetMapping("/view/{boardNum}")
    public String boardView(@PathVariable Long boardNum, Model model) {
        Board board = boardFinder.getBoard(boardNum);
        model.addAttribute("board", board);
        return "board/view";
    }

    /**
     * 게시글 작성 폼 페이지
     */
    @GetMapping("/write")
    public String boardWriteForm(Model model) {
        // 빈 DTO 객체를 모델에 추가 (Thymeleaf 폼 바인딩용)
        model.addAttribute("boardCreateRequest", new BoardCreateRequest("", ""));
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
}
