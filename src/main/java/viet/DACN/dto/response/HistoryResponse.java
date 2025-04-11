package viet.DACN.dto.response;


import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class HistoryResponse {
    Long quizzId;
    String score;
    Date createAt;
}
