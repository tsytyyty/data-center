package com.data.center.pojo.Do;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TabulateData {

    private int id;

    private String port;

    private String good;

    private int year;

    private int month;

    //吞吐量
    private int throughPut;

}
