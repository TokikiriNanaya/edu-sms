package com.caizi.edu.sms.biz.service.impl;

import com.antnest.mscore.base.service.impl.AntnestBaseServiceImpl;
import com.antnest.mscore.exception.BizException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.caizi.edu.sms.biz.mapper.KpiTempItemMapper;
import com.caizi.edu.sms.biz.service.KpiTempItemService;
import com.caizi.edu.sms.entity.KpiTempItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 模板指标项
 *
 * @author 张培文
 * @date 2023/5/11 15:54
 */

@Slf4j
@Service
public class KpiTempItemServiceImpl extends AntnestBaseServiceImpl<KpiTempItemMapper, KpiTempItem> implements KpiTempItemService {

    /**
     * 通过模板id获取所有模板指标项
     */
    @Override
    public List<KpiTempItem> findTempItemByTempId(Long tempId) {
        QueryWrapper<KpiTempItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(KpiTempItem.TEMP_ID, tempId);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 通过tempId和baseItemId获取模板指标项
     */
    @Override
    public List<KpiTempItem> findTempItemByTempIdAndBaseItemId(Long tempId, Long baseItemId) {
        QueryWrapper<KpiTempItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(KpiTempItem.TEMP_ID, tempId);
        queryWrapper.eq(KpiTempItem.BASE_ITEM_ID, baseItemId);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void saveTempItem(KpiTempItem kpiTempItem) {
        super.save(kpiTempItem);
    }


    /**
     * 重新排序
     *
     * @param id     需要排序的tempItem（模板指标项） id
     * @param tempId 模板id
     * @param pid    父节点id 用来区分层级
     */
    @Override
    public void resetSortNumByTempId(Long id, Long tempId, Long pid) {
        int sortNum;
        // 倒序排列当前模板sortNum
        QueryWrapper<KpiTempItem> queryWrapper = new QueryWrapper<>();
        // 根据 模板id 筛选当前模板下所有 模板指标项
        queryWrapper.eq(KpiTempItem.TEMP_ID, tempId);
        queryWrapper.eq(KpiTempItem.BASE_ITEM_PID, pid);
        queryWrapper.orderByDesc(KpiTempItem.SORT_NUM);
        List<KpiTempItem> kpiTempItemList = baseMapper.selectList(queryWrapper);
        // 如果没查到数据 或者查到一个数据（就是刚插入的数据） 则从1开始
        if (CollectionUtils.isEmpty(kpiTempItemList) || kpiTempItemList.get(0).getSortNum() == null) sortNum = 1;
            // 否则排序号从最新递增
        else sortNum = kpiTempItemList.get(0).getSortNum() + 1;
        // 更新指标项
        KpiTempItem kpiTempItem = baseMapper.selectById(id);
        baseMapper.updateById(kpiTempItem.setSortNum(sortNum));
    }

    /**
     * 批量删除模板指标项
     *
     * @param tempId      模板id
     * @param tempItemIds 模板指标项id（三级？）
     */
    @Override
    public void removeTempItemBatchByIds(Long tempId, List<Long> tempItemIds) {
        QueryWrapper<KpiTempItem> queryWrapper = new QueryWrapper<>();
        // 获取到所有需要删除的 模板指标项 对象
        List<KpiTempItem> kpiTempItemList = baseMapper.selectBatchIds(tempItemIds);
        if (CollectionUtils.isEmpty(kpiTempItemList))
            throw new BizException("未找到需要删除的模板指标项或模板指标项已被删除！");
        // 遍历获取所有父id（用于后续重新排序）
        List<Long> pidList = new ArrayList<>();
        for (KpiTempItem item : kpiTempItemList) {
            pidList.add(item.getBaseItemPid());
        }
        // 去重
        List<Long> pids = this.delRepeat(pidList);

        // 删除后排序
        super.removeByIds(tempItemIds);
        // 重新排序
        this.resortByIds(pids);

    }

    /**
     * 模板指标项调序
     */
    @Override
    public void updateTempItemSortNum(Long tempItemId, Integer operFlag) {
        QueryWrapper<KpiTempItem> queryWrapper = new QueryWrapper<>();
        // 通过 模板指标项id 获取 模板id
        KpiTempItem kpiTempItem = baseMapper.selectById(tempItemId);
        Long tempId = kpiTempItem.getTempId();
        // 筛选当模板所有 模板指标项
        queryWrapper.eq(KpiTempItem.TEMP_ID, tempId);
        // 筛选父id相同的节点
        queryWrapper.eq(KpiTempItem.BASE_ITEM_PID, kpiTempItem.getBaseItemPid());
        String warnMsg = "";
        if (operFlag == 10) {
            /*
            上移 根据sortNum顺序排列 例如2换1 遍历找到2 互换
            1
            2↑
            3
             */
            queryWrapper.orderByAsc(KpiTempItem.SORT_NUM);
            warnMsg = "当前节点是第一个节点，不能上移！";
        } else if (operFlag == 20) {
            /*
            下移 根据sortNum倒序排列 转化为上移问题 例如2换3 遍历找到2 互换
            1   3
            2↓  2↑
            3   1
             */
            queryWrapper.orderByDesc(KpiTempItem.SORT_NUM);
            warnMsg = "当前节点是最后一个节点，不能下移！";
        } else throw new BizException("操作标识输入不正确！");

        List<KpiTempItem> kpiTempItemList = baseMapper.selectList(queryWrapper);
        for (int i = 0; i < kpiTempItemList.size(); i++) {
            // 找到当前对象
            if (Objects.equals(kpiTempItemList.get(i).getId(), tempItemId)) {
                if (i == 0) throw new BizException(warnMsg);
                KpiTempItem preTempItem = kpiTempItemList.get(i - 1);
                // 互换
                Integer temp = preTempItem.getSortNum();
                preTempItem.setSortNum(kpiTempItem.getSortNum());
                kpiTempItem.setSortNum(temp);
                // 放到list里一块更新
                List<KpiTempItem> saveKpiTempItem = new ArrayList<>();
                saveKpiTempItem.add(preTempItem);
                saveKpiTempItem.add(kpiTempItem);
                super.updateBatchById(saveKpiTempItem);
            }
        }
    }

    /**
     * list去重
     */
    private List<Long> delRepeat(List<Long> ids) {
        List<Long> listNew = new ArrayList<>();
        for (Long id : ids) {
            if (!listNew.contains(id)) {
                listNew.add(id);
            }
        }
        return listNew;
    }

    /**
     * 重新排序当前节点下所有子节点
     */
    private void resortByIds(List<Long> ids) {
        // 存储要更新的节点 结束后一并更新
        List<KpiTempItem> total=new ArrayList<>();
        for (Long id : ids) {

            QueryWrapper<KpiTempItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(KpiTempItem.BASE_ITEM_PID, id).orderByAsc();
            // 获取所有子节点
            List<KpiTempItem> kpiTempItemList = baseMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(kpiTempItemList)) return;
            // 遍历排序 从1开始
            for (int i = 0; i < kpiTempItemList.size(); i++) {
                // 重新赋值sortNum后加入total
                total.add(kpiTempItemList.get(i).setSortNum(i + 1));
            }
        }
        super.updateBatchById(total);
    }

}
