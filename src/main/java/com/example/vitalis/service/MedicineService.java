package com.example.vitalis.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.vitalis.entity.Medicine;
import com.example.vitalis.entity.Medicine;
import com.example.vitalis.exception.CustomException;
import com.example.vitalis.common.enums.ResultCodeEnum;
import com.example.vitalis.mapper.MedicineMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<Medicine> medicines = medicineMapper.selectAll(medicine);
        String size = medicines.get(medicines.size() - 1).getId();

        medicineMapper.insert(medicine);
    }

    public void deleteById(String id) {
        medicineMapper.deleteById(id);
    }

    public void deleteBatch(List<String> ids) {
        for (String id : ids){
            medicineMapper.deleteById(id);
        }
    }

    public void updateById(Medicine medicine) {
        Medicine medicine1 = medicineMapper.selectById(medicine.getId());
        if (ObjectUtil.isNull(medicine1)) {
            throw new CustomException(ResultCodeEnum.MEDICINE_NOT_EXIST_ERROR);
        }
        medicineMapper.updateById(medicine);
    }

    public Medicine selectById(String id ) {
        return medicineMapper.selectById(id);
    }

    public List<Medicine> selectAll(Medicine medicine) {
        return medicineMapper.selectAll(medicine);
    }

    public PageInfo<Medicine> selectPage(Medicine medicine, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Medicine> medicines = medicineMapper.selectAll(medicine);
        return PageInfo.of(medicines);
    }

    public List<Medicine> querySearch(String partName) {
        Medicine medicine = new Medicine();
        List<Medicine> medicines = medicineMapper.selectAll(medicine);
        List<Medicine> medicines1 = new ArrayList<>();
        int cnt = 0;
        for (Medicine medicine1: medicines) {

            if (cnt >= 10) {
                break;
            }
            if (medicine1.getName().contains(partName)) {
                medicines1.add(medicine1);
                cnt += 1;
            }
        }
        return medicines1;
    }

    public PageInfo<Medicine> querySearchAdmin(String partName, Integer pageNum, Integer pageSize) {
        Medicine medicine = new Medicine();
        List<Medicine> medicines = medicineMapper.selectAll(medicine);
        List<Medicine> medicines1 = new ArrayList<>();
        for (Medicine medicine1: medicines) {
            if (medicine1.getName().contains(partName)) {
                medicines1.add(medicine1);
            }
        }
        int total = medicines1.size(); // 总记录数
        int start = (pageNum - 1) * pageSize; // 起始索引
        int end = Math.min(start + pageSize, total); // 结束索引

        // 获取当前页数据
        List<Medicine> pageList = medicines1.subList(start, end);

        // 封装到 PageInfo 中
        PageInfo<Medicine> pageInfo = new PageInfo<>(pageList);
        pageInfo.setTotal(total); // 总记录数
        pageInfo.setPageNum(pageNum); // 当前页
        pageInfo.setPageSize(pageSize); // 每页大小
        pageInfo.setPages((total + pageSize - 1) / pageSize); // 总页数

        return pageInfo;
    }


}
