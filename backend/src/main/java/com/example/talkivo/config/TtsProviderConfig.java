package com.example.talkivo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TtsProviderConfig {

    @Value("${tts.text.max-length:1000}")
    private int maxTextLength;

    public int getMaxTextLength() {
        return maxTextLength;
    }
}