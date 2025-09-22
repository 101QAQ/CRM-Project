package com.duanHaiTao.dongLiYunKe.service;

import com.duanHaiTao.dongLiYunKe.model.TSystemInfo;

public interface SystemService {

    TSystemInfo getSystemInfo();

    int reset(TSystemInfo systemInfo);

}
