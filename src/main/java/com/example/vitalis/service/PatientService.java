package com.example.vitalis.service;

import com.example.vitalis.entity.Patient;
import com.example.vitalis.mapper.PatientMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PatientService {

    @Resource
    private PatientMapper patientMapper;

    public void add(Patient patient) {
        patientMapper.insert(patient);
    }


    public void deleteById(String id) {
        patientMapper.deleteById(id);
    }

    public void deleteBatch(List<String> ids) {
        for (String id : ids) {
            patientMapper.deleteById(id);
        }
    }

    public void updateById(Patient patient) {
        patientMapper.updateById(patient);
    }

    public Patient selectById(String id) {
        return patientMapper.selectById(id);
    }

    public List<Patient> selectAll(Patient patient) {
        return patientMapper.selectAll(patient);
    }

    public PageInfo<Patient> selectPage(Patient patient, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Patient> patients = patientMapper.selectAll(patient);
        return new PageInfo<>(patients);
    }



}
