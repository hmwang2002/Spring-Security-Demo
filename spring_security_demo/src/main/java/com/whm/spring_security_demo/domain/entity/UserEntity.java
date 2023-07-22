package com.whm.spring_security_demo.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author whm
 * @date 2023/7/22 17:24
 */
@Data
@Builder
@TableName("user")
public class UserEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -6592972053673427168L;

    @TableId(type = IdType.INPUT)
    private Integer id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

}
