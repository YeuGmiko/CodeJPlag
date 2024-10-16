package com.yeu.code.jplag.service;

import com.yeu.code.jplag.vo.Response;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface JPlagService {
    void getJPlagResultByFiles(String var1, List<MultipartFile> var2, MultipartFile var3, String var4);

    Response<byte[]> getJPlagResultFileToResponse(String var1) throws IOException;

    void initJPlagResourceFilePath();

    File getJPlagResourcePath();
}
