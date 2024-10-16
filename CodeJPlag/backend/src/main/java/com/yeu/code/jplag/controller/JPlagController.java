package com.yeu.code.jplag.controller;

import com.yeu.code.jplag.exception.RestException;
import com.yeu.code.jplag.service.JPlagService;
import com.yeu.code.jplag.vo.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/jplag")
public class JPlagController {
    private static int usedCount = 0;
    @Autowired
    JPlagService jplagService;

    @PostMapping("/files")
    public Response<byte[]> jplagByFiles(
            @RequestPart(value = "language",required = true) String language,
            @RequestPart(value = "files",required = true) List<MultipartFile> files,
            @RequestPart(value = "baseCode",required = false) MultipartFile baseCode,
            HttpServletRequest req) {

        if (usedCount == 0) { // init resource
            this.jplagService.initJPlagResourceFilePath();
        }

        ++usedCount;

        HttpSession session;
        try {
            session = req.getSession();
            this.jplagService.getJPlagResultByFiles(language, files, baseCode, session.getId());
        } catch (Exception var8) {
            --usedCount;
            throw var8;
        }

        --usedCount;

        try {
            return this.jplagService.getJPlagResultFileToResponse(session.getId());
        } catch (IOException var7) {
            var7.printStackTrace();
            throw new RestException(500, "Server error, upload result file failed");
        }
    }
}
