package com.ironhack.ironbankapi.log.controller;

import com.ironhack.ironbankapi.log.model.LogItem;
import com.ironhack.ironbankapi.log.repository.LogItemRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/log")
public class LogController {

    final LogItemRepository logItemRepository;

    public LogController(LogItemRepository logItemRepository) {
        this.logItemRepository = logItemRepository;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    List<LogItem> getAllLogs() {
        return logItemRepository.findAll(Sort.by(new Sort.Order(Sort.Direction.DESC, "timestamp")));
    }

}
