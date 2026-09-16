package com.literature.common.dto;
import lombok.Data;
import java.io.Serializable;

@Data
public class LiteratureParseMsg implements Serializable {
    // 全局唯一文献ID，幂等key
    private String literatureId;
    // 文件存储地址
    private String fileUrl;
    // 文件类型 pdf / docx
    private String fileType;
}
