package com.example.vitalis.mapper;

import com.example.vitalis.entity.Patient;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface PatientMapper {

    int insert(Patient patient);

    int deleteById(String  id);

    int updateById(Patient patient);

    Patient selectById(String id);

    List<Patient> selectAll(Patient patient);
}
