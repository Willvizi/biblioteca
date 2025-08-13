package com.example.biblioteca.dto.google;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GoogleBookVolumeInfo {
    private String title;
    private List<String> authors;
    private List<GoogleBookIndustryIdentifier> industryIdentifiers;
    private List<String> categories;
    private String mainCategory;
}
