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
@TableName("t_countdown_product")
public class CountdownProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * countdown product id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * product id
     */
    private Long productId;

    /**
     * countdown price
     */
    private BigDecimal countdownPrice;

    /**
     * stock count of product
     */
    private Integer stockCount;

    /**
     * countdown start time
     */
    private Date startDate;

    /**
     * countdown end time
     */
    private Date endDate;


}
