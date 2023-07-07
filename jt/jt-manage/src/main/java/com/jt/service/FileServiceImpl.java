package com.jt.service;

import com.jt.vo.ImageVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@PropertySource("classpath:/properties/image.properties")//读取指定的配置文件
public class FileServiceImpl implements FileService{
    @Value("${image.fileDir}")
    private String fileDir;
    @Value("${image.urlPath}")
    private String urlPath;
    private static Set<String> typeSet = new HashSet();
    static{
        typeSet.add(".jpg");
        typeSet.add(".png");
        typeSet.add(".gif");
        typeSet.add(".jpeg");
    }
//    @Autowired
//    private FileMapper fileMapper;
    /*业务逻辑
    * 步骤：
    * 1.校验图片的类型：jpg/png/gif
    * 2.校验文件是否为恶意程序...
    * 3.采用分目录的结构进行存储
    * 4.避免文件重名 UUID 是避免文件重名的方法
    * @param uploadFile
    * @return */
    @Override
    public ImageVo upload(MultipartFile uploadFile) {
        //一.校验图片类型 三种方式：1.利用集合校验，2.正则表达式
        //1.1获取文件名称
        String fileName = uploadFile.getOriginalFilename();
        fileName = fileName.toLowerCase();
        //1.2获取文件后缀类型(子字符串)
        int index = fileName.lastIndexOf(".");
        //1.3 .jpg还是jpg，包含的知识点是数组，子串，前包后不包
        String fileType = fileName.substring(index);
        //1.4判断是否为图片类型
        if(!typeSet.contains(fileType)){
            return ImageVo.fail();
        }
        //二.校验文件是否是恶意程序
        //2.1 将数据转化为图片对象
        try{
            BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            if(width==0||height==0){
                return ImageVo.fail();
            }
            //三.实现分目录存储
            //3.1按照/yyyy/MM/dd/的方式进行目录的划分
            String dateDir = new SimpleDateFormat("/yyyy/MM/dd/").format(new Date());
            ///Users/nanjie/Pictures/2020/12/1
            String fileDirPath = fileDir + dateDir;
            File dirFile = new File(fileDirPath);
            //3.2创建目录
            if(!dirFile.exists()){
                dirFile.mkdirs();
            }
            //四实现文件上传
            //4.1准备文件名称 UUID 用横杠连接
            String uuid = UUID.randomUUID().toString().replace("-", "");
            //4.2动态生成文件文件名称 uuid.type
            String uuidName = uuid + fileType;
            //4.3实现文件的上传
            File realFile = new File(fileDirPath+uuidName);
            uploadFile.transferTo(realFile);
            String url =urlPath + dateDir+ uuidName;
            return ImageVo.success(url,width,height);
        }catch(IOException e){
            e.printStackTrace();
            return ImageVo.fail();
        }
    }
}
