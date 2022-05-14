package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dto.HistoryGetAllResponseDTO;
import org.example.exception.AccountRemovedException;
import org.example.exception.ForbiddenException;
import org.example.manager.HistoryManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class HistoryController {
    private HistoryManager manager;

    @RequestMapping("/history.getAll")
    public List<HistoryGetAllResponseDTO> getAll(int limit, long offset) throws ForbiddenException, AccountRemovedException {
        return manager.getAll(limit, offset);
    }
}
