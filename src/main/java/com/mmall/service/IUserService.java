package com.mmall.service;

import com.mmall.common.ServiceResponse;
import com.mmall.pojo.User;

/**
 * Created by win on 2018/5/15.
 */
public interface IUserService {

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    ServiceResponse<User> login (String username, String password);

    /**
     * 用户注册
     * @param user
     * @return
     */
    ServiceResponse<User> register(User user);

    /**
     * 判断用户名和邮件是否存在
     * @param str
     * @param type
     * @return
     */
    public ServiceResponse<User> checkValid(String str,String type);

    /**
     * 忘记密码，提示问题
     * @param username
     * @return
     */
    public ServiceResponse selectQuestion(String username);


    /**
     * 验证忘记密码的问题
     * @param username
     * @param question
     * @param anwser
     * @return
     */
    public ServiceResponse checkAnswer(String username,String question,String anwser);

    /**
     * 重置密码
     * @param username
     * @param passwordNew
     * @param forgettoken
     * @return
     */
    public ServiceResponse<String> forgetRestPassword(String username,String passwordNew,String forgettoken);

    /**
     * 登陆状态下修改密码
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    public ServiceResponse<String> resetPassword(String passwordOld,String passwordNew,User user);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    public ServiceResponse<User> updateInformation(User user);

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    public ServiceResponse<User> getInformation(Integer userId);

    /**
     * backend
     * 校验是否是管理员
     * @param user
     * @return
     */
    public ServiceResponse checkAdminRole(User user);
}
