package com.fpt.folderHandleLib;

import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileFolderLib {
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

    private static boolean createFileFolder(File sourceFile) {
        boolean isCreated = false;
        try {
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

    public static boolean mergeFileData(String sourcePath, String targetPath) {
        boolean isMerge = false;
        try {
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            String processedTargetPath = isPathAbsolute(targetPath) ? targetPath : toAbsolute(targetPath);
            File sourceFile = new File(processedSourcePath);
            File targetFile = new File(processedTargetPath);
            if (sourceFile.exists() && targetFile.exists() && sourceFile.isFile() && targetFile.isFile()) {
                String data = FileUtils.readFileToString(sourceFile, StandardCharsets.UTF_8);
                isMerge = appendFileContent(data, targetFile);
                sourceFile.deleteOnExit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isMerge;
    }

    private static boolean appendFileContent(String newData, File sourceFile) {
        boolean isAppend = false;
        try {
            newData = newData.trim();
            if (sourceFile.exists() && sourceFile.isFile() && !newData.isEmpty()) {
                String currentData = FileUtils.readFileToString(sourceFile, StandardCharsets.UTF_8);
                FileUtils.writeStringToFile(sourceFile, currentData + "\n" + newData, StandardCharsets.UTF_8);
            }
            isAppend = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isAppend;
    }

    public static boolean writeDataToFile(String newData, String sourcePath) {
        boolean isWrite = false;
        try {
            newData = newData.trim();
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            File sourceFile = new File(processedSourcePath);
            if (sourceFile.exists()) {
                if (sourceFile.isFile() && !newData.isEmpty()) {
                    FileUtils.writeStringToFile(sourceFile, newData, StandardCharsets.UTF_8);
                    isWrite = true;
                }
            } else {
                if (createFileFolder(sourceFile)) {
                    FileUtils.writeStringToFile(sourceFile, newData, StandardCharsets.UTF_8);
                    isWrite = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isWrite;
    }

    public static boolean overWriteFile(String newData, String sourcePath) {
        boolean isOverWrite = false;
        try {
            newData = newData.trim();
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            File sourceFile = new File(processedSourcePath);
            if (sourceFile.exists() && sourceFile.isFile() && !newData.isEmpty()) {
                FileUtils.writeStringToFile(sourceFile, newData, StandardCharsets.UTF_8);
                isOverWrite = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isOverWrite;
    }

    public static boolean appendFileContent(String newData, String sourcePath) {
        boolean isAppend = false;
        try {
            newData = newData.trim();
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            File sourceFile = new File(processedSourcePath);
            if (sourceFile.exists() && sourceFile.isFile() && !newData.isEmpty()) {
                FileUtils.writeStringToFile(sourceFile, newData, StandardCharsets.UTF_8);
            }
            isAppend = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isAppend;
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

    public static String readFileData(String sourcePath) {
        String data = null;
        try {
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            File sourceFile = new File(processedSourcePath);
            if (sourceFile.exists() && sourceFile.isFile())
                data = FileUtils.readFileToString(sourceFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private static String readFileData(File sourceFile) {
        String data = null;
        try {
            if (sourceFile.exists() && sourceFile.isFile())
                data = FileUtils.readFileToString(sourceFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String getFileFolderSize(String sourcePath) {
        String size = null;
        try {
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            File sourceFile = new File(processedSourcePath);
            if (sourceFile.exists()) {
                if (sourceFile.isFile()) // file size
                    size = humanReadableByteCountBin(FileUtils.sizeOf(sourceFile));
                else if (sourceFile.isDirectory()) // directory size
                    size = humanReadableByteCountBin(FileUtils.sizeOfDirectory(sourceFile));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    private static String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
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
