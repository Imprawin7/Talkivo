package com.example.talkivo.service;

import com.example.talkivo.config.TtsProviderConfig;
import com.example.talkivo.dto.TtsRequestDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TtsServiceTest {

    @Test
    void piperTtsClientShouldExist() {

        TtsProviderConfig providerConfig = new TtsProviderConfig();

        AudioStorageService audioStorageService =
                new AudioStorageService();

        PiperTtsClient piperTtsClient =
                new PiperTtsClient();

        TtsServiceImpl ttsService =
                new TtsServiceImpl(
                        providerConfig,
                        audioStorageService,
                        piperTtsClient
                );

        assertNotNull(ttsService);
    }
}