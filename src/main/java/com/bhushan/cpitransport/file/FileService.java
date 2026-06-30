package com.bhushan.cpitransport.file;

import java.nio.file.Files;
import java.nio.file.Paths;

public class FileService {

    public void saveZip(byte[] zipData,
                        String iflowId,
                        String sourceEnvironment) {

        try {

            Files.createDirectories(
                    Paths.get("downloads", sourceEnvironment,"iflows")
            );

            Files.write(
                    Paths.get(
                            "downloads",
                            sourceEnvironment,"iflows",
                            iflowId + ".zip"
                    ),
                    zipData
            );

            System.out.println("Download Successful!");
            System.out.println("File saved at : downloads/"
                    + sourceEnvironment + "/"
                    + iflowId + ".zip");

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

    public void saveConfiguration(String configuration,
                                  String iflowId,
                                  String sourceEnvironment) {

        try {

            Files.createDirectories(
                    Paths.get("downloads", sourceEnvironment,"configs")
            );

            Files.writeString(
                    Paths.get(
                            "downloads",
                            sourceEnvironment,"configs",
                            iflowId + "-config.json"
                    ),
                    configuration
            );

            System.out.println("Configuration Saved Successfully!");
            System.out.println("File saved at : downloads/"
                    + sourceEnvironment + "/"
                    + iflowId + "-config.json");

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

}