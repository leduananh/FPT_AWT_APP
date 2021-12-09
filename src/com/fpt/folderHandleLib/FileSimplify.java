package com.fpt.folderHandleLib;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.json.simple.JSONObject;

public class FileSimplify {
    private static final Charset LIB_DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String LIB_DEFAULT_ZONE_ID = "Asia/Ho_Chi_Minh";
    private static final String LIB_DEFAULT_DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
    private static final String LINE_ENDING = "\n";

    public static void createFolder(String path) throws IOException {
        if (isStrEmptyOrNull(path)) {
            final String message = "one of argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        Files.createDirectories(Paths.get(path));
    }

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

    public static void copyToFolder(String sourcePath, String destFolderPath) throws IOException {
        if (isStrEmptyOrNull(sourcePath, destFolderPath)) {
            final String message = "one of argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        FileUtils.copyToDirectory(toFile(sourcePath), toFile(destFolderPath));
    }

    public static boolean isHidden(String path) throws IOException {
        return Files.isHidden(Paths.get(path));
    }

    public static String getListFileAndFolderNames(String folderPath) throws IOException {
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

    public static String getListFileNames(String folderPath) throws IOException {
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

    public static void mergeFile(String filePath, String destFilePath) throws IOException {
        if (isStrEmptyOrNull(filePath, destFilePath)) {
            final String message = "one of argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        if (!isFile(filePath)) {
            final String message = filePath + " is not exist, or not an file";
            throw new FileNotFoundException(message);
        }
        appendContent("", destFilePath);// append line separate
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        Files.write(Paths.get(destFilePath), bytes, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public static void appendFile(String content, String filePath) throws IOException {
        if (content != null && isStrEmptyOrNull(filePath)) {
            final String message = "one of argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        if (isFolder(filePath)) {
            final String message = filePath + " is a folder";
            throw new FileNotFoundException(message);
        }
        BufferedWriter bw = createUTF8BufferWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        bw.newLine();
        bw.append(content);
        bw.close();
    }

    private static BufferedWriter createUTF8BufferWriter(String path, OpenOption... options) throws IOException {
        return Files.newBufferedWriter(Paths.get(path), LIB_DEFAULT_CHARSET, options);
    }

    public static void overwriteFile(String content, String filePath) throws IOException {
        if (content != null && isStrEmptyOrNull(filePath)) {
            final String message = "one of argument is null or empty";
            throw new IllegalArgumentException(message);
        }
        if (isFolder(filePath)) {
            final String message = filePath + " is a folder";
            throw new FileNotFoundException(message);
        }
        Files.write(Paths.get(filePath), content.getBytes(LIB_DEFAULT_CHARSET));
    }

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

    public static String getFileOrFolderSizeReadable(String sourcePath) throws FileNotFoundException {
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

    public static String getFileOrFolderAttributes(String sourcePath) throws IOException {
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

    public static String getFileOrFolderCreationDate(String sourcePath) throws IOException {
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

    public static String getAllLineMatchContentJson(String content, String filePath) throws IOException {
        if (!isFile(filePath)) {
            final String message = filePath + " is not exist, or not an file";
            throw new FileNotFoundException(message);
        } else {
            List<String> lines = listFileLine(filePath);
            List<JSONObject> rs = new ArrayList<>();
            IntStream.range(0, lines.size()).filter(index -> lines.get(index).contains(content)).forEach(index -> {
                JSONObject lineObj = new JSONObject();
                lineObj.put("line", lines.get(index));
                lineObj.put("lineIndex", numberFormat(index + 1));
                rs.add(lineObj);
            });
            try {
                return writeValueAsJsonStr(rs, LIB_DEFAULT_DATE_TIME_PATTERN);
            } catch (OutOfMemoryError error) {
                throw new IOException("Data is too large to write as String");
            }
        }
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
}
