package com.seanco.bfcm.pojo;

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
@TableName("t_countdown_order")
public class CountdownOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * countdown order id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * user id
     */
    private Long userId;

    /**
     * order id
     */
    private Long orderId;

    /**
     * product id
     */
    private Long productId;


}
