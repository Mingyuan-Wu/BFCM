package com.seanco.bfcm.pojo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("t_product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * product id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * product name
     */
    private String productName;

    /**
     * product title
     */
    private String productTitle;

    /**
     * product image
     */
    private String productImg;

    /**
     * product detail
     */
    private String productDetail;

    /**
     * product price
     */
    private BigDecimal productPrice;

    /**
     * number of product in stock
     */
    private Integer productStock;


}
