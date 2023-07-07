package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ImageVo {
    //{"error":0,"url":"图片的保存路径","width":"图片的宽度","height":图片的高度}
    private Integer error;//0表示文件上传正确 1.上传有误
    private String url;//图片浏览的网络地址
    private Integer width;//宽度
    private Integer height;//高度
    public static ImageVo fail(){
        return new ImageVo(1,null,null,null);
    }
    public static ImageVo success(String url,Integer width,Integer height){
        return new ImageVo(0,url,width,height);
    }
}
