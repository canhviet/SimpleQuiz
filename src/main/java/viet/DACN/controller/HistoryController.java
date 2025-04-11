package viet.DACN.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import viet.DACN.dto.request.HistoryRequest;
import viet.DACN.dto.response.HistoryResponse;
import viet.DACN.dto.response.ResponseData;
import viet.DACN.service.HistoryService;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class HistoryController {
    private final HistoryService historyService;

    @PostMapping("/")
    public ResponseData<?> addHistory(@RequestBody HistoryRequest request) {
        Long historyId = historyService.saveHistory(request);
        return new ResponseData<>(HttpStatus.CREATED.value(), "add history request", historyId);
    }

    @GetMapping("/{userId}")
    public ResponseData<?> getAll(@PathVariable Long userId) {
        try {
            List<HistoryResponse> list = historyService.allHistoryOfUser(userId);

            return new ResponseData<>(HttpStatus.OK.value(), "list all history", list);
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
