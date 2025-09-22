package com.duanHaiTao.dongLiYunKe.service;

import com.github.pagehelper.PageInfo;

public interface TranHistoryService {

    PageInfo<Object> historyPage(int pageNum,Integer tranId);

}
