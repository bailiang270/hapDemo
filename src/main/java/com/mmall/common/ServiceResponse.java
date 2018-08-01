package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * 实现序列化接口Serializable
 * Created by win on 2018/5/15.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)//值为空时的key不显示在返回json字符串中
public class ServiceResponse<T> implements Serializable {

    //状态
    private int status;
    //返回信息
    private String msg;
    //返回数据
    private T data;

    private ServiceResponse(int status) {
        this.status = status;
    }

    private ServiceResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServiceResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ServiceResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    @JsonIgnore//不显示在json中
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    //成功的构造方法
    public static <T> ServiceResponse<T> createBySuccess(){
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode());
    }
    public static <T> ServiceResponse<T> createBySuccessMessage(String msg){
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }
    public static <T> ServiceResponse<T> createBySuccess(T data){
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }
    public static <T> ServiceResponse<T> createBySuccess(T data,String msg){
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    //失败的构造方法
    public static <T> ServiceResponse<T> craeteByError(){
        return new ServiceResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }
    public static <T> ServiceResponse<T> createByErrorMessage(String errorMessage){
        return new ServiceResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }

    //提示Code构造方法
    public static <T> ServiceResponse<T> createByErrorCodeMessage(int errorCode,String errorMessage ){
        return new ServiceResponse<T>(errorCode,errorMessage);
    }
}
