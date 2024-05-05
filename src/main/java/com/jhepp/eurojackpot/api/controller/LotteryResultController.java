package com.jhepp.eurojackpot.api.controller;

import com.jhepp.eurojackpot.api.model.LotteryResult;
import com.jhepp.eurojackpot.api.service.LotteryService;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * The controller has a single method that exposes the GET /results endpoint, providing optional query params for filtering results.
 * The endpoint is also available in <a href="http://localhost:8080/api/swagger-ui/index.html">Swagger UI</a>.
 */
@RestController
@CrossOrigin(origins = "*") // for dev purposes
@RequestMapping("/results")
public class LotteryResultController {
    private static final Logger logger = LoggerFactory.getLogger(LotteryResultController.class);

    private static final int MIN_VALID_NUMBER = 1;
    private static final int MAX_VALID_NUMBER = 50;
    private final LotteryService lotteryService;

    @Autowired
    public LotteryResultController(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    /**
     * Returns a collection of {@link LotteryResult} based on the parameters provided, or the full list if no params.
     * If validation fails, returns BAD_REQUEST.
     * This method is available in Swagger UI <a href="http://localhost:8080/api/swagger-ui/index.html#/lottery-result-controller/getLotteryResults">here</a>
     */
    @GetMapping
    public ResponseEntity<List<LotteryResult>> getLotteryResults(@Parameter(description = "Search by lottery result number", example = "123")
                                                                 @RequestParam(required = false) Integer resultNumber,
                                                                 @Parameter(description = "Set starting date to filter lottery results", example = "19.04.2024")
                                                                 @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate startDate,
                                                                 @Parameter(description = "Set finish date to filter lottery results", example = "27.04.2024")
                                                                 @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate finishDate,
                                                                 @Parameter(description = "Search by lottery result winning number", example = "32")
                                                                 @RequestParam(required = false) Integer winningNumber,
                                                                 @Parameter(description = "Defines whether to include the supplementary numbers into the winning number search", example = "true")
                                                                 @RequestParam(required = false) boolean includeSupplementaryNumbers) {
        if (isNotValid(resultNumber, startDate, finishDate, winningNumber, includeSupplementaryNumbers)) {
            logger.info("Validation failed for input: resultNumber={}, startDate={}, finishDate={}, winningNumber={}, includeSupplementaryNumbers={}",
                    resultNumber, startDate, finishDate, winningNumber, includeSupplementaryNumbers);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(lotteryService.findLotteryResults(resultNumber, startDate, finishDate, winningNumber, includeSupplementaryNumbers));
    }

    /**
     * The stated validation rules are:
     *
     * @param resultNumber  - must be greater than 0
     * @param startDate     - must not be after finishDate
     * @param finishDate    - must not be before startDate
     * @param winningNumber - must be between 1 and 50
     * @param includeSup    - must be true only if winningNumber is also valid
     * @return true if invalid, false otherwise
     */
    private boolean isNotValid(Integer resultNumber, LocalDate startDate, LocalDate finishDate, Integer winningNumber, boolean includeSup) {
        return (resultNumber != null && resultNumber < MIN_VALID_NUMBER)
                || (startDate != null && finishDate != null && startDate.isAfter(finishDate))
                || (winningNumber != null && (winningNumber < MIN_VALID_NUMBER || winningNumber > MAX_VALID_NUMBER))
                || (includeSup && winningNumber == null);
    }
}
