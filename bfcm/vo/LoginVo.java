package com.seanco.bfcm.vo;

import com.seanco.bfcm.validator.MobileValid;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class LoginVo {
    @NotNull
    @MobileValid
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;
}
