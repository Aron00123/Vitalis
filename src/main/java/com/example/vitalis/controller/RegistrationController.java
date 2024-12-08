package com.example.vitalis.controller;

import com.example.vitalis.common.Result;
import com.example.vitalis.entity.Patient;
import com.example.vitalis.entity.Registration;
import com.example.vitalis.entity.RegistrationForShow;
import com.example.vitalis.service.RegistrationService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Resource
    private RegistrationService registrationService;

    @PostMapping("/add")
    public Result add(@RequestBody Registration registration) {
        registration.setStatus("未就诊");
        registrationService.add(registration);
        return Result.success();
    }

    @PostMapping("/delete")
    public Result deleteById(@RequestBody Map<String, Object> params) {
        Integer id = (Integer) params.get("id");
        registrationService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        registrationService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping("/update")
    public Result updateById(@RequestBody Registration registration) {
        registrationService.updateById(registration);
        return Result.success();
    }

    @PostMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Registration registration = registrationService.selectById(id);
        return Result.success(registration);
    }

    @PostMapping("/selectAll")
    public Result selectAll(Registration registration) {
        List<Registration> list = registrationService.selectAll(registration);
        return Result.success(list);
    }

    @PostMapping("/selectPage")
    public Result selectPage(@RequestBody Map<String, Object> params) {
        String patientId = (String) params.get("patientId");
        Integer pageNum = (Integer) params.get("pageNum");
        Integer pageSize = (Integer) params.get("pageSize");
        Registration registration = new Registration();
        registration.setPatientId(patientId);
        PageInfo<RegistrationForShow> page = registrationService.selectPage(registration, pageNum, pageSize);
        return Result.success(page);
    }
}
