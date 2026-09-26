package com.example.talkivo.dto;

/**
 * Represents a single selectable voice, returned by GET /api/voices
 */
public class VoiceDto {

    private String id;
    private String name;
    private String language;
    private String gender;

    public VoiceDto() {
    }

    public VoiceDto(String id, String name, String language, String gender) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
