package com.example.talkivo.service;

import com.example.talkivo.dto.TtsRequestDto;
import com.example.talkivo.dto.TtsResponseDto;
import com.example.talkivo.dto.VoiceDto;

import java.util.List;

public interface TtsService {

    /**
     * Validates the request against supported languages/voices, calls the
     * TTS provider, stores the resulting audio, and returns a URL to it.
     */
    TtsResponseDto generateSpeech(TtsRequestDto request);

    /**
     * Returns the list of voices available for selection in the UI.
     */
    List<VoiceDto> getAvailableVoices();
}
