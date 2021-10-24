package com.seanco.bfcm.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum ResponseInfoEnum {
    // general
    SUCCESS(200, "SUCCESS"),
    ERROR(500, "INTERNAL_ERROR"),

    // login module
    LOGIN_ERROR(50001, "mobile or password incorrect"),
    MOBILE_ENTER_ERROR(50002, "mobile format not correct"),
    USER_NOT_FOUND(50006, "User not found"),
    PASSWORD_UPDATE_FAILED(50007, "Password udpate failed"),

    // bind error
    BIND_ERROR(50003, "parameter validation failed"),

    // countdown error
    NO_STOCK(50004, "stock is empty"),
    DUPLICATE_ORDER(50005, "can only order one per item per person"),

    // order error
    ORDER_NOT_EXIST(50008, "order does not exist")
    ;

    private final Integer code;
    private final String msg;
}
