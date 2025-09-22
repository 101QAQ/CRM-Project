package com.duanHaiTao.dongLiYunKe.service.impl;

import com.duanHaiTao.dongLiYunKe.model.TSystemInfo;
import com.duanHaiTao.dongLiYunKe.repository.TSystemInfomapper;
import com.duanHaiTao.dongLiYunKe.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemServiceImpl implements SystemService {

    @Autowired
    private TSystemInfomapper systemInfoMapper;

    @Override
    public TSystemInfo getSystemInfo() {
        return systemInfoMapper.selectByPrimaryKey(1);
    }

    @Override
    public int reset(TSystemInfo systemInfo) {
        return systemInfoMapper.updateByPrimaryKeySelective(systemInfo);
    }
}
