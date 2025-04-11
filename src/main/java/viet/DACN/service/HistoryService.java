package viet.DACN.service;

import java.util.List;

import viet.DACN.dto.request.HistoryRequest;
import viet.DACN.dto.response.HistoryResponse;

public interface HistoryService {
    List<HistoryResponse> allHistoryOfUser(Long userId);
    Long saveHistory(HistoryRequest request);
}
