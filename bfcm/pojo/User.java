package com.seanco.bfcm.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author seanco
 * @since 2021-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * userid + tel
     */
    private Long id;

    private String nickname;

    /**
     * MD5(MD5(password + salt) + salt)
     */
    private String password;

    private String salt;

    /**
     * user profile icon
     */
    private String head;

    private Date registerDate;

    private Date lastLoginDate;

    private Integer loginCount;


}
