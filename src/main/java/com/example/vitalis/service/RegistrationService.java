package com.example.vitalis.service;

import com.example.vitalis.entity.Prescription;
import com.example.vitalis.entity.Registration;
import com.example.vitalis.mapper.RegistrationMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RegistrationService {

    @Resource
    private RegistrationMapper registrationMapper;

    public void add(Registration registration) {
        registrationMapper.insert(registration);
    }

    public void deleteById(Integer id) {
        registrationMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids){
            registrationMapper.deleteById(id);
        }
    }

    public void updateById(Registration registration) {
        registrationMapper.updateById(registration);
    }

    public Registration selectById(Integer id) {
        return registrationMapper.selectById(id);
    }

    public List<Registration> selectAll(Registration registration) {
        return registrationMapper.selectAll(registration);
    }

    public PageInfo<Registration> selectPage(Registration registration, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Registration> registrations = registrationMapper.selectAll(registration);
        return new PageInfo<Registration>(registrations);
    }
}
