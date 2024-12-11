package com.example.vitalis.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.vitalis.common.enums.ResultCodeEnum;
import com.example.vitalis.entity.*;
import com.example.vitalis.exception.CustomException;
import com.example.vitalis.mapper.*;
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
    @Autowired
    private PrescriptionMapper prescriptionMapper;

    public int add(Registration registration) {
        Registration registrationCheck = new Registration();
        registrationCheck.setPatientId(registration.getPatientId());
        registrationCheck.setDoctorId(registration.getDoctorId());
        registrationCheck.setAppointDate(registration.getAppointDate());
        List<Registration> registrationListForCheck = registrationMapper.selectAll(registrationCheck);
        if (!registrationListForCheck.isEmpty()) {
            return 1;
        }
        //
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
        return 0;
    }

    public void deleteById(Integer id) {
        Prescription prescription = prescriptionMapper.selectById(id);
        if (prescription != null) {
            prescriptionMapper.deleteById(id);
        }
        registrationMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids){
            Prescription prescription = prescriptionMapper.selectById(id);
            if (prescription != null) {
                prescriptionMapper.deleteById(id);
            }
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

    public PageInfo<RegistrationForShow> selectPage(Registration registration, Integer pageNum, Integer pageSize, String status) {
        List<Registration> registrations = registrationMapper.selectAll(registration);
        List<RegistrationForShow> registrationForShows = new ArrayList<>();
        if (status == null) {
            if (registration.getDoctorId() != null) {
                for (Registration registrationReal : registrations) {
                    if (registrationReal.getDoctorId().equals(registration.getDoctorId())) {
                        Patient patient = patientMapper.selectById(registrationReal.getPatientId());
                        Doctor doctor = doctorMapper.selectById(registrationReal.getDoctorId());
                        Department department = departmentMapper.selectById(registrationReal.getDepartmentId());
                        registrationForShows.add(new RegistrationForShow(registrationReal.getRegistrationId(), patient.getName(), doctor.getName(),
                                department.getType(), registrationReal.getAppointDate(), registrationReal.getStatus()));
                    }
                }
            }
            else if (registration.getPatientId() != null) {
                for (Registration registrationReal : registrations) {
                    if (registrationReal.getPatientId().equals(registration.getPatientId())) {
                        Patient patient = patientMapper.selectById(registrationReal.getPatientId());
                        Doctor doctor = doctorMapper.selectById(registrationReal.getDoctorId());
                        Department department = departmentMapper.selectById(registrationReal.getDepartmentId());
                        registrationForShows.add(new RegistrationForShow(registrationReal.getRegistrationId(), patient.getName(), doctor.getName(),
                                department.getType(), registrationReal.getAppointDate(), registrationReal.getStatus()));
                    }
                }
            }
        }
        else {
            if (registration.getDoctorId() != null) {
                for (Registration registrationReal : registrations) {
                    if (registrationReal.getDoctorId().equals(registration.getDoctorId()) && registrationReal.getStatus().equals(status)) {
                        Patient patient = patientMapper.selectById(registrationReal.getPatientId());
                        Doctor doctor = doctorMapper.selectById(registrationReal.getDoctorId());
                        Department department = departmentMapper.selectById(registrationReal.getDepartmentId());
                        registrationForShows.add(new RegistrationForShow(registrationReal.getRegistrationId(), patient.getName(), doctor.getName(),
                                department.getType(), registrationReal.getAppointDate(), registrationReal.getStatus()));
                    }
                }
            }
            else if (registration.getPatientId() != null) {
                for (Registration registrationReal : registrations) {
                    if (registrationReal.getPatientId().equals(registration.getPatientId()) && registrationReal.getStatus().equals(status)) {
                        Patient patient = patientMapper.selectById(registrationReal.getPatientId());
                        Doctor doctor = doctorMapper.selectById(registrationReal.getDoctorId());
                        Department department = departmentMapper.selectById(registrationReal.getDepartmentId());
                        registrationForShows.add(new RegistrationForShow(registrationReal.getRegistrationId(), patient.getName(), doctor.getName(),
                                department.getType(), registrationReal.getAppointDate(), registrationReal.getStatus()));
                    }
                }
            } else {
                List<Registration> registrations1 = registrationMapper.selectAll(registration);
                for (Registration registrationReal : registrations1) {
                    if (registrationReal.getStatus().equals(status)) {
                        Patient patient = patientMapper.selectById(registrationReal.getPatientId());
                        Doctor doctor = doctorMapper.selectById(registrationReal.getDoctorId());
                        Department department = departmentMapper.selectById(registrationReal.getDepartmentId());
                        registrationForShows.add(new RegistrationForShow(registrationReal.getRegistrationId(), patient.getName(), doctor.getName(),
                                department.getType(), registrationReal.getAppointDate(), registrationReal.getStatus()));
                    }
                }
            }
        }

        int total = registrationForShows.size(); // 总记录数
        int start = (pageNum - 1) * pageSize; // 起始索引
        int end = Math.min(start + pageSize, total); // 结束索引

        // 获取当前页数据
        List<RegistrationForShow> pageList = registrationForShows.subList(start, end);

        // 封装到 PageInfo 中
        PageInfo<RegistrationForShow> pageInfo = new PageInfo<>(pageList);
        pageInfo.setTotal(total); // 总记录数
        pageInfo.setPageNum(pageNum); // 当前页
        pageInfo.setPageSize(pageSize); // 每页大小
        pageInfo.setPages((total + pageSize - 1) / pageSize); // 总页数

        return pageInfo;
    }

    public PageInfo<RegistrationForShow> selectPage2(Integer pageNum, Integer pageSize, String status) {
        PageHelper.startPage(pageNum, pageSize);
        Registration registrationStatus = new Registration();
        registrationStatus.setStatus(status);
        List<Registration> registrations = registrationMapper.selectAll(registrationStatus);
        List<RegistrationForShow> registrationForShows = new ArrayList<>();
        for (Registration registration : registrations) {
            Patient patient = patientMapper.selectById(registration.getPatientId());
            Doctor doctor = doctorMapper.selectById(registration.getDoctorId());
            Department department = departmentMapper.selectById(registration.getDepartmentId());
            registrationForShows.add(new RegistrationForShow(registration.getRegistrationId(), patient.getName(), doctor.getName(),
                    department.getType(), registration.getAppointDate(), registration.getStatus()));
        }
        return PageInfo.of(registrationForShows);
    }

    public PageInfo<RegistrationForShow> selectAllWithoutId(Registration registration, Integer pageNum, Integer pageSize, String status) {

        PageHelper.startPage(pageNum, pageSize);
        Registration newRegistration = new Registration();
        newRegistration.setDoctorId(registration.getDoctorId());
        List<Registration> registrations = registrationMapper.selectAll(newRegistration);
        List<RegistrationForShow> registrationForShows = new ArrayList<>();
        Patient patient1 = patientMapper.selectById(registration.getPatientId());
        Doctor doctor1 = doctorMapper.selectById(registration.getDoctorId());
        Department department1 = departmentMapper.selectById(registration.getDepartmentId());
        registrationForShows.add(new RegistrationForShow(registration.getRegistrationId(), patient1.getName(), doctor1.getName(), department1.getType(), registration.getAppointDate(), registration.getStatus()));
        for (Registration registrationReal : registrations) {
            if (registrationReal.getDoctorId().equals(registration.getDoctorId()) && !registrationReal.getRegistrationId().equals(registration.getRegistrationId()) && registrationReal.getStatus().equals(status)) {
                Patient patient = patientMapper.selectById(registrationReal.getPatientId());
                Doctor doctor = doctorMapper.selectById(registrationReal.getDoctorId());
                Department department = departmentMapper.selectById(registrationReal.getDepartmentId());
                registrationForShows.add(new RegistrationForShow(registrationReal.getRegistrationId(), patient.getName(), doctor.getName(),
                        department.getType(), registrationReal.getAppointDate(), registrationReal.getStatus()));
            }
        }
        return PageInfo.of(registrationForShows);

    }

    public void updateByIdAdmin(Registration registration, String status) {
        Registration registration1 = registrationMapper.selectById(registration.getRegistrationId());
        if (!registration1.getStatus().equals(status)) {
            registration1.setStatus(status);
        }
        if (registration1.getStatus().equals("未就诊")) {
            Prescription prescription = prescriptionMapper.selectById(registration1.getRegistrationId());
            if (prescription != null) {
                prescriptionMapper.deleteById(registration1.getRegistrationId());
            }
        }
        registrationMapper.updateById(registration1);
    }
}
