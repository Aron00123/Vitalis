package com.example.vitalis.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.Page;
import com.example.vitalis.common.enums.ResultCodeEnum;
import com.example.vitalis.entity.Disease;
import com.example.vitalis.exception.CustomException;
import com.example.vitalis.mapper.DiseaseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DiseaseService {

    @Resource
    private DiseaseMapper diseaseMapper;

    public void add(Disease disease) {
        Disease disease1 = diseaseMapper.selectById(disease.getId());
        if (ObjectUtil.isNotNull(disease1)) {
            throw new CustomException(ResultCodeEnum.DISEASE_EXIST_ERROR);
        }
        diseaseMapper.insert(disease);
    }

    public void deleteById(Integer id) {
        diseaseMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids){
            diseaseMapper.deleteById(id);
        }
    }

    public void updateById(Disease disease) {
        diseaseMapper.updateById(disease);
    }

    public Disease selectById(Integer id ) {
        return diseaseMapper.selectById(id);
    }

    public List<Disease> selectAll(Disease disease) {
        return diseaseMapper.selectAll(disease);
    }

    public PageInfo<Disease> selectPage(Disease disease, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Disease> diseases = diseaseMapper.selectAll(disease);
        return new PageInfo<>(diseases);
    }


}
