package com.duanHaiTao.dongLiYunKe.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.duanHaiTao.dongLiYunKe.config.Listener.TClueDataListener;
import com.duanHaiTao.dongLiYunKe.exceptions.PhoneIsExistException;
import com.duanHaiTao.dongLiYunKe.model.TClue;
import com.duanHaiTao.dongLiYunKe.model.TUser;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import com.duanHaiTao.dongLiYunKe.query.QueryClue;
import com.duanHaiTao.dongLiYunKe.repository.*;
import com.duanHaiTao.dongLiYunKe.service.ClueService;
import com.duanHaiTao.dongLiYunKe.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ClueServiceImpl implements ClueService {

    @Autowired
    private TCluemapper cluemapper;
    @Autowired
    private TDicValuemapper dicValuemapper;

    @Autowired
    private TProductmapper productmapper;

    @Autowired
    private TClueRemarkmapper clueRemarkMapper;

    @Override
    public PageInfo<Object> cluePage(Integer pageNum,String condition) throws JsonProcessingException {
        PageHelper.startPage(pageNum, 10);

        QueryClue queryClue = new ObjectMapper().readValue(condition, QueryClue.class);

        List<TClue> tClues = cluemapper.selectAll(BaseQuery.builder().build(),queryClue);
        return new PageInfo<>(tClues);
    }

    @PreAuthorize("hasAuthority('clue:import')")
    @Override
    public void upFile(MultipartFile file,String token) throws IOException {
        TUser tUser = JwtUtil.parseToken(token);
        EasyExcel.read(file.getInputStream(), TClue.class, new TClueDataListener(cluemapper,dicValuemapper,productmapper,tUser.getId()))
                .sheet()
                .doRead();
    }

    @Override
    public List<TClue> downModel() {
        List<TClue> tClueList=new ArrayList<>();
        TClue tClue = TClue.builder().ownerId(1).activityId(40).fullName("王杰").appellationStr("先生")
                .phone("13700000000").weixin("13700000000").qq("13700000000").email("wangjie@163.com")
                .age(23).job("工程师").yearIncome(BigDecimal.valueOf(30)).address("北京亦庄").needLoanStr("需要")
                .intentionStateStr("有意向").intentionProductStr("比亚迪海豹").stateStr("未联系").sourceStr("车展会")
                .description("近期在看车").nextContactTime(new Date()).build();
        tClueList.add(tClue);
        return tClueList;
    }

    @PreAuthorize("hasAuthority('clue:add')")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int add(TClue clue,String token) throws JsonProcessingException {
        if (cluemapper.selectPhoneNum(clue.getPhone())<=0){
            TUser tUser = JwtUtil.parseToken(token);
            clue.setCreateTime(new Date());
            clue.setCreateBy(tUser.getId());
            return cluemapper.insertSelective(clue);
        }else {
            throw new PhoneIsExistException("手机号码已经存在");
        }
    }

    @Override
    public TClue findClueById(int id) {
        return cluemapper.selectByPrimaryKey(id);
    }

    @PreAuthorize("hasAuthority('clue:edit')")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int edit(TClue clue, String token) throws JsonProcessingException {
        TUser tUser = JwtUtil.parseToken(token);
        clue.setEditTime(new Date());
        clue.setEditBy(tUser.getId());
        return cluemapper.updateByPrimaryKeySelective(clue);
    }


    @PreAuthorize("hasAuthority('clue:delete')")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int delete(int id) {
        int i = clueRemarkMapper.deleteByClueId(id);
        i+=cluemapper.deleteByPrimaryKey(id);
        return i;
    }

    @PreAuthorize("hasAuthority('clue:delete')")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteBatch(List<String> ids) {
        List<Integer> idList=new ArrayList<>();

        ids.forEach(id->idList.add(Integer.parseInt(id)));

        clueRemarkMapper.deleteBatchByClueIds(idList);

        return cluemapper.deleteBatch(idList);
    }

    @Override
    public TClue findClueDetails(Integer id) {
        return cluemapper.selectDetail(id);
    }

    @Override
    public Boolean checkPhone(String phone) {
        int count=cluemapper.selectPhoneNum(phone);
        return count<=0;
    }
}
