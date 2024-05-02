package com.jhepp.eurojackpot.api.service;

import com.jhepp.eurojackpot.api.dao.LotteryResultDao;
import com.jhepp.eurojackpot.api.model.LotteryResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LotteryServiceTest {

    static List<LotteryResult> sampleList;
    static LocalDate today = LocalDate.now();
    @Mock
    LotteryResultDao lotteryResultDao;
    @InjectMocks
    LotteryService lotteryService;

    @BeforeAll
    static void setUp() {
        sampleList = List.of(
                new LotteryResult(1, today.minusDays(10), 1, 2, 3, 4, 5, 6, 7),
                new LotteryResult(2, today.minusDays(5), 5, 6, 7, 8, 9, 10, 11),
                new LotteryResult(3, today, 9, 10, 11, 12, 13, 14, 15),
                new LotteryResult(4, today.plusDays(3), 13, 14, 15, 16, 17, 18, 19)
        );
    }

    @Test
    void getLotteryResultByNumber() {
        when(lotteryResultDao.getAllResults()).thenReturn(sampleList);

        var actualResult = lotteryService.findLotteryResults(1, null, null, null, false);

        assertEquals(sampleList.getFirst(), actualResult.getFirst());
    }

    @Test
    void getLotteryResultByNumber_Empty() {
        when(lotteryResultDao.getAllResults()).thenReturn(sampleList);

        var actualResult = lotteryService.findLotteryResults(99, null, null, null, false);

        assertTrue(actualResult.isEmpty());
    }

    @Test
    void getLotteryResultByDate() {
        when(lotteryResultDao.getAllResults()).thenReturn(sampleList);

        var actualResult = lotteryService.findLotteryResults(null, today.minusDays(7), today.plusDays(2), null, false);

        assertEquals(2, actualResult.size());
        assertEquals(2, actualResult.getFirst().getNumber());
        assertEquals(3, actualResult.getLast().getNumber());
    }

    @Test
    void getLotteryResultByDate_OpenEnded() {
        when(lotteryResultDao.getAllResults()).thenReturn(sampleList);

        var actualListNoFinish = lotteryService.findLotteryResults(null, today, null, null, false);
        var actualListNoStart = lotteryService.findLotteryResults(null, null, today.minusDays(1), null, false);

        assertEquals(2, actualListNoFinish.size());
        assertEquals(3, actualListNoFinish.getFirst().getNumber());
        assertEquals(4, actualListNoFinish.getLast().getNumber());

        assertEquals(2, actualListNoStart.size());
        assertEquals(1, actualListNoStart.getFirst().getNumber());
        assertEquals(2, actualListNoStart.getLast().getNumber());
    }

    @Test
    void getLotteryResultByDate_Empty() {
        when(lotteryResultDao.getAllResults()).thenReturn(sampleList);

        var actualList = lotteryService.findLotteryResults(null, today.plusDays(1), today.plusDays(2), null, false);

        assertTrue(actualList.isEmpty());
    }

    @Test
    void getLotteryResultByWinningNumber() {
        when(lotteryResultDao.getAllResults()).thenReturn(sampleList);

        var actualListThree = lotteryService.findLotteryResults(null, null, null, 3, false);
        var actualListFive = lotteryService.findLotteryResults(null, null, null, 5, false);

        assertEquals(1, actualListThree.size());
        assertEquals(2, actualListFive.size());
    }

    @Test
    void getLotteryResultByWinningNumber_WithSupplement() {
        when(lotteryResultDao.getAllResults()).thenReturn(sampleList);

        var actualListSix = lotteryService.findLotteryResults(null, null, null, 6, true);
        var actualListNineteen = lotteryService.findLotteryResults(null, null, null, 19, true);

        assertEquals(2, actualListSix.size());
        assertEquals(1, actualListNineteen.size());
    }

    @Test
    void getLotteryResultByWinningNumber_Empty() {
        when(lotteryResultDao.getAllResults()).thenReturn(sampleList);

        var actualListNineteen = lotteryService.findLotteryResults(null, null, null, 19, false);
        var actualListTwentyOne = lotteryService.findLotteryResults(null, null, null, 21, true);

        assertTrue(actualListNineteen.isEmpty());
        assertTrue(actualListTwentyOne.isEmpty());
    }

    @Test
    void getLotteryResultByWinningNumber_WithDates() {
        when(lotteryResultDao.getAllResults()).thenReturn(sampleList);

        var actualListSeven = lotteryService.findLotteryResults(null, today.minusDays(7), today.plusDays(2), 7, true);
        var actualListThirteen = lotteryService.findLotteryResults(null, today, today.plusDays(2), 13, true);
        var actualListFifteen = lotteryService.findLotteryResults(null, today, today.plusDays(2), 15, false);

        assertEquals(1, actualListSeven.size());
        assertEquals(1, actualListThirteen.size());
        assertTrue(actualListFifteen.isEmpty());
    }
}