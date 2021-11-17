package com.fpt.folderHandleLib;

import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileFolderCrudLib {

    public static boolean createFileFolder(String sourcePath) {
        boolean isCreated = false;
        try {
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            File sourceFile = new File(processedSourcePath);
            if (isFile(sourceFile.getName())) {
                Files.createParentDirs(sourceFile);
                Files.touch(sourceFile);
                isCreated = true;
            } else {
                isCreated = sourceFile.mkdirs();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isCreated;
    }

    public static boolean moveFileFolder(String sourcePath, String targetPath) {
        boolean isMoved = false;
        try {
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            String processedTargetPath = isPathAbsolute(targetPath) ? sourcePath : toAbsolute(targetPath);
            File sourceFile = new File(processedSourcePath);
            File targetFile = new File(processedTargetPath);
            if (sourceFile.exists() && sourceFile.isDirectory() && targetFile.exists() && targetFile.isDirectory()) {
                //move folder
                FileUtils.m
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isMoved;
    }

    private static boolean isPathAbsolute(String path) {
        return Paths.get(path).isAbsolute();
    }

    private static String toAbsolute(String path) {
        return Paths.get(path).toAbsolutePath().toString();
    }

    private static boolean isFile(String name) {
        return name.indexOf(".") != -1;
    }
}
