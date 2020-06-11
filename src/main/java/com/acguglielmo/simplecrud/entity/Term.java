package com.acguglielmo.simplecrud.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class Term {

    @Column(name = "DT_BEGGINING")
    private LocalDate beggining;

    @Column(name = "DT_END")
    private LocalDate end;

}
