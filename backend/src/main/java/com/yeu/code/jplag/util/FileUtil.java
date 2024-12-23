
package com.yeu.code.jplag.util;

import com.yeu.code.jplag.exception.RestException;

import java.io.*;
import java.util.zip.ZipFile;

public class FileUtil {

    public static void outputByIO(InputStream is, OutputStream os) throws IOException {
        try {
            byte[] buffer = new byte[1024];
            int byteSize;
            while((byteSize = is.read(buffer)) != -1) {
                os.write(buffer, 0, byteSize);
            }
            os.flush();
        } finally {
            os.flush();
            os.close();
            is.close();
        }

    }

    public static void copyFiles(File source, File target) throws IOException {
        if (!source.exists()) {
            throw new IOException("The file is not exists: " + source.getAbsolutePath());
        } else if (target.isFile()) {
            throw new IOException("The param \"target\" can not be a file!");
        } else {
            if (source.isFile()) {
                outputByIO(new FileInputStream(source), new FileOutputStream(new File(target, source.getName())));
            } else {
                String sourceBasePath = source.getAbsolutePath();
                _copyFiles(sourceBasePath, source, target);
            }
        }
    }

    private static void _copyFiles(String sourceBasePath, File source, File target) throws IOException {
        if (!source.exists()) {
            throw new IOException("The file is not exists: " + source.getAbsolutePath());
        } else if (source.isFile()) {
            String childPath = source.getAbsolutePath().substring(sourceBasePath.length());
            File copyFile = new File(target.getAbsolutePath(), childPath);
            isOutToFile(new FileInputStream(source), copyFile);
        } else if (!source.mkdirs()) {
            throw new IOException("Directory create failed: " + source.getAbsolutePath());
        } else {
            File[] files = source.listFiles();
            int len = files.length;

            for(int i = 0; i < len; ++i) {
                File file = files[i];
                _copyFiles(sourceBasePath + source.getName(), file, new File(target, source.getName()));
            }

        }
    }

    public static void isOutToFile(InputStream is, File target) throws IOException {
        System.out.println(target.getAbsolutePath());
        if (target.isDirectory()) {
            throw new IOException("The param \"target\" can not be a directory");
        } else if (!target.getParentFile().exists() && !target.getParentFile().mkdirs()) {
            throw new IOException("Directory create failed: " + target.getParentFile().getAbsolutePath());
        }
        outputByIO(is, new FileOutputStream(target));
    }

    public static String getFilePath(String... splitPath) {
        StringBuffer res = new StringBuffer();
        int len = splitPath.length;

        for(int i = 0; i < len; ++i) {
            String path = splitPath[i];
            res.append(path).append(File.separator);
        }

        return res.substring(0, res.length() - 1);
    }

    public static void delAllDirsAndFiles(File target) throws IOException {
        if (target.exists()) {
            if (target.isDirectory()) {
                File[] files = target.listFiles();
                int len = files.length;

                for(int i = 0; i < len; ++i) {
                    File file = files[i];
                    delAllDirsAndFiles(file);
                }
            }

            if (!target.delete()) {
                throw new IOException("The file delete failed: " + target.getAbsolutePath());
            }
        }
    }

    public static void createDir(File target) {
        if (!target.exists() && !target.mkdirs()) {
            throw new RestException(500, "Server error, directory of jplag system create failed");
        }
    }

    public static void unZip(File source, File target) {
        try {
            if (!source.exists()) {
                throw new IOException("The file [" + source.getAbsolutePath() + "] is not exists!");
            }

            String basePath = target.getAbsolutePath();
            ZipFile zip = new ZipFile(source);

            zip.stream().forEach((entry) -> {
                System.out.println(entry.getName());

                try {
                    File zipItem = new File(basePath, entry.getName());
                    if (entry.isDirectory()) {
                        if (!zipItem.mkdirs()) {
                            throw new IOException("文件创建失败");
                        }
                    } else {
                        zipItem.getParentFile().mkdirs();
                        outputByIO(zip.getInputStream(entry), new FileOutputStream(zipItem));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            zip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
