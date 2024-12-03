package com.example.vitalis.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.vitalis.entity.Prescription;
import com.example.vitalis.exception.CustomException;
import com.example.vitalis.common.enums.ResultCodeEnum;
import com.example.vitalis.mapper.PrescriptionMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PrescriptionService {

    @Resource
    private PrescriptionMapper prescriptionMapper;

    public void add(Prescription prescription) {
        Prescription prescription1 = prescriptionMapper.selectById(prescription.getPrescriptionId());
        if (ObjectUtil.isNotNull(prescription1)) {
            throw new CustomException(ResultCodeEnum.PRESCRIPTION_EXIST_ERROR);
        }
        prescriptionMapper.insert(prescription);
    }

    public void deleteById(Integer id) {
        prescriptionMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids){
            prescriptionMapper.deleteById(id);
        }
    }

    public void updateById(Prescription prescription) {
        prescriptionMapper.updateById(prescription);
    }

    public Prescription selectById(Integer id ) {
        return prescriptionMapper.selectById(id);
    }

    public List<Prescription> selectAll(Prescription prescription) {
        return prescriptionMapper.selectAll(prescription);
    }

    public PageInfo<Prescription> selectPage(Prescription prescription, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Prescription> prescriptions = prescriptionMapper.selectAll(prescription);
        return new PageInfo<Prescription>(prescriptions);
    }
}
