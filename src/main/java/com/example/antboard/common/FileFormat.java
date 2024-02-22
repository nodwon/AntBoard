package com.example.antboard.common;

import lombok.Getter;
import java.util.Arrays;
import java.util.List;

@Getter
public enum FileFormat {
    IMAGE(List.of("gif", "jpeg", "jpg", "png", "pjpeg"));

    final List<String> formats;

    FileFormat(List<String> formats) {
        this.formats = formats;
    }

    public static FileFormat of(String format) {
        return Arrays.stream(FileFormat.values())
                .filter(v -> v.formats.contains(format))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}