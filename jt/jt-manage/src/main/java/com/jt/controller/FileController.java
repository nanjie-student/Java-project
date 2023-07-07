package com.jt.controller;

import com.jt.service.FileService;
import com.jt.vo.ImageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class FileController {
    @Autowired
    private FileService fileService;
    /*
    * url地址:Http://localhost:8091/file.jsp
    * 参数：文件信息fileImage
    * 返回值：String*/
    @RequestMapping("/file")
    public String file(MultipartFile fileImage) throws IOException {
        //1.获取图片的名称
        String name = fileImage.getOriginalFilename();
        //2.定义文件目录
        String fileDirPath ="/Users/nanjie/Pictures";
        //3.创建目录
        File fileDir = new File(fileDirPath);
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
        //4.生成文件的全路径
        String filePath = fileDirPath + "/" + name;
        File imagefile = new File(filePath);
        //5.实现全路径
        fileImage.transferTo(imagefile);
        return "文件上传成功";
    }
    /*
    * 业务需求：实现文件的上传操作
    * URL：http://loaclhost:8091/pic/upload?dir=image
    * 参数：uploadFile
    * 返回值：ImageVo对象
    * */
    @RequestMapping("/pic/upload")
    public ImageVo upload(MultipartFile uploadFile){
        return fileService.upload(uploadFile);
    }
}
