package com.study.cucumberRestApi.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Objects.nonNull;

public final class ResourceFileLoader {
    private Class<?> clazz;
    private String filename;
    private String path;

    public ResourceFileLoader(Class<?> clazz) {
        checkNotNull(clazz, "Class can't be null");
        this.clazz = clazz;
    }

    public ResourceFileLoader withName(String filename) {
        this.filename = filename;
        return this;
    }

    public ResourceFileLoader withPath(String path) {
        this.path = path;
        return this;
    }
    
    public<T> T jsonToObject(Class<T> objectType) {
        String pathToFile = buildPathToFile();
        InputStream is = clazz.getClassLoader().getResourceAsStream(pathToFile);
        checkNotNull(is, format("Can't read '%s' file from resources '%s'", filename, path));
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).readValue(is, objectType);
        }
        catch (Exception e) {
            return null;
        }
    }

    private String buildPathToFile() {
        checkNotNull(filename, "Filename of a file from resources can't be null");
        StringBuilder sb = new StringBuilder();
        if (nonNull(path)) {
            sb.append(path).append("/");
        }
        sb.append(filename);
        return sb.toString();
    }

    public static File getFileFromResources(String filename) throws IOException {
        String pathToFileInResources = format("%s%s%s", "files", File.separator, filename);
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(pathToFileInResources);
        File tmpFile = new File(System.getProperty("user.dir") + File.separator + filename);
        assert inputStream != null;
        FileUtils.copyInputStreamToFile(inputStream, tmpFile);
        return tmpFile;
    }
}
