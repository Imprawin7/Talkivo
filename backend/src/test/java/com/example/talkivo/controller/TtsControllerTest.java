package com.example.talkivo.controller;

import com.example.talkivo.dto.TtsResponseDto;
import com.example.talkivo.service.TtsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TtsController.class)
class TtsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TtsService ttsService;

    @Test
    void returnsBadRequest_whenTextIsEmpty() throws Exception {
        String body = """
                {"text": "", "language": "en-US", "voice": "en-US-1"}
                """;

        mockMvc.perform(post("/api/tts")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnsAudioUrl_whenRequestIsValid() throws Exception {
        when(ttsService.generateSpeech(any())).thenReturn(new TtsResponseDto(true, "/audio/sample.mp3"));

        String body = """
                {"text": "Hello there", "language": "en-US", "voice": "en-US-1"}
                """;

        mockMvc.perform(post("/api/tts")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.audioUrl").value("/audio/sample.mp3"));
    }
}
