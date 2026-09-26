package com.example.talkivo.service;

import com.example.talkivo.exception.TtsProviderException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class PiperTtsClient {

    @Value("${piper.python-path:python}")
    private String pythonPath;

    @Value("${piper.models-dir:./piper/models}")
    private String modelsDir;

    public byte[] synthesize(
            String text,
            String languageCode,
            String voiceName) {

        Path modelsPath = Paths.get(modelsDir)
                .toAbsolutePath()
                .normalize();

        String model = mapVoice(languageCode, voiceName);

        Path modelFile = modelsPath
                .resolve(model + ".onnx")
                .normalize();

        if (!modelFile.startsWith(modelsPath)) {
            throw new TtsProviderException(
                    "Invalid Piper model path"
            );
        }

        if (!Files.exists(modelFile)) {
            throw new TtsProviderException(
                    "Piper voice model not found: " + model
            );
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    pythonPath,
                    "-m",
                    "piper",
                    "-m",
                    modelFile.toString(),
                    "--output-raw"
            );

            processBuilder.redirectErrorStream(false);

            Process process = processBuilder.start();

            try {
                process.getOutputStream().write(
                        text.getBytes(StandardCharsets.UTF_8)
                );
                process.getOutputStream().write('\n');
                process.getOutputStream().close();

                ByteArrayOutputStream errorOutput =
                        new ByteArrayOutputStream();

                Thread errorReader = new Thread(() -> {
                    try (InputStream errorStream =
                                 process.getErrorStream()) {
                        errorStream.transferTo(errorOutput);
                    } catch (IOException ignored) {
                    }
                });

                errorReader.start();

                byte[] rawAudio =
                        process.getInputStream().readAllBytes();

                int exitCode = process.waitFor();

                errorReader.join();

                String consoleOutput =
                        errorOutput.toString(StandardCharsets.UTF_8);

                if (exitCode != 0) {
                    throw new TtsProviderException(
                            "Piper failed: " + consoleOutput
                    );
                }

                if (rawAudio.length == 0) {
                    throw new TtsProviderException(
                            "Piper generated empty audio"
                    );
                }

                return createWav(rawAudio);

            } finally {
                if (process.isAlive()) {
                    process.destroyForcibly();
                }
            }

        } catch (TtsProviderException e) {
            throw e;

        } catch (IOException e) {
            throw new TtsProviderException(
                    "Failed to execute Piper TTS",
                    e
            );

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            throw new TtsProviderException(
                    "Piper process was interrupted",
                    e
            );
        }
    }

    private byte[] createWav(byte[] rawAudio) {

        int sampleRate = 22050;
        int channels = 1;
        int bitsPerSample = 16;

        int byteRate =
                sampleRate * channels * bitsPerSample / 8;

        int blockAlign =
                channels * bitsPerSample / 8;

        int dataSize = rawAudio.length;

        ByteArrayOutputStream wav =
                new ByteArrayOutputStream(44 + dataSize);

        try {
            wav.write("RIFF".getBytes(StandardCharsets.US_ASCII));
            writeIntLE(wav, 36 + dataSize);

            wav.write("WAVE".getBytes(StandardCharsets.US_ASCII));

            wav.write("fmt ".getBytes(StandardCharsets.US_ASCII));
            writeIntLE(wav, 16);
            writeShortLE(wav, 1);
            writeShortLE(wav, channels);
            writeIntLE(wav, sampleRate);
            writeIntLE(wav, byteRate);
            writeShortLE(wav, blockAlign);
            writeShortLE(wav, bitsPerSample);

            wav.write("data".getBytes(StandardCharsets.US_ASCII));
            writeIntLE(wav, dataSize);
            wav.write(rawAudio);

            return wav.toByteArray();

        } catch (IOException e) {
            throw new TtsProviderException(
                    "Failed to create WAV audio",
                    e
            );
        }
    }

    private void writeIntLE(
            ByteArrayOutputStream output,
            int value) {

        output.write(value & 0xff);
        output.write((value >> 8) & 0xff);
        output.write((value >> 16) & 0xff);
        output.write((value >> 24) & 0xff);
    }

    private void writeShortLE(
            ByteArrayOutputStream output,
            int value) {

        output.write(value & 0xff);
        output.write((value >> 8) & 0xff);
    }

    private String mapVoice(
            String languageCode,
            String voiceName) {

        if (languageCode == null) {
            return "en_US-hfc_male-medium";
        }

        boolean female =
                voiceName != null
                        && voiceName.toLowerCase()
                        .contains("female");

        return switch (languageCode.toLowerCase()) {

            case "en" ->
                    female
                            ? "en_US-hfc_female-medium"
                            : "en_US-hfc_male-medium";

            case "hi" ->
                    female
                            ? "hi_IN-priyamvada-medium"
                            : "hi_IN-pratham-medium";

            case "ne" ->
                    voiceName != null
                            && voiceName.toLowerCase()
                            .contains("google")
                            ? "ne_NP-google-medium"
                            : "ne_NP-chitwan-medium";

            case "es" ->
                    female
                            ? "es_AR-daniela-high"
                            : "es_MX-ald-medium";

            case "ur" ->
                    female
                            ? "ur_PK-aegis_female-medium"
                            : "ur_PK-fasih-medium";

            default ->
                    throw new TtsProviderException(
                            "Unsupported language: "
                                    + languageCode
                    );
        };
    }
}