package com.example.vitalis.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.vitalis.common.enums.ResultCodeEnum;
import com.example.vitalis.entity.*;
import com.example.vitalis.exception.CustomException;
import com.example.vitalis.mapper.DepartmentMapper;
import com.example.vitalis.mapper.DoctorMapper;
import com.example.vitalis.mapper.PatientMapper;
import com.example.vitalis.mapper.RegistrationMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegistrationService {

    @Resource
    private RegistrationMapper registrationMapper;
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private PatientMapper patientMapper;

    public void add(Registration registration) {
        Registration registrationNull = new Registration();
        List<Registration> registrationArrayList = registrationMapper.selectAll(registrationNull);
        int registrationSize;
        if (!registrationArrayList.isEmpty()) {
            registrationSize = registrationArrayList.get(registrationArrayList.size() - 1).getRegistrationId();
        } else {
            registrationSize = 0;
        }
        Registration registration1 = registrationMapper.selectById(registrationSize + 1);
        if (ObjectUtil.isNotNull(registration1)) {
            throw new CustomException(ResultCodeEnum.REGISTRATION_EXIST_ERROR);
        }
        registration.setRegistrationId(registrationSize + 1);
        Doctor doctor = doctorMapper.selectById(registration.getDoctorId());
        registration.setDepartmentId(doctor.getDepartId());
        Department department = departmentMapper.selectById(registration.getDepartmentId());
        registration.setAppointAddress(department.getFloor());
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
        Registration registration1 = registrationMapper.selectById(registration.getRegistrationId());
        if (registration1 == null) {
            throw new CustomException(ResultCodeEnum.REGISTRATION_NOT_EXIST_ERROR);
        }
        registrationMapper.updateById(registration);
    }

    public Registration selectById(Integer id) {
        return registrationMapper.selectById(id);
    }

    public List<Registration> selectAll(Registration registration) {
        return registrationMapper.selectAll(registration);
    }

    public PageInfo<RegistrationForShow> selectPage(Registration registration, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Registration> registrations = registrationMapper.selectAll(registration);
        List<RegistrationForShow> registrationForShows = new ArrayList<>();
        for (Registration registrationReal : registrations) {
            Patient patient = patientMapper.selectById(registrationReal.getPatientId());
            Doctor doctor = doctorMapper.selectById(registrationReal.getDoctorId());
            Department department = departmentMapper.selectById(registrationReal.getDepartmentId());
            registrationForShows.add(new RegistrationForShow(registrationReal.getRegistrationId(), patient.getName(), doctor.getName(),
                    department.getType(), registrationReal.getAppointDate(), registrationReal.getStatus()));
        }
        return new PageInfo<RegistrationForShow>(registrationForShows);
    }
}
