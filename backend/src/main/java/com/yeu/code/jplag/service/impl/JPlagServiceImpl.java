package com.yeu.code.jplag.service.impl;

import com.yeu.code.jplag.enums.JPlagLanguage;
import com.yeu.code.jplag.exception.RestException;
import com.yeu.code.jplag.service.JPlagService;
import com.yeu.code.jplag.util.FileUtil;
import com.yeu.code.jplag.vo.Response;
import de.jplag.JPlag;
import de.jplag.JPlagResult;
import de.jplag.Language;
import de.jplag.exceptions.ExitException;
import de.jplag.options.JPlagOptions;
import de.jplag.reporting.reportobject.ReportObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Set;

@Service
public class JPlagServiceImpl implements JPlagService {

    @Value("${custom.base-resource-path}")
    private String BASE_RESOURCE_PATH;

    public void getJPlagResultByFiles(String language, List<MultipartFile> files, MultipartFile baseCode, String sessionId) {
        JPlagLanguage JLanguage;
        try {
            JLanguage = JPlagLanguage.getByName(language);
        } catch (IllegalArgumentException e) {
            throw new RestException(404, e.getMessage());
        }

        if (files.isEmpty()) {
            throw new RestException(404, "Files can't be empty");
        } else {
            this.initJPlagSystemFilePath(sessionId);

            boolean withBaseCode = false;
            if (baseCode != null && !baseCode.isEmpty()) {
                System.out.println(baseCode.getOriginalFilename());
                withBaseCode = true;
            }

            try {
                File filesPath = new File(getJPlagResourcePath(), FileUtil.getFilePath("jplag", sessionId, "files"));
                for (MultipartFile file : files) {
                    FileUtil.isOutToFile(file.getInputStream(), new File(filesPath, file.getOriginalFilename()));
                }

                if (withBaseCode) {
                    FileUtil.isOutToFile(baseCode.getInputStream(), new File(getJPlagResourcePath(), FileUtil.getFilePath("jplag", sessionId, "baseCode", baseCode.getOriginalFilename())));
                }
            } catch (IOException e) {
                throw new RestException(500, e.getMessage());
            }

            try {
                this.createJPlagResult(JLanguage.getLanguage(), "files", withBaseCode, sessionId);
            } catch (Exception e) {
                throw new RestException(500, e.getMessage());
            }
        }
    }

    public Response<byte[]> getJPlagResultFileToResponse(String sessionId) throws IOException {
        File result = new File(getJPlagResourcePath(), FileUtil.getFilePath("jplag", sessionId, "result.zip"));
        if (!result.exists()) {
            throw new RestException(500, "Failed to get result");
        } else {
            InputStream is = new FileInputStream(result);

            Response res;
            try {
                System.out.println(result.length());
                res = new Response(200, "Success", is.readAllBytes());
            } catch (Throwable var7) {
                try {
                    is.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }

                throw var7;
            }
            is.close();
            return res;
        }
    }

    private void initJPlagSystemFilePath(String sessionId) {
        File basePath = getJPlagResourcePath();
        try {
            FileUtil.delAllDirsAndFiles(new File(getJPlagResourcePath(), FileUtil.getFilePath("jplag", sessionId)));
        } catch (IOException var4) {
            throw new RestException(500, var4.getMessage());
        }
        FileUtil.createDir(new File(basePath, FileUtil.getFilePath("jplag", sessionId, "files")));
        FileUtil.createDir(new File(basePath, FileUtil.getFilePath("jplag", sessionId, "dirs")));
        FileUtil.createDir(new File(basePath, FileUtil.getFilePath("jplag", sessionId, "baseCode")));
        FileUtil.createDir(new File(basePath, FileUtil.getFilePath("jplag", sessionId, "temp")));
    }

    @Override
    public void initJPlagResourceFilePath() {
        try {
            FileUtil.delAllDirsAndFiles(new File(getJPlagResourcePath(), "jplag"));
        } catch (IOException var4) {
            throw new RestException(500, var4.getMessage());
        }
    }
    @Override
    public File getJPlagResourcePath() {
        return new File(BASE_RESOURCE_PATH);
    }
    private void createJPlagResult(Language language, String path, boolean withBaseCode, String sessionId) throws FileNotFoundException, ExitException {
        File basePath = getJPlagResourcePath();
        String targetPath = FileUtil.getFilePath("jplag", sessionId, path);
        JPlagOptions options = new JPlagOptions(language, Set.of(new File(basePath, targetPath)), Set.of());
        options.withMinimumTokenMatch(0);
        if (withBaseCode) {
            options.withBaseCodeSubmissionDirectory(new File(basePath, FileUtil.getFilePath("jplag", sessionId, "baseCode")));
        }

        JPlagResult result = JPlag.run(options);
        ReportObjectFactory factory = new ReportObjectFactory(new File(basePath, FileUtil.getFilePath("jplag", sessionId, "result.zip")));
        factory.createAndSaveReport(result);
    }

    private void createDir(File target) {
        if (!target.exists() && !target.mkdirs()) {
            throw new RestException(500, "Server error, directory of jplag system create failed");
        }
    }
}
