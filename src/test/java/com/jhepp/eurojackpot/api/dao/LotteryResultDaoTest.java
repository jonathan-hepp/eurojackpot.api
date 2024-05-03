package com.jhepp.eurojackpot.api.dao;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LotteryResultDaoTest {

    String csvSample = """
                #;date;1;2;3;4;5;6;7
                735;29.03.2024;7;11;30;31;39;5;10
                733;22.03.2024;5;17;36;37;50;3;7
                734;26.03.2024;12;15;17;30;32;1;6
                732;19.03.2024;1;20;28;32;49;3;10
            """;

    @Test
    public void testGetAllResults() throws IOException {
        var csvData = File.createTempFile("csvsample", ".tmp");
        var writer = new FileWriter(csvData);
        writer.write(csvSample);
        writer.close();

        var lotteryResultDao = new LotteryResultDao(csvData);

        var results = lotteryResultDao.getAllResults();

        assertEquals(4, results.size());
        // verify it is sorted correctly
        assertEquals(735, results.getFirst().getNumber());
        assertEquals(734, results.get(1).getNumber());
        assertEquals(733, results.get(2).getNumber());
        assertEquals(732, results.getLast().getNumber());
    }

    @Test
    public void testGetAllResults_Error() {
        var exception = assertThrows(RuntimeException.class, () -> new LotteryResultDao(null));

        assertTrue(exception.getMessage().contains("Failed to load lottery results from CSV file"));
    }

    @Test
    public void testGetAllResults_WrongData() throws IOException {
        var csvData = File.createTempFile("csvfail", ".tmp");
        var writer = new FileWriter(csvData);
        writer.write("malformed data");
        writer.close();

        var lotteryResultDao = new LotteryResultDao(csvData);

        var results = lotteryResultDao.getAllResults();

        assertTrue(results.isEmpty());
    }
}