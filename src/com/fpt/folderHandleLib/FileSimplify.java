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
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FileSimplify {
    private static final String SEPARATOR = "\\";
    private static final Charset LIB_DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String LIB_DEFAULT_ZONE_ID = "Asia/Ho_Chi_Minh";
    private static final String LIB_DEFAULT_DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
    private static final String LIB_DEFAULT_FOLDER = "data";
    private static final String LINE_ENDING = "\n";
    private static final String DEFAULT_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    public static boolean isExists(Path path) {
        return Files.exists(path, LinkOption.NOFOLLOW_LINKS);
    }

    public static boolean isExists(String path) {
        return Files.exists(Paths.get(path), LinkOption.NOFOLLOW_LINKS);
    }

    public static boolean isNotExists(Path path) {
        return Files.notExists(path, LinkOption.NOFOLLOW_LINKS);
    }

    public static boolean isNotExists(String path) {
        return Files.notExists(Paths.get(path), LinkOption.NOFOLLOW_LINKS);
    }

    public static boolean isDirectory(Path path) {
        return Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS);
    }

    public static boolean isDirectory(String path) {
        return Files.isDirectory(Paths.get(path), LinkOption.NOFOLLOW_LINKS);
    }

    public static boolean isFile(String path) {
        return Files.isRegularFile(Paths.get(path), LinkOption.NOFOLLOW_LINKS);
    }

    public static boolean isHidden(String path) throws IOException {
        return Files.isHidden(Paths.get(path));
    }

    public static boolean isHidden(Path path) throws IOException {
        return Files.isHidden(path);
    }

    public static void createFolder(Path path) throws IOException {
        Files.createDirectories(path);
    }

    public static void moveFileOrFolder(Path sourcePath, Path destFolderPath) throws IOException {
        if (isNotExists(sourcePath)) {
            final String message = sourcePath + " is not exist";
            throw new FileNotFoundException(message);
        }
        createFolder(destFolderPath);
        Files.move(sourcePath, destFolderPath.resolve(sourcePath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void copyFileOrFolder(Path sourcePath, Path destFolderPath) throws IOException {
        if (Files.notExists(sourcePath)) {
            final String message = sourcePath + " is not exist";
            throw new FileNotFoundException(message);
        }
        createFolder(destFolderPath);
        Files.copy(sourcePath, destFolderPath.resolve(sourcePath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
    }

    public static File toFile(String path) {
        return Paths.get(path).toFile();
    }

    public static String getListFileAndFolderNames(String folderPath) throws IOException {
        if (!isDirectory(folderPath)) {
            final String message = folderPath + " is not exist or not an folder";
            throw new FileNotFoundException(message);
        } else {
            Set<JsonObject> fileFolderNames = Stream.of(toFile(folderPath).listFiles()).map(file -> {
                JsonObject nameObj = new JsonObject();
                nameObj.addProperty("name", file.getName());
                nameObj.addProperty("path", file.getAbsolutePath());
                nameObj.addProperty("isFile", file.isFile());
                nameObj.addProperty("isFolder", file.isDirectory());
                return nameObj;
            }).collect(Collectors.toSet());
            return new GsonBuilder().setPrettyPrinting().create().toJson(fileFolderNames, Set.class);
        }
    }

    public static String getListFileNames(String folderPath) throws FileNotFoundException {
        if (!isDirectory(folderPath)) {
            final String message = folderPath + " is not exist, or not an folder";
            throw new FileNotFoundException(message);
        } else {
            Set<JsonObject> fileNames = Stream.of(toFile(folderPath).listFiles()).filter(file -> file.isFile()).map(file -> {
                JsonObject nameObj = new JsonObject();
                nameObj.addProperty("name", file.getName());
                nameObj.addProperty("path", file.getAbsolutePath());
                nameObj.addProperty("isFile", file.isFile());
                nameObj.addProperty("isFolder", file.isDirectory());
                return nameObj;
            }).collect(Collectors.toSet());
            return new GsonBuilder().setPrettyPrinting().create().toJson(fileNames, Set.class);
        }
    }

    public static void mergeFileContent(String filePath, String destFilePath) throws IOException {
        if (!isFile(filePath)) {
            final String message = filePath + " is not exist, or not an file";
            throw new FileNotFoundException(message);
        }
        if (!isFile(destFilePath)) {
            final String message = destFilePath + " is not exist, or not an file";
            throw new FileNotFoundException(message);
        }
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        appendContentIntoFile("", destFilePath);
        Files.write(Paths.get(destFilePath), bytes, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public static void appendContentIntoFile(String content, String filePath) throws IOException {
        if (!isFile(filePath)) {
            final String message = filePath + " is not exist, or not an file";
            throw new FileNotFoundException(message);
        }
        BufferedWriter bw = Files.newBufferedWriter(Paths.get(filePath), LIB_DEFAULT_CHARSET, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        try {
            bw.newLine();
            bw.write(content);
        } catch (Exception e) {
            throw new IOException(e);
        }
        bw.close();
    }

    public static void overwriteFileContent(String content, String filePath) throws IOException {
        if (isDirectory(filePath)) {
            final String message = filePath + " is a directory";
            throw new FileNotFoundException(message);
        }
        Files.write(Paths.get(filePath), content.getBytes(StandardCharsets.UTF_8));
    }

    public static String getListFolderNames(String folderPath) throws FileNotFoundException {
        if (!isDirectory(folderPath)) {
            final String message = folderPath + " is not exist, or not an folder";
            throw new FileNotFoundException(message);
        } else {
            Set<JsonObject> folderNames = Stream.of(toFile(folderPath).listFiles()).filter(file -> file.isDirectory()).map(file -> {
                JsonObject nameObj = new JsonObject();
                nameObj.addProperty("name", file.getName());
                nameObj.addProperty("path", file.getAbsolutePath());
                nameObj.addProperty("isFile", file.isFile());
                nameObj.addProperty("isFolder", file.isDirectory());
                return nameObj;
            }).collect(Collectors.toSet());
            return new GsonBuilder().setPrettyPrinting().create().toJson(folderNames, Set.class);
        }
    }

    public static String readFileData(String filePath) throws Exception {
        if (!isFile(filePath)) {
            final String message = filePath + " is not exist, or not an file";
            throw new FileNotFoundException(message);
        } else {
            try {
                return FileUtils.readFileToString(Paths.get(filePath).toFile(), LIB_DEFAULT_CHARSET);
            } catch (OutOfMemoryError error) {
                throw new Exception("Not enough memory to read data");
            }

        }
    }

    public static String getFileOrFolderSizeReadable(String sourcePath) throws FileNotFoundException {
        if (isNotExists(sourcePath)) {
            final String message = sourcePath + " is not exist";
            throw new FileNotFoundException(message);
        }
        long sizeByte = FileUtils.sizeOf(Paths.get(sourcePath).toFile());
        return humanReadableByteCountBin(sizeByte);
    }

    public static String getFileOrFolderAttributes(String sourcePath) throws IOException {
        if (isNotExists(sourcePath)) {
            final String message = sourcePath + " is not exist";
            throw new FileNotFoundException(message);
        } else {
            File sourceFile = toFile(sourcePath);
            JsonObject fileFolderAttributesObj = new JsonObject();
            BasicFileAttributes basicFileAttributes = getFileFolderBasicAttributes(sourceFile);
            fileFolderAttributesObj.addProperty("name", sourceFile.getName());
            fileFolderAttributesObj.addProperty("path", sourceFile.getAbsolutePath());
            long sizeByte = getFileFolderSizeByte(sourceFile);
            fileFolderAttributesObj.addProperty("sizeByte", sizeByte);
            fileFolderAttributesObj.addProperty("sizeReadable", humanReadableByteCountBin(sizeByte));
            fileFolderAttributesObj.addProperty("createDateTime", getFileFolderCreationDate(basicFileAttributes));
            fileFolderAttributesObj.addProperty("lastModifiedDateTime", getFileFolderLastModifiedDate(basicFileAttributes));
            fileFolderAttributesObj.addProperty("lastAccesDateTime", getFileFolderLastAccessDate(basicFileAttributes));
            fileFolderAttributesObj.addProperty("isFile", basicFileAttributes.isRegularFile());
            fileFolderAttributesObj.addProperty("isFolder", basicFileAttributes.isDirectory());
            fileFolderAttributesObj.addProperty("isOther", basicFileAttributes.isOther());
            fileFolderAttributesObj.addProperty("isSymbolicLink", basicFileAttributes.isSymbolicLink());
            fileFolderAttributesObj.addProperty("isHidden", sourceFile.isHidden());
            return new GsonBuilder().setPrettyPrinting().create().toJson(fileFolderAttributesObj, JsonObject.class);
        }
    }

    public static String getListFileAndFolderAttributes(String folderPath) throws FileNotFoundException {
        if (!isDirectory(folderPath)) {
            final String message = folderPath + " is not exist, or not an folder";
            throw new FileNotFoundException(message);
        } else {
            List<JsonObject> attributes = listFileFolder(folderPath).stream().map(file -> {
                try {
                    return getFileFolderAttributes(file);
                } catch (IOException e) {
                    return null;
                }
            }).filter(file -> file != null).collect(Collectors.toList());
            return new GsonBuilder().setPrettyPrinting().create().toJson(attributes, List.class);
        }
    }

    public static String getFileOrFolderCreationDate(String sourcePath) throws IOException {
        if (isNotExists(sourcePath)) {
            final String message = sourcePath + " is not exist";
            throw new FileNotFoundException(message);
        }
        return toDateStringFromFileTime(getFileFolderBasicAttributes(toFile(sourcePath)).creationTime());
    }

    public static String getFileFolderLastModifiedDate(String sourcePath) throws IOException {
        if (isNotExists(sourcePath)) {
            final String message = sourcePath + " is not exist";
            throw new FileNotFoundException(message);
        }
        return toDateStringFromFileTime(getFileFolderBasicAttributes(toFile(sourcePath)).lastModifiedTime());
    }

    public static String getFileFolderLastAccessDate(String sourcePath) throws IOException {
        if (isNotExists(sourcePath)) {
            final String message = sourcePath + " is not exist";
            throw new FileNotFoundException(message);
        }
        return toDateStringFromFileTime(getFileFolderBasicAttributes(toFile(sourcePath)).lastAccessTime());
    }

    public static String getLineAtRowIndex(int rowIndex, String filePath) throws IOException, IndexOutOfBoundsException {
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
        if (!isFile(filePath)) {
            final String message = filePath + " is not exist, or not an file";
            throw new FileNotFoundException(message);
        } else {
            List<String> lines = listFileLine(filePath);
            return lines.stream().filter(line -> line.contains(content)).collect(Collectors.joining(LINE_ENDING));
        }
    }

    private static String numberFormat(int number) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        return decimalFormat.format(number);
    }

    public static String getAllLineMatchContentJson(String content, String filePath) throws IOException {
        if (!isFile(filePath)) {
            final String message = filePath + " is not exist, or not an file";
            throw new FileNotFoundException(message);
        } else {
            List<String> lines = listFileLine(filePath);
            List<JsonObject> rs = new ArrayList<>();
            IntStream.range(0, lines.size()).filter(index -> lines.get(index).contains(content)).forEach(index -> {
                JsonObject lineObj = new JsonObject();
                lineObj.addProperty("line", lines.get(index));
                lineObj.addProperty("lineIndex", numberFormat(index + 1));
                rs.add(lineObj);
            });
            return writeValueAsJsonStr(rs, DEFAULT_DATE_TIME_FORMAT);
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

    private static boolean isPathAbsolute(String path) {
        return Paths.get(path).isAbsolute();
    }

    private static String toAbsolute(String path) {
        return getUsersProjectRootDirectory() + SEPARATOR + LIB_DEFAULT_FOLDER + SEPARATOR + path;
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

    private static JsonObject getFileFolderAttributes(File sourceFile) throws IOException {
        BasicFileAttributes basicFileAttributes = getFileFolderBasicAttributes(sourceFile);
        if (basicFileAttributes == null)
            return null;
        JsonObject fileFolderAttributesObj = new JsonObject();
        fileFolderAttributesObj.addProperty("name", sourceFile.getName());
        fileFolderAttributesObj.addProperty("path", sourceFile.getAbsolutePath());
        long sizeByte = getFileFolderSizeByte(sourceFile);
        fileFolderAttributesObj.addProperty("sizeByte", sizeByte);
        fileFolderAttributesObj.addProperty("sizeReadable", humanReadableByteCountBin(sizeByte));
        fileFolderAttributesObj.addProperty("createDateTime", getFileFolderCreationDate(basicFileAttributes));
        fileFolderAttributesObj.addProperty("lastModifiedDateTime", getFileFolderLastModifiedDate(basicFileAttributes));
        fileFolderAttributesObj.addProperty("lastAccesDateTime", getFileFolderLastAccessDate(basicFileAttributes));
        fileFolderAttributesObj.addProperty("isFile", basicFileAttributes.isRegularFile());
        fileFolderAttributesObj.addProperty("isFolder", basicFileAttributes.isDirectory());
        fileFolderAttributesObj.addProperty("isOther", basicFileAttributes.isOther());
        fileFolderAttributesObj.addProperty("isSymbolicLink", basicFileAttributes.isSymbolicLink());
        fileFolderAttributesObj.addProperty("isHidden", sourceFile.isHidden());
        return fileFolderAttributesObj;
    }

    private static Set<File> listFileFolder(File file) {
        return Stream.of(file.listFiles()).collect(Collectors.toSet());
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
}
