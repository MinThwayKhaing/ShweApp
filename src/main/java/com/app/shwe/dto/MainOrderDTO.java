package com.app.shwe.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainOrderDTO {
    private int order_id;
    private String sys_key;
    private int car_type;
    private String car_brand;
    private Date start_date;
    private Date end_date;
    private String period;
    private String status;

}
