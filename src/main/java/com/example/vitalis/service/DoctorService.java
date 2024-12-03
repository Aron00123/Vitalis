package com.example.vitalis.service;

import com.example.vitalis.entity.Doctor;
import com.example.vitalis.entity.Patient;
import com.example.vitalis.mapper.DoctorMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DoctorService {

    @Resource
    private DoctorMapper doctorMapper;

    public void add(Doctor doctor) {
        doctorMapper.insert(doctor);
    }

    public void deleteById(String id) {
        doctorMapper.deleteById(id);
    }

    public void deleteBatch(List<String> ids) {
        for (String id : ids) {
            doctorMapper.deleteById(id);
        }
    }

    public void updateById(Doctor doctor) {
        doctorMapper.updateById(doctor);
    }

    public Doctor selectById(String id) {
        return doctorMapper.selectById(id);
    }

    public List<Doctor> selectAll(Doctor doctor) {
        return doctorMapper.selectAll(doctor);
    }

    public PageInfo<Doctor> selectPage(Doctor doctor, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Doctor> doctors = doctorMapper.selectAll(doctor);
        return new PageInfo<>(doctors);
    }

}
