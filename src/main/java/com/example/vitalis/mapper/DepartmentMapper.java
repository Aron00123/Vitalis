package com.example.vitalis.mapper;

import com.example.vitalis.entity.Department;

import java.util.List;

public interface DepartmentMapper {

    /**
     * 新增
     */
    int insert(Department department);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Department department);

    /**
     * 根据ID查询
     */
    Department selectById(Integer id);

    /**
     * 查询所有
     */
    List<Department> selectAll(Department department);

}