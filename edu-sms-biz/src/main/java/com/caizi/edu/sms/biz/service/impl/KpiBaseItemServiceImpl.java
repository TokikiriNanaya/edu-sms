package com.caizi.edu.sms.biz.service.impl;

import com.antnest.mscore.base.service.impl.AntnestBaseServiceImpl;
import com.antnest.mscore.exception.BizException;
import com.antnest.mscore.page.Pageable;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caizi.edu.sms.biz.mapper.KpiBaseItemMapper;
import com.caizi.edu.sms.biz.service.KpiBaseItemService;
import com.caizi.edu.sms.biz.service.KpiTempItemService;
import com.caizi.edu.sms.biz.service.KpiTempMainService;
import com.caizi.edu.sms.biz.utils.NumberUtil;
import com.caizi.edu.sms.dto.org.DtoFindThirdKpiBaseItem;
import com.caizi.edu.sms.dto.org.DtoSaveKpiBaseItem;
import com.caizi.edu.sms.entity.KpiBaseItem;
import com.caizi.edu.sms.entity.KpiTempItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 张培文
 * @date 2023/5/9 12:09
 */
@Slf4j
@Service
public class KpiBaseItemServiceImpl extends AntnestBaseServiceImpl<KpiBaseItemMapper, KpiBaseItem> implements KpiBaseItemService {

    @Resource
    private KpiTempItemService kpiTempItemService;

    @Resource
    private KpiTempMainService kpiTempMainService;

    /**
     * 查询指标树
     */
    @Override
    public List<KpiBaseItem> findBaseItemTree(String name) {
        // 未输入查询条件时 查询所有数据
        if (StringUtils.isEmpty(name)) name = "";
        QueryWrapper<KpiBaseItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(KpiBaseItem.NAME, name)
                .orderByAsc(KpiBaseItem.PID, KpiBaseItem.SORT_NUM);
        List<KpiBaseItem> kpiBaseItemList = baseMapper.selectList(queryWrapper);
        return kpiBaseItemList != null && kpiBaseItemList.size() > 0 ? kpiBaseItemList : null;
    }

