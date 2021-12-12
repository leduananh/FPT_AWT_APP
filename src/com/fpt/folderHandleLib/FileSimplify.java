package com.fpt.folderHandleLib;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.CharacterIterator;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.json.simple.JSONObject;

import javax.management.DescriptorKey;

public class FileSimplify {
    private static final Charset LIB_DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String LIB_DEFAULT_ZONE_ID = "Asia/Ho_Chi_Minh";
    private static final String LIB_DEFAULT_DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
    private static final String LINE_ENDING = "\n";

    @DescriptorKey("Prefix:ART; "
            + "Create new folder from path [1], create parent folder if not exists; "
            + "folderPath - Other - is the path to folder; "
            + "Pass the test if all folder was created successfully, otherwise the system will give an error message if it failed;")
    public static void createFolder(String path) throws IOException {
        if (isStrEmptyOrNull(path)) {
            final String message = "argument is null or empty";
            throw new IllegalArgumentException(message);
        } else {
            Files.createDirectories(Paths.get(path));
        }
    }

    @DescriptorKey("Prefix:ART; "
            + "Move file/folder from source path [1] to destination folder path [2], create destination folder if not exist; "
            + "sourcePath - Other - is path to file/folder with file format; "
            + "destFolderPath - Other - is destination path to folder; "
            + "Pass the test if the file/folder is moved successfully, otherwise the system will give an error message if it failed;")
    public static void moveToFolder(String sourcePath, String destFolderPath) throws IOException {
        if (isStrEmptyOrNull(sourcePath, destFolderPath)) {
            final String message = "one of argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        if (isNotExists(sourcePath)) {
            final String message = sourcePath + " is not exist";
            throw new FileNotFoundException(message);
        } else {
            FileUtils.moveToDirectory(toFile(sourcePath), toFile(destFolderPath), true);
        }
    }

    @DescriptorKey("Prefix:ART; "
            + "Move file/folder inside of source path [1] to destination folder path [2], create destination folder if not exist; "
            + "sourcePath - Other - is path to file/folder with file format; "
            + "destFolderPath - Other - is destination path to folder; "
            + "Pass the test if the file/folder inside source path is moved successfully, otherwise the system will give an error message if it failed;")
    public static void moveListFiles(String folderPath, String destFolderPath) throws IOException {
        if (isStrEmptyOrNull(folderPath, destFolderPath)) {
            final String message = "one of argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        if (!isFolder(folderPath)) {
            final String message = folderPath + " is not exist or not a folder";
            throw new FileNotFoundException(message);
        } else {
            int i = 0;
            File[] files = toFile(folderPath).listFiles();
            List<String> error = new LinkedList<>();
            while (i < files.length) {
                try {
                    moveToFolder(files[i].getAbsolutePath().toString(), destFolderPath);
                    i++;
                } catch (Exception e) {
                    error.add(files[i].getAbsolutePath().toString());
                    i++;
                }
            }
            if (error.size() > 0) {
                throw new IOException("cant move these file/folder: " + error.stream().collect(Collectors.joining(", ")));
            }
        }
    }

    @DescriptorKey("Prefix:ART; "
            + "copy file/folder from source path [1] to destination folder path [2], create destination folder if not exists; "
            + "sourcePath - Other - is path to folder/file with file format; "
            + "destFolderPath - Other - is path to destination folder; "
            + "Pass the test if file/folder was copied successfully, otherwise the system will give an error message if it failed;")
    public static void copyToFolder(String sourcePath, String destFolderPath) throws IOException {
        if (isStrEmptyOrNull(sourcePath, destFolderPath)) {
            final String message = "one of argument is null or empty";
            throw new IllegalArgumentException(message);
        } else {
            FileUtils.copyToDirectory(toFile(sourcePath), toFile(destFolderPath));
        }
    }

