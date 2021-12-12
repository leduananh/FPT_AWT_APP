package com.fpt.folderHandleLib;

//import com.fpt.folderHandleLib.dto.FileFolderAttributesDto;
//import com.fpt.folderHandleLib.dto.FileFolderNameDto;

import org.apache.commons.io.FileUtils;

import javax.management.DescriptorKey;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FileFolderLib {

//    public final String SEPARATOR = "\\";
//    public final Charset LIB_DEFAULT_CHARSET = StandardCharsets.UTF_8;
//    public final String LIB_DEFAULT_ZONE_ID = "Asia/Ho_Chi_Minh";
//    public final String LIB_DEFAULT_DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
//    public final String LIB_DEFAULT_FOLDER = "data";
//    public final String LINE_ENDING = "\n";
//    public  final String ROOT_PATH = getUsersProjectRootDirectory().toAbsolutePath().toString();
//
//    @DescriptorKey("Prefix:ART; "
//            + "Move file/folder [1] to destination folder path [2], will overwrite existing folder and create if not exist; "
//            + "sourcePath - Other - is relative/absolute path to file/folder with file format; "
//            + "destPath - Other - is destination relative/absolute path to folder;")
//    public boolean moveFileAndFolder(String sourcePath, String destPath) throws IOException {
//        sourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
//        destPath = isPathAbsolute(destPath) ? destPath : toAbsolute(destPath);
//        File sourceFile = new File(sourcePath);
//        File destFile = new File(destPath);
//        boolean isCreateDestDir = false;
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//
//        if (getParentPath(sourceFile).equals(getParentPath(destFile)) && !destFile.exists()) {
//            // rename current file folder
//            sourceFile.renameTo(destFile);
//            return true;
//        }
//
//        FileUtils.forceMkdir(destFile);
//        FileUtils.moveToDirectory(sourceFile, destFile, isCreateDestDir);
//        return true;
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "copy file [1] to path to folder [2], create folder if not exists; "
//            + "filePath - file - is fileName/absolute path to file with file format; "
//            + "destPath - Other - is destination relative/absolute path to folder;")
//    public boolean copyFileOrFolderToFolder(String filePath, String destPath) throws IOException {
//        filePath = isPathAbsolute(filePath) ? filePath : toAbsolute(filePath);
//        destPath = isPathAbsolute(destPath) ? destPath : toAbsolute(destPath);
//        File sourceFile = new File(filePath);
//        File destFile = new File(destPath);
//
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//
//        if (!destFile.exists()) {
//            createFolder(destFile.getAbsolutePath());
//        }
//
//        if (sourceFile.isFile()) {
//            FileUtils.copyFileToDirectory(sourceFile, destFile);
//        } else {
//            FileUtils.copyDirectoryToDirectory(sourceFile, destFile);
//        }
//
//        return true;
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Create new folder from path [1]; "
//            + "sourcePath - Other - is the absolute path to folder;")
//    public boolean createFolder(String sourcePath) throws IOException {
//        sourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
//        File sourceFile = new File(sourcePath);
//        if (sourceFile.exists()) {
//            final String message = sourceFile + " is already created";
//            throw new IllegalArgumentException(message);
//        }
//
//        if (sourceFile.isFile()) {
//            final String message = sourceFile + " is a file";
//            throw new IllegalArgumentException(message);
//        }
//
//        if (!sourceFile.mkdirs()) {
//            // Double-check that some other thread or process hasn't made
//            // the directory in the background
//            if (!sourceFile.isDirectory()) {
//                final String message = "Unable to create directory " + sourceFile;
//                throw new IOException(message);
//            }
//        }
//        return true;
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Check if the path to file/folder [1] exists; "
//            + "sourcePath - Other - is relative/absolute path to folder/file with file format;")
//    public boolean checkFileOrFolderIsExists(String sourcePath) {
//        sourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
//        return new File(sourcePath).exists();
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Check if the path to file/folder [1] is hidden; "
//            + "sourcePath - Other - is relative/absolute path to file/folder;")
//    public boolean checkFileOrFolderIsHidden(String sourcePath) throws FileNotFoundException {
//        sourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
//        File sourceFile = new File(sourcePath);
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//        return sourceFile.isHidden();
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "List files and folders name from path to folder [1]; "
//            + "sourcePath - Other - is relative/absolute path to folder;")
//    public String listFileAndFolderNames(String sourcePath) throws IOException {
//        sourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
//        File sourceFile = new File(sourcePath);
//
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//
//        if (sourceFile.isFile()) {
//            final String message = sourceFile + " is a file";
//            throw new IllegalArgumentException(message);
//        }
//
//        Set<FileFolderNameDto> fileFolderNames = Stream.of(sourceFile.listFiles()).map(file -> {
//            String name = file.getName();
//            String path = file.getAbsolutePath();
//            boolean isFile = file.isFile();
//            boolean isFolder = file.isDirectory();
//            return new FileFolderNameDto(name, path, isFile, isFolder);
//        }).collect(Collectors.toSet());
//        return "new GsonBuilder().setPrettyPrinting().create().toJson(fileFolderNames, Set.class)";
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "List files name from path to folder [1]; "
//            + "sourcePath - Other - is relative/absolute path to folder;")
//    public String listFileNames(String sourcePath) throws FileNotFoundException {
//        sourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
//        File sourceFile = new File(sourcePath);
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//
//        if (sourceFile.isFile()) {
//            final String message = sourceFile + " is a file";
//            throw new IllegalArgumentException(message);
//        }
//
//        Set<FileFolderNameDto> fileNames = Stream.of(sourceFile.listFiles()).filter(file -> file.isFile()).map(file -> {
//            String name = file.getName();
//            String path = file.getAbsolutePath();
//            boolean isFile = file.isFile();
//            boolean isFolder = file.isDirectory();
//            return new FileFolderNameDto(name, path, isFile, isFolder);
//        }).collect(Collectors.toSet());
//
//        return "new GsonBuilder().setPrettyPrinting().create().toJson(fileNames, Set.class)";
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Merge content from path to source file [1] into path to destination file [2]; "
//            + "filePath - File - is fileName/absolute path to file with file format; "
//            + "destFilePath - File - is fileName/absolute path to destination file;")
//    public boolean mergeFileContent(String filePath, String destFilePath) throws IOException {
//        File sourceFile = new File(filePath);
//        File destFile = new File(destFilePath);
//
//        if (!sourceFile.exists() || !destFile.exists()) {
//            final String message = sourceFile + " or " + destFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//
//        if (!sourceFile.isFile() || !destFile.isFile()) {
//            final String message = sourceFile + " or " + destFile + " is not a file";
//            throw new IllegalArgumentException(message);
//        }
//
//        List<String> sourceLines = listFileLine(sourceFile);
//        List<String> destLines = listFileLine(destFile);
//        List<String> mergeList = Stream.of(destLines, sourceLines)
//                .flatMap(Collection::stream)
//                .collect(Collectors.toList());
//        FileUtils.writeLines(destFile, mergeList, LINE_ENDING);
//        return true;
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Append content [1] to path to file [2], will create file if not exist; "
//            + "content - Other - is new content; "
//            + "filePath - File - is fileName/absolute path to file with file format;")
//    public boolean appendContentIntoFile(String content, String filePath) throws IOException, IllegalArgumentException {
//        if (content.isEmpty()) {
//            final String message = "content is empty";
//            throw new IllegalArgumentException(message);
//        }
//
//        File sourceFile = new File(filePath);
//
//        if (sourceFile.isDirectory()) {
//            final String message = sourceFile + " is a folder";
//            throw new IllegalArgumentException(message);
//        }
//
//        if (sourceFile.exists()) {
//            return appendContentToFile(sourceFile, content);
//        } else if (sourceFile.createNewFile()) {
//            return appendContentToFile(sourceFile, content);
//        } else {
//            final String message = "unable to write content into " + sourceFile;
//            throw new IOException(message);
//        }
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Overwrite content [1] from path to file [2], create file if not exist; "
//            + "content - Other - is new content; "
//            + "filePath - File - is fileName/absolute path to file with file format;")
//    synchronized public boolean overwriteFileContent(String content, String filePath) throws IOException, IllegalArgumentException {
//        if (content.isEmpty()) {
//            final String message = "content is empty";
//            throw new IllegalArgumentException(message);
//        }
//
//        File sourceFile = new File(filePath);
//
//        if (sourceFile.isDirectory()) {
//            final String message = sourceFile + " is a folder";
//            throw new IllegalArgumentException(message);
//        }
//        try {
//            if (sourceFile.exists()) {
//                FileUtils.writeStringToFile(sourceFile, content, LIB_DEFAULT_CHARSET);
//                return true;
//            } else if (createFileWithContent(sourceFile,content)) {
//
//                FileUtils.writeStringToFile(sourceFile, content, LIB_DEFAULT_CHARSET);
//                return true;
//            } else {
//                final String message = "unable to write content into " + sourceFile;
//                throw new RuntimeException(message);
//            }
//        } catch (IOException e) {
//            throw new IOException(e.getMessage());
//        }
//    }
//
//    private boolean createFileWithContent(File file, String content) {
//        try {
//            FileUtils.forceMkdirParent(file);
//            FileUtils.writeStringToFile(file,content,LIB_DEFAULT_CHARSET);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "List folder names from path to folder [1]; "
//            + "sourcePath - Other - is relative/absolute path to folder;")
//    public String listFolderNames(String sourcePath) throws FileNotFoundException {
//        sourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
//        File sourceFile = new File(sourcePath);
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//
//        if (sourceFile.isFile()) {
//            final String message = sourceFile + " is a file";
//            throw new IllegalArgumentException(message);
//        }
//
//        Set<FileFolderNameDto> folderNames = Stream.of(sourceFile.listFiles()).filter(file -> file.isDirectory())
//                .map(file -> {
//                    String name = file.getName();
//                    String path = file.getAbsolutePath();
//                    boolean isFile = file.isFile();
//                    boolean isFolder = file.isDirectory();
//                    return new FileFolderNameDto(name, path, isFile, isFolder);
//                }).collect(Collectors.toSet());
//
//        return "new GsonBuilder().setPrettyPrinting().create().toJson(folderNames, Set.class)";
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Read content from path to file [1]; "
//            + "filePath - File - is fileName/absolute path to file with file format;")
//    public String readFileData(String filePath) throws IOException {
//        File sourceFile = new File(filePath);
//
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//
//        if (sourceFile.isDirectory()) {
//            final String message = sourceFile + " is a folder";
//            throw new IllegalArgumentException(message);
//        }
//
//        return FileUtils.readFileToString(sourceFile, LIB_DEFAULT_CHARSET);
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Get readable size from path to file/folder [1]; "
//            + "sourcePath - Other - is relative/absolute path to folder/file with file format;")
//    public String getFileOrFolderSizeReadable(String sourcePath) throws IllegalArgumentException {
//        sourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
//        return humanReadableByteCountBin(FileUtils.sizeOf(new File(sourcePath)));
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Get attributes detail from path to file/folder [1]; "
//            + "sourcePath - Other - is relative/absolute path to folder/file with file format;")
//    public String fileOrFolderAttributes(String sourcePath) throws IOException {
//        sourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
//        File sourceFile = new File(sourcePath);
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//        BasicFileAttributes basicFileAttributes = getFileFolderBasicAttributes(sourceFile);
//        FileFolderAttributesDto fileFolderAttributesDto = new FileFolderAttributesDto();
//        fileFolderAttributesDto.setName(sourceFile.getName());
//        fileFolderAttributesDto.setPath(sourceFile.getAbsolutePath());
//        fileFolderAttributesDto.setSizeByte(getFileFolderSizeByte(sourceFile));
//        fileFolderAttributesDto.setSizeText(humanReadableByteCountBin(fileFolderAttributesDto.getSizeByte()));
//        fileFolderAttributesDto.setCreationDateTime(getFileFolderCreationDate(basicFileAttributes));
//        fileFolderAttributesDto.setLastModifiedDateTime(getFileFolderLastModifiedDate(basicFileAttributes));
//        fileFolderAttributesDto.setLastAccessDateTime(getFileFolderLastAccessDate(basicFileAttributes));
//        fileFolderAttributesDto.setFile(basicFileAttributes.isRegularFile());
//        fileFolderAttributesDto.setDirectory(basicFileAttributes.isDirectory());
//        fileFolderAttributesDto.setOther(basicFileAttributes.isOther());
//        fileFolderAttributesDto.setSymbolicLink(basicFileAttributes.isSymbolicLink());
//        fileFolderAttributesDto.setHidden(sourceFile.isHidden());
//        return "new GsonBuilder().setPrettyPrinting().create().toJson(fileFolderAttributesDto, FileFolderAttributesDto.class)";
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "List files and folder attributes detail from path to folder [1]; "
//            + "sourcePath - Other - is relative/absolute path to folder;")
//    public String listFileAndFolderAttributes(String sourcePath) throws FileNotFoundException {
//        sourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
//        File sourceFile = new File(sourcePath);
//
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//
//        if (sourceFile.isFile()) {
//            final String message = sourceFile + " is a file";
//            throw new IllegalArgumentException(message);
//        }
//
//        List<FileFolderAttributesDto> attributes = listFileFolder(sourceFile).stream()
//                .map(file -> getFileFolderAttributes(file))
//                .filter(file -> file != null)
//                .collect(Collectors.toList());
//
//        return "new GsonBuilder().setPrettyPrinting().create().toJson(attributes, List.class)";
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Get created date time from path to file/folder [1] in json format; "
//            + "sourcePath - Other - is relative/absolute path to folder/file with file format;")
//    public String getFileOrFolderCreationDateJson(String sourcePath) throws IOException {
//        sourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
//        File sourceFile = new File(sourcePath);
//
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//
////        JsonObject date = new JsonObject();
////        date.addProperty("name", sourceFile.getName());
////        date.addProperty("path", sourceFile.getAbsolutePath());
////        date.addProperty("createdDate", toDateStringFromFileTime(getFileFolderBasicAttributes(sourceFile).creationTime()));
//
//        return null;
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Get created date time from path to file/folder [1]; "
//            + "sourcePath - Other - is relative/absolute path to folder/file with file format;")
//    public String getFileOrFolderCreationDate(String sourcePath) throws IOException {
//        sourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
//        File sourceFile = new File(sourcePath);
//
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//
//        return toDateStringFromFileTime(getFileFolderBasicAttributes(sourceFile).creationTime());
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Get last modified date time from path to file/folder [1] in json format; "
//            + "sourcePath - Other - is relative/absolute path to folder/file with file format;")
//    public String getFileFolderLastModifiedDateJson(String sourcePath) throws IOException {
//        sourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
//        File sourceFile = new File(sourcePath);
//
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//
//        return null;
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Get last modified date time from path to file/folder [1]; "
//            + "sourcePath - Other - is relative/absolute path to folder/file with file format;")
//    public String getFileFolderLastModifiedDate(String sourcePath) throws IOException {
//        sourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
//        File sourceFile = new File(sourcePath);
//
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//
//        return toDateStringFromFileTime(getFileFolderBasicAttributes(sourceFile).lastModifiedTime());
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Get last accessed date time from path to file/folder [1] in json format; "
//            + "sourcePath - Other - is relative/absolute path to folder/file with file format;")
//    public String getFileFolderLastAccessDateJson(String sourcePath) throws IOException {
//        sourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
//        File sourceFile = new File(sourcePath);
//
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//
//        return null;
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Get last accessed date time from path to file/folder [1]; "
//            + "sourcePath - Other - is relative/absolute path to folder/file with file format;")
//    public String getFileFolderLastAccessDate(String sourcePath) throws IOException {
//        sourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
//        File sourceFile = new File(sourcePath);
//
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//
//        return toDateStringFromFileTime(getFileFolderBasicAttributes(sourceFile).lastAccessTime());
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Checking content [1] is exists in path to file [2], case sensitive; "
//            + "content - Other - is search keyword; "
//            + "filePath - File - is fileName/absolute path to file with file format;")
//    public boolean checkFileHadContent(String content, String filePath) throws IOException {
//        File sourceFile = new File(filePath);
//
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//
//        if (sourceFile.isDirectory()) {
//            final String message = sourceFile + " is a folder";
//            throw new IllegalArgumentException(message);
//        }
//
//        return FileUtils.readFileToString(sourceFile, LIB_DEFAULT_CHARSET).contains(content);
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Get line at row index [1] from path to file [2]; "
//            + "index - Other - is row index (number); "
//            + "filePath - File - is fileName/absolute path to file with file format;")
//    public String getLineAtRowIndex(int rowIndex, String filePath) throws IOException {
//        rowIndex = rowIndex - 1;
//
//        if (rowIndex < 0) {
//            final String message = "row index less than zero";
//            throw new IllegalArgumentException(message);
//        }
//
//        File sourceFile = new File(filePath);
//
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//
//        if (sourceFile.isDirectory()) {
//            final String message = sourceFile + " is a folder";
//            throw new IllegalArgumentException(message);
//        }
//
//        List<String> lines = listFileLine(sourceFile);
//
//        if (rowIndex >= lines.size()) {
//            final String message = "Indexing out of bounds";
//            throw new IndexOutOfBoundsException(message);
//        }
//        return lines.get(rowIndex);
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Get first line match with content [1] from path to file [2]; "
//            + "content - Other - is lookup content; "
//            + "filePath - File - is fileName/absolute path to file with file format;")
//    public String getFirstLineAtRowMatchContent(String content, String filePath) throws IOException {
//        File sourceFile = new File(filePath);
//
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//
//        if (sourceFile.isDirectory()) {
//            final String message = sourceFile + " is a folder";
//            throw new IllegalArgumentException(message);
//        }
//
//        List<String> lines = listFileLine(sourceFile);
//
//        return lines.stream().filter(line -> line.contains(content)).findFirst().get();
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Get all line match with content [1] from path to file [2]; "
//            + "content - Other - is lookup content; "
//            + "filePath - File - is fileName/absolute path to file with file format;")
//    public String getAllLineAtRowMatchContent(String content, String filePath) throws IOException {
//        File sourceFile = new File(filePath);
//
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//
//        if (sourceFile.isDirectory()) {
//            final String message = sourceFile + " is a folder";
//            throw new IllegalArgumentException(message);
//        }
//
//        List<String> lines = listFileLine(sourceFile);
//
//        return lines.stream().filter(line -> line.contains(content)).collect(Collectors.joining(LINE_ENDING));
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Get all line match with content [1] from path to file [2] in json format; "
//            + "content - Other - is lookup content; "
//            + "filePath - File - is fileName/absolute path to file with file format;")
//    public String getAllLineAtRowMatchContentJson(String content, String filePath) throws IOException {
//        File sourceFile = new File(filePath);
//
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//
//        if (sourceFile.isDirectory()) {
//            final String message = sourceFile + " is a folder";
//            throw new IllegalArgumentException(message);
//        }
//
//        List<String> lines = listFileLine(sourceFile);
//     return null;
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Replace content [1] with new content [2] from path to file [3], case sensitive; "
//            + "content - Other - is search keyword; "
//            + "newContent - Other - is replace keyword; "
//            + "filePath - File - is fileName/absolute path to file with file format;")
//    public boolean fileReplaceAllContent(String keyword, String replaceKeyword, String filePath) throws IOException {
//        File sourceFile = new File(filePath);
//
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//
//        if (sourceFile.isDirectory()) {
//            final String message = sourceFile + " is a folder";
//            throw new IllegalArgumentException(message);
//        }
//
//        List<String> replaceLine = FileUtils.readLines(sourceFile, LIB_DEFAULT_CHARSET).stream()
//                .map(line -> line.replace(keyword, replaceKeyword)).collect(Collectors.toList());
//
//        FileUtils.writeLines(sourceFile, replaceLine, LINE_ENDING);
//        return true;
//    }
//
//    @DescriptorKey("Prefix:ART; "
//            + "Delete path to file/folder [1]; "
//            + "sourcePath - Other - is relative/absolute path to folder/file with file format;")
//    public boolean deleteFileOrFolder(String sourcePath) throws IOException {
//        sourcePath = isPathAbsolute(sourcePath) ? sourcePath : toAbsolute(sourcePath);
//        File sourceFile = new File(sourcePath);
//        if (!sourceFile.exists()) {
//            final String message = sourceFile + " is not exist";
//            throw new FileNotFoundException(message);
//        }
//
//        if (sourceFile.getAbsolutePath().equals(getUsersProjectRootDirectory())) {
//            final String message = sourceFile + " is project root folder";
//            throw new IllegalArgumentException(message);
//        }
//
//        FileUtils.forceDelete(sourceFile);
//        return true;
//    }
//
//    public Path getUsersProjectRootDirectory() {
//        String envRootDir = System.getProperty("user.dir");
//        Path rootDir = Paths.get(".").normalize().toAbsolutePath();
//        if (rootDir.startsWith(envRootDir)) {
//            return rootDir;
//        } else {
//            throw new RuntimeException("Root dir not found in user directory.");
//        }
//    }
//
//    public boolean isPathAbsolute(String path) {
//        return Paths.get(path).isAbsolute();
//    }
//
//    public String toAbsolute(String path) {
//        return getUsersProjectRootDirectory() + SEPARATOR + LIB_DEFAULT_FOLDER + SEPARATOR + path;
//    }
//
//    public String getFileFolderCreationDate(BasicFileAttributes basicFileAttributes) {
//        return toDateStringFromFileTime(basicFileAttributes.creationTime());
//    }
//
//    public String getFileFolderLastAccessDate(BasicFileAttributes basicFileAttributes) {
//        return toDateStringFromFileTime(basicFileAttributes.lastAccessTime());
//    }
//
//    public BasicFileAttributes getFileFolderBasicAttributes(File file) {
//        try {
//            return java.nio.file.Files
//                    .getFileAttributeView(file.toPath(), BasicFileAttributeView.class)
//                    .readAttributes();
//        } catch (IOException e) {
//            return null;
//        }
//    }
//
//    public String toDateStringFromFileTime(FileTime fileTime) {
//        return fileTime.toInstant().atZone(ZoneId.of(LIB_DEFAULT_ZONE_ID)).toLocalDateTime()
//                .format(DateTimeFormatter.ofPattern(LIB_DEFAULT_DATE_TIME_PATTERN));
//    }
//
//    public String humanReadableByteCountBin(long bytes) {
//        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
//        if (absB < 1024) {
//            return bytes + " B";
//        }
//        long value = absB;
//        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
//        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
//            value >>= 10;
//            ci.next();
//        }
//        value *= Long.signum(bytes);
//        return String.format("%.1f %ciB", value / 1024.0, ci.current());
//    }
//
//    public FileFolderAttributesDto getFileFolderAttributes(File sourceFile) {
//        BasicFileAttributes basicFileAttributes = getFileFolderBasicAttributes(sourceFile);
//        if (basicFileAttributes == null) return null;
//        FileFolderAttributesDto fileFolderAttributesDto = new FileFolderAttributesDto();
//        fileFolderAttributesDto.setName(sourceFile.getName());
//        fileFolderAttributesDto.setPath(sourceFile.getAbsolutePath());
//        fileFolderAttributesDto.setSizeByte(getFileFolderSizeByte(sourceFile));
//        fileFolderAttributesDto.setSizeText(humanReadableByteCountBin(fileFolderAttributesDto.getSizeByte()));
//        fileFolderAttributesDto.setCreationDateTime(getFileFolderCreationDate(basicFileAttributes));
//        fileFolderAttributesDto.setLastModifiedDateTime(getFileFolderLastModifiedDate(basicFileAttributes));
//        fileFolderAttributesDto.setLastAccessDateTime(getFileFolderLastAccessDate(basicFileAttributes));
//        fileFolderAttributesDto.setFile(basicFileAttributes.isRegularFile());
//        fileFolderAttributesDto.setDirectory(basicFileAttributes.isDirectory());
//        fileFolderAttributesDto.setOther(basicFileAttributes.isOther());
//        fileFolderAttributesDto.setSymbolicLink(basicFileAttributes.isSymbolicLink());
//        fileFolderAttributesDto.setHidden(sourceFile.isHidden());
//        return fileFolderAttributesDto;
//    }
//
//    public Set<File> listFileFolder(File file) {
//        return Stream.of(file.listFiles()).collect(Collectors.toSet());
//    }
//
//    public Long getFileFolderSizeByte(File sourceFile) throws IllegalArgumentException {
//        return FileUtils.sizeOf(sourceFile);
//    }
//
//    public String getFileFolderLastModifiedDate(BasicFileAttributes basicFileAttributes) {
//        return toDateStringFromFileTime(basicFileAttributes.lastModifiedTime());
//    }
//
//    public String getParentPath(File file) {
//        return file.toPath().getParent().toString();
//    }
//
//    public List<String> listFileLine(File file) throws IOException {
//        return Files.lines(file.toPath()).collect(Collectors.toList());
//    }
//
//    public boolean appendContentToFile(File file, String content) throws IOException {
//        List<String> lines = Files.lines(file.toPath()).collect(Collectors.toList());
//        lines.add(content);
//        FileUtils.writeLines(file, lines, LINE_ENDING);
//        return true;
//    }
}