    /**
     * 保存指标项
     */
    @Override
    public KpiBaseItem saveBaseItem(DtoSaveKpiBaseItem dtoSaveKpiBaseItem) {
        /*
        id：如果是修改操作，必传
        name：名称，必传
        code：编码
        pid：父节点ID，必传（根节点传0）
        feature：指标特征（如果是3级指标，必传）
         */
        // 必传项判空
        if (dtoSaveKpiBaseItem == null) throw new BizException("传入的对象为空！");
        String name = dtoSaveKpiBaseItem.getName();
        if (StringUtils.isEmpty(name)) throw new BizException("传入的姓名为空！");
        Long pid = dtoSaveKpiBaseItem.getPid();
        if (pid == null) throw new BizException("传入的父节点ID为空！");
        Long kpiBaseItemId = dtoSaveKpiBaseItem.getId();
        String code = dtoSaveKpiBaseItem.getCode();

        // code 唯一性校验
        if (!this.doCheckCodeOnly(kpiBaseItemId, code)) throw new BizException("填写的【指标项编码】已被使用，保存失败");
        // name 唯一性校验
        if (!this.doCheckNameOnly(kpiBaseItemId, name)) throw new BizException("填写的【指标项名称】已被使用，保存失败");

        // 新建基础指标项用于新增/修改
        KpiBaseItem kpiBaseItem = new KpiBaseItem();

        // 获取父节点
        KpiBaseItem parentKpiBaseItem = baseMapper.selectById(pid);
        if (pid != 0 && parentKpiBaseItem == null) throw new BizException("未找到该节点的父节点！");

        // 设置全路径
        kpiBaseItem.setParentPathFull(getFullPathByPid(pid));

        //设置层级（这里通过获取父id对象的层级 来划分子id的层级）
        if (pid == 0) {
            // 一级节点
            kpiBaseItem.setLevelNum(1);
        } else {
            // 二级、三级节点
            kpiBaseItem.setLevelNum(parentKpiBaseItem.getLevelNum() + 1);
        }

        int levelNum = kpiBaseItem.getLevelNum();

        // 三级节点需要必传指标特征 非三级节点指标特征置空
        Integer feature = dtoSaveKpiBaseItem.getFeature();
        if (levelNum == 3) {
            if (feature == null) throw new BizException("传入的指标特征为空！");
            if (feature != 10 && feature != 20) throw new BizException("传入的指标特征不合法！");
        } else dtoSaveKpiBaseItem.setFeature(null);

        // id不存在为新增 存在则修改
        if (kpiBaseItemId == null) {
            // 根据算法拼接新指标项编码
            if (levelNum == 1) {
                // 一级指标项 新增到根节点 获取当前节点最新编码顺位+1作为新编码
                code = this.getLastChildCodeByPid(0L) + 1 + "";
            } else if (levelNum == 2 || levelNum == 3) {
                // 二级、三级指标项 新增到到该级节点父节点下 获取父节点指标项实体类
                // 父节点code + 最新节点code自增
                code = parentKpiBaseItem.getCode() + (this.getLastChildCodeByPid(pid) + 1);
            } else throw new BizException("节点层级定位出错，最大层级为3层！");

            // 保存新指标项编码
            dtoSaveKpiBaseItem.setCode(code);
            // 保存排序号（通过父节点ID获取该节点下 子节点个数 加一作为新排序号）
            kpiBaseItem.setSortNum(this.getChildSortNumByPid(pid) + 1);
            DtoSaveKpiBaseItem.convertToKpiBaseItem(dtoSaveKpiBaseItem, kpiBaseItem);
            super.save(kpiBaseItem);
        } else {
            if (kpiBaseItemId.equals(pid)) throw new BizException("父节点不能为自身！");
            // 修改指标项
            if (super.getById(kpiBaseItemId) == null) throw new BizException("未查询到指标项信息，更新失败！");
            // 校验code编码合法性(9位数字的字符串)
            if (!StringUtils.isEmpty(code)) {
                if (code.length() != 9 && !NumberUtil.isNumber(code))
                    throw new BizException("填写的【指标项编码】不合法，保存失败");
            }
            DtoSaveKpiBaseItem.convertToKpiBaseItem(dtoSaveKpiBaseItem, kpiBaseItem);
            super.updateById(kpiBaseItem);
        }
        return kpiBaseItem;
    }

