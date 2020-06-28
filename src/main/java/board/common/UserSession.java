package board.common;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserSession implements Serializable {
    private String id;
    private String authority;           //00 : 일반유저, 01 : 관리자, 02 : 마스터
    private String name;
}