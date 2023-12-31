package com.jt.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component      //将对象交给容器管理
public class MyMetaObjectHandler implements MetaObjectHandler {

    //完成入库和更新操作的自动赋值.
    @Override
    public void insertFill(MetaObject metaObject) {
        Date date = new Date();
        this.setInsertFieldValByName("created", date,metaObject);
        this.setInsertFieldValByName("updated", date,metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

        this.setUpdateFieldValByName("updated", new Date(), metaObject);
    }

}
