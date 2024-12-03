package com.example.vitalis.mapper;

import com.example.vitalis.entity.Disease;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface DiseaseMapper {

    int insert(Disease disease);

    int deleteById(Integer id);

    int updateById(Disease disease);

    Disease selectById(Integer id);

    List<Disease> selectAll(Disease disease);
}
