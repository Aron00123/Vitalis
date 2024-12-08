package com.example.vitalis.controller;

import com.example.vitalis.common.Result;
import com.example.vitalis.entity.Patient;
import com.example.vitalis.service.PatientService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Resource
    private PatientService patientService;

    @PostMapping("/add")
    public Result add(@RequestBody Patient patient) {
        patientService.add(patient);
        return Result.success();
    }

    @PostMapping("/delete/{id}")
    public Result deleteById(@PathVariable String id) {
        patientService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<String> ids) {
        patientService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping("/update")
    public Result updateById(@RequestBody Patient patient) {
        patientService.updateById(patient);
        return Result.success();
    }

    @PostMapping("/selectById/{id}")
    public Result selectById(@PathVariable String id) {
        Patient patient = patientService.selectById(id);
        return Result.success(patient);
    }

    @PostMapping("/selectAll")
    public Result selectAll(Patient patient ) {
        List<Patient> list = patientService.selectAll(patient);
        return Result.success(list);
    }

    @PostMapping("/selectPage")
    public Result selectPage(Patient patient,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Patient> page = patientService.selectPage(patient, pageNum, pageSize);
        return Result.success(page);
    }
}
