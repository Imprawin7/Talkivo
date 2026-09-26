package com.example.talkivo.controller;

import com.example.talkivo.dto.VoiceDto;
import com.example.talkivo.service.TtsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VoiceController {

    private final TtsService ttsService;

    public VoiceController(TtsService ttsService) {
        this.ttsService = ttsService;
    }

    @GetMapping("/voices")
    public List<VoiceDto> getVoices() {
        return ttsService.getAvailableVoices();
    }
}
