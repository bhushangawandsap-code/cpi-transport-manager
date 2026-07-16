package com.bhushan.cpitransport.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class Base64Util {
    public String encodeFile(Path path) {

        try {

            byte[] fileBytes = Files.readAllBytes(path);

            return Base64.getEncoder().encodeToString(fileBytes);

        } catch (IOException e) {

            throw new RuntimeException("Unable to encode file.", e);

        }

    }
}
