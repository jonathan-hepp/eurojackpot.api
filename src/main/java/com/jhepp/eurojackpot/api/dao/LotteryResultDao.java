package com.jhepp.eurojackpot.api.dao;

import com.jhepp.eurojackpot.api.model.LotteryResult;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.List;

/**
 * Class responsible for providing the source data.
 * The data is read from a CSV file resource, parsed, deserialized, and provided as a collection for consumption.
 * Given the nature of the data, the collection is sorted to display lottery results in descending order by number.
 */
@Component
public class LotteryResultDao {

    private static final Logger logger = LoggerFactory.getLogger(LotteryResultDao.class);
    private static final String FILE_NAME = "classpath:data/results.csv";
    private static final char CSV_SEPARATOR = ';';
    private static final int SKIP_LINES = 1;

    private final List<LotteryResult> results;

    public LotteryResultDao() {
        try {
            this.results = sortResults(loadResultsFromSource(ResourceUtils.getFile(FILE_NAME)));
        } catch (FileNotFoundException e) {
            logger.error("CSV file not found: {}", FILE_NAME);
            throw new RuntimeException("Failed to load lottery results from CSV file", e);
        }
    }

    /**
     * For test purposes
     *
     * @param file - test csv file
     */
    public LotteryResultDao(File file) {
        this.results = sortResults(loadResultsFromSource(file));
    }

    /**
     * Retrieves all lottery results.
     *
     * @return List of all lottery results.
     */
    public List<LotteryResult> getAllResults() {
        return results;
    }

    /**
     * Using OpenCSV to read the source data from the provided file, mapping it to a {@link LotteryResult} collection.
     *
     * @param file - a csv file to extract the data from
     * @return a collection of results if parsing succeeds, an exception otherwise.
     */
    private List<LotteryResult> loadResultsFromSource(File file) {
        try (FileReader fileReader = new FileReader(file)) {
            return new CsvToBeanBuilder<LotteryResult>(fileReader)
                    .withType(LotteryResult.class)
                    .withSeparator(CSV_SEPARATOR)
                    .withSkipLines(SKIP_LINES)
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            logger.error("CSV file not found: {}", FILE_NAME);
            throw new RuntimeException("Failed to load lottery results from CSV file", e);
        } catch (Exception e) {
            logger.error("Error reading CSV file: {} - {}", FILE_NAME, e.getMessage());
            throw new RuntimeException("Failed to load lottery results from CSV file", e);
        }
    }

    /**
     * Sorts the list of lottery results by the result number in descending order.
     *
     * @param results List of lottery results to be sorted.
     * @return Sorted list of lottery results.
     */
    private List<LotteryResult> sortResults(List<LotteryResult> results) {
        return results.stream()
                .sorted(Comparator.comparing(LotteryResult::getNumber).reversed())
                .toList();
    }
}
