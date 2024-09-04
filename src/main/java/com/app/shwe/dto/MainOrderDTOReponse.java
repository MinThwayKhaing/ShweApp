package com.app.shwe.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MainOrderDTOReponse {
    private int order_id;
    private int id;
    private String sys_key;
    private Date createdDate;
    private String userName;
}
