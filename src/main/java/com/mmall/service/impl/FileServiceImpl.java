package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by 12996 on 2018/7/17.
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file,String path){

        String fileName = file.getOriginalFilename();
        //获取扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        //重新命名文件
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
        //打印日志
        logger.info("开始上传文件，上传的文件名:{}，上传的路径:{}，新闻件名:{}",fileName,path,uploadFileName);

        //判断路径是否存在，不存在新建
        File fileDir = new File(path);
        if (!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path,uploadFileName);
        try {
            //文件上传成功
            file.transferTo(targetFile);
            //文件上传到ftp服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //删除本地文件
            targetFile.delete();


        } catch (IOException e) {
            logger.error("上传文件异常",e);
            return null;
        }

        return targetFile.getName() ;
    }


}
