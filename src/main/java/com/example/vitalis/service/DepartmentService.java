package com.example.vitalis.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.vitalis.common.enums.ResultCodeEnum;
import com.example.vitalis.entity.Department;
import com.example.vitalis.exception.CustomException;
import com.example.vitalis.mapper.DepartmentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DepartmentService {

    @Resource
    private DepartmentMapper departmentMapper;

    public void add(Department department) {
        Department department1 = departmentMapper.selectById(department.getDepartId());
        if (ObjectUtil.isNotEmpty(department1)) {
            throw new CustomException(ResultCodeEnum.DEPARTMENT_EXIST_ERROR);
        }
        departmentMapper.insert(department);
    }

    public void deleteById(Integer id) {
        departmentMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            departmentMapper.deleteById(id);
        }
    }

    public void updateById(Department department) {
        Department department2 = departmentMapper.selectById(department.getDepartId());
        if (ObjectUtil.isNull(department2)) {
            throw new CustomException(ResultCodeEnum.DEPARTMENT_NOT_EXIST_ERROR);
        }
        departmentMapper.updateById(department);
    }

    public Department selectById(Integer id) {
        return departmentMapper.selectById(id);
    }

    public List<Department> selectAll(Department department) {
        return departmentMapper.selectAll(department);
    }

    public PageInfo<Department> selectPage(Department department, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Department> list = departmentMapper.selectAll(department);
        return PageInfo.of(list);
    }

}
