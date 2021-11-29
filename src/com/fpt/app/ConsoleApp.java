package com.fpt.app;

import com.fpt.enumType.AppConfig;
import com.fpt.enumType.UsedClazzEnum;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class ConsoleApp implements RunCmd {
    private Menu appMenu;
    private String APP_TITLE;
    private final String REQUEST_INPUT_TITLE = "input function index to invoke: ";

    public ConsoleApp(String title) {
        this.APP_TITLE = title;
        this.appMenu = new Menu("\n" + APP_TITLE);
    }

    public void start() {
        runCmd("title " + APP_TITLE);
        while (true) {
            this.appMenu.show();
            String functionName = readUserInput();
            methodInvoke(functionName);
        }
    }

    private String readUserInput() {
        String functionName = null;

        while (true) {
            System.out.print(REQUEST_INPUT_TITLE);
            try {
                Scanner scanner = new Scanner(System.in);
                int menuIndex = scanner.nextInt();
                if (menuIndex > this.appMenu.getFunctionSize())
                    throw new IllegalArgumentException("dont have this function...");
                int arrayIndex = menuIndex - 1;
                functionName = this.appMenu.getFunctionNameByIndex(arrayIndex);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("only number...");
            }
        }
        return functionName;
    }

    private void methodInvoke(String methodName) {
        try {
            String packageName = findPackageWithMethodName(methodName);
            Class clazz = Class.forName(packageName);
            Method method = clazz.getMethod(methodName);
            method.invoke(clazz.getConstructor().newInstance());
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private String findPackageWithMethodName(String methodName) {
        return Arrays.stream(UsedClazzEnum.values()).filter(usedClazzEnum -> {
            boolean isEqual = false;
            try {
                Class clazz = Class.forName(usedClazzEnum.getClazzPackage());
                isEqual = getAllMethods(clazz).stream().anyMatch(method -> method.getName().equals(methodName));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return isEqual;
        }).findFirst().get().getClazzPackage();
    }

    private Collection<Method> getAllMethods(Class<?> target) {
        Class<?> clazz = target;
        Collection<MethodSignature> methodSignatures = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            addIfAbsentAndNonSynthetic(methodSignatures, method);
        }
        for (Method method : clazz.getMethods()) {
            addIfAbsentAndNonSynthetic(methodSignatures, method);
        }
        Package pkg = clazz.getPackage();
        clazz = clazz.getSuperclass();
        while (clazz != null) {
            for (Method method : clazz.getDeclaredMethods()) {
                int modifier = method.getModifiers();
                if (Modifier.isPrivate(modifier)) {
                    continue;
                }
                if (Modifier.isPublic(modifier) || Modifier.isProtected(modifier)) {
                    addIfAbsentAndNonSynthetic(methodSignatures, method);
                } else if ((pkg != null && pkg.equals(clazz.getPackage())) || (pkg == null
                        && clazz.getPackage() == null)) {
                    addIfAbsentAndNonSynthetic(methodSignatures, method);
                }
            }
            clazz = clazz.getSuperclass();
        }
        Collection<Method> allMethods = new ArrayList<>(methodSignatures.size());
        for (MethodSignature methodSignature : methodSignatures) {
            allMethods.add(methodSignature.getMethod());
        }
        return allMethods;
    }

    private void addIfAbsentAndNonSynthetic(Collection<MethodSignature> collection,
                                            Method method) {
        MethodSignature methodSignature = new MethodSignature(method);
        if (!method.isSynthetic() && !collection.contains(methodSignature)) {
            collection.add(methodSignature);
        }
    }

    @Override
    public void runCmd(String command) {
        try {
            new ProcessBuilder("cmd", "/C", command).inheritIO().start().waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
