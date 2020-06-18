package com.acguglielmo.simplecrud.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class Term {

    @Column(name = "DT_BEGGINING")
    private Long beggining;

    @Column(name = "DT_END")
    private Long end;

}
