package com.example.vitalis.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.Page;
import com.example.vitalis.common.enums.ResultCodeEnum;
import com.example.vitalis.entity.Disease;
import com.example.vitalis.entity.Disease;
import com.example.vitalis.exception.CustomException;
import com.example.vitalis.mapper.DiseaseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
        Disease disease1 = diseaseMapper.selectById(disease.getId());
        if (ObjectUtil.isNull(disease1)) {
            throw new CustomException(ResultCodeEnum.DISEASE_EXIST_ERROR);
        }
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
        return PageInfo.of(diseases);
    }

    public List<Disease> querySearch(String partName) {
        Disease disease = new Disease();
        List<Disease> diseases = diseaseMapper.selectAll(disease);
        List<Disease> diseases1 = new ArrayList<>();
        int cnt = 0;
        for (Disease disease1: diseases) {
            if (cnt >= 10) {
                break;
            }
            if (disease1.getName().contains(partName)) {
                diseases1.add(disease1);
                cnt += 1;
            }
        }
        return diseases1;
    }

    public PageInfo<Disease> querySearchAdmin(String partName, Integer pageNum, Integer pageSize) {
        // PageHelper.startPage(PageNum, PageSize);
        Disease disease = new Disease();
        List<Disease> diseases = diseaseMapper.selectAll(disease);
        List<Disease> diseases1 = new ArrayList<>();
        for (Disease disease1: diseases) {
            if (disease1.getName().contains(partName)) {
                diseases1.add(disease1);
            }
        }
        int total = diseases1.size(); // 总记录数
        int start = (pageNum - 1) * pageSize; // 起始索引
        int end = Math.min(start + pageSize, total); // 结束索引

        // 获取当前页数据
        List<Disease> pageList = diseases1.subList(start, end);

        // 封装到 PageInfo 中
        PageInfo<Disease> pageInfo = new PageInfo<>(pageList);
        pageInfo.setTotal(total); // 总记录数
        pageInfo.setPageNum(pageNum); // 当前页
        pageInfo.setPageSize(pageSize); // 每页大小
        pageInfo.setPages((total + pageSize - 1) / pageSize); // 总页数

        return pageInfo;
    }



}
