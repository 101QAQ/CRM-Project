package com.duanHaiTao.dongLiYunKe.config.Listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import com.duanHaiTao.dongLiYunKe.model.TClue;
import com.duanHaiTao.dongLiYunKe.repository.TCluemapper;
import com.duanHaiTao.dongLiYunKe.repository.TDicValuemapper;
import com.duanHaiTao.dongLiYunKe.repository.TProductmapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

@Slf4j
public class TClueDataListener implements ReadListener<TClue> {
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;

    private List<TClue> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    private TCluemapper TClueMapper;

    private TDicValuemapper dicValueMapper;

    private TProductmapper productMapper;

    private Integer createUserId;

    public TClueDataListener(TCluemapper TClueMapper, TDicValuemapper dicValueMapper, TProductmapper productMapper, Integer createUserId) {
        this.TClueMapper = TClueMapper;
        this.dicValueMapper = dicValueMapper;
        this.productMapper = productMapper;
        this.createUserId = createUserId;
    }

    public TClueDataListener() {}

    @Override
    public void invoke(TClue clue, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(clue));
        clue.setCreateBy(createUserId);
        clue.setAppellation(dicValueMapper.selectByValue(clue.getAppellationStr()).getId());
        clue.setIntentionState(dicValueMapper.selectByValue(clue.getIntentionStateStr()).getId());
        clue.setNeedLoan(dicValueMapper.selectByValue(clue.getNeedLoanStr()).getId());
        clue.setSource(dicValueMapper.selectByValue(clue.getSourceStr()).getId());
        clue.setState(dicValueMapper.selectByValue(clue.getStateStr()).getId());
        clue.setIntentionProduct(productMapper.selectByProductName(clue.getIntentionProductStr()).getId());
        clue.setCreateTime(new Date());
        cachedDataList.add(clue);
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        TClueMapper.insertBatch(cachedDataList);
        log.info("存储数据库成功！");
    }
}
