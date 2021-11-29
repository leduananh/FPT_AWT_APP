package com.fpt.service;

import com.fpt.app.RunCmd;
import com.fpt.enumType.AppConfig;
import com.fpt.folderHandleLib.FileFolderLib;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FileFolderCrudServiceImpl implements FileFolderCrudService {
    private final Scanner scanner = new Scanner(System.in);
    private final FileFolderLib fileFolderLib = new FileFolderLib();

    @Override
    public void createFolder() throws IOException {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to create: ");
        boolean isCreated = fileFolderLib.createFolder(sourcePath);
        System.out.println("respond status: " + isCreated);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void moveFileFolder() throws IOException {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root: ");
        String targetPath = readText("input your targetPath file/folder \n is absolute path or relative path include parent directory and file name starting from project root: ");
        boolean isMoved = fileFolderLib.moveFileAndFolder(sourcePath, targetPath);
        System.out.println("respond status: " + isMoved);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void checkFileFolderExist() {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to checking: ");
        boolean isExist = fileFolderLib.checkFileOrFolderIsExists(sourcePath);
        System.out.println("respond status: " + isExist);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void renameFileFolder() throws IOException {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to rename: ");
        String newName = readText("input your new file/folder name: ");
//        boolean isRenamed = fileFolderLib.renameFileFolder(sourcePath, newName);
//        System.out.println("respond status: " + isRenamed);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void listFileFolderNames() throws IOException {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to rename: ");
        String fileFolderNamesJson = fileFolderLib.listFileAndFolderNames(sourcePath);
        System.out.println("files/directories name array json: \n" + fileFolderNamesJson);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void listFileNames() throws FileNotFoundException {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to rename: ");
        String fileNamesJson = fileFolderLib.listFileNames(sourcePath);
        System.out.println("files name array json: \n" + fileNamesJson);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void listFolderNames() throws FileNotFoundException {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to rename: ");
        String folderNamesJson = fileFolderLib.listFolderNames(sourcePath);
        System.out.println("subdirectories name array json: \n" + folderNamesJson);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void listFileFolderAttributes() throws FileNotFoundException {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to rename: ");
        String json = fileFolderLib.listFileAndFolderAttributes(sourcePath);
        System.out.println("list attributes : " + json);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void mergeFileData() throws IOException {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to merge: ");
        String targetPath = readText("input your targetPath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to merge: ");
        boolean isMerge = fileFolderLib.mergeFileContent(sourcePath, targetPath);
        System.out.println("respond status: " + isMerge);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void appendFileContent() throws IOException {
        String newData = readText("input your new data to append to source file: ");
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to append new data: ");
        boolean isAppend = fileFolderLib.appendContentIntoFile(newData, sourcePath);
        System.out.println("respond status: " + isAppend);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void readFileData() throws IOException {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to read: ");
        String data = fileFolderLib.readFileData(sourcePath);
        System.out.println("respond data: " + data);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void writeDataToFile() throws IOException {
        String newData = readText("input your new data to append to source file: ");
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to write: ");
        boolean isWrite = fileFolderLib.appendContentIntoFile(newData, sourcePath);
        System.out.println("respond status: " + isWrite);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void overWriteFile() throws IOException {
        String newData = readText("input your new data to append to source file: ");
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to over write: ");
        boolean isWrite = fileFolderLib.overwriteFileContent(newData, sourcePath);
        System.out.println("respond status: " + isWrite);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void fileFolderSize() throws FileNotFoundException {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to get file/folder size: ");
        String size = fileFolderLib.getFileOrFolderSizeReadable(sourcePath);
        System.out.println(sourcePath + " size: " + size);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void fileFolderAttributes() throws IOException {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to get file/folder size: ");
        String attributesJson = fileFolderLib.fileOrFolderAttributes(sourcePath);
        System.out.println("attributes: \n" + attributesJson);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void fileFolderCreationDate() throws IOException {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to get creation date: ");
        String date = fileFolderLib.getFileOrFolderCreationDate(sourcePath);
        System.out.println("creation date: " + date);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void fileFolderLastModifiedDate() throws IOException {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to get last modified date: ");
        String date = fileFolderLib.getFileFolderLastModifiedDate(sourcePath);
        System.out.println("last modified date: " + date);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void fileFolderLastAccessDate() throws IOException {
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to get last access date: ");
        String date = fileFolderLib.getFileFolderLastAccessDate(sourcePath);
        System.out.println("last access date: " + date);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void overWriteFileData() throws IOException {
        String newData = readText("input your new data to append to source file: ");
        String sourcePath = readText("input your sourcePath file/folder \n is absolute path or relative path include parent directory and file name starting from project root to over write file: ");
        boolean status = fileFolderLib.overwriteFileContent(newData, sourcePath);
        System.out.println("status: " + status);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void fileHasKeyword() throws IOException {
        String keyword = readText("input your keyword to checking: ");
        String sourcePath = readText("input your sourcePath file \n is absolute path or relative path include parent directory and file name starting from project root to check keyword exist: ");
        boolean status = fileFolderLib.checkFileHadContent(keyword, sourcePath);
        System.out.println("is exist: " + status);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void fileDataAtRowIndex() throws IOException {
        int rowIndex = readInt("input your row index: ");
        String sourcePath = readText("input your sourcePath file \n is absolute path or relative path include parent directory and file name starting from project root to get row data: ");
        String rowData = fileFolderLib.getLineAtRowIndex(rowIndex, sourcePath);
        System.out.println("row data: " + rowData);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void fileReplaceAll() throws IOException {
        String keyword = readText("input your search keyword: ");
        String replaceKeyword = readText("input your replace keyword: ");
        String sourcePath = readText("input your sourcePath file \n is absolute path or relative path include parent directory and file name starting from project root to get row data: ");
        boolean isReplaced = fileFolderLib.fileReplaceAllContent(keyword, replaceKeyword, sourcePath);
        System.out.println(" is Replaced: " + isReplaced);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void deleteFileFolder() throws IOException {
        String sourcePath = readText("input your sourcePath to delete file/folder \n is absolute path or relative path include parent directory and file name starting from project root: ");
        boolean isRenamed = fileFolderLib.deleteFileOrFolder(sourcePath);
        System.out.println("respond status: " + isRenamed);
        pressToContinue("press ENTER to back to main menu...");
    }

    @Override
    public void countToThenFocus() {
        int range = readInt("input your range to count: ");
        int count = 0;
        while (count < range) {
            count++;
            System.out.println("count :" + count);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String batchContent = "CreateObject(\"wscript.shell\").appactivate \"MAIN\"";
        try {
            fileFolderLib.overwriteFileContent(batchContent, "D:\\x\\focus.vbs");
            Runtime.getRuntime().exec("wscript " + "D:\\x\\focus.vbs");
            Thread.sleep(1000);
            fileFolderLib.deleteFileOrFolder("D:\\x\\focus.vbs");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openAndKillProcess() {
        try {

            Process proceso = Runtime.getRuntime().exec("netsh wlan set hostednetwork mode=allow ssid=miHotspot key=12345678");
            InputStream inputstream = proceso.getInputStream();
            BufferedInputStream bf = new BufferedInputStream(inputstream);
            byte[] contents = new byte[1024];

            int bytesRead = 0;
            String cmdContent="";
            while ((bytesRead = bf.read(contents)) != -1) {
                cmdContent += new String(contents, 0, bytesRead);
            }
            System.out.println(cmdContent);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        try {
            String namesPath = fileFolderLib.ROOT_PATH + "\\names.txt";
            Process process = new ProcessBuilder("cmd", "/C", "CHCP 65001 | TASKLIST /v /FI \"SESSIONNAME eq Console\" /FI \"STATUS eq RUNNING\" /fo list | find /i \"window title\" | find /v \"N/A\" > " + namesPath).inheritIO().start();
            process.waitFor();

            List<String> windowTitles = FileUtils.readLines(new File(namesPath), StandardCharsets.UTF_8).stream().map(title -> title.replace("Window Title:", "")).collect(Collectors.toList());
            fileFolderLib.deleteFileOrFolder(namesPath);

            while (true) {
                IntStream.range(0, windowTitles.size()).forEach(index -> System.out.println(index + 1 + ". " + windowTitles.get(index)));
                int index = readInt("pick index of app to active: ") - 1;
                String title = windowTitles.get(index);
                int killOrActive = readInt("kill press 1 | active press 2 | quit press 3: ");
                if (killOrActive == 1) {

                    Process process1 = new ProcessBuilder("cmd", "/c", "taskkill", "/F", "/FI", "WINDOWTITLE eq " + title.trim(), "/T").inheritIO().start();
                    process1.waitFor();
                }

                if (killOrActive == 2) {
                    String script = "CreateObject(\"WScript.Shell\").AppActivate Wscript.Arguments.Item(0)";
                    String focusFilePath = fileFolderLib.ROOT_PATH + "\\focus.vbs";
                    fileFolderLib.overwriteFileContent(script, focusFilePath);
                    String activeTitle = title.indexOf(" | ") != -1 ? title.substring(0, title.indexOf(" | ")) : title.indexOf(" - ") != -1 ? title.substring(0, title.indexOf(" - ")) : title;
                    Process process1 = new ProcessBuilder("cscript", focusFilePath, activeTitle.trim()).inheritIO().start();
                    process1.waitFor();
                    fileFolderLib.deleteFileOrFolder(focusFilePath);
                }

                if (killOrActive == 3)
                    break;

                System.out.println("dont have this function");
                continue;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


//        try {
//            Process p = Runtime.getRuntime().exec("tasklist.exe /v /fo list");
//            p.waitFor();
//
//            String result = IOUtils.toString(p.getInputStream(), StandardCharsets.UTF_8);
//            System.out.println(result);
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }


    }

    private void pressToContinue(String title) {
        System.out.println("\n" + title);
        scanner.nextLine();
    }

    private String readText(String request) {
        String input = null;
        while (true) {
            try {
                printRequest(request);
                input = scanner.nextLine();
                if (input.isEmpty())
                    throw new Exception("do not empty...");
                else if (isNumeric(input))
                    throw new Exception("only text...");
                break;
            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
            }
        }
        return input;
    }

    private int readInt(String request) {
        int input = 0;
        while (true) {
            try {
                printRequest(request);
                String data = scanner.nextLine();
                input = Integer.parseInt(data);
                break;
            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
            }
        }
        return input;
    }

    private void printRequest(String request) {
        System.out.print("+ " + request);
    }

    private boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (strNum == null)
            return false;
        return pattern.matcher(strNum).matches();
    }

    private Process runVbs() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("wscript " + AppConfig.APP_FOCUS_BAT_PATH.getConfigValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return process;

    }

    private Process runCmd(String command) {
        Process process = null;
        try {
            process = new ProcessBuilder("cmd", "/C", command).inheritIO().start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return process;
    }
}
