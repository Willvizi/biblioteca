package com.example.biblioteca.dto.google;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GoogleBooksResponse {
    private List<GoogleBookItem> items;
}
