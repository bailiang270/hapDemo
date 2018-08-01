package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServiceResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by win on 2018/5/15.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServiceResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount==0){
            return ServiceResponse.createByErrorMessage("该用户不存在！");
        }
        //密码转成MD5
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username,md5Password);
        if (user==null){
            return ServiceResponse.createByErrorMessage("密码错误！");
        }
        //密码至空
        user.setPassword(StringUtils.EMPTY);
        return ServiceResponse.createBySuccess(user,"登录成功！");
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @Override
    public ServiceResponse<User> register(User user){
        ServiceResponse serviceResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if (!serviceResponse.isSuccess()){
            return serviceResponse;
        }
        serviceResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if (!serviceResponse.isSuccess()){
            return serviceResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount==0){
            return ServiceResponse.createByErrorMessage("注册失败！");
        }
        return ServiceResponse.createBySuccessMessage("注册成功！");
    }


    /**
     * 判断用户名和邮件是否存在
     * @param str
     * @param type
     * @return
     */
    @Override
    public ServiceResponse<User> checkValid(String str,String type){
        if(StringUtils.isNotBlank(type)){
            //开始校验
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if(resultCount>0){
                    return ServiceResponse.createByErrorMessage("该用户已存在！");
                }
            }
            if (Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if (resultCount>0){
                    return  ServiceResponse.createByErrorMessage("email已存在");
                }
            }
        }else {
            return ServiceResponse.createByErrorMessage("参数错误");
        }
        return ServiceResponse.createBySuccessMessage("校验成功");
    }


    /**
     * 忘记密码，提示问题
     * @param username
     * @return
     */
    @Override
    public ServiceResponse<String> selectQuestion(String username){
        ServiceResponse validResponse = this.checkValid(username,Const.USERNAME);
        if (validResponse.isSuccess()){
            return ServiceResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)){
            return ServiceResponse.createBySuccess(question);
        }
        return ServiceResponse.createByErrorMessage("找回密码的问题是空的");
    }


    /**
     * 验证忘记密码的问题
     * @param username
     * @param question
     * @param anwser
     * @return
     */
    @Override
    public ServiceResponse<String> checkAnswer(String username,String question,String anwser){
        int resultCount = userMapper.checkAnswer(username,question,anwser);
        if (resultCount>0){
            //说名问题以及问题答案是这个用户的，并且返回问题是正确的
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServiceResponse.createBySuccess(forgetToken);
        }
        return ServiceResponse.createByErrorMessage("问题的答案是错误的");
    }

    /**
     * 重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @Override
    public ServiceResponse<String> forgetRestPassword(String username,String passwordNew,String forgetToken){
        if (StringUtils.isBlank(forgetToken)){
            return ServiceResponse.createByErrorMessage("参数错误，参数需要传递");
        }
        ServiceResponse validResponse = this.checkValid(username,Const.USERNAME);
        if (validResponse.isSuccess()){
            return ServiceResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if (StringUtils.equals(forgetToken,token)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,md5Password);
            if (rowCount>0){
                return ServiceResponse.createBySuccessMessage("修改密码成功");
            }
        }else {
            return ServiceResponse.createByErrorMessage("token错误，请重新获取重置密码的token");
        }
        return ServiceResponse.createByErrorMessage("修改密码失败");
    }

    /**
     * 登陆状态下修改密码
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    @Override
    public ServiceResponse<String> resetPassword(String passwordOld,String passwordNew,User user){
        //防止横向越权，要校验一下这个用户的旧密码，一定要指定这个用户，因为我们回查询一个count(1)，如果不指定id,那么结果就是ture,count>0
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if (resultCount==0){
            return ServiceResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount>0) {
            return ServiceResponse.createBySuccessMessage("密码修改成功");
        }
        return ServiceResponse.createByErrorMessage("密码更新失败");
    }


    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @Override
    public ServiceResponse<User> updateInformation(User user){
        //username是不能被更新的
        //email也要进行一个校验，校验新的email是不是已经存在，并且存在email如果相同，不能是当前用户
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if (resultCount>0){
            return ServiceResponse.createByErrorMessage("email已存在，请更换email在尝试更新");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount>0){
            return ServiceResponse.createBySuccess(updateUser,"更新个人信息成功");
        }
        return ServiceResponse.createByErrorMessage("更新个人信息失败");
    }

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    @Override
    public ServiceResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null){
            return ServiceResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServiceResponse.createBySuccess(user);
    }

    /**
     * backend
     * 校验是否是管理员
     * @param user
     * @return
     */
    @Override
    public ServiceResponse checkAdminRole(User user){
        if (user!=null&&user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServiceResponse.createBySuccess();
        }
        return ServiceResponse.craeteByError();
    }



}
