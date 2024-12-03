package com.example.vitalis.mapper;

import com.example.vitalis.entity.Prescription;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface PrescriptionMapper {

    int insert(Prescription prescription);

    int deleteById(Integer id);

    int updateById(Prescription prescription);

    Prescription selectById(Integer id);

    List<Prescription> selectAll(Prescription prescription);
}
