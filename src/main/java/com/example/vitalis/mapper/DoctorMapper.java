package com.example.vitalis.mapper;

import com.example.vitalis.entity.Doctor;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface DoctorMapper {

    int insert(Doctor doctor);

    int deleteById(String id);

    int updateById(Doctor doctor);

    Doctor selectById(String id);

    List<Doctor> selectAll(Doctor doctor);
}
