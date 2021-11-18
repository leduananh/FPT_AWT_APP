package com.fpt.folderHandleLib;

import com.fpt.folderHandleLib.dto.FileFolderAttributesDto;
import com.google.common.io.Files;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import javax.management.DescriptorKey;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileFolderLib {

    private static final String SEPARATOR = "\\";
    private static final Charset LIB_DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String LIB_DEFAULT_ZONE_ID = "Asia/Ho_Chi_Minh";
    private static final String LIB_DEFAULT_DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";

    @DescriptorKey("Prefix:ART; "
            + "create new file/directories from [1] with response status store in variable [boolean result]; "
            + "sourcePath - File - is fileName/folderName/relative/absolute path;")
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

    @DescriptorKey("Prefix:ART; "
            + "move file/directory from source path [1] to destination location [2] with response status store in variable [boolean result]; "
            + "sourcePath - File - is fileName/folderName can be relative/absolute path; "
            + "targetPath - File - is destination folder can be relative/absolute path;")
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

    @DescriptorKey("Prefix:ART; "
            + "check file/directory from source location [1] is exists with response status store in variable [boolean result]; "
            + "sourcePath - File - is fileName/folderName can be relative/absolute path;")
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

    @DescriptorKey("Prefix:ART; "
            + "rename file/directory from source location [1] to new name [2] with response status store in variable [boolean result]; "
            + "sourcePath - File - is fileName/folderName can be relative/absolute path; "
            + "newName - Other - is fileName/folderName can be relative/absolute path;")
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

    @DescriptorKey("Prefix:ART; "
            + "list files and subdirectories name from file/directory from source location [1] with response data store in variable [jsonStringArray result]; "
            + "sourcePath - File - is fileName/folderName can be relative/absolute path;")
    public static String listFileFolderNames(String sourcePath) {
        String namesJson = null;
        try {
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            File sourceFile = new File(processedSourcePath);
            if (sourceFile.exists()) {
                Set<String> fileFolderNames = Stream.of(sourceFile.listFiles())
                        .map(file -> file.isFile() ? file.getName() + " - file" : file.getName() + " - folder")
                        .collect(Collectors.toSet());
                if (fileFolderNames != null && fileFolderNames.size() != 0)
                    namesJson = new Gson().toJson(fileFolderNames, Set.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return namesJson;
    }

    public static Set<String> listFileNames(String sourcePath) {
        Set<String> fileNames = null;
        try {
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            File sourceFile = new File(processedSourcePath);
            if (sourceFile.exists())
                fileNames = Stream.of(sourceFile.listFiles())
                        .filter(file -> file.isFile())
                        .map(File::getName)
                        .collect(Collectors.toSet());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileNames;
    }

    private static Set<File> listFileFolder(String sourcePath) {
        Set<File> files = null;
        try {
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            File sourceFile = new File(processedSourcePath);
            if (sourceFile.exists())
                files = Stream.of(sourceFile.listFiles())
                        .collect(Collectors.toSet());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    public static boolean mergeFileData(String sourcePath, String targetPath) {
        boolean isMerge = false;
        try {
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            String processedTargetPath = isPathAbsolute(targetPath) ? targetPath : toAbsolute(targetPath);
            File sourceFile = new File(processedSourcePath);
            File targetFile = new File(processedTargetPath);
            if (sourceFile.exists() && targetFile.exists() && sourceFile.isFile() && targetFile.isFile()) {
                String data = FileUtils.readFileToString(sourceFile, LIB_DEFAULT_CHARSET);
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
                String currentData = FileUtils.readFileToString(sourceFile, LIB_DEFAULT_CHARSET);
                FileUtils.writeStringToFile(sourceFile, currentData + "\n" + newData, LIB_DEFAULT_CHARSET);
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
                    FileUtils.writeStringToFile(sourceFile, newData, LIB_DEFAULT_CHARSET);
                    isWrite = true;
                }
            } else {
                if (createFileFolder(sourceFile)) {
                    FileUtils.writeStringToFile(sourceFile, newData, LIB_DEFAULT_CHARSET);
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
                FileUtils.writeStringToFile(sourceFile, newData, LIB_DEFAULT_CHARSET);
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
                FileUtils.writeStringToFile(sourceFile, newData, LIB_DEFAULT_CHARSET);
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
                data = FileUtils.readFileToString(sourceFile, LIB_DEFAULT_CHARSET);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private static String readFileData(File sourceFile) {
        String data = null;
        try {
            if (sourceFile.exists() && sourceFile.isFile())
                data = FileUtils.readFileToString(sourceFile, LIB_DEFAULT_CHARSET);
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

    private static Long getFileFolderSizeByte(String sourcePath) {
        Long size = 0l;
        try {
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            File sourceFile = new File(processedSourcePath);
            if (sourceFile.exists()) {
                if (sourceFile.isFile()) // file size
                    size = FileUtils.sizeOf(sourceFile);
                else if (sourceFile.isDirectory()) // directory size
                    size = FileUtils.sizeOfDirectory(sourceFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    private static Long getFileFolderSizeByte(File sourceFile) {
        Long size = 0l;
        try {
            if (sourceFile.exists()) {
                if (sourceFile.isFile()) // file size
                    size = FileUtils.sizeOf(sourceFile);
                else if (sourceFile.isDirectory()) // directory size
                    size = FileUtils.sizeOfDirectory(sourceFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static String getFileFolderAttributes(String sourcePath) {
        String attributesJson = null;
        try {
            FileFolderAttributesDto fileFolderAttributesDto = new FileFolderAttributesDto();
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            if (checkFileFolderExists(processedSourcePath)) {
                BasicFileAttributes basicFileAttributes = getFileFolderBasicAttributes(processedSourcePath);
                if (basicFileAttributes.isRegularFile() || basicFileAttributes.isDirectory())
                    fileFolderAttributesDto.setName(getFileFolderNameFromPath(processedSourcePath));
                else fileFolderAttributesDto.setName(sourcePath);
                Long sizeByte = getFileFolderSizeByte(processedSourcePath);
                fileFolderAttributesDto.setSizeByte(sizeByte);
                fileFolderAttributesDto.setSizeText(humanReadableByteCountBin(sizeByte));
                fileFolderAttributesDto.setCreationDateTime(getFileFolderCreationDate(basicFileAttributes));
                fileFolderAttributesDto.setLastModifiedDateTime(getFileFolderLastModifiedDate(basicFileAttributes));
                fileFolderAttributesDto.setLastAccessDateTime(getFileFolderLastAccessDate(basicFileAttributes));
                fileFolderAttributesDto.setFile(basicFileAttributes.isRegularFile());
                fileFolderAttributesDto.setDirectory(basicFileAttributes.isDirectory());
                fileFolderAttributesDto.setOther(basicFileAttributes.isOther());
                fileFolderAttributesDto.setSymbolicLink(basicFileAttributes.isSymbolicLink());
                attributesJson = new Gson().toJson(fileFolderAttributesDto, FileFolderAttributesDto.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attributesJson;
    }

    public static String listFileFolderAttributes(String sourcePath) {
        String attributesJson = null;
        try {
            FileFolderAttributesDto fileFolderAttributesDto = new FileFolderAttributesDto();
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            Set<File> files = listFileFolder(processedSourcePath);
            List<FileFolderAttributesDto> attributes = files.stream().map(file -> getFileFolderAttributes(file)).collect(Collectors.toList());
            if (attributes != null && attributes.size() != 0)
                attributesJson = new Gson().toJson(attributes, List.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attributesJson;
    }

    private static FileFolderAttributesDto getFileFolderAttributes(File sourceFile) {
        FileFolderAttributesDto fileFolderAttributesDto = new FileFolderAttributesDto();
        try {
            if (sourceFile.exists()) {
                BasicFileAttributes basicFileAttributes = getFileFolderBasicAttributes(sourceFile.getAbsolutePath());
                fileFolderAttributesDto.setName(sourceFile.getName());
                Long sizeByte = getFileFolderSizeByte(sourceFile);
                fileFolderAttributesDto.setSizeByte(sizeByte);
                fileFolderAttributesDto.setSizeText(humanReadableByteCountBin(sizeByte));
                fileFolderAttributesDto.setCreationDateTime(getFileFolderCreationDate(basicFileAttributes));
                fileFolderAttributesDto.setLastModifiedDateTime(getFileFolderLastModifiedDate(basicFileAttributes));
                fileFolderAttributesDto.setLastAccessDateTime(getFileFolderLastAccessDate(basicFileAttributes));
                fileFolderAttributesDto.setFile(basicFileAttributes.isRegularFile());
                fileFolderAttributesDto.setDirectory(basicFileAttributes.isDirectory());
                fileFolderAttributesDto.setOther(basicFileAttributes.isOther());
                fileFolderAttributesDto.setSymbolicLink(basicFileAttributes.isSymbolicLink());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileFolderAttributesDto;
    }

    public static String getFileFolderCreationDate(String sourcePath) {
        String date = null;
        String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
        if (checkFileFolderExists(processedSourcePath))
            date = toDateStringFromFileTime(getFileFolderBasicAttributes(processedSourcePath).creationTime());
        return date;
    }

    private static String getFileFolderCreationDate(BasicFileAttributes basicFileAttributes) {
        return toDateStringFromFileTime(basicFileAttributes.creationTime());
    }

    public static String getFileFolderLastModifiedDate(String sourcePath) {
        String date = null;
        String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
        if (checkFileFolderExists(processedSourcePath))
            date = toDateStringFromFileTime(getFileFolderBasicAttributes(processedSourcePath).lastModifiedTime());
        return date;
    }

    private static String getFileFolderLastModifiedDate(BasicFileAttributes basicFileAttributes) {
        return toDateStringFromFileTime(basicFileAttributes.lastModifiedTime());
    }

    public static String getFileFolderLastAccessDate(String sourcePath) {
        String date = null;
        String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
        if (checkFileFolderExists(processedSourcePath))
            date = toDateStringFromFileTime(getFileFolderBasicAttributes(processedSourcePath).lastAccessTime());
        return date;
    }

    private static String getFileFolderLastAccessDate(BasicFileAttributes basicFileAttributes) {
        return toDateStringFromFileTime(basicFileAttributes.lastAccessTime());
    }

    private static BasicFileAttributes getFileFolderBasicAttributes(String sourcePath) {
        BasicFileAttributes basicFileAttributes = null;
        try {
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            if (checkFileFolderExists(processedSourcePath))
                basicFileAttributes = java.nio.file.Files.getFileAttributeView(Paths.get(processedSourcePath), BasicFileAttributeView.class).readAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return basicFileAttributes;
    }

    private static String toDateStringFromFileTime(FileTime fileTime) {
        return fileTime
                .toInstant()
                .atZone(ZoneId.of(LIB_DEFAULT_ZONE_ID))
                .toLocalDateTime().format(DateTimeFormatter.ofPattern(LIB_DEFAULT_DATE_TIME_PATTERN));
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

    private static String getFileFolderNameFromPath(String sourcePath) {
        String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
        File sourceFile = new File(processedSourcePath);
        return sourceFile.exists() ? sourceFile.getName() : "";
    }

    private static String replaceFileFolderPathName(String path, String replaceName) {
        return path.substring(0, path.lastIndexOf(SEPARATOR) + 1) + replaceName;
    }
}
