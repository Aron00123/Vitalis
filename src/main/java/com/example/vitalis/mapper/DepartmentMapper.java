package com.example.vitalis.mapper;

import com.example.vitalis.entity.Department;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface DepartmentMapper {

    int insert(Department department);

    int deleteById(Integer id);

    int updateById(Department department);

    Department selectById(Integer id);

    List<Department> selectAll(Department department);
}