    @DescriptorKey("Prefix:ART; "
            + "Check if file/folder is hidden from source path [1]; "
            + "sourcePath - Other - is the path to file/folder; "
            + "Returns true if file/folder is hidden, false otherwise;")
    public static boolean isHidden(String path) throws IOException {
        if (isStrEmptyOrNull(path)) {
            final String message = "argument is null or empty";
            throw new IllegalArgumentException(message);
        } else {
            return Files.isHidden(Paths.get(path));
        }
    }

    @DescriptorKey("Prefix:ART; "
            + "Get list of file/folder name from path to folder [1]; "
            + "folderPath - Other - is the path to folder; "
            + "Returns list of file/folder name in json format, with json schema: [{\"name\": String, \"path\": String, \"isFile\": boolean, \"isFolder\": boolean},...];")
    public static String getListNameFromFolder(String folderPath) throws IOException {
        if (isStrEmptyOrNull(folderPath)) {
            final String message = "argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        if (!isFolder(folderPath)) {
            final String message = folderPath + " is not exist or not an folder";
            throw new FileNotFoundException(message);
        } else {
            Set<JSONObject> fileFolderNames = Stream.of(toFile(folderPath).listFiles()).map(file -> {
                JSONObject nameObj = new JSONObject();
                nameObj.put("name", file.getName());
                nameObj.put("path", file.getAbsolutePath());
                nameObj.put("isFile", file.isFile());
                nameObj.put("isFolder", file.isDirectory());
                return nameObj;
            }).collect(Collectors.toSet());
            try {
                return writeValueAsJsonStr(fileFolderNames);
            } catch (JsonProcessingException e) {
                throw new IOException("An error occurred while writing json data\n");
            }
        }
    }

    @DescriptorKey("Prefix:ART; "
            + "Get list file name from path to folder [1]; "
            + "folderPath - Other - is the path to folder; "
            + "Returns list of file name in json format, with json schema: [{\"name\": String, \"path\": String, \"isFile\": boolean, \"isFolder\": boolean},...];")
    public static String getListFileNameFromFolder(String folderPath) throws IOException {
        if (isStrEmptyOrNull(folderPath)) {
            final String message = "argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        if (!isFolder(folderPath)) {
            final String message = folderPath + " is not exist, or not an folder";
            throw new FileNotFoundException(message);
        } else {
            Set<JSONObject> fileNames = Stream.of(toFile(folderPath).listFiles()).filter(file -> file.isFile()).map(file -> {
                JSONObject nameObj = new JSONObject();
                nameObj.put("name", file.getName());
                nameObj.put("path", file.getAbsolutePath());
                nameObj.put("isFile", file.isFile());
                nameObj.put("isFolder", file.isDirectory());
                return nameObj;
            }).collect(Collectors.toSet());
            try {
                return writeValueAsJsonStr(fileNames);
            } catch (JsonProcessingException e) {
                throw new IOException("An error occurred while writing json data\n");
            }
        }
    }

    @DescriptorKey("Prefix:ART; "
            + "Get list of folder name from path to folder [1]; "
            + "folderPath - Other - is relative/absolute path to folder; "
            + "Returns list of folder name in json format, with json schema: [{\"name\": String, \"path\": String, \"isFile\": boolean, \"isFolder\": boolean},...];")
    public static String getListFolderNames(String folderPath) throws IOException {
        if (isStrEmptyOrNull(folderPath)) {
            final String message = "argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        if (!isFolder(folderPath)) {
            final String message = folderPath + " is not exist, or not an folder";
            throw new FileNotFoundException(message);
        } else {
            Set<JSONObject> folderNames = Stream.of(toFile(folderPath).listFiles()).filter(file -> file.isDirectory()).map(file -> {
                JSONObject nameObj = new JSONObject();
                nameObj.put("name", file.getName());
                nameObj.put("path", file.getAbsolutePath());
                nameObj.put("isFile", file.isFile());
                nameObj.put("isFolder", file.isDirectory());
                return nameObj;
            }).collect(Collectors.toSet());
            try {
                return writeValueAsJsonStr(folderNames);
            } catch (JsonProcessingException e) {
                throw new IOException("An error occurred while writing json data\n");
            }
        }
    }

