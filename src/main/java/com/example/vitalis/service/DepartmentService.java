package com.example.vitalis.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.vitalis.common.enums.ResultCodeEnum;
import com.example.vitalis.entity.Department;
import com.example.vitalis.entity.Department;
import com.example.vitalis.exception.CustomException;
import com.example.vitalis.mapper.DepartmentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {

    @Resource
    private DepartmentMapper departmentMapper;

    public void add(Department department) {
        List<Department> departmentList = departmentMapper.selectAll(new Department());
        int id;
        if (departmentList.isEmpty()) {
            id = 0;
        } else {
            id = departmentList.get(departmentList.size() - 1).getDepartId() + 1;
        }
        department.setDepartId(id);
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

    public List<Department> querySearch(String queryString) {
        Department department = new Department();
        List<Department> departments = departmentMapper.selectAll(department);
        List<Department> departments1 = new ArrayList<>();
        int cnt = 0;
        for (Department department1 : departments) {
            if (cnt >= 10) {
                break;
            }
            if (department1.getType().contains(queryString)) {
                departments1.add(department1);
                cnt += 1;
            }
        }
        return departments1;
    }

    public PageInfo<Department> querySearchAdmin(String queryString, Integer pageNum, Integer pageSize) {
        Department department = new Department();
        department.setType(queryString);
        List<Department> departments = departmentMapper.selectAll(department);
        List<Department> departments1 = new ArrayList<>();
        for (Department department1: departments) {
            if (department1.getType().contains(queryString)) {
                departments1.add(department1);
            }
        }
        int total = departments1.size(); // 总记录数
        int start = (pageNum - 1) * pageSize; // 起始索引
        int end = Math.min(start + pageSize, total); // 结束索引

        // 获取当前页数据
        List<Department> pageList = departments1.subList(start, end);

        // 封装到 PageInfo 中
        PageInfo<Department> pageInfo = new PageInfo<>(pageList);
        pageInfo.setTotal(total); // 总记录数
        pageInfo.setPageNum(pageNum); // 当前页
        pageInfo.setPageSize(pageSize); // 每页大小
        pageInfo.setPages((total + pageSize - 1) / pageSize); // 总页数

        return pageInfo;
    }

}
