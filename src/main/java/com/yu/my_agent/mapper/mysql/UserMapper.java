package com.yu.my_agent.mapper.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yu.my_agent.entity.mysql.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

// MySQL Mapper接口
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // MyBatis-Plus已经提供了常用的CRUD方法
    // 可以在这里添加自定义的SQL方法
    
    List<User> selectUsersByEmailContaining(@Param("email") String email);
}
