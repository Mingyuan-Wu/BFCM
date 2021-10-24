package com.seanco.bfcm.pojo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author seanco
 * @since 2021-08-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * order id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * user id
     */
    private Long userId;

    /**
     * product id
     */
    private Long productId;

    /**
     * delivery address id
     */
    private Long deliveryAddrId;

    /**
     * [dup] product name
     */
    private String productName;

    /**
     * [dup] product number
     */
    private Integer productCount;

    /**
     * [dup] product single price
     */
    private BigDecimal productPrice;

    /**
     * order status: 0: NEW, 1: SHIPPED, 2: RECEIVED, 3: REFUNDED, 4: COMPLETED
     */
    private Integer status;

    /**
     * order create time
     */
    private Date createDate;

    /**
     * order pay time
     */
    private Date payDate;


}
