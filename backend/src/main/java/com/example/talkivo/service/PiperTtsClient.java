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
import java.util.UUID;

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

        Path inputFile = null;
        Path outputFile = null;

        try {
            String model = mapVoice(languageCode, voiceName);

            Path modelsPath = Paths.get(modelsDir)
                    .toAbsolutePath()
                    .normalize();

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

            String id = UUID.randomUUID().toString();

            inputFile = Paths.get(
                    System.getProperty("java.io.tmpdir"),
                    "talkivo-" + id + ".txt"
            );

            outputFile = Paths.get(
                    System.getProperty("java.io.tmpdir"),
                    "talkivo-" + id + ".wav"
            );

            Files.writeString(
                    inputFile,
                    text,
                    StandardCharsets.UTF_8
            );

            ProcessBuilder processBuilder = new ProcessBuilder(
                    pythonPath,
                    "-m",
                    "piper",
                    "-m",
                    modelFile.toString(),
                    "-i",
                    inputFile.toString(),
                    "-f",
                    outputFile.toString()
            );

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            ByteArrayOutputStream processOutput =
                    new ByteArrayOutputStream();

            try (InputStream inputStream =
                         process.getInputStream()) {
                inputStream.transferTo(processOutput);
            }

            int exitCode = process.waitFor();

            String consoleOutput =
                    processOutput.toString(StandardCharsets.UTF_8);

            if (exitCode != 0) {
                throw new TtsProviderException(
                        "Piper failed: " + consoleOutput
                );
            }

            if (!Files.exists(outputFile)) {
                throw new TtsProviderException(
                        "Piper did not generate an audio file"
                );
            }

            byte[] audioBytes =
                    Files.readAllBytes(outputFile);

            if (audioBytes.length == 0) {
                throw new TtsProviderException(
                        "Piper generated an empty audio file"
                );
            }

            return audioBytes;

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

        } finally {
            try {
                if (inputFile != null) {
                    Files.deleteIfExists(inputFile);
                }

                if (outputFile != null) {
                    Files.deleteIfExists(outputFile);
                }

            } catch (IOException ignored) {
            }
        }
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