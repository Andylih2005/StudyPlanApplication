package com.example.StudyPlanApplication.service;

import org.jodconverter.core.DocumentConverter;
import org.jodconverter.local.JodConverter;
import org.jodconverter.local.office.LocalOfficeManager;

import java.io.File;

public class OfficeToPdfUtil {

    private static final String LIBRE_OFFICE_PATH = "C:\\Program Files\\LibreOffice";

    public static void convertToPdf(File inputFile, File outputFile) throws Exception {
        var officeManager = LocalOfficeManager.builder()
                .officeHome(new File(LIBRE_OFFICE_PATH))
                .install()
                .build();
        try {
            officeManager.start();
            JodConverter
                    .convert(inputFile)
                    .to(outputFile)
                    .execute();
        } finally {
            officeManager.stop();
        }
    }

}

