package com.example.vitalis.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.vitalis.common.enums.ResultCodeEnum;
import com.example.vitalis.common.enums.RoleEnum;
import com.example.vitalis.entity.Account;
import com.example.vitalis.entity.Plan;
import com.example.vitalis.exception.CustomException;
import com.example.vitalis.mapper.PlanMapper;
import com.example.vitalis.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 排班信息表业务处理
 **/
@Service
public class PlanService {

    @Resource
    private PlanMapper planMapper;

    /**
     * 新增
     */
    public void add(Plan plan) {
        // 要先做一次这个排班的校验：同一个医生同一天只能有一条排班记录
        // 根据医生的id和星期几查询一下有么有该记录
        Plan dbPlan = planMapper.selectByDoctorIdAndWeek(plan.getDoctorId(), plan.getWeek());
        if (ObjectUtil.isNotEmpty(dbPlan)) {
            throw new CustomException(ResultCodeEnum.PLAN_EXIST_ERROR);
        }
        planMapper.insert(plan);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        planMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            planMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Plan plan) {
        Plan dbPlan = planMapper.selectByDoctorIdAndWeek(plan.getDoctorId(), plan.getWeek());
        if (ObjectUtil.isNotEmpty(dbPlan) && !plan.getId().equals(dbPlan.getId())) {
            throw new CustomException(ResultCodeEnum.PLAN_EXIST_ERROR);
        }
        planMapper.updateById(plan);
    }

    /**
     * 根据ID查询
     */
    public Plan selectById(Integer id) {
        return planMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Plan> selectAll(Plan plan) {
        return planMapper.selectAll(plan);
    }

    /**
     * 分页查询
     */
    public PageInfo<Plan> selectPage(Plan plan, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.DOCTOR.name().equals(currentUser.getRole())) {
            plan.setDoctorId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Plan> list = planMapper.selectAll(plan);
        return PageInfo.of(list);
    }

}