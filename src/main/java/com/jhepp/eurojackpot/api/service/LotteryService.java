package com.jhepp.eurojackpot.api.service;

import com.jhepp.eurojackpot.api.dao.LotteryResultDao;
import com.jhepp.eurojackpot.api.model.LotteryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class responsible for processing the results obtained from the data layer based on the parameters received from the consumer.
 */
@Service
public class LotteryService {

    private final LotteryResultDao lotteryResultDao;

    @Autowired
    public LotteryService(LotteryResultDao lotteryResultDao) {
        this.lotteryResultDao = lotteryResultDao;
    }

    /*
     * Return a list of lottery results based on the provided parameters. No field is mandatory.
     */
    public List<LotteryResult> findLotteryResults(Integer number, LocalDate start, LocalDate finish, Integer winningNumber, boolean includeSupplementaryNumbers) {
        return lotteryResultDao.getAllResults().stream()
                .filter(result -> number == null || number.equals(result.getNumber()))
                .filter(result -> isWithinDateRange(result.getDate(), start, finish))
                .filter(result -> winningNumber == null || isWinningNumber(result, winningNumber, includeSupplementaryNumbers))
                .collect(Collectors.toList());
    }

    /*
     * Filter lottery results by date, allowing open-ended search.
     */
    private boolean isWithinDateRange(LocalDate date, LocalDate start, LocalDate finish) {
        LocalDate startDate = Optional.ofNullable(start).orElse(LocalDate.MIN);
        LocalDate finishDate = Optional.ofNullable(finish).orElse(LocalDate.MAX);
        return !date.isBefore(startDate) && !date.isAfter(finishDate);
    }

    /*
     * Filter lottery results by draw winning numbers. Explicit filter pass if null.
     */
    private boolean isWinningNumber(LotteryResult result, Integer winningNumber, boolean includeSupplementaryNumbers) {
        if (winningNumber == null) return true;
        return result.getDraw1().equals(winningNumber)
                || result.getDraw2().equals(winningNumber)
                || result.getDraw3().equals(winningNumber)
                || result.getDraw4().equals(winningNumber)
                || result.getDraw5().equals(winningNumber)
                || (includeSupplementaryNumbers && (result.getDraw6().equals(winningNumber) || result.getDraw7().equals(winningNumber)));
    }
}
