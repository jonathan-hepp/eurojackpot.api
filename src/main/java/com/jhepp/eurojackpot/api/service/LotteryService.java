package com.jhepp.eurojackpot.api.service;

import com.jhepp.eurojackpot.api.dao.LotteryResultDao;
import com.jhepp.eurojackpot.api.model.LotteryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
                .filter(r -> byDate(r, start, finish))
                .filter(r -> byWinningNumber(r, winningNumber, includeSupplementaryNumbers))
                .filter(r -> byNumber(r, number)).toList();
    }

    /*
     * Filter lottery results by number. Explicit filter pass if null.
     */
    private boolean byNumber(LotteryResult result, Integer number) {
        if (number == null) return true;
        return number.equals(result.getNumber());
    }

    /*
     * Filter lottery results by date, allowing open-ended search.
     */
    private boolean byDate(LotteryResult result, LocalDate start, LocalDate finish) {
        var startDate = start != null ? start : LocalDate.MIN;
        var finishDate = finish != null ? finish : LocalDate.MAX;
        return !result.getDate().isBefore(startDate) && !result.getDate().isAfter(finishDate);
    }

    /*
     * Filter lottery results by draw winning numbers. Explicit filter pass if null.
     */
    private boolean byWinningNumber(LotteryResult result, Integer winningNumber, boolean includeSupplementaryNumbers) {
        if (winningNumber == null) return true;
        return result.getDraw1().equals(winningNumber)
                || result.getDraw2().equals(winningNumber)
                || result.getDraw3().equals(winningNumber)
                || result.getDraw4().equals(winningNumber)
                || result.getDraw5().equals(winningNumber)
                || (includeSupplementaryNumbers && (result.getDraw6().equals(winningNumber) || result.getDraw7().equals(winningNumber)));
    }
}
