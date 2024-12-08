package com.example.vitalis.mapper;

import com.example.vitalis.entity.Medicine;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface MedicineMapper {

    int insert(Medicine medicine);

    int deleteById(Integer id);

    int updateById(Medicine medicine);

    Medicine selectById(String id);

    List<Medicine> selectAll(Medicine medicine);
}
