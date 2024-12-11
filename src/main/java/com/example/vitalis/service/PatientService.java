package com.example.vitalis.service;

import com.example.vitalis.entity.Account;
import com.example.vitalis.entity.Patient;
import com.example.vitalis.entity.Patient;
import com.example.vitalis.mapper.AccountMapper;
import com.example.vitalis.mapper.PatientMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {

    @Resource
    private PatientMapper patientMapper;
    @Autowired
    private AccountMapper accountMapper;

    public void add(Patient patient) {
        patientMapper.insert(patient);
    }


    public void deleteById(String id) {
        patientMapper.deleteById(id);
        Account account = accountMapper.selectById(id);
        if (account != null) {
            accountMapper.deleteById(id);
        }
    }

    public void deleteBatch(List<String> ids) {
        for (String id : ids) {
            patientMapper.deleteById(id);
            Account account = accountMapper.selectById(id);
            if (account != null) {
                accountMapper.deleteById(id);
            }
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
        return PageInfo.of(patients);
    }

    public List<Patient> querySearch(String queryString) {
        Patient patient = new Patient();
        List<Patient> patients = patientMapper.selectAll(patient);
        List<Patient> patients1 = new ArrayList<>();
        int cnt = 0;
        for (Patient patient1 : patients) {
            if (cnt >= 10) {
                break;
            }
            if (patient1.getName().contains(queryString)) {
                patients1.add(patient1);
                cnt += 1;
            }
        }
        return patients1;
    }

    public PageInfo<Patient> querySearchAdmin(String queryString, Integer pageNum, Integer pageSize) {
        Patient patient = new Patient();
        patient.setName(queryString);
        List<Patient> patients = patientMapper.selectAll(patient);
        List<Patient> patients1 = new ArrayList<>();
        for (Patient patient1: patients) {
            if (patient1.getName().contains(queryString)) {
                patients1.add(patient1);
            }
        }
        int total = patients1.size(); // 总记录数
        int start = (pageNum - 1) * pageSize; // 起始索引
        int end = Math.min(start + pageSize, total); // 结束索引

        // 获取当前页数据
        List<Patient> pageList = patients1.subList(start, end);

        // 封装到 PageInfo 中
        PageInfo<Patient> pageInfo = new PageInfo<>(pageList);
        pageInfo.setTotal(total); // 总记录数
        pageInfo.setPageNum(pageNum); // 当前页
        pageInfo.setPageSize(pageSize); // 每页大小
        pageInfo.setPages((total + pageSize - 1) / pageSize); // 总页数

        return pageInfo;
    }



}
