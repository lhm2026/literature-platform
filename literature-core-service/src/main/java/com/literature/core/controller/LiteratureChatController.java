package com.literature.core.controller;
import com.literature.common.result.Result;
import com.literature.core.service.LiteratureChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/literature/chat")
@RequiredArgsConstructor
public class LiteratureChatController {
    private final LiteratureChatService chatService;

    @PostMapping
    public Result<String> chat(@RequestBody String question) {
        String answer = chatService.chat(question);
        return Result.success(answer);
    }
}
