package com.seanco.bfcm.vo;

import com.seanco.bfcm.pojo.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVo extends Product {
    private BigDecimal countdownPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
