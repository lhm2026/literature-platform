package com.literature.core.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("literature")
public class Literature {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String fileName;
    private String fileUrl;
    private String status;
    private String author;
    private Long createTime;
}
