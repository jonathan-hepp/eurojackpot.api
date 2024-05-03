package com.jhepp.eurojackpot.api.controller;

import com.jhepp.eurojackpot.api.model.LotteryResult;
import com.jhepp.eurojackpot.api.service.LotteryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LotteryResultControllerTest {
    @Mock
    LotteryService lotteryService;
    @InjectMocks
    LotteryResultController lotteryResultController;

    @Test
    void testGetLotteryResults() {
        var today = LocalDate.now();
        when(lotteryService.findLotteryResults(any(), any(), any(), any(), anyBoolean())).thenReturn(List.of(
                new LotteryResult(1, today.minusDays(10), 1, 2, 3, 4, 5, 6, 7),
                new LotteryResult(2, today.minusDays(5), 5, 6, 7, 8, 9, 10, 11),
                new LotteryResult(3, today, 9, 10, 11, 12, 13, 14, 15),
                new LotteryResult(4, today.plusDays(3), 13, 14, 15, 16, 17, 18, 19)
        ));

        var result = lotteryResultController.getLotteryResults(1, null, null, null, false);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.hasBody());
        assertFalse(Objects.requireNonNull(result.getBody()).isEmpty());
        assertEquals(1, result.getBody().getFirst().getNumber());
    }

    @Test
    void testGetLotteryResults_Empty() {
        when(lotteryService.findLotteryResults(any(), any(), any(), any(), anyBoolean())).thenReturn(List.of());

        var result = lotteryResultController.getLotteryResults(99, null, null, null, false);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.hasBody());
        assertTrue(Objects.requireNonNull(result.getBody()).isEmpty());
    }

    @Test
    void testGetLotteryResults_NumberValidation() {
        var result = lotteryResultController.getLotteryResults(0, null, null, null, false);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertFalse(result.hasBody());
    }

    @Test
    void testGetLotteryResults_DatesValidation() {
        var result = lotteryResultController.getLotteryResults(null, LocalDate.of(2024, 3, 12), LocalDate.of(2024, 1, 7), null, false);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertFalse(result.hasBody());
    }

    @Test
    void testGetLotteryResults_WinningNumberValidation() {
        var result = lotteryResultController.getLotteryResults(null, null, null, 123, false);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertFalse(result.hasBody());
    }

    @Test
    void testGetLotteryResults_IncludeSupValidation() {
        var result = lotteryResultController.getLotteryResults(null, null, null, null, true);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertFalse(result.hasBody());
    }
}