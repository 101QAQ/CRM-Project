package com.duanHaiTao.dongLiYunKe.service;

import com.duanHaiTao.dongLiYunKe.model.TClue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ClueService {

    PageInfo<Object> cluePage(Integer pageNum,String condition) throws JsonProcessingException;


    void upFile(MultipartFile file,String token) throws IOException;

    List<TClue> downModel();

    int add(TClue clue,String token) throws JsonProcessingException;

    TClue findClueById(int id);

    int edit(TClue clue,String token) throws JsonProcessingException;

    int delete(int id);

    int deleteBatch(List<String> ids);

    TClue findClueDetails(Integer id);

    Boolean checkPhone(String phone);

}
