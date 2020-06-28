package board.board.mapper;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardDto> selectBoardList() throws Exception;

    void insertBoard(BoardDto board) throws Exception;

    void updateHitCount(int boardIdx);

    BoardDto selectBoardDetail(int boardIdx);

    void updateBoard(BoardDto board);

    void deleteBoard(int boardIdx);

    void insertBoardFileList(List<BoardFileDto> list) throws Exception;

    List<BoardFileDto> selectBoardFileList(int boardIdx) throws Exception;

    BoardFileDto selectBoardFileInformation(@Param("idx") int idx, @Param("boardIdx") int boardIdx);
}
