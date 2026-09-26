package com.example.talkivo.controller;

import com.example.talkivo.dto.TtsRequestDto;
import com.example.talkivo.dto.TtsResponseDto;
import com.example.talkivo.service.TtsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TtsController {

    private final TtsService ttsService;

    public TtsController(TtsService ttsService) {
        this.ttsService = ttsService;
    }

    @PostMapping("/tts")
    public ResponseEntity<TtsResponseDto> generateSpeech(@Valid @RequestBody TtsRequestDto request) {
        TtsResponseDto response = ttsService.generateSpeech(request);
        return ResponseEntity.ok(response);
    }
}
