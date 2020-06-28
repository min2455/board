package board.common;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserSession{
    private String id;
    private String authority;           //00 : 일반유저, 01 : 관리자, 02 : 마스터
    private String name;
}