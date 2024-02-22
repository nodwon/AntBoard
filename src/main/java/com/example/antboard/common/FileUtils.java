package com.example.antboard.common;

import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

public final class FileUtils {

    private static final String BASE_DIR = "images";

    //홈디렉토리로 변환
    public static String getLocalHomeDirectory() {
        return System.getProperty("user.home");
    }

    public static String getSavedFilePath(String path, Long entityId, MultipartFile multipartFile) {
        if (Objects.isNull(multipartFile.getOriginalFilename())) {
            throw new IllegalArgumentException();
        }

        String format = getFormat(multipartFile.getOriginalFilename()).toLowerCase();
        return String.format("%s%s/%s.%s", path, entityId, createFileId(), format);
    }

    public static String getFormat(String originalName) {
        String format = originalName.substring(originalName.lastIndexOf('.') + 1);
        FileFormat.of(format);
        return format;
    }
    public static String createPath(MultipartFile multipartFile) {
        final String fileId = FileUtils.createFileId();
        final String format = FileUtils.getFormat(Objects.requireNonNull(multipartFile.getContentType()));
        FileFormat fileFormat = FileFormat.of(format);
        return String.format("%s/%s.%s", BASE_DIR, fileId, fileFormat.name().toLowerCase());
    }

    public static String createFileId() {
        return UUID.randomUUID().toString();
    }
}
