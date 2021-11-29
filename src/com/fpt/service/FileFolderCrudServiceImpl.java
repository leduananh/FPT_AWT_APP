package com.fpt.service;

import com.fpt.app.RunCmd;
import com.fpt.enumType.AppConfig;
import com.fpt.folderHandleLib.FileFolderLib;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FileFolderCrudServiceImpl implements FileFolderCrudService, RunCmd {
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
        String batchContent = "call :focus %~1\n" +
                "exit /b\n" +
                "\n" +
                ":focus\n" +
                "setlocal EnableDelayedExpansion \n" +
                "\n" +
                "    set pr=%~1\n" +
                "    set pr=!pr:\"=!\n" +
                "\n" +
                "    echo CreateObject(\"wscript.shell\").appactivate \"!pr!\" > \"%tmp%\\focus.vbs\"\n" +
                "    call \"%tmp%\\focus.vbs\"\n" +
                "    del \"%tmp%\\focus.vbs\"\n" +
                "        exit\n" +
                "\n" +
                "goto :eof \n" +
                "endlocal ";

        try {
            fileFolderLib.overwriteFileContent(batchContent, AppConfig.APP_FOCUS_BAT_NAME.getConfigValue());
            runCmd("start " + AppConfig.APP_FOCUS_BAT_NAME.getConfigValue() + " \"" + AppConfig.APP_TITLE.getConfigValue() + "\"");
            fileFolderLib.deleteFileOrFolder("D:\\x\\out\\artifacts\\x_jar\\"+AppConfig.APP_FOCUS_BAT_NAME.getConfigValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @Override
    public void runCmd(String command) {
        try {
            Process process = new ProcessBuilder("cmd", "/C", command).inheritIO().start();
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
