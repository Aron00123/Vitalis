package com.example.vitalis.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.vitalis.entity.Medicine;
import com.example.vitalis.exception.CustomException;
import com.example.vitalis.common.enums.ResultCodeEnum;
import com.example.vitalis.mapper.MedicineMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

@Service
public class MedicineService {

    @Resource
    private MedicineMapper medicineMapper;

    public void add(Medicine medicine) {
        Medicine medicine1 = medicineMapper.selectById(medicine.getId());
        if (ObjectUtil.isNotNull(medicine1)) {
            throw new CustomException(ResultCodeEnum.MEDICINE_EXIST_ERROR);
        }
        medicineMapper.insert(medicine);
    }

    public void deleteById(Integer id) {
        medicineMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids){
            medicineMapper.deleteById(id);
        }
    }

    public void updateById(Medicine medicine) {
        medicineMapper.updateById(medicine);
    }

    public Medicine selectById(Integer id ) {
        return medicineMapper.selectById(id);
    }

    public List<Medicine> selectAll(Medicine medicine) {
        return medicineMapper.selectAll(medicine);
    }

    public PageInfo<Medicine> selectPage(Medicine medicine, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Medicine> medicines = medicineMapper.selectAll(medicine);
        return new PageInfo<Medicine>(medicines);
    }


}
