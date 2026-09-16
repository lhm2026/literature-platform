package com.literature.common.enums;
import lombok.Getter;

@Getter
public enum LiteratureStatus {
    UPLOADED("0", "已上传待解析"),
    PARSING("1", "解析中"),
    SUCCESS("2", "解析完成"),
    FAIL("3", "解析失败");

    private final String code;
    private final String desc;
    LiteratureStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
