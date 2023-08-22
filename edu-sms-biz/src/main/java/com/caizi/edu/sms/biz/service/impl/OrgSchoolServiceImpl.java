package com.caizi.edu.sms.biz.service.impl;

import com.antnest.mscore.base.service.impl.AntnestBaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.caizi.edu.sms.biz.mapper.OrgSchoolMapper;
import com.caizi.edu.sms.biz.service.OrgSchoolService;
import com.caizi.edu.sms.entity.OrgSchool;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 科室学校分配 服务实现类
 * </p>
 *
 * @author zhaoyan
 * @since 2022-09-04
 */
@Service
public class OrgSchoolServiceImpl extends AntnestBaseServiceImpl<OrgSchoolMapper, OrgSchool> implements OrgSchoolService {

    @Override
    public List<OrgSchool> findByDeptId(Long deptId) {
        if (deptId == null) return new ArrayList<>();
        QueryWrapper<OrgSchool> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(OrgSchool.ORG_ID, deptId);
        queryWrapper.orderByAsc(OrgSchool.SORT_NUM);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<OrgSchool> findBySchoolIdList(List<Long> schoolIdList) {
        if (schoolIdList == null || schoolIdList.size() == 0) return new ArrayList<>();
        QueryWrapper<OrgSchool> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(OrgSchool.SCHOOL_ID, schoolIdList);
        queryWrapper.orderByAsc(OrgSchool.ORG_ID, OrgSchool.SORT_NUM);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void removeByDeptId(Long deptId) {
        if (deptId == null) return;
        UpdateWrapper<OrgSchool> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(OrgSchool.ORG_ID, deptId);
        super.remove(updateWrapper);
    }

    @Override
    public void removeBySchoolIdList(List<Long> schoolIdList) {
        if (schoolIdList == null || schoolIdList.size() == 0) return;
        UpdateWrapper<OrgSchool> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in(OrgSchool.SCHOOL_ID, schoolIdList);
        super.remove(updateWrapper);
    }

    @Override
    public OrgSchool findBySchoolId(Long schoolId) {
        if (schoolId == null) return null;
        QueryWrapper<OrgSchool> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(OrgSchool.SCHOOL_ID, schoolId);
        return baseMapper.selectOne(queryWrapper);
    }
}
