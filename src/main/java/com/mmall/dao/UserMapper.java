package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    //校验用户名是否存在
    int checkUsername(String username);
    //校验密码和用户名时候正确
    User selectLogin(@Param("username")String username,@Param("password")String password);
    //校验Email是否存在
    int checkEmail(String email);
    //忘记密码，问题提示
    String selectQuestionByUsername(String username);
    //忘记密码，问题校验
    int checkAnswer(@Param("username")String username,@Param("question")String question,@Param("answer")String answer);
    //更新密码，by用户名
    int updatePasswordByUsername(@Param("username")String username,@Param("passwordNew")String passwordNew);
    //指定用户校验密码
    int checkPassword(@Param("password")String password,@Param("userId")Integer userId);
    //指定用户验证邮箱
    int checkEmailByUserId(@Param("email")String email,@Param("userId")Integer userId);

}
