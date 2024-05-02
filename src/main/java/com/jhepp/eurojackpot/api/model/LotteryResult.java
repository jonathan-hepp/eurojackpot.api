package com.jhepp.eurojackpot.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
