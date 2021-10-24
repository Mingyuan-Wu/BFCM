package com.seanco.bfcm.vo;

import com.seanco.bfcm.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountdownOrderVo {
    private User user;
    private Long productId;
}
