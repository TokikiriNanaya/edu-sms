package com.caizi.edu.sms.biz.service.impl;

import com.antnest.mscore.base.service.impl.AntnestBaseServiceImpl;
import com.antnest.mscore.exception.BizException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.caizi.edu.sms.biz.mapper.KpiTempMainMapper;
import com.caizi.edu.sms.biz.service.KpiBaseItemService;
import com.caizi.edu.sms.biz.service.KpiTempItemService;
import com.caizi.edu.sms.biz.service.KpiTempMainService;
import com.caizi.edu.sms.dto.org.VOTempItem;
import com.caizi.edu.sms.dto.org.VOTempItemTreeAndStat;
import com.caizi.edu.sms.entity.KpiBaseItem;
import com.caizi.edu.sms.entity.KpiTempItem;
import com.caizi.edu.sms.entity.KpiTempMain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 指标模板
 *
 * @author 张培文
 * @date 2023/5/11 9:25
 */
@Slf4j
@Service
public class KpiTempMainServiceImpl extends AntnestBaseServiceImpl<KpiTempMainMapper, KpiTempMain> implements KpiTempMainService {

    @Resource
    private KpiTempItemService kpiTempItemService;

    @Resource
    private KpiBaseItemService kpiBaseItemService;

    @Override
    public List<KpiTempMain> findTempList() {
        QueryWrapper<KpiTempMain> queryWrapper = new QueryWrapper<>();
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 查询模板指标项树及统计信息
     */
    @Override
    public VOTempItemTreeAndStat findTempItemTreeAndStat(Long tempId, Integer levelNum, String name) {
        if (tempId == null) throw new BizException("指标模板ID不能为空！");
        // 通过模板id获取当前模板所有模板指标项
        List<KpiTempItem> kpiTempItemList = kpiTempItemService.findTempItemByTempId(tempId);
        if (CollectionUtils.isEmpty(kpiTempItemList)) throw new BizException("指标模板ID不存在！");
        List<Long> ids = new ArrayList<>();
        // 遍历获取当前模板所有基础指标项id
        for (KpiTempItem kpiTempItem : kpiTempItemList) {
            ids.add(kpiTempItem.getBaseItemId());
        }
        // 通过id批量查询基础指标项
        List<KpiBaseItem> kpiBaseItemList = kpiBaseItemService.selectBatchIds(ids);

        int firstLevel = 0;
        int secondLevel = 0;
        int thirdLevel = 0;

        VOTempItemTreeAndStat voTempItemTreeAndStat = new VOTempItemTreeAndStat();
        // 逐个封装至voTempItemTreeAndStat的mainData
        List<VOTempItem> voTempItems = new ArrayList<>();
        for (int i = 0; i < kpiBaseItemList.size(); i++) {
            // 条件筛选
            if (levelNum != null && !Objects.equals(kpiBaseItemList.get(i).getLevelNum(), levelNum)) continue;
            if (!StringUtils.isEmpty(name) && !kpiBaseItemList.get(i).getName().contains(name)) continue;

            if (kpiBaseItemList.get(i).getLevelNum() == 1) firstLevel++;
            if (kpiBaseItemList.get(i).getLevelNum() == 2) secondLevel++;
            if (kpiBaseItemList.get(i).getLevelNum() == 3) thirdLevel++;
            VOTempItem voTempItem = new VOTempItem();
            // 模板指标项id
            voTempItem.setId(kpiTempItemList.get(i).getId());
            // 模板id
            voTempItem.setTempId(kpiTempItemList.get(i).getTempId());
            // 基础指标id
            voTempItem.setBaseItemId(kpiTempItemList.get(i).getBaseItemId());
            // 排序号
            voTempItem.setSortNum(kpiTempItemList.get(i).getSortNum());
            // 名称
            voTempItem.setName(kpiBaseItemList.get(i).getName());
            // 编码
            voTempItem.setCode(kpiBaseItemList.get(i).getCode());
            // 基础指标项父ID
            voTempItem.setBaseItemPid(kpiTempItemList.get(i).getBaseItemPid());
            // 层级
            voTempItem.setLevelNum(kpiBaseItemList.get(i).getLevelNum());
            // 指标特征
            voTempItem.setFeature(kpiBaseItemList.get(i).getFeature());

            voTempItems.add(voTempItem);
        }
        voTempItemTreeAndStat.setMainData(voTempItems);
        voTempItemTreeAndStat.setFirstLevel(firstLevel);
        voTempItemTreeAndStat.setSecondLevel(secondLevel);
        voTempItemTreeAndStat.setThirdLevel(thirdLevel);
        return voTempItemTreeAndStat;
    }

    /**
     * 存模板主信息
     */
    @Override
    public KpiTempMain saveTemp(Long id, String name) {
        if (StringUtils.isEmpty(name)) throw new BizException("指标模板名称不能为空！");
        if (!this.doCheckNameOnly(id, name)) throw new BizException("指标模板名称不唯一！");
        KpiTempMain kpiTempMain = new KpiTempMain();
        if (id == null) {
            // 新增
            kpiTempMain.setName(name);
            // 倒序查找最新排序号
            QueryWrapper<KpiTempMain> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc(KpiTempMain.SORT_NUM);
            List<KpiTempMain> kpiTempMainList = baseMapper.selectList(queryWrapper);
            int sortNum;
            // 无数据从0开始排 有数据则从最新排序号+1
            if (CollectionUtils.isEmpty(kpiTempMainList)) sortNum = 1;
            else sortNum = kpiTempMainList.get(0).getSortNum() + 1;
            kpiTempMain.setSortNum(sortNum);
            super.save(kpiTempMain);
        } else {
            // 修改
            kpiTempMain = baseMapper.selectById(id);
            if (kpiTempMain == null) throw new BizException("指标模板不存在！");
            kpiTempMain.setName(name);
            super.updateById(kpiTempMain);
        }
        return kpiTempMain;
    }

    /**
     * 删除模板
     */
    @Override
    public void removeTemp(Long tempId) {
        super.removeById(tempId);
    }

    /**
     * 批量新增模板指标项
     */
    @Override
    public void saveTempItemBatch(Long tempId, List<Long> baseItemIdList) {
        // 先获取到所有要添加的三级基础指标项
        List<KpiBaseItem> kpiBaseItemList = kpiBaseItemService.selectBatchIds(baseItemIdList);
        for (KpiBaseItem kpiBaseItem : kpiBaseItemList) {
            // 获递归取父节点一并存入
            kpiBaseItemService.savaTempItemByThirdBaseItemId(kpiBaseItem.getId(), tempId);
        }

    }

    /**
     * 批量删除模板指标项
     */
    @Override
    public void removeTempItemBatch(Long tempId, List<Long> tempItemIds) {
        kpiTempItemService.removeTempItemBatchByIds(tempId, tempItemIds);

    }

    /**
     * 模板指标项调序
     */
    @Override
    public void updateTempItemSortNum(Long tempItemId, Integer operFlag) {
        kpiTempItemService.updateTempItemSortNum(tempItemId,operFlag);
    }

    /**
     * 检查模板名称唯一性
     */
    private boolean doCheckNameOnly(Long id, String name) {
        QueryWrapper<KpiTempMain> queryWrapper = new QueryWrapper<>();
        if (id != null) {
            queryWrapper.ne(KpiTempMain.ID, id);
        }
        queryWrapper.eq(KpiTempMain.NAME, name);
        List<KpiTempMain> kpiTempMainList = baseMapper.selectList(queryWrapper);
        return kpiTempMainList == null || kpiTempMainList.size() == 0;
    }


}
