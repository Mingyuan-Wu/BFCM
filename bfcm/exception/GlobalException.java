package com.seanco.bfcm.exception;

import com.seanco.bfcm.vo.ResponseInfoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalException extends RuntimeException {
    private ResponseInfoEnum responseInfoEnum;
}
