package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by 12996 on 2018/7/17.
 */
public interface IFileService {

    /**
     * 上传文件
     * @param file
     * @param path
     * @return
     */
    public String upload(MultipartFile file, String path);
}
