package board.board.service;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

public interface BoardService {

    List<BoardDto> selectBoardList() throws Exception;

    void insertBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception;

    BoardDto selectBoardDetail(int boardIdx) throws Exception;

    void updateBoard(BoardDto board);

    void deleteBoard(int boardIdx);

    BoardFileDto selectBoardFileInformation(int idx, int boardIdx) throws Exception;
}
