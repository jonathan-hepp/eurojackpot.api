package com.jhepp.eurojackpot.api.controller;

import com.jhepp.eurojackpot.api.model.LotteryResult;
import com.jhepp.eurojackpot.api.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/results")
public class LotteryResultController {

    private final LotteryService lotteryService;

    @Autowired
    public LotteryResultController(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    @GetMapping
    public ResponseEntity<List<LotteryResult>> getLotteryResults(@RequestParam(required = false) Integer resultNumber,
                                                                 @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate startDate,
                                                                 @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate finishDate,
                                                                 @RequestParam(required = false) Integer winningNumber,
                                                                 @RequestParam(required = false) boolean includeSupplementaryNumbers) {
        return ResponseEntity.ok(lotteryService.findLotteryResults(resultNumber, startDate, finishDate, winningNumber, includeSupplementaryNumbers));
    }
}
