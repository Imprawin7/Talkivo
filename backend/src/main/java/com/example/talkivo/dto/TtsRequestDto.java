package com.example.talkivo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Incoming payload for POST /api/tts
 */
public class TtsRequestDto {

    @NotBlank(message = "Text must not be empty")
    @Size(max = 1000, message = "Text exceeds the maximum allowed length")
    private String text;

    @NotBlank(message = "Language must be specified")
    private String language;

    @NotBlank(message = "Voice must be specified")
    private String voice;

    public TtsRequestDto() {
    }

    public TtsRequestDto(String text, String language, String voice) {
        this.text = text;
        this.language = language;
        this.voice = voice;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }
}
