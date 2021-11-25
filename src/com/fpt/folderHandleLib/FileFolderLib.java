package com.fpt.folderHandleLib;

import com.fpt.folderHandleLib.dto.FileFolderAttributesDto;
import com.fpt.folderHandleLib.dto.FileFolderNameDto;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;

import javax.management.DescriptorKey;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileFolderLib {

    private final String SEPARATOR = "\\";
    private final Charset LIB_DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final String LIB_DEFAULT_ZONE_ID = "Asia/Ho_Chi_Minh";
    private final String LIB_DEFAULT_DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
    private final String LIB_DEFAULT_FOLDER = "data";
    private final String LINE_ENDING = "\n";

    @DescriptorKey("Prefix:ART; "
            + "Create new folder from path [1]; "
            + "sourcePath - Other - is the absolute path to folder;")
    public boolean createFolder(String sourcePath) throws IOException {
        String processedSourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
        File sourceFile = new File(processedSourcePath);
        if (sourceFile.exists()) {
            final String message = sourceFile + " is already created";
            throw new FileExistsException(message);
        }

        if (sourceFile.isFile()) {
            final String message = sourceFile + " is a file";
            throw new IllegalArgumentException(message);
        }

        if (!sourceFile.mkdirs()) {
            // Double-check that some other thread or process hasn't made
            // the directory in the background
            if (!sourceFile.isDirectory()) {
                final String message = "Unable to create directory " + sourceFile;
                throw new IOException(message);
            }
        }
        return true;
    }

    @DescriptorKey("Prefix:ART; "
            + "Move file/folder [1] to destination folder path [2], will overwrite existing folder and create if not exist; "
            + "sourcePath - Other - is relative/absolute path to the file or folder; "
            + "destPath - Other - is destination relative/absolute path to the folder;")
    public boolean moveFileAndFolder(String sourcePath, String destPath) throws IOException {
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

    @DescriptorKey("Prefix:ART; "
            + "Check if the path to file/folder [1] exists; "
            + "sourcePath - Other - is relative/absolute path to file/folder;")
    public boolean checkFileOrFolderIsExists(String sourcePath) {
        return new File(sourcePath).exists();
    }

    @DescriptorKey("Prefix:ART; "
            + "Check if the path to the file/folder [1] is hidden; "
            + "sourcePath - Other - is relative/absolute path to file/folder;")
    public boolean checkFileOrFolderIsHidden(String sourcePath) throws FileNotFoundException {
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            final String message = sourceFile + " is not exist";
            throw new FileNotFoundException(message);
        }
        return sourceFile.isHidden();
    }

    @DescriptorKey("Prefix:ART; "
            + "List files and folders name from path to folder [1]; "
            + "sourcePath - Other - is relative/absolute path to the folder;")
    public String listFileAndFolderNames(String sourcePath) throws IOException {
        File sourceFile = new File(sourcePath);

        if (!sourceFile.exists()) {
            final String message = sourceFile + " is not exist";
            throw new FileNotFoundException(message);
        }

        if (sourceFile.isFile()) {
            final String message = sourceFile + " is a file";
            throw new IllegalArgumentException(message);
        }

        Set<FileFolderNameDto> fileFolderNames = Stream.of(sourceFile.listFiles()).map(file -> {
            String name = file.getName();
            String path = file.getAbsolutePath();
            boolean isFile = file.isFile();
            boolean isFolder = file.isDirectory();
            return new FileFolderNameDto(name, path, isFile, isFolder);
        }).collect(Collectors.toSet());
        return new GsonBuilder().setPrettyPrinting().create().toJson(fileFolderNames, Set.class);
    }

    @DescriptorKey("Prefix:ART; "
            + "List files name from path to folder [1]; "
            + "sourcePath - Other - is relative/absolute path to the folder;")
    public String listFileNames(String sourcePath) throws FileNotFoundException {
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            final String message = sourceFile + " is not exist";
            throw new FileNotFoundException(message);
        }

        if (sourceFile.isFile()) {
            final String message = sourceFile + " is a file";
            throw new IllegalArgumentException(message);
        }

        Set<FileFolderNameDto> fileNames = Stream.of(sourceFile.listFiles()).filter(file -> file.isFile()).map(file -> {
            String name = file.getName();
            String path = file.getAbsolutePath();
            boolean isFile = file.isFile();
            boolean isFolder = file.isDirectory();
            return new FileFolderNameDto(name, path, isFile, isFolder);
        }).collect(Collectors.toSet());

        return new GsonBuilder().setPrettyPrinting().create().toJson(fileNames, Set.class);
    }

    @DescriptorKey("Prefix:ART; "
            + "Merge content from path to source file [1] into path to destination file [2]; "
            + "sourcePath - File - is relative/absolute path to file; "
            + "destPath - File - is relative/absolute path to destination file;")
    public boolean mergeFileContent(String sourcePath, String destPath) throws IOException {
        File sourceFile = new File(sourcePath);
        File destFile = new File(destPath);

        if (!sourceFile.exists() || !destFile.exists()) {
            final String message = sourceFile + " or " + destFile + " is not exist";
            throw new FileNotFoundException(message);
        }

        if (!sourceFile.isFile() || !destFile.isFile()) {
            final String message = sourceFile + " or " + destFile + " is not a file";
            throw new IllegalArgumentException(message);
        }

        List<String> sourceLines = new BufferedReader(new FileReader(sourceFile)).lines().collect(Collectors.toList());
        List<String> destLines = new BufferedReader(new FileReader(destFile)).lines().collect(Collectors.toList());
        List<String> mergeList = Stream.of(destLines, sourceLines)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        FileUtils.writeLines(destFile, mergeList, LINE_ENDING);
        return true;
    }

    @DescriptorKey("Prefix:ART; "
            + "Append content [1] into the file [2], will create file if not exist; "
            + "content - Other - is new content; "
            + "sourcePath - File - is relative/absolute path to the file;")
    public boolean appendContentIntoFile(String content, String sourcePath) throws IOException, IllegalArgumentException {
        if (content.isEmpty()) {
            final String message = "content is empty";
            throw new IllegalArgumentException(message);
        }

        File sourceFile = new File(sourcePath);

        if (sourceFile.isDirectory()) {
            final String message = sourceFile + " is a folder";
            throw new IllegalArgumentException(message);
        }

        if (sourceFile.exists()) {
            return appendContentToFile(sourceFile, content);
        } else if (sourceFile.createNewFile()) {
            return appendContentToFile(sourceFile, content);
        } else {
            final String message = "unable to write content into " + sourceFile;
            throw new IOException(message);
        }
    }

    @DescriptorKey("Prefix:ART; "
            + "Overwrite content [1] from path to file [2], create file if not exist; "
            + "content - Other - is new content; "
            + "sourcePath - File - is relative/absolute path to the file;")
    public boolean overwriteFileContent(String content, String sourcePath) throws IOException, IllegalArgumentException {
        if (content.isEmpty()) {
            final String message = "content is empty";
            throw new IllegalArgumentException(message);
        }

        File sourceFile = new File(sourcePath);

        if (sourceFile.isDirectory()) {
            final String message = sourceFile + " is a folder";
            throw new IllegalArgumentException(message);
        }

        if (sourceFile.exists()) {
            FileUtils.writeStringToFile(sourceFile, content, LIB_DEFAULT_CHARSET);
            return true;
        } else if (sourceFile.createNewFile()) {
            FileUtils.writeStringToFile(sourceFile, content, LIB_DEFAULT_CHARSET);
            return true;
        } else {
            final String message = "unable to write content into " + sourceFile;
            throw new IOException(message);
        }
    }

    @DescriptorKey("Prefix:ART; "
            + "List folder names from path to folder [1]; "
            + "sourcePath - Other - is relative/absolute path to folder;")
    public String listFolderNames(String sourcePath) throws FileNotFoundException {
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            final String message = sourceFile + " is not exist";
            throw new FileNotFoundException(message);
        }

        if (sourceFile.isFile()) {
            final String message = sourceFile + " is a file";
            throw new IllegalArgumentException(message);
        }

        Set<FileFolderNameDto> folderNames = Stream.of(sourceFile.listFiles()).filter(file -> file.isDirectory())
                .map(file -> {
                    String name = file.getName();
                    String path = file.getAbsolutePath();
                    boolean isFile = file.isFile();
                    boolean isFolder = file.isDirectory();
                    return new FileFolderNameDto(name, path, isFile, isFolder);
                }).collect(Collectors.toSet());

        return new GsonBuilder().setPrettyPrinting().create().toJson(folderNames, Set.class);
    }

    @DescriptorKey("Prefix:ART; "
            + "Read content from path to file [1]; "
            + "sourcePath - File - is relative/absolute path to file;")
    public String readFileData(String sourcePath) throws IOException {
        File sourceFile = new File(sourcePath);

        if (!sourceFile.exists()) {
            final String message = sourceFile + " is not exist";
            throw new FileNotFoundException(message);
        }

        if (sourceFile.isDirectory()) {
            final String message = sourceFile + " is a folder";
            throw new IllegalArgumentException(message);
        }

        return FileUtils.readFileToString(sourceFile, LIB_DEFAULT_CHARSET);
    }

    @DescriptorKey("Prefix:ART; "
            + "Get readable size from path to file/folder [1]; "
            + "sourcePath - Other - is relative/absolute path to file/folder;")
    public String getFileOrFolderSizeReadable(String sourcePath) throws IllegalArgumentException{
        return humanReadableByteCountBin(FileUtils.sizeOf(new File(sourcePath)));
    }

    @DescriptorKey("Prefix:ART; "
            + "Get attributes detail from path to file/folder [1]; "
            + "sourcePath - Other - is relative/absolute path to file or folder;")
    public String fileOrFolderAttributes(String sourcePath) throws IOException {
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            final String message = sourceFile + " is not exist";
            throw new FileNotFoundException(message);
        }
        BasicFileAttributes basicFileAttributes = getFileFolderBasicAttributes(sourceFile);
        FileFolderAttributesDto fileFolderAttributesDto = new FileFolderAttributesDto();
        fileFolderAttributesDto.setName(sourceFile.getName());
        fileFolderAttributesDto.setPath(sourceFile.getAbsolutePath());
        fileFolderAttributesDto.setSizeByte(getFileFolderSizeByte(sourceFile));
        fileFolderAttributesDto.setSizeText(humanReadableByteCountBin(fileFolderAttributesDto.getSizeByte()));
        fileFolderAttributesDto.setCreationDateTime(getFileFolderCreationDate(basicFileAttributes));
        fileFolderAttributesDto.setLastModifiedDateTime(getFileFolderLastModifiedDate(basicFileAttributes));
        fileFolderAttributesDto.setLastAccessDateTime(getFileFolderLastAccessDate(basicFileAttributes));
        fileFolderAttributesDto.setFile(basicFileAttributes.isRegularFile());
        fileFolderAttributesDto.setDirectory(basicFileAttributes.isDirectory());
        fileFolderAttributesDto.setOther(basicFileAttributes.isOther());
        fileFolderAttributesDto.setSymbolicLink(basicFileAttributes.isSymbolicLink());
        fileFolderAttributesDto.setHidden(sourceFile.isHidden());
        return new GsonBuilder().setPrettyPrinting().create().toJson(fileFolderAttributesDto, FileFolderAttributesDto.class);
    }

    @DescriptorKey("Prefix:ART; "
            + "List files and folder attributes detail from path to folder [1]; "
            + "sourcePath - Other - is relative/absolute path to folder;")
    public String listFileAndFolderAttributes(String sourcePath) throws FileNotFoundException {
        File sourceFile = new File(sourcePath);

        if (!sourceFile.exists()) {
            final String message = sourceFile + " is not exist";
            throw new FileNotFoundException(message);
        }

        if (sourceFile.isFile()) {
            final String message = sourceFile + " is a file";
            throw new IllegalArgumentException(message);
        }

        List<FileFolderAttributesDto> attributes = listFileFolder(sourceFile).stream()
                .map(file -> getFileFolderAttributes(file))
                .filter(file -> file != null)
                .collect(Collectors.toList());

        return new GsonBuilder().setPrettyPrinting().create().toJson(attributes, List.class);
    }

    @DescriptorKey("Prefix:ART; "
            + "Get created date time from path to file/folder [1]; "
            + "sourcePath - Other - is relative/absolute path to file/folder;")
    public String getFileOrFolderCreationDate(String sourcePath) throws IOException {
        File sourceFile = new File(sourcePath);

        if (!sourceFile.exists()) {
            final String message = sourceFile + " is not exist";
            throw new FileNotFoundException(message);
        }

        return toDateStringFromFileTime(getFileFolderBasicAttributes(sourceFile).creationTime());
    }

    @DescriptorKey("Prefix:ART; "
            + "Get last modified date time from path to file/folder [1]; "
            + "sourcePath - Other - is relative/absolute path to file/folder;")
    public String getFileFolderLastModifiedDate(String sourcePath) throws IOException {
        File sourceFile = new File(sourcePath);

        if (!sourceFile.exists()) {
            final String message = sourceFile + " is not exist";
            throw new FileNotFoundException(message);
        }

        return toDateStringFromFileTime(getFileFolderBasicAttributes(sourceFile).lastModifiedTime());
    }

    @DescriptorKey("Prefix:ART; "
            + "Get last accessed date time from path to file/folder [1]; "
            + "sourcePath - Other - is relative/absolute path to file/folder;")
    public String getFileFolderLastAccessDate(String sourcePath) throws IOException {
        File sourceFile = new File(sourcePath);

        if (!sourceFile.exists()) {
            final String message = sourceFile + " is not exist";
            throw new FileNotFoundException(message);
        }

        return toDateStringFromFileTime(getFileFolderBasicAttributes(sourceFile).lastAccessTime());
    }

    @DescriptorKey("Prefix:ART; "
            + "Checking content [1] is exists in path to file [2], case sensitive; "
            + "content - Other - is search keyword; "
            + "sourcePath - File - is relative/absolute path to file;")
    public boolean checkFileHadContent(String content, String sourcePath) throws IOException {
        File sourceFile = new File(sourcePath);

        if (!sourceFile.exists()) {
            final String message = sourceFile + " is not exist";
            throw new FileNotFoundException(message);
        }

        if (sourceFile.isDirectory()) {
            final String message = sourceFile + " is a folder";
            throw new IllegalArgumentException(message);
        }

        return FileUtils.readFileToString(sourceFile, LIB_DEFAULT_CHARSET).contains(content);
    }

    @DescriptorKey("Prefix:ART; "
            + "Get line at row index [1] from path to file [2]; "
            + "index - Other - is row index (number); "
            + "sourcePath - File - is relative/absolute path to file;")
    public String fileDataAtRowIndex(int rowIndex, String sourcePath) throws IOException {
        rowIndex = rowIndex - 1;

        if (rowIndex < 0) {
            final String message = "row index less than zero";
            throw new IllegalArgumentException(message);
        }

        File sourceFile = new File(sourcePath);

        if (!sourceFile.exists()) {
            final String message = sourceFile + " is not exist";
            throw new FileNotFoundException(message);
        }

        if (sourceFile.isDirectory()) {
            final String message = sourceFile + " is a folder";
            throw new IllegalArgumentException(message);
        }

        List<String> lines = FileUtils.readLines(sourceFile, LIB_DEFAULT_CHARSET);

        if (rowIndex >= lines.size()) {
            final String message = "Indexing out of bounds";
            throw new IndexOutOfBoundsException(message);
        }
        return lines.get(rowIndex);
    }

    @DescriptorKey("Prefix:ART; "
            + "Replace content [1] with new content [2] from path to file [3], case sensitive; "
            + "content - Other - is search keyword; "
            + "newContent - Other - is replace keyword; "
            + "sourcePath - File - is relative/absolute path to file;")
    public boolean fileReplaceAllContent(String keyword, String replaceKeyword, String sourcePath) throws IOException {
        File sourceFile = new File(sourcePath);

        if (!sourceFile.exists()) {
            final String message = sourceFile + " is not exist";
            throw new FileNotFoundException(message);
        }

        if (sourceFile.isDirectory()) {
            final String message = sourceFile + " is a folder";
            throw new IllegalArgumentException(message);
        }

        List<String> replaceLine = FileUtils.readLines(sourceFile, LIB_DEFAULT_CHARSET).stream()
                .map(line -> line.replace(keyword, replaceKeyword)).collect(Collectors.toList());

        FileUtils.writeLines(sourceFile, replaceLine, LINE_ENDING);
        return true;
    }

    @DescriptorKey("Prefix:ART; "
            + "Delete path to file/folder [1]; "
            + "sourcePath - Other - is relative/absolute path to file/folder;")
    public boolean deleteFileOrFolder(String sourcePath) throws IOException {
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            final String message = sourceFile + " is not exist";
            throw new FileNotFoundException(message);
        }
        FileUtils.forceDelete(sourceFile);
        return true;
    }

    private boolean isPathAbsolute(String path) {
        return Paths.get(path).isAbsolute();
    }

    private String toAbsolute(String path) {
        return Paths.get(LIB_DEFAULT_FOLDER + SEPARATOR + path).toAbsolutePath().toString();
    }

    private String getFileFolderCreationDate(BasicFileAttributes basicFileAttributes) {
        return toDateStringFromFileTime(basicFileAttributes.creationTime());
    }

    private String getFileFolderLastAccessDate(BasicFileAttributes basicFileAttributes) {
        return toDateStringFromFileTime(basicFileAttributes.lastAccessTime());
    }

    private BasicFileAttributes getFileFolderBasicAttributes(File file) {
        try {
            return java.nio.file.Files
                    .getFileAttributeView(file.toPath(), BasicFileAttributeView.class)
                    .readAttributes();
        } catch (IOException e) {
            return null;
        }
    }

    private String toDateStringFromFileTime(FileTime fileTime) {
        return fileTime.toInstant().atZone(ZoneId.of(LIB_DEFAULT_ZONE_ID)).toLocalDateTime()
                .format(DateTimeFormatter.ofPattern(LIB_DEFAULT_DATE_TIME_PATTERN));
    }

    private String humanReadableByteCountBin(long bytes) {
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

    private FileFolderAttributesDto getFileFolderAttributes(File sourceFile) {
        BasicFileAttributes basicFileAttributes = getFileFolderBasicAttributes(sourceFile);
        if (basicFileAttributes == null) return null;
        FileFolderAttributesDto fileFolderAttributesDto = new FileFolderAttributesDto();
        fileFolderAttributesDto.setName(sourceFile.getName());
        fileFolderAttributesDto.setPath(sourceFile.getAbsolutePath());
        fileFolderAttributesDto.setSizeByte(getFileFolderSizeByte(sourceFile));
        fileFolderAttributesDto.setSizeText(humanReadableByteCountBin(fileFolderAttributesDto.getSizeByte()));
        fileFolderAttributesDto.setCreationDateTime(getFileFolderCreationDate(basicFileAttributes));
        fileFolderAttributesDto.setLastModifiedDateTime(getFileFolderLastModifiedDate(basicFileAttributes));
        fileFolderAttributesDto.setLastAccessDateTime(getFileFolderLastAccessDate(basicFileAttributes));
        fileFolderAttributesDto.setFile(basicFileAttributes.isRegularFile());
        fileFolderAttributesDto.setDirectory(basicFileAttributes.isDirectory());
        fileFolderAttributesDto.setOther(basicFileAttributes.isOther());
        fileFolderAttributesDto.setSymbolicLink(basicFileAttributes.isSymbolicLink());
        fileFolderAttributesDto.setHidden(sourceFile.isHidden());
        return fileFolderAttributesDto;
    }

    private Set<File> listFileFolder(File file) {
        return Stream.of(file.listFiles()).collect(Collectors.toSet());
    }

    private Long getFileFolderSizeByte(File sourceFile) throws IllegalArgumentException {
        return FileUtils.sizeOf(sourceFile);
    }

    private String getFileFolderLastModifiedDate(BasicFileAttributes basicFileAttributes) {
        return toDateStringFromFileTime(basicFileAttributes.lastModifiedTime());
    }

    private String getParentPath(File file) {
        return file.toPath().getParent().toString();
    }

    private boolean appendContentToFile(File file, String content) throws IOException {
        List<String> lines = new BufferedReader(new FileReader(file)).lines().collect(Collectors.toList());
        lines.add(content);
        FileUtils.writeLines(file, lines, LINE_ENDING);
        return true;
    }
}