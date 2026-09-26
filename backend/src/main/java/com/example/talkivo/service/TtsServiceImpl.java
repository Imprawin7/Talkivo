package com.example.talkivo.service;

import com.example.talkivo.config.TtsProviderConfig;
import com.example.talkivo.dto.TtsRequestDto;
import com.example.talkivo.dto.TtsResponseDto;
import com.example.talkivo.dto.VoiceDto;
import com.example.talkivo.exception.InvalidTextException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TtsServiceImpl implements TtsService {

    private final TtsProviderConfig providerConfig;
    private final AudioStorageService audioStorageService;
    private final PiperTtsClient ttsClient;

    private static final List<VoiceDto> AVAILABLE_VOICES = List.of(

            new VoiceDto(
                    "en-male",
                    "English Male",
                    "en",
                    "male"
            ),

            new VoiceDto(
                    "en-female",
                    "English Female",
                    "en",
                    "female"
            ),

            new VoiceDto(
                    "hi-male",
                    "Hindi Male",
                    "hi",
                    "male"
            ),

            new VoiceDto(
                    "hi-female",
                    "Hindi Female",
                    "hi",
                    "female"
            ),

            new VoiceDto(
                    "ne-chitwan",
                    "Nepali Chitwan",
                    "ne",
                    "standard"
            ),

            new VoiceDto(
                    "ne-google",
                    "Nepali Google",
                    "ne",
                    "standard"
            ),

            new VoiceDto(
                    "es-male",
                    "Spanish Male",
                    "es",
                    "male"
            ),

            new VoiceDto(
                    "es-female",
                    "Spanish Female",
                    "es",
                    "female"
            ),

            new VoiceDto(
                    "ur-male",
                    "Urdu Male",
                    "ur",
                    "male"
            ),

            new VoiceDto(
                    "ur-female",
                    "Urdu Female",
                    "ur",
                    "female"
            )
    );

    public TtsServiceImpl(
            TtsProviderConfig providerConfig,
            AudioStorageService audioStorageService,
           PiperTtsClient ttsClient) {

        this.providerConfig = providerConfig;
        this.audioStorageService = audioStorageService;
        this.ttsClient = ttsClient;
    }

    @Override
    public TtsResponseDto generateSpeech(TtsRequestDto request) {

        validate(request);

        byte[] audioBytes = ttsClient.synthesize(
                request.getText(),
                request.getLanguage(),
                request.getVoice()
        );

        String audioUrl =
                audioStorageService.save(audioBytes, "wav");

        return new TtsResponseDto(true, audioUrl);
    }

    @Override
    public List<VoiceDto> getAvailableVoices() {
        return AVAILABLE_VOICES;
    }

    private void validate(TtsRequestDto request) {

        if (request.getText().length()
                > providerConfig.getMaxTextLength()) {

            throw new InvalidTextException(
                    "Text exceeds the maximum allowed length of "
                            + providerConfig.getMaxTextLength()
                            + " characters"
            );
        }

        boolean languageExists =
                AVAILABLE_VOICES.stream()
                        .anyMatch(v ->
                                v.getLanguage()
                                        .equals(request.getLanguage())
                        );

        if (!languageExists) {
            throw new InvalidTextException(
                    "Selected language is not supported"
            );
        }

        boolean voiceMatches =
                AVAILABLE_VOICES.stream()
                        .anyMatch(v ->
                                v.getId()
                                        .equals(request.getVoice())
                                        && v.getLanguage()
                                        .equals(request.getLanguage())
                        );

        if (!voiceMatches) {
            throw new InvalidTextException(
                    "Selected voice does not belong to the selected language"
            );
        }
    }
}