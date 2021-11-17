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

            if (sourceFile.isDirectory()) {
                Files.createParentDirs(sourceFile);
                if (sourceFile.isFile())
                    Files.touch(sourceFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isCreated;
    }

    private static boolean isPathAbsolute(String path) {
        return Paths.get(path).isAbsolute();
    }

    private static String toAbsolute(String path) {
        return Paths.get(path).toAbsolutePath().toString();
    }
}
