
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
            File[] var3 = source.listFiles();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                File file = var3[var5];
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
        String[] var2 = splitPath;
        int var3 = splitPath.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String path = var2[var4];
            res.append(path).append(File.separator);
        }

        return res.substring(0, res.length() - 1);
    }

    public static void delAllDirsAndFiles(File target) throws IOException {
        if (target.exists()) {
            if (target.isDirectory()) {
                File[] var2 = target.listFiles();
                int var3 = var2.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    File file = var2[var4];
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

    public static void unZip(File source, File target) throws IOException {
        try {
            if (!source.exists()) {
                throw new IOException("The file [" + source.getAbsolutePath() + "] is not exists!");
            }

            String basePath = target.getAbsolutePath();
            ZipFile zip = new ZipFile(source);

            try {
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
                    } catch (IOException var4) {
                        throw new RuntimeException(var4);
                    }
                });
            } catch (Throwable var7) {
                try {
                    zip.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }

                throw var7;
            }

            zip.close();
        } catch (IOException var8) {
            var8.printStackTrace();
        }
    }
}