    /**
     * 删除指标项
     */
    @Override
    public void removeBaseItem(Long itemId) {
        QueryWrapper<KpiBaseItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(KpiBaseItem.PID, itemId);
        List<KpiBaseItem> kpiBaseItemList = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(kpiBaseItemList)) {
            super.removeById(itemId);
            // 后续排序号要自减
//            QueryWrapper<KpiBaseItem> qw = new QueryWrapper<>();
//            qw.eq(KpiBaseItem.ID,itemId);
//            List<KpiBaseItem> kbList = baseMapper.selectList(qw);
        } else throw new BizException("该指标下尚有子指标，不能删除！");
    }


    /**
     * 查询三级指标项列表
     */
    @Override
    public IPage<KpiBaseItem> findThirdBaseItemList(DtoFindThirdKpiBaseItem dtoFindThirdKpiBaseItem) {
        // 参数校验
        if (dtoFindThirdKpiBaseItem == null) throw new BizException("传入的查询对象为空！");
        Pageable pageable = dtoFindThirdKpiBaseItem.getPageable();
        if (pageable == null) throw new BizException("传入的分页查询条件为空！");
        int pageNumber = pageable.getPageNumber(); // 页码
        int pageSize = pageable.getPageSize(); // 每页数据量
        if (pageNumber < 0 || pageSize < 0) throw new BizException("分页参数不合法！");

        List<KpiBaseItem> result = new ArrayList<>(); // 存放查询出来的三级指标
        IPage<KpiBaseItem> page = new Page<>(); // 分页对象 用于返回
        page.setSize(pageSize); // 每页数量
        page.setCurrent(pageNumber); // 当前页码
        List<KpiBaseItem> records = new ArrayList<>(); // 用于存放筛选后的数据 存入page的records中
        /*
         这里要查询所有指标项而不只是三级
         如果查询的是1、2级指标项 把该指标项的所有子指标项也查出来
         */
        Long selectItemId = dtoFindThirdKpiBaseItem.getSelectItemId();

        // 设置总页数
        // 总数
        if (selectItemId == null) {
            // 所传id为空 代表是通过表单搜索

            QueryWrapper<KpiBaseItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(KpiBaseItem.LEVEL_NUM, 3);

            // 条件搜索
            Integer feature = dtoFindThirdKpiBaseItem.getFeature();
            if (feature != null) queryWrapper.eq(KpiBaseItem.FEATURE, feature);
            String name = dtoFindThirdKpiBaseItem.getName();
            if (!StringUtils.isEmpty(name)) queryWrapper.like(KpiBaseItem.NAME, name).orderByAsc(KpiBaseItem.SORT_NUM);
            result = baseMapper.selectList(queryWrapper);

        } else {
            // id不为空 代表是通过点击树状结构搜索
            // 通过id查询该id下所有三级指标项
            List<KpiBaseItem> kpiBaseItemList = this.getThreeLevelsById(selectItemId);
            // 遍历 只需要三级节点
            for (KpiBaseItem kpiBaseItem : kpiBaseItemList) {
                if (kpiBaseItem.getLevelNum() == 3) result.add(kpiBaseItem);
            }
        }
        // 分页
        page.setTotal(result.size()); // 总数
        page.setPages(page.getPages()); // 设置总页数

        // 最后统一处理分页
        // 输入的页码大于总页数 直接返回
        if (page.getCurrent() > page.getPages()) return page;
        long start = (page.getCurrent() - 1) * page.getSize();
        // 开始下标超过数据总数 直接返回
        if (start > page.getTotal()) return page;
        long end = Math.min(start + page.getSize(), page.getTotal());
        for (long i = start; i < end; i++) {
            records.add(result.get((int) i));
        }
        page.setRecords(records);
        return page;
    }

    /**
     * 查询指标项详情
     */
    public KpiBaseItem getKpiBaseItemById(Long id) {
        return super.getById(id);
    }

    /**
     * 检查指标项编码唯一性
     */
    private boolean doCheckCodeOnly(Long kpiBaseItemId, String code) {
        if (StringUtils.isEmpty(code)) return true;
        QueryWrapper<KpiBaseItem> queryWrapper = new QueryWrapper<>();
        // 筛选除当前id外的数据
        if (kpiBaseItemId != null) {
            queryWrapper.ne(KpiBaseItem.ID, kpiBaseItemId);
        }
        // 再筛选code重复的数据
        queryWrapper.eq(KpiBaseItem.CODE, code);
        List<KpiBaseItem> kpiBaseItemList = baseMapper.selectList(queryWrapper);
        // 当筛选结果为空时 表示code唯一
        return kpiBaseItemList == null || kpiBaseItemList.size() == 0;
    }

    /**
     * 检查指标项名称唯一性
     */
    private boolean doCheckNameOnly(Long kpiBaseItemId, String name) {
        if (StringUtils.isEmpty(name)) return true;
        QueryWrapper<KpiBaseItem> queryWrapper = new QueryWrapper<>();
        if (kpiBaseItemId != null) {
            queryWrapper.ne(KpiBaseItem.ID, kpiBaseItemId);
        }
        queryWrapper.eq(KpiBaseItem.NAME, name);
        List<KpiBaseItem> kpiBaseItemList = baseMapper.selectList(queryWrapper);
        return kpiBaseItemList == null || kpiBaseItemList.size() == 0;
    }

    /**
     * 通过父节点ID获取该节点下 子节点个数
     */
    private int getChildNumByPid(Long pid) {
        QueryWrapper<KpiBaseItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(KpiBaseItem.PID, pid);
        return baseMapper.selectCount(queryWrapper);
    }

    /**
     * 通过父节点ID获取该节点下 最新节点排序号
     */
    private int getChildSortNumByPid(Long pid) {
        QueryWrapper<KpiBaseItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(KpiBaseItem.PID, pid).orderByDesc(KpiBaseItem.SORT_NUM);
        List<KpiBaseItem> kpiBaseItemList = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(kpiBaseItemList)) return 0;
        return kpiBaseItemList.get(0).getSortNum();
    }

    /**
     * 通过父节点ID获取该节点下 最新编码
     */
    private Long getLastChildCodeByPid(Long pid) {
        QueryWrapper<KpiBaseItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(KpiBaseItem.PID, pid)
                .orderByDesc(KpiBaseItem.CODE);
        List<KpiBaseItem> kpiBaseItemList = baseMapper.selectList(queryWrapper);
        // 不为空返回最新code 否则表示表中无一级节点 此时返回100（初始节点）
        if (kpiBaseItemList != null && kpiBaseItemList.size() > 0 && !StringUtils.isEmpty(kpiBaseItemList.get(0).getCode())) {
            //最新节点code
            return Long.parseLong(org.apache.commons.lang3.StringUtils.right(kpiBaseItemList.get(0).getCode(), 3));
        }
        return 100L;
    }

    /**
     * 通过父节点ID获取全路径
     */
    private String getFullPathByPid(Long pid) {
        if (pid != 0) {
            KpiBaseItem kpiBaseItem = baseMapper.selectById(pid);
            if (kpiBaseItem.getPid() == 0)
                return kpiBaseItem.getName();
            return kpiBaseItem.getName() + "/" + baseMapper.selectById(kpiBaseItem.getPid()).getName();
        }
        return null;
    }

    public List<KpiBaseItem> selectBatchIds(List<Long> ids) {
        return super.getByIds(ids);
    }


    /**
     * 通过三级指标递归查询并存入所有父节点
     */
    @Override
    public void savaTempItemByThirdBaseItemId(Long id, Long tempId) {
        KpiBaseItem kpiBaseItem = super.getById(id);
        Long pid = kpiBaseItem.getPid();
        // 查询tempId（模板id）和baseItemId（基础指标id） 如果无数据则将 基础指标 存入 指标模板项
        List<KpiTempItem> baseItemByTempIdAndBaseItemId = kpiTempItemService.findTempItemByTempIdAndBaseItemId(tempId, id);
        if (CollectionUtils.isEmpty(baseItemByTempIdAndBaseItemId)) {

            KpiTempItem kpiTempItem = new KpiTempItem();
            kpiTempItem.setTempId(tempId)
                    .setBaseItemId(id)
                    .setBaseItemPid(pid);
            // 存入数据库
            kpiTempItemService.saveTempItem(kpiTempItem);
            // 重新排序
            kpiTempItemService.resetSortNumByTempId(kpiTempItem.getId(), tempId, pid);
        }
        // 如果当前节点不是根节点
        if (pid != 0) {
            // 递归
            savaTempItemByThirdBaseItemId(pid, tempId);
        }
    }


    /**
     * 通过2、3级节点的id获取当前节点下的所有三级子节点
     */
    private List<KpiBaseItem> getThreeLevelsById(Long id) {
        // 存储所有三级子节点
        List<KpiBaseItem> result = new ArrayList<>();

        QueryWrapper<KpiBaseItem> queryWrapper = new QueryWrapper<>();
        List<KpiBaseItem> kpiBaseItemList = baseMapper.selectList(queryWrapper);

        // 先找到该节点
        KpiBaseItem kpiBaseItem = baseMapper.selectById(id);
        if (kpiBaseItem != null) {
            result.add(kpiBaseItem);
            // 找该节点的所有子节点
            List<KpiBaseItem> children = findChildrenById(kpiBaseItem.getId(), kpiBaseItemList);
            for (KpiBaseItem child : children) {
                result.add(child);
                List<KpiBaseItem> grandchildren = findChildrenById(child.getId(), kpiBaseItemList);
                result.addAll(grandchildren);
            }
        } else throw new BizException("所选节点不存在！");
        return result;
    }

    /**
     * 通过节点id获取所有子节点
     */
    private List<KpiBaseItem> findChildrenById(Long id, List<KpiBaseItem> kpiBaseItemList) {
        List<KpiBaseItem> children = new ArrayList<>();
        for (KpiBaseItem kpiBaseItem : kpiBaseItemList) {
            if (kpiBaseItem.getPid().equals(id)) {
                children.add(kpiBaseItem);
            }
        }
        return children;
    }


}
