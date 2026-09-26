package com.example.talkivo.dto;

/**
 * Response payload for POST /api/tts
 */
public class TtsResponseDto {

    private boolean success;
    private String audioUrl;

    public TtsResponseDto() {
    }

    public TtsResponseDto(boolean success, String audioUrl) {
        this.success = success;
        this.audioUrl = audioUrl;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
}
