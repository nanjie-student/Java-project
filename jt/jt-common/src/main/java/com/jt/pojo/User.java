package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@TableName("tb_user")
public class User extends BasePojo{
    @TableId(type = IdType.AUTO)
    private Long id;//主键
    private String username;//用户名
    private String password;//密码
    private String phone;//电话号码
    private String email;//

}
