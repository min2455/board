package board.board.controller;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;
import board.board.service.BoardService;
import board.common.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.util.List;

@Controller
@Slf4j
@Transactional
public class BoardController {

    @Autowired
    private BoardService boardService;

    @RequestMapping("/board/openBoardList.do")
    public ModelAndView openBoaordList() throws Exception {
        log.debug("openBoardList");

        ModelAndView mv = new ModelAndView("/board/boardList");

        List<BoardDto> list = boardService.selectBoardList();
        mv.addObject("list", list);

        return mv;
    }

    @RequestMapping("/board/openBoardWrite.do")
    public String openBoardWrite() throws Exception{
        return "/board/boardWrite";
    }

    @RequestMapping("/board/insertBoard.do")
    public String insertBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
        boardService.insertBoard(board, multipartHttpServletRequest);
        return "redirect:/board/openBoardList.do";
    }

    @RequestMapping("/board/openBoardDetail.do")
    public ModelAndView openBoardDetail(@RequestParam int boardIdx) throws Exception {
        ModelAndView mv = new ModelAndView("/board/boardDetail");

        BoardDto board = boardService.selectBoardDetail(boardIdx);
        mv.addObject("board", board);

        return mv;
    }

    @RequestMapping("/board/updateBoard.do")
    public String updateBoard(BoardDto board) throws Exception {
        boardService.updateBoard(board);
        return "redirect:/board/openBoardList.do";
    }

    @RequestMapping("/board/deleteBoard.do")
    public String deleteBoard(int boardIdx) throws Exception {
        boardService.deleteBoard(boardIdx);
        return "redirect:/board/openBoardList.do";
    }

    @RequestMapping("/index.do")
    public String index() throws Exception {
        return "index";
    }

    @RequestMapping("/login.do")
    public String login(HttpServletRequest req) throws Exception {
        UserSession user = UserSession.builder()
                                .id(req.getParameter("id"))
                                .authority(req.getParameter("authority"))
                                .name(req.getParameter("name"))
                                .build();
        req.getSession().setAttribute("userSession", user);
        return "redirect:/board/openBoardList.do";
    }

    @RequestMapping("/board/downloadBoardFile.do")
    public void downloadBoardFile(@RequestParam int idx, @RequestParam int boardIdx, HttpServletResponse response) throws Exception {
        BoardFileDto boardFile = boardService.selectBoardFileInformation(idx, boardIdx);
        if(!ObjectUtils.isEmpty(boardFile)) {
            String fileName = boardFile.getOriginalFileName();

            byte[] files = FileUtils.readFileToByteArray(new File(boardFile.getStoredFilePath()));

            response.setContentType("application/octet-stream");
            response.setContentLength(files.length);
            response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(fileName,"UTF-8")+"\";");

            response.getOutputStream().write(files);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
    }

    @ExceptionHandler(ArithmeticException.class)
    public String arithmeticEx(Exception e) {
        log.error("exception", e);
        return "/error/error_arithmetic";
    }

}