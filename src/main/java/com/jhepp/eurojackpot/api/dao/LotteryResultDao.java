package com.jhepp.eurojackpot.api.dao;

import com.jhepp.eurojackpot.api.model.LotteryResult;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.List;

@Component
public class LotteryResultDao {

    private static final String fileName = "classpath:data/results.csv";
    private List<LotteryResult> results;

    public List<LotteryResult> getAllResults() {
        if (results == null) {
            results = loadResultsFromSource().stream().sorted(Comparator.comparing(LotteryResult::getNumber).reversed()).toList();
        }
        return results;
    }

    private List<LotteryResult> loadResultsFromSource() {
        try {
            return new CsvToBeanBuilder(new FileReader(ResourceUtils.getFile(fileName)))
                    .withType(LotteryResult.class)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
