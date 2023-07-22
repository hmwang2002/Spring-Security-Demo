package com.whm.spring_security_demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whm.spring_security_demo.domain.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author whm
 * @date 2023/7/22 18:31
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
