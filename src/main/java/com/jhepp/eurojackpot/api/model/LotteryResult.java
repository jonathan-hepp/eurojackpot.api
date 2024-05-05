package com.jhepp.eurojackpot.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Model that represents a single lottery result record, each field mapped to its position in the source CSV file.
 * Since it does not contain db context and is read-only, this class can be serialized directly to the frontend
 * without an intermediary DTO.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LotteryResult {
    @CsvBindByPosition(position = 0)
    private Integer number;

    @CsvDate(value = "dd.MM.yyyy")
    @CsvBindByPosition(position = 1)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate date;

    @CsvBindByPosition(position = 2)
    private Integer draw1;

    @CsvBindByPosition(position = 3)
    private Integer draw2;

    @CsvBindByPosition(position = 4)
    private Integer draw3;

    @CsvBindByPosition(position = 5)
    private Integer draw4;

    @CsvBindByPosition(position = 6)
    private Integer draw5;

    @CsvBindByPosition(position = 7)
    private Integer draw6;

    @CsvBindByPosition(position = 8)
    private Integer draw7;
}
