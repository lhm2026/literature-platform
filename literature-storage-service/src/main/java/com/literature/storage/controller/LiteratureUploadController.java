package com.literature.storage.controller;
import com.literature.common.dto.LiteratureParseMsg;
import com.literature.common.result.Result;
import com.literature.storage.service.LiteratureMsgProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@RestController
@RequestMapping("/literature")
@RequiredArgsConstructor
public class LiteratureUploadController {
    private final LiteratureMsgProducer msgProducer;

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        // 全局唯一文献ID，幂等标识
        String literatureId = UUID.randomUUID().toString();
        // 模拟文件存储（生产替换为OSS）
        String fileUrl = "/upload/" + literatureId + "_" + file.getOriginalFilename();
        String fileType = getFileType(file.getOriginalFilename());

        LiteratureParseMsg msg = new LiteratureParseMsg();
        msg.setLiteratureId(literatureId);
        msg.setFileUrl(fileUrl);
        msg.setFileType(fileType);
        msgProducer.sendParseMsg(msg);

        return Result.success(literatureId);
    }

    private String getFileType(String fileName) {
        if(fileName.toLowerCase().endsWith(".pdf")) return "pdf";
        if(fileName.toLowerCase().endsWith(".docx")) return "docx";
        return "other";
    }
}
