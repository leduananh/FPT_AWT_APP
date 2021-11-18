package com.fpt.folderHandleLib;

import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileFolderCrudLib {
    private static final String separator = "\\";

    public static boolean createFileFolder(String sourcePath) {
        boolean isCreated = false;
        try {
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            File sourceFile = new File(processedSourcePath);
            if (isFile(sourceFile.getName(), true)) {
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
            if (sourceFile.exists()) {
                if (sourceFile.isFile() && targetFile.exists() && targetFile.isFile()) {
                    //move file to dir
                    FileUtils.moveFile(sourceFile, targetFile);
                    isMoved = true;
                } else if (sourceFile.isFile() && targetFile.exists() && targetFile.isDirectory()) {
                    //move file to dir
                    FileUtils.moveFileToDirectory(sourceFile, targetFile, true);
                    isMoved = true;
                } else if (sourceFile.isDirectory() && targetFile.exists() && targetFile.isDirectory()) {
                    //move dir to dir
                    FileUtils.moveDirectoryToDirectory(sourceFile, targetFile, true);
                    isMoved = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isMoved;
    }

    public static boolean checkFileFolderExists(String sourcePath) {
        boolean isExist = false;
        try {
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            File sourceFile = new File(processedSourcePath);
            isExist = sourceFile.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExist;
    }

    public static boolean renameFileFolder(String sourcePath, String newName) {
        boolean isRenamed = false;
        try {
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            String processedNewNamePath = replaceFileFolderPathName(processedSourcePath, newName);
            File sourceFile = new File(processedSourcePath);
            if (sourceFile.exists()) {
                if (sourceFile.isFile() && isFile(processedNewNamePath, false)) {
                    // rename file
                    System.out.println("rename file: " + sourceFile.getPath() + "\nto: " + processedNewNamePath);
                    sourceFile.renameTo(new File(processedNewNamePath));
                    isRenamed = true;
                } else if (sourceFile.isDirectory()) {
                    // rename directory
                    System.out.println("rename folder: " + sourceFile.getPath() + "\nto: " + processedNewNamePath);
                    sourceFile.renameTo(new File(processedNewNamePath));
                    isRenamed = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isRenamed;
    }

    public static Set<String> listFileFolderNames(String sourcePath) {
        Set<String> fileFolderNames = null;
        try {
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            File sourceFile = new File(processedSourcePath);
            fileFolderNames = Stream.of(sourceFile.listFiles())
//                    .filter(file -> !file.isDirectory())
                    .map(file -> file.isFile() ? file.getName() + " - file" : file.getName() + " - folder")
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileFolderNames;
    }

    public static Set<String> listFileNames(String sourcePath) {
        Set<String> fileNames = null;
        try {
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            File sourceFile = new File(processedSourcePath);
            fileNames = Stream.of(sourceFile.listFiles())
                    .filter(file -> file.isFile())
                    .map(File::getName)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileNames;
    }

    public static Set<String> listFolderNames(String sourcePath) {
        Set<String> folderNames = null;
        try {
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            File sourceFile = new File(processedSourcePath);
            folderNames = Stream.of(sourceFile.listFiles())
                    .filter(file -> file.isDirectory())
                    .map(File::getName)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return folderNames;
    }

    public static boolean deleteFileFolder(String sourcePath) {
        boolean isDeleted = false;
        try {
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            File sourceFile = new File(processedSourcePath);
            if (sourceFile.exists()) {
                System.out.println("deleted: " + sourceFile.getPath());
                FileUtils.forceDelete(sourceFile);
                isDeleted = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDeleted;
    }

    private static boolean isPathAbsolute(String path) {
        return Paths.get(path).isAbsolute();
    }

    private static String toAbsolute(String path) {
        return Paths.get(path).toAbsolutePath().toString();
    }

    private static boolean isFile(String nameOrPath, boolean isFileName) {
        if (!isFileName) {
            File file = new File(nameOrPath);
            nameOrPath = file.getName();
        }
        return nameOrPath.indexOf(".") != -1;
    }

    private static String replaceFileFolderPathName(String path, String replaceName) {
        return path.substring(0, path.lastIndexOf(separator) + 1) + replaceName;
    }
}
