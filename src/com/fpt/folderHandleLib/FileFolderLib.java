package com.fpt.folderHandleLib;

import com.fpt.folderHandleLib.dto.FileFolderAttributesDto;
import com.google.common.io.Files;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.FileUtils;

import javax.management.DescriptorKey;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.spi.FileSystemProvider;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileFolderLib {

    public final String SEPARATOR = "\\";
    public final Charset LIB_DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public final String LIB_DEFAULT_ZONE_ID = "Asia/Ho_Chi_Minh";
    public final String LIB_DEFAULT_DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
    public final String LIB_DEFAULT_FOLDER = "data";

    @DescriptorKey("Prefix:ART; "
            + "Create new file/directories from file/directory path [1] location; "
            + "sourcePath - Other - is relative/absolute file or directory path location;")
    public boolean createFolder(String sourcePath) throws IOException {
        boolean isCreated = false;
        String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
        File sourceFile = new File(processedSourcePath);
        System.out.println(sourceFile.isFile());
        if (!sourceFile.exists()) {
            if (isFile(sourceFile.getName(), true)) {
                Files.createParentDirs(sourceFile);
                Files.touch(sourceFile);
                isCreated = true;
            } else {
                isCreated = sourceFile.mkdirs();
            }
        } else {
            if (sourceFile.isDirectory())
                throw new IllegalArgumentException("directory from " + sourceFile.getAbsolutePath() + " path is already created");
            else
                throw new FileNotFoundException("file from " + sourceFile.getAbsolutePath() + " path is already created");
        }
        return isCreated;
    }

    @DescriptorKey("Prefix:ART; "
            + "Move file/directories from file/directory path [1] location to destination directory path [2] location; "
            + "sourcePath - File - is relative/absolute file or directory path location; "
            + "targetPath - File - is destination relative/absolute directory path location;")
    public boolean moveFileFolder(String sourcePath, String destPath) throws IOException {
        sourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
        destPath = isPathAbsolute(destPath) ? destPath : toAbsolute(destPath);
        File sourceFile = new File(sourcePath);
        File destFile = new File(destPath);
        boolean isCreateDestDir = false;
        if (!sourceFile.exists()) {
            final String message = sourceFile + " is not exist";
            throw new FileNotFoundException(message);
        }
        if (getParentPath(sourceFile).equals(getParentPath(destFile)) && !destFile.exists()) {
            // rename current file folder
            sourceFile.renameTo(destFile);
            return true;
        }
        FileUtils.forceMkdir(destFile);
        FileUtils.moveToDirectory(sourceFile, destFile, isCreateDestDir);
        return true;
    }

    private String getParentPath(File file) {
        return file.toPath().getParent().toString();
    }

    @DescriptorKey("Prefix:ART; "
            + "Check file/directory from file/directory path [1] location is exists; "
            + "sourcePath - File - is relative/absolute file or directory path location;")
    public boolean checkFileFolderExists(String sourcePath) {
        boolean isExist = false;
        File sourceFile = new File(sourcePath);
        isExist = sourceFile.exists();
        return isExist;
    }

    @DescriptorKey("Prefix:ART; "
            + "Check file/directory from file/directory path [1] location is hidden; "
            + "sourcePath - File - is relative/absolute file or directory path location;")
    public boolean checkFileFolderIsHidden(String sourcePath) throws FileNotFoundException {
        boolean isHidden = false;
        File sourceFile = new File(sourcePath);
        if (sourceFile.exists())
            isHidden = sourceFile.isHidden();
        else
            throw new FileNotFoundException("file from " + sourceFile.getAbsolutePath() + " path is not exist");
        return isHidden;
    }

    @DescriptorKey("Prefix:ART; "
            + "Rename file/directory from file/directory path [1] location to new name [2]; "
            + "sourcePath - File - is relative/absolute file or directory path location; "
            + "newName - Other - is new file/directory name;")
    public boolean renameFileFolder(String sourcePath, String newName) throws IOException {
        boolean isRenamed = false;
        String processedNewNamePath = replaceFileFolderPathName(sourcePath, newName);
        System.out.println(sourcePath);
        File sourceFile = new File(sourcePath);
        if (sourceFile.exists()) {
            if (sourceFile.isFile() && isFile(processedNewNamePath, false)) {
                // rename file
                System.out.println("rename file: " + sourceFile.getPath() + "\nto: " + processedNewNamePath);
                sourceFile.renameTo(new File(processedNewNamePath));
                isRenamed = true;
            } else {
                // rename directory
                System.out.println("rename folder: " + sourceFile.getPath() + "\nto: " + processedNewNamePath);
                sourceFile.renameTo(new File(processedNewNamePath));
                isRenamed = true;
            }
        } else {
            if (sourceFile.isDirectory())
                throw new FileNotFoundException(
                        "directory from " + sourceFile.getAbsolutePath() + " path is not exist");
            else
                throw new FileNotFoundException("file from " + sourceFile.getAbsolutePath() + " path is not exist");
        }
        return isRenamed;
    }

    @DescriptorKey("Prefix:ART; "
            + "List files and subdirectories name from directory path [1] location; "
            + "sourcePath - File - is relative/absolute directory path location;")
    public String listFileFolderNames(String sourcePath) throws IOException {
        String namesJson = null;
        File sourceFile = new File(sourcePath);
        if (sourceFile.exists() && sourceFile.isDirectory()) {
            Set<String> fileFolderNames = Stream.of(sourceFile.listFiles())
                    .map(file -> file.isFile() ? file.getName() + " - file" : file.getName() + " - folder")
                    .collect(Collectors.toSet());
            if (fileFolderNames != null && fileFolderNames.size() != 0)
                namesJson = new GsonBuilder().setPrettyPrinting().create().toJson(fileFolderNames, Set.class);
        } else if (sourceFile.isFile())
            throw new IllegalArgumentException("source from " + sourceFile.getAbsolutePath() + " path is a file");
        else
            throw new FileNotFoundException("source from " + sourceFile.getAbsolutePath() + " is not exist");
        return namesJson;
    }

    @DescriptorKey("Prefix:ART; "
            + "List files name from directory path [1] location; "
            + "sourcePath - File - is relative/absolute directory path location;")
    public String listFileNames(String sourcePath) throws FileNotFoundException {
        String fileNamesJson = null;
        File sourceFile = new File(sourcePath);
        if (sourceFile.exists() && sourceFile.isDirectory()) {
            Set<String> fileNames = Stream.of(sourceFile.listFiles()).filter(file -> file.isFile()).map(File::getName)
                    .collect(Collectors.toSet());
            if (fileNames != null && fileNames.size() != 0)
                fileNamesJson = new GsonBuilder().setPrettyPrinting().create().toJson(fileNames, Set.class);
        } else if (sourceFile.isDirectory())
            throw new IllegalArgumentException("source from " + sourceFile.getAbsolutePath() + " path is a directory");
        else
            throw new FileNotFoundException("source from " + sourceFile.getAbsolutePath() + " is not exist");
        return fileNamesJson;
    }

    @DescriptorKey("Prefix:ART; "
            + "Merge file from file path [1] location to destination file path [2] location; "
            + "sourcePath - File - is relative/absolute file path location; "
            + "targetPath - File - is relative/absolute destination file path location;")
    public boolean mergeFileData(String sourcePath, String targetPath) throws IOException, FileNotFoundException {
        boolean isMerge = false;
        File sourceFile = new File(sourcePath);
        File targetFile = new File(targetPath);
        if (sourceFile.exists() && targetFile.exists()) {
            if (sourceFile.isFile() && targetFile.isFile()) {
                String data = FileUtils.readFileToString(sourceFile, LIB_DEFAULT_CHARSET);
                isMerge = appendFileContent(data, targetFile);
            } else
                throw new IllegalArgumentException("source/target path is not a file");
        } else
            throw new FileNotFoundException("source/target path is not exist");
        return isMerge;
    }

    @DescriptorKey("Prefix:ART; "
            + "Write content [1] into bottom of file path [2] location, create file and file parent directories if not exist; "
            + "content - Other - is text content data; "
            + "sourcePath - File - is relative/absolute file path location;")
    public boolean writeDataToFile(String newData, String sourcePath) throws IOException, IllegalArgumentException {
        boolean isWrite = false;
        newData = newData.trim();
        File sourceFile = new File(sourcePath);
        if (!newData.isEmpty()) {
            if (sourceFile.exists() && sourceFile.isFile())
                isWrite = appendFileContent(newData, sourceFile);
            else {
                if (createFileFolder(sourceFile)) {
                    FileUtils.writeStringToFile(sourceFile, newData, LIB_DEFAULT_CHARSET);
                    isWrite = true;
                }
            }
        } else
            throw new IllegalArgumentException("content is empty");
        return isWrite;
    }

    @DescriptorKey("Prefix:ART; "
            + "Append new content [1] into existing file path [2] location; "
            + "content - Other - is text content; "
            + "sourcePath - File - is relative/absolute file path;")
    public boolean appendFileContent(String newData, String sourcePath) throws IOException, FileNotFoundException {
        boolean isAppend = false;
        newData = newData.trim();
        File sourceFile = new File(sourcePath);
        if (!newData.isEmpty()) {
            if (sourceFile.exists() && sourceFile.isFile()) {
                String currentData = FileUtils.readFileToString(sourceFile, LIB_DEFAULT_CHARSET);
                FileUtils.writeStringToFile(sourceFile, currentData + "\n" + newData, LIB_DEFAULT_CHARSET);
                isAppend = true;
            } else if (sourceFile.isDirectory())
                throw new IllegalArgumentException("source from " + sourceFile.getAbsolutePath() + " path is a directory");
            else
                throw new FileNotFoundException("source from " + sourceFile.getAbsolutePath() + " is not exist");
        } else
            throw new IllegalArgumentException("content is empty");
        return isAppend;
    }

    @DescriptorKey("Prefix:ART; "
            + "List subdirectories name from directory path [1] location; "
            + "sourcePath - File - is relative/absolute file path location;")
    public String listFolderNames(String sourcePath) throws FileNotFoundException {
        String folderNamesJson = null;
        File sourceFile = new File(sourcePath);
        if (sourceFile.exists() && sourceFile.isDirectory()) {
            Set<String> fileNames = Stream.of(sourceFile.listFiles())
                    .filter(file -> file.isDirectory())
                    .map(File::getName).collect(Collectors.toSet());
            if (fileNames != null && fileNames.size() != 0)
                folderNamesJson = new GsonBuilder().setPrettyPrinting().create().toJson(fileNames, Set.class);
        } else if (sourceFile.isFile())
            throw new IllegalArgumentException("source from " + sourceFile.getAbsolutePath() + " path is a file");
        else
            throw new FileNotFoundException("source from " + sourceFile.getAbsolutePath() + " is not exist");
        return folderNamesJson;
    }

    @DescriptorKey("Prefix:ART; "
            + "Read data from file path [1] location; "
            + "sourcePath - File - is relative/absolute file path location;")
    public String readFileData(String sourcePath) throws IOException, FileNotFoundException {
        String data = null;
        File sourceFile = new File(sourcePath);
        if (sourceFile.exists() && sourceFile.isFile())
            data = FileUtils.readFileToString(sourceFile, LIB_DEFAULT_CHARSET);
        else if (sourceFile.isDirectory())
            throw new IllegalArgumentException("source from " + sourceFile.getAbsolutePath() + " path is a directory");
        else
            throw new FileNotFoundException("source from " + sourceFile.getAbsolutePath() + " is not exist");
        return data;
    }

    @DescriptorKey("Prefix:ART; "
            + "Get readable size from file/directory path [1] location; "
            + "sourcePath - File - is relative/absolute file path location;")
    public String getFileFolderSize(String sourcePath) throws FileNotFoundException {
        String size = null;
        File sourceFile = new File(sourcePath);
        if (sourceFile.exists()) {
            if (sourceFile.isFile()) // file size
                size = humanReadableByteCountBin(FileUtils.sizeOf(sourceFile));
            else if (sourceFile.isDirectory()) // directory size
                size = humanReadableByteCountBin(FileUtils.sizeOfDirectory(sourceFile));
        } else
            throw new FileNotFoundException("source from " + sourceFile.getAbsolutePath() + " path is not exist");
        return size;
    }

    @DescriptorKey("Prefix:ART; "
            + "Get attributes detail from  file/directory path [1] location; "
            + "sourcePath - File - is relative/absolute file/directory path location;")
    public String fileFolderAttributes(String sourcePath) throws FileNotFoundException {
        String attributesJson = null;
        FileFolderAttributesDto fileFolderAttributesDto = new FileFolderAttributesDto();
        if (checkFileFolderExists(sourcePath)) {
            BasicFileAttributes basicFileAttributes = getFileFolderBasicAttributes(sourcePath);
            if (basicFileAttributes.isRegularFile() || basicFileAttributes.isDirectory())
                fileFolderAttributesDto.setName(getFileFolderNameFromPath(sourcePath));
            else
                fileFolderAttributesDto.setName(sourcePath);
            Long sizeByte = getFileFolderSizeByte(sourcePath);
            fileFolderAttributesDto.setSizeByte(sizeByte);
            fileFolderAttributesDto.setSizeText(humanReadableByteCountBin(sizeByte));
            fileFolderAttributesDto.setCreationDateTime(getFileFolderCreationDate(basicFileAttributes));
            fileFolderAttributesDto.setLastModifiedDateTime(getFileFolderLastModifiedDate(basicFileAttributes));
            fileFolderAttributesDto.setLastAccessDateTime(getFileFolderLastAccessDate(basicFileAttributes));
            fileFolderAttributesDto.setFile(basicFileAttributes.isRegularFile());
            fileFolderAttributesDto.setDirectory(basicFileAttributes.isDirectory());
            fileFolderAttributesDto.setOther(basicFileAttributes.isOther());
            fileFolderAttributesDto.setSymbolicLink(basicFileAttributes.isSymbolicLink());
            fileFolderAttributesDto.setHidden(checkFileFolderIsHiddenFromPath(sourcePath));
            attributesJson = new GsonBuilder().setPrettyPrinting().create().toJson(fileFolderAttributesDto, FileFolderAttributesDto.class);
        } else
            throw new FileNotFoundException("source from " + sourcePath + " path is not exist");
        return attributesJson;
    }

    @DescriptorKey("Prefix:ART; "
            + "List files and subdirectories attributes detail from directory path [1] location; "
            + "sourcePath - File - is relative/absolute directory path location;")
    public String listFileFolderAttributes(String sourcePath) throws FileNotFoundException {
        String attributesJson = null;
        List<FileFolderAttributesDto> attributes = new ArrayList<>();
        File sourceFile = new File(sourcePath);
        if (sourceFile.exists() && sourceFile.isDirectory()) {
            Set<File> files = listFileFolder(sourcePath);
            if (files != null && files.size() != 0)
                attributes = files.stream()
                        .map(file -> getFileFolderAttributes(file))
                        .collect(Collectors.toList());
            attributesJson = new GsonBuilder().setPrettyPrinting().create().toJson(attributes, List.class);
        } else if (sourceFile.isFile())
            throw new IllegalArgumentException("source from " + sourceFile.getAbsolutePath() + " path is a file");
        else
            throw new FileNotFoundException("source from " + sourceFile.getAbsolutePath() + " path is not exist");
        return attributesJson;
    }

    @DescriptorKey("Prefix:ART; "
            + "Get created date time from file/directory path [1] location; "
            + "sourcePath - File - is relative/absolute file or directory path location;")
    public String getFileFolderCreationDate(String sourcePath) throws FileNotFoundException {
        String date = null;
        if (checkFileFolderExists(sourcePath))
            date = toDateStringFromFileTime(getFileFolderBasicAttributes(sourcePath).creationTime());
        else
            throw new FileNotFoundException("source from " + sourcePath + " path is not exist");
        return date;
    }

    @DescriptorKey("Prefix:ART; "
            + "Get last modified date time from file/directory path [1] location; "
            + "sourcePath - File - is relative/absolute file or directory path location;")
    public String getFileFolderLastModifiedDate(String sourcePath) throws FileNotFoundException {
        String date = null;
        if (checkFileFolderExists(sourcePath))
            date = toDateStringFromFileTime(getFileFolderBasicAttributes(sourcePath).lastModifiedTime());
        else
            throw new FileNotFoundException("source from " + sourcePath + " path is not exist");
        return date;
    }

    @DescriptorKey("Prefix:ART; "
            + "Get last accessed date time from file/directory path [1] location; "
            + "sourcePath - File - is relative/absolute file or directory path location;")
    public String getFileFolderLastAccessDate(String sourcePath) throws FileNotFoundException {
        String date = null;
        if (checkFileFolderExists(sourcePath))
            date = toDateStringFromFileTime(getFileFolderBasicAttributes(sourcePath).lastAccessTime());
        else
            throw new FileNotFoundException("source from " + sourcePath + " path is not exist");
        return date;
    }

    @DescriptorKey("Prefix:ART; "
            + "Checking keyword [1] is exists in file path [2] location; "
            + "keyword - Other - is search keyword; "
            + "sourcePath - File - is relative/absolute file path location;")
    public boolean fileHasKeyword(String keyword, String sourcePath) throws FileNotFoundException {
        boolean isExist = false;
        keyword = keyword.trim();
        File sourceFile = new File(sourcePath);
        if (sourceFile.exists() && sourceFile.isFile())
            isExist = readFileData(sourceFile).trim().contains(keyword);
        else if (sourceFile.isDirectory())
            throw new IllegalArgumentException("source from " + sourceFile.getAbsolutePath() + " path is a directory");
        else
            throw new FileNotFoundException("source from " + sourceFile.getAbsolutePath() + " path is not exist");
        return isExist;
    }

    @DescriptorKey("Prefix:ART; "
            + "Get line at row index [1] from file path [2] location; "
            + "index - Other - is row index (number); "
            + "sourcePath - File - is relative/absolute file path location;")
    public String fileDataAtRowIndex(int rowIndex, String sourcePath) throws IOException {
        String rowData = null;
        rowIndex = rowIndex - 1;
        if (rowIndex >= 0) {
            File sourceFile = new File(sourcePath);
            if (sourceFile.exists() && sourceFile.isFile()) {
                List<String> lines = FileUtils.readLines(sourceFile, LIB_DEFAULT_CHARSET);
                if (rowIndex < lines.size())
                    rowData = lines.get(rowIndex);
            } else if (sourceFile.isDirectory())
                throw new IllegalArgumentException("source from " + sourceFile.getAbsolutePath() + " path is a directory");
            else
                throw new FileNotFoundException("source from " + sourceFile.getAbsolutePath() + " path is not exist");
        } else
            throw new IllegalArgumentException("row index: " + rowIndex + " is smaller than zero");
        return rowData;
    }

    @DescriptorKey("Prefix:ART; "
            + "Replace keyword [1] with new keyword [2] from file path [3] location; "
            + "keyword - Other - search keyword; "
            + "replaceKeyword - Other - replace keyword; "
            + "sourcePath - File - is relative/absolute file path;")
    public boolean fileReplaceAll(String keyword, String replaceKeyword, String sourcePath) throws FileNotFoundException {
        boolean isReplaced = false;
        File sourceFile = new File(sourcePath);
        if (sourceFile.exists() && sourceFile.isFile())
            isReplaced = overWriteFileData(readFileData(sourceFile).replace(keyword, replaceKeyword), sourceFile);
        else if (sourceFile.isDirectory())
            throw new IllegalArgumentException("source path " + sourceFile.getAbsolutePath() + " path is a directory");
        else
            throw new FileNotFoundException("source path " + sourceFile.getAbsolutePath() + " is not exist");
        return isReplaced;
    }

    @DescriptorKey("Prefix:ART; "
            + "Delete file/directory from file/directory path [1] location; "
            + "sourcePath - File - is relative/absolute file path location;")
    public boolean deleteFileFolder(String sourcePath) throws IOException {
        boolean isDeleted = false;
        File sourceFile = new File(sourcePath);
        if (sourceFile.exists()) {
            System.out.println("deleted: " + sourceFile.getPath());
            FileUtils.forceDelete(sourceFile);
            isDeleted = true;
        } else
            throw new FileNotFoundException("source from " + sourceFile.getAbsolutePath() + " path is not exist");
        return isDeleted;
    }

    public boolean isPathAbsolute(String path) {
        return Paths.get(path).isAbsolute();
    }

    public String toAbsolute(String path) {
        return Paths.get(LIB_DEFAULT_FOLDER + SEPARATOR + path).toAbsolutePath().toString();
    }

    public boolean isFile(String nameOrPath, boolean isFileName) {
        if (!isFileName) {
            File file = new File(nameOrPath);
            nameOrPath = file.getName();
        }
        return nameOrPath.indexOf(".") != -1;
    }

    public String getFileFolderNameFromPath(String sourcePath) {
        String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
        File sourceFile = new File(processedSourcePath);
        return sourceFile.exists() ? sourceFile.getName() : "";
    }

    public String replaceFileFolderPathName(String path, String replaceName) {
        return path.substring(0, path.lastIndexOf(SEPARATOR) + 1) + replaceName;
    }

    public boolean createFileFolder(File sourceFile) {
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

    public String getFileFolderCreationDate(BasicFileAttributes basicFileAttributes) {
        return toDateStringFromFileTime(basicFileAttributes.creationTime());
    }

    public String getFileFolderLastAccessDate(BasicFileAttributes basicFileAttributes) {
        return toDateStringFromFileTime(basicFileAttributes.lastAccessTime());
    }

    public BasicFileAttributes getFileFolderBasicAttributes(String sourcePath) {
        BasicFileAttributes basicFileAttributes = null;
        try {
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            if (checkFileFolderExists(processedSourcePath))
                basicFileAttributes = java.nio.file.Files
                        .getFileAttributeView(Paths.get(processedSourcePath), BasicFileAttributeView.class)
                        .readAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return basicFileAttributes;
    }

    public String toDateStringFromFileTime(FileTime fileTime) {
        return fileTime.toInstant().atZone(ZoneId.of(LIB_DEFAULT_ZONE_ID)).toLocalDateTime()
                .format(DateTimeFormatter.ofPattern(LIB_DEFAULT_DATE_TIME_PATTERN));
    }

    public String humanReadableByteCountBin(long bytes) {
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

    public FileFolderAttributesDto getFileFolderAttributes(File sourceFile) {
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
                fileFolderAttributesDto.setHidden(sourceFile.isHidden());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileFolderAttributesDto;
    }

    public boolean checkFileFolderIsHiddenFromPath(String sourcePath) {
        boolean isHidden = false;
        String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
        File sourceFile = new File(processedSourcePath);
        if (sourceFile.exists())
            isHidden = sourceFile.isHidden();
        return isHidden;
    }

    public Long getFileFolderSizeByte(String sourcePath) {
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

    public String readFileData(File sourceFile) {
        String data = null;
        try {
            if (sourceFile.exists() && sourceFile.isFile())
                data = FileUtils.readFileToString(sourceFile, LIB_DEFAULT_CHARSET);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public Set<File> listFileFolder(String sourcePath) {
        Set<File> files = null;
        try {
            String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
            File sourceFile = new File(processedSourcePath);
            if (sourceFile.exists() && sourceFile.isDirectory())
                files = Stream.of(sourceFile.listFiles()).collect(Collectors.toSet());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    public boolean appendFileContent(String newData, File sourceFile) {
        boolean isAppend = false;
        try {
            newData = newData.trim();
            if (sourceFile.exists() && sourceFile.isFile() && !newData.isEmpty()) {
                String currentData = FileUtils.readFileToString(sourceFile, LIB_DEFAULT_CHARSET);
                FileUtils.writeStringToFile(sourceFile, currentData.trim() + "\n" + newData, LIB_DEFAULT_CHARSET);
            }
            isAppend = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isAppend;
    }

    public boolean overWriteFileData(String newData, String sourcePath) {
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

    public boolean overWriteFileData(String newData, File sourceFile) {
        boolean isOverWrite = false;
        try {
            newData = newData.trim();
            if (sourceFile.exists() && sourceFile.isFile() && !newData.isEmpty()) {
                FileUtils.writeStringToFile(sourceFile, newData, LIB_DEFAULT_CHARSET);
                isOverWrite = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isOverWrite;
    }

    public Long getFileFolderSizeByte(File sourceFile) {
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

    public String getFileFolderLastModifiedDate(BasicFileAttributes basicFileAttributes) {
        return toDateStringFromFileTime(basicFileAttributes.lastModifiedTime());
    }
}