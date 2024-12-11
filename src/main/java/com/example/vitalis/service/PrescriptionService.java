package com.example.vitalis.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.vitalis.entity.Doctor;
import com.example.vitalis.entity.Patient;
import com.example.vitalis.entity.Prescription;
import com.example.vitalis.entity.Registration;
import com.example.vitalis.exception.CustomException;
import com.example.vitalis.common.enums.ResultCodeEnum;
import com.example.vitalis.mapper.DoctorMapper;
import com.example.vitalis.mapper.PatientMapper;
import com.example.vitalis.mapper.PrescriptionMapper;
import com.example.vitalis.mapper.RegistrationMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrescriptionService {

    @Resource
    private PrescriptionMapper prescriptionMapper;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private RegistrationMapper registrationMapper;
    @Autowired
    private PatientMapper patientMapper;
    @Autowired
    private DoctorMapper doctorMapper;

    public void add(Prescription prescription) {
        Prescription prescriptionSave = prescriptionMapper.selectById(prescription.getPrescriptionId());
        if (prescriptionSave != null) {
            prescriptionMapper.updateById(prescription);
            return;
        }
        Registration registration = registrationService.selectById(prescription.getRegistrationId());
        String doctorId = registration.getDoctorId();
        String patientId = registration.getPatientId();
        prescription.setDoctorId(doctorId);
        prescription.setPatientId(patientId);
        prescriptionMapper.insert(prescription);
    }

    public void deleteById(Integer id) {
        prescriptionMapper.deleteById(id);
        Registration registration = registrationMapper.selectById(id);
        if (registration != null) {
            registrationMapper.deleteById(id);
        }
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids){
            prescriptionMapper.deleteById(id);
            Registration registration = registrationMapper.selectById(id);
            if (registration != null) {
                registrationMapper.deleteById(id);
            }
        }
    }

    public void updateById(Prescription prescription) {
        Prescription prescription1 = prescriptionMapper.selectById(prescription.getPrescriptionId());
        if (ObjectUtil.isNull(prescription1)) {
            throw new CustomException(ResultCodeEnum.PRESCRIPTION_NOT_EXIST_ERROR);
        }
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
        return PageInfo.of(prescriptions);
    }

    public PageInfo<Map<String, Object>> selectPageForAdmin(Prescription prescription, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Prescription> prescriptions = prescriptionMapper.selectAll(prescription);
        List<Map<String, Object>> params = new ArrayList<>();
        for (Prescription p : prescriptions) {
            Map<String, Object> map = new HashMap<>();
            map.put("prescription", p);
            String patientId = p.getPatientId();
            Patient patient = patientMapper.selectById(patientId);
            map.put("patient", patient);
            String doctorId = p.getDoctorId();
            Doctor doctor = doctorMapper.selectById(doctorId);
            map.put("doctor", doctor);
            params.add(map);
        }
        return PageInfo.of(params);
    }
}
