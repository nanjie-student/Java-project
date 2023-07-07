package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@TableName("tb_item_desc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ItemDesc extends BasePojo{
    @TableId
    private Long itemId;//商品ID与Item表中的数据一致
    private String itemDesc;//商品详情

}
