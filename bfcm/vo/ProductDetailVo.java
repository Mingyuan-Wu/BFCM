package com.seanco.bfcm.vo;

import com.seanco.bfcm.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailVo {
    private User user;
    private ProductVo productVo;
    private int countdownStatus;
    private int remainSeconds;
}