    @DescriptorKey("Prefix:ART; "
            + "Merge content from file [1] to destination file [2]; "
            + "filePath - File - is the path to file with file format; "
            + "destFilePath - File - is path to destination file with file format; "
            + "Pass the test if file content has been merged successfully, otherwise the system will give an error message if it failed;")
    public static void mergeFile(String filePath, String destFilePath, OpenOption... options) throws IOException {
        if (isStrEmptyOrNull(filePath, destFilePath)) {
            final String message = "one of argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        if (!isFile(filePath)) {
            final String message = filePath + " is not exist, or not an file";
            throw new IllegalArgumentException(message);
        }
        if (isFolder(destFilePath)) {
            final String message = filePath + " is an folder";
            throw new IllegalArgumentException(message);
        }
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        writeByteFile(bytes, destFilePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    @DescriptorKey("Prefix:ART; "
            + "Append new content [1] to file [2], will create file if not exist; "
            + "content - Other - is the new content; "
            + "filePath - File - is the path to file with file format; "
            + "Pass the test if the file has been successfully concatenated with the new content, otherwise the system will give an error message if it failed;")
    public static void appendFile(String content, String filePath) throws IOException {
        if (content != null && isStrEmptyOrNull(filePath)) {
            final String message = "one of argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        if (isFolder(filePath)) {
            final String message = filePath + " is a folder";
            throw new FileNotFoundException(message);
        }
        writeByteFile(content.getBytes(LIB_DEFAULT_CHARSET), filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    @DescriptorKey("Prefix:ART; "
            + "Overwrite content [1] to file [2], create file if not exist; "
            + "content - Other - is the new content; "
            + "filePath - File - is the path to file with file format; "
            + "Pass the test if file has been overwritten successfully with the new content, otherwise the system will give an error message if it failed;")
    public static void overwriteFile(String content, String filePath) throws IOException {
        if (content != null && isStrEmptyOrNull(filePath)) {
            final String message = "one of argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        if (isFolder(filePath)) {
            final String message = filePath + " is a folder";
            throw new FileNotFoundException(message);
        }
        writeByteFile(content.getBytes(LIB_DEFAULT_CHARSET),filePath);
    }

    private static void writeByteFile(byte[] bytes, String to, OpenOption... options) throws IOException {
        Path destPath = Paths.get(to);
        try (OutputStream out = Files.newOutputStream(destPath, options)) {
            int len = bytes.length;
            int rem = len;
            if (destPath.toFile().isFile() && countLine(to) > 0) out.write("\n".getBytes(StandardCharsets.UTF_8));
            while (rem > 0) {
                int n = Math.min(rem, 8192);
                out.write(bytes, (len - rem), n);
                rem -= n;
            }
            out.flush();
        }
    }

    @DescriptorKey("Prefix:ART; "
            + "count total number of rows in file [1]; "
            + "filePath - File - is the path to file with file format; "
            + "Pass the test if number of rows has been counted successfully, otherwise the system will give an error message if it failed;")
    public static long countLine(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath)).count();
    }

    @DescriptorKey("Prefix:ART; "
            + "Read content from file [1]; "
            + "filePath - File - is the path to file with file format; "
            + "Returns the file content in plain text;")
    public static String readFileData(String filePath) throws IOException {
        if (isStrEmptyOrNull(filePath)) {
            final String message = "argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        if (!isFile(filePath)) {
            final String message = filePath + " is not exist, or not an file";
            throw new FileNotFoundException(message);
        } else {
            try {
                return FileUtils.readFileToString(Paths.get(filePath).toFile(), LIB_DEFAULT_CHARSET);
            } catch (OutOfMemoryError error) {
                throw new IOException("Data is too large to write as String");
            }
        }
    }

    @DescriptorKey("Prefix:ART; "
            + "Get file/folder readable size from source path [1]; "
            + "sourcePath - Other - is the path to folder/file with file format; "
            + "Returns the size of file/folder in readable text, exp: 5 Kib, 5 Mib, 5 Gib,...;")
    public static String getFileOrFolderReadableSize(String sourcePath) throws Exception {
        if (isStrEmptyOrNull(sourcePath)) {
            final String message = "argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        if (isNotExists(sourcePath)) {
            final String message = sourcePath + " is not exist";
            throw new FileNotFoundException(message);
        }
        long sizeByte = FileUtils.sizeOf(Paths.get(sourcePath).toFile());
        return humanReadableByteCountBin(sizeByte);
    }

    @DescriptorKey("Prefix:ART; "
            + "Get file/folder attributes detail from source path [1]; "
            + "sourcePath - Other - is relative/absolute path to folder/file with file format; "
            + "Returns file/folder attributes in json format, with json schema: "
            + "{\"name\": String, \"path\": String, \"sizeByte\": Long, \"sizeReadable\": String, \"createDateTime\": String, \"lastModifiedDateTime\": String, \"isFile\": boolean, \"isFolder\": boolean, \"isOther\": boolean, \"isSymbolicLink\": boolean, \"isHidden\": boolean };")
    public static String getAttributes(String sourcePath) throws IOException {
        if (isStrEmptyOrNull(sourcePath)) {
            final String message = "argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        if (isNotExists(sourcePath)) {
            final String message = sourcePath + " is not exist";
            throw new FileNotFoundException(message);
        } else {
            File sourceFile = toFile(sourcePath);
            JSONObject fileFolderAttributesObj = new JSONObject();
            BasicFileAttributes basicFileAttributes = getFileFolderBasicAttributes(sourceFile);
            fileFolderAttributesObj.put("name", sourceFile.getName());
            fileFolderAttributesObj.put("path", sourceFile.getAbsolutePath());
            long sizeByte = getFileFolderSizeByte(sourceFile);
            fileFolderAttributesObj.put("sizeByte", sizeByte);
            fileFolderAttributesObj.put("sizeReadable", humanReadableByteCountBin(sizeByte));
            fileFolderAttributesObj.put("createDateTime", getFileFolderCreationDate(basicFileAttributes));
            fileFolderAttributesObj.put("lastModifiedDateTime", getFileFolderLastModifiedDate(basicFileAttributes));
            fileFolderAttributesObj.put("lastAccessDateTime", getFileFolderLastAccessDate(basicFileAttributes));
            fileFolderAttributesObj.put("isFile", basicFileAttributes.isRegularFile());
            fileFolderAttributesObj.put("isFolder", basicFileAttributes.isDirectory());
            fileFolderAttributesObj.put("isOther", basicFileAttributes.isOther());
            fileFolderAttributesObj.put("isSymbolicLink", basicFileAttributes.isSymbolicLink());
            fileFolderAttributesObj.put("isHidden", sourceFile.isHidden());
            try {
                return writeValueAsJsonStr(fileFolderAttributesObj);
            } catch (JsonProcessingException e) {
                throw new IOException("An error occurred while writing json data\n");
            }
        }
    }

    @DescriptorKey("Prefix:ART; "
            + "List file and folder attributes detail from folder [1]; "
            + "folderPath - Other - is the path to folder;"
            + "Returns list of file/folder attributes in json format, with json schema: "
            + "[{\"name\": String, \"path\": String, \"sizeByte\": Long, \"sizeReadable\": String, \"createDateTime\": String, \"lastModifiedDateTime\": String, \"isFile\": boolean, \"isFolder\": boolean, \"isOther\": boolean, \"isSymbolicLink\": boolean, \"isHidden\": boolean }];")
    public static String getListFileAndFolderAttributes(String folderPath) throws IOException {
        if (isStrEmptyOrNull(folderPath)) {
            final String message = "argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        if (!isFolder(folderPath)) {
            final String message = folderPath + " is not exist, or not an folder";
            throw new FileNotFoundException(message);
        } else {
            List<JSONObject> attributes = listFileFolder(folderPath).stream().map(file -> {
                try {
                    return getFileFolderAttributes(file);
                } catch (IOException e) {
                    return null;
                }
            }).filter(file -> file != null).collect(Collectors.toList());
            try {
                return writeValueAsJsonStr(attributes);
            } catch (JsonProcessingException e) {
                throw new IOException("An error occurred while writing json data\n");
            }
        }
    }

    @DescriptorKey("Prefix:ART; "
            + "Get file/folder created date time from source path [1]; "
            + "sourcePath - Other - is the path to folder/file with file format; "
            + "Returns file/folder creation date in plain text, format: dd/MM/yyyy hh:mm:ss")
    public static String getCreationDate(String sourcePath) throws IOException {
        if (isStrEmptyOrNull(sourcePath)) {
            final String message = "argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        if (isNotExists(sourcePath)) {
            final String message = sourcePath + " is not exist";
            throw new FileNotFoundException(message);
        }
        return toDateStringFromFileTime(getFileFolderBasicAttributes(toFile(sourcePath)).creationTime());
    }

    @DescriptorKey("Prefix:ART; "
            + "Get file/folder last modified date time from source path [1]; "
            + "sourcePath - Other - is the path to folder/file with file format; "
            + "Returns file/folder last modified date in plain text, format: dd/MM/yyyy hh:mm:ss")
    public static String getFileFolderLastModifiedDate(String sourcePath) throws IOException {
        if (isStrEmptyOrNull(sourcePath)) {
            final String message = "argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        if (isNotExists(sourcePath)) {
            final String message = sourcePath + " is not exist";
            throw new FileNotFoundException(message);
        }
        return toDateStringFromFileTime(getFileFolderBasicAttributes(toFile(sourcePath)).lastModifiedTime());
    }

    @DescriptorKey("Prefix:ART; "
            + "Get file/folder last accessed date time from source path [1]; "
            + "sourcePath - Other - is the path to folder/file with file format; "
            + "Returns file/folder last accessed date in plain text, format: dd/MM/yyyy hh:mm:ss")
    public static String getFileFolderLastAccessDate(String sourcePath) throws IOException {
        if (isStrEmptyOrNull(sourcePath)) {
            final String message = "argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        if (isNotExists(sourcePath)) {
            final String message = sourcePath + " is not exist";
            throw new FileNotFoundException(message);
        }
        return toDateStringFromFileTime(getFileFolderBasicAttributes(toFile(sourcePath)).lastAccessTime());
    }

    @DescriptorKey("Prefix:ART; "
            + "Get line at row index [1] from path to file [2]; "
            + "index - Other - is row index (number); "
            + "filePath - File - is fileName/absolute path to file with file format; "
            + "Returns line content at row index in plain text;")
    public static String getLineAtRowIndex(Integer rowIndex, String filePath) throws IOException, IndexOutOfBoundsException {
        if (rowIndex != null && isStrEmptyOrNull(filePath)) {
            final String message = "argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        rowIndex = rowIndex - 1;
        if (rowIndex < 0) {
            final String message = "line index does not exist";
            throw new IllegalArgumentException(message);
        }
        if (!isFile(filePath)) {
            final String message = filePath + " is not exist, or not an file";
            throw new FileNotFoundException(message);
        } else {
            return listFileLine(filePath).get(rowIndex);
        }
    }

    @DescriptorKey("Prefix:ART; "
            + "Get all line match with content [1] from file [2], case sensitive; "
            + "content - Other - is lookup content; "
            + "filePath - File - is the path to file with file format; "
            + "Returns all the line match with search content in plain text;")
    public static String getAllLineMatchContent(String content, String filePath) throws IOException {
        if (content != null && isStrEmptyOrNull(filePath)) {
            final String message = "one of argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        if (!isFile(filePath)) {
            final String message = filePath + " is not exist, or not an file";
            throw new FileNotFoundException(message);
        } else {
            List<String> lines = listFileLine(filePath);
            return lines.stream().filter(line -> line.contains(content)).collect(Collectors.joining(LINE_ENDING));
        }
    }

    @DescriptorKey("Prefix:ART; "
            + "Get all line match with content [1] from file [2], case sensitive; "
            + "content - Other - is lookup content; "
            + "filePath - File - is the path to file with file format; "
            + "Returns all the line match with search content in json format, json schema: {\\\"line\\\": String, \\\"lineIndex\\\": Integer};;")
    public static String getAllLineMatchContentJson(String content, String filePath) throws IOException {
        if (!isFile(filePath)) {
            final String message = filePath + " is not exist, or not an file";
            throw new FileNotFoundException(message);
        } else {
            List<String> lines = listFileLine(filePath);
            List<JSONObject> result = new ArrayList<>();
            IntStream.range(0, lines.size()).filter(index -> lines.get(index).contains(content)).forEach(index -> {
                JSONObject lineObj = new JSONObject();
                lineObj.put("line", lines.get(index));
                lineObj.put("lineIndex", numberFormat(index + 1));
                result.add(lineObj);
            });
            try {
                return writeValueAsJsonStr(result, LIB_DEFAULT_DATE_TIME_PATTERN);
            } catch (OutOfMemoryError error) {
                throw new IOException("Data is too large to write as String");
            }
        }
    }

    @DescriptorKey("Prefix:ART; "
            + "Replace content [1] with new content [2] from file [3], case sensitive; "
            + "oldContent - Other - is search content; "
            + "newContent - Other - is replace content; "
            + "filePath - File - is the path to file with file format; "
            + "Pass the test if the file has been successfully replaced with the new content, otherwise the system will give an error message if it failed;")
    public static void replaceAllFileContent(String searchContent, String replaceContent, String filePath) throws IOException {
        if (!isFile(filePath)) {
            final String message = filePath + " is not exist, or not an file";
            throw new FileNotFoundException(message);
        } else {
            File sourceFile = toFile(filePath);
            List<String> replaceLine = FileUtils.readLines(sourceFile, LIB_DEFAULT_CHARSET).stream()
                    .map(line -> line.replace(searchContent, replaceContent)).collect(Collectors.toList());
            FileUtils.writeLines(sourceFile, replaceLine, LINE_ENDING);
        }
    }

    @DescriptorKey("Prefix:ART; "
            + "Delete file/folder from source path [1]; "
            + "sourcePath - Other - is the path to folder/file with file format; "
            + "Pass the test if the file/folder was successfully deleted, otherwise the system will give an error message if it failed;")
    public static void deleteFileOrFolder(String sourcePath) throws IOException {
        File sourceFile = toFile(sourcePath);
        if (sourceFile.getAbsolutePath().equals(getUsersProjectRootDirectory())) {
            final String message = sourceFile + " is project root folder";
            throw new IllegalArgumentException(message);
        } else {
            FileUtils.forceDelete(sourceFile);
        }
    }

    private static Path getUsersProjectRootDirectory() {
        String envRootDir = System.getProperty("user.dir");
        Path rootDir = Paths.get(".").normalize().toAbsolutePath();
        if (rootDir.startsWith(envRootDir)) {
            return rootDir;
        } else {
            throw new RuntimeException("Root dir not found in user directory.");
        }
    }

    private static String getFileFolderCreationDate(BasicFileAttributes basicFileAttributes) {
        return toDateStringFromFileTime(basicFileAttributes.creationTime());
    }

    private static String getFileFolderLastAccessDate(BasicFileAttributes basicFileAttributes) {
        return toDateStringFromFileTime(basicFileAttributes.lastAccessTime());
    }

    private static BasicFileAttributes getFileFolderBasicAttributes(File file) throws IOException {
        return java.nio.file.Files.getFileAttributeView(file.toPath(), BasicFileAttributeView.class).readAttributes();
    }

    private static String toDateStringFromFileTime(FileTime fileTime) {
        return fileTime.toInstant().atZone(ZoneId.of(LIB_DEFAULT_ZONE_ID)).toLocalDateTime()
                .format(DateTimeFormatter.ofPattern(LIB_DEFAULT_DATE_TIME_PATTERN));
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

    private static JSONObject getFileFolderAttributes(File sourceFile) throws IOException {
        BasicFileAttributes basicFileAttributes = getFileFolderBasicAttributes(sourceFile);
        if (basicFileAttributes == null)
            return null;
        JSONObject fileFolderAttributesObj = new JSONObject();
        fileFolderAttributesObj.put("name", sourceFile.getName());
        fileFolderAttributesObj.put("path", sourceFile.getAbsolutePath());
        long sizeByte = getFileFolderSizeByte(sourceFile);
        fileFolderAttributesObj.put("sizeByte", sizeByte);
        fileFolderAttributesObj.put("sizeReadable", humanReadableByteCountBin(sizeByte));
        fileFolderAttributesObj.put("createDateTime", getFileFolderCreationDate(basicFileAttributes));
        fileFolderAttributesObj.put("lastModifiedDateTime", getFileFolderLastModifiedDate(basicFileAttributes));
        fileFolderAttributesObj.put("lastAccesDateTime", getFileFolderLastAccessDate(basicFileAttributes));
        fileFolderAttributesObj.put("isFile", basicFileAttributes.isRegularFile());
        fileFolderAttributesObj.put("isFolder", basicFileAttributes.isDirectory());
        fileFolderAttributesObj.put("isOther", basicFileAttributes.isOther());
        fileFolderAttributesObj.put("isSymbolicLink", basicFileAttributes.isSymbolicLink());
        fileFolderAttributesObj.put("isHidden", sourceFile.isHidden());
        return fileFolderAttributesObj;
    }

    private static Set<File> listFileFolder(String path) {
        return Stream.of(Paths.get(path).toFile()).collect(Collectors.toSet());
    }

    private static Long getFileFolderSizeByte(File sourceFile) throws IllegalArgumentException {
        return FileUtils.sizeOf(sourceFile);
    }

    private static String getFileFolderLastModifiedDate(BasicFileAttributes basicFileAttributes) {
        return toDateStringFromFileTime(basicFileAttributes.lastModifiedTime());
    }

    private static List<String> listFileLine(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath)).collect(Collectors.toList());
    }

    public static boolean isExists(String path) {
        return Files.exists(Paths.get(path), LinkOption.NOFOLLOW_LINKS);
    }

    private static boolean isNotExists(String path) {
        return Files.notExists(Paths.get(path), LinkOption.NOFOLLOW_LINKS);
    }

    private static boolean isFolder(String path) {
        return Files.isDirectory(Paths.get(path), LinkOption.NOFOLLOW_LINKS);
    }

    private static boolean isFile(String path) {
        return Files.isRegularFile(Paths.get(path), LinkOption.NOFOLLOW_LINKS);
    }

    private static File toFile(String path) {
        return Paths.get(path).toFile();
    }

    public static boolean isStrEmptyOrNull(String... verifyStrings) {
        int i = 0;
        while (i < verifyStrings.length) {
            if (verifyStrings[i] == null || verifyStrings[i].isEmpty()) {
                return true;
            }
            i++;
        }
        return false;
    }

    private static Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    private static String numberFormat(int number) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        return decimalFormat.format(number);
    }

    private static BufferedWriter createUTF8BufferWriter(String path, OpenOption... options) throws IOException {
        return Files.newBufferedWriter(Paths.get(path), LIB_DEFAULT_CHARSET, options);
    }

    private static ObjectWriter objWriterDateTimeFormat(String format) {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setDateFormat(new SimpleDateFormat(format))// write date data with datetime format
                .writerWithDefaultPrettyPrinter();
    }

    private static String writeValueAsJsonStr(Object value, String dateFormat) throws JsonProcessingException {
        return objWriterDateTimeFormat(dateFormat).writeValueAsString(value);
    }

    private static String writeValueAsJsonStr(Object value) throws JsonProcessingException {
        return objWriterDateTimeFormat(LIB_DEFAULT_DATE_TIME_PATTERN).writeValueAsString(value);
    }
}
