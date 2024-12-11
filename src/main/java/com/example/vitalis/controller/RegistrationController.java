package com.example.vitalis.controller;

import com.example.vitalis.common.Result;
import com.example.vitalis.entity.Account;
import com.example.vitalis.entity.Patient;
import com.example.vitalis.entity.Registration;
import com.example.vitalis.entity.RegistrationForShow;
import com.example.vitalis.service.AccountService;
import com.example.vitalis.service.RegistrationService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Resource
    private RegistrationService registrationService;
    @Autowired
    private AccountService accountService;

    @PostMapping("/add")
    public Result add(@RequestBody Registration registration) {
        registration.setStatus("未就诊");
        return Result.success(registrationService.add(registration));
    }

    @PostMapping("/delete")
    public Result deleteById(@RequestBody Map<String, Object> params) {
        Integer id = (Integer) params.get("id");
        registrationService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestBody Map<String, Object> params) {
        List<Integer> ids = (List<Integer>) params.get("ids");
        registrationService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping("/update")
    public Result updateById(@RequestBody Map<String ,Object>params) {
        Integer id = (Integer) params.get("id");
        Registration registration = registrationService.selectById(id);
        String status = (String)params.get("status");
        Integer isAdmin = (Integer) params.get("isAdmin");
        if (isAdmin == 1) {
            registrationService.updateByIdAdmin(registration, status);
        } else {
            registration.setStatus(status);
            registrationService.updateById(registration);
        }
        return Result.success();
    }

    @PostMapping("/selectById")
    public Result selectById(@RequestBody Map<String, Object> params) {
        Registration registration = registrationService.selectById(Integer.parseInt((String) params.get("registrationId")));
        return Result.success(registration);
    }

    @PostMapping("/selectByIdWithPage")
    public Result selectByIdWithPage(@RequestBody Map<String, Object> params) {
        Integer pageNum = (Integer) params.get("pageNum");
        Integer pageSize = (Integer) params.get("pageSize");
        Registration registration = registrationService.selectById(Integer.parseInt((String) params.get("registrationId")));
        String status = (String) params.get("status");

        PageInfo<RegistrationForShow> page = registrationService.selectAllWithoutId(registration, pageNum, pageSize, status);
        return Result.success(page);
    }

    @PostMapping("/selectAll")
    public Result selectAll(Registration registration) {
        List<Registration> list = registrationService.selectAll(registration);
        return Result.success(list);
    }

    @PostMapping("/selectPage")
    public Result selectPage(@RequestBody Map<String, Object> params) {
        String id = (String) params.get("userId");
        Integer pageNum = (Integer) params.get("pageNum");
        Integer pageSize = (Integer) params.get("pageSize");
        String status = (String) params.get("status");
        Registration registration = new Registration();
        if (id != null) {
            Account account = accountService.selectById(id);
            switch (account.getRole()) {
                case "PATIENT":
                    registration.setPatientId(id);
                    break;
                case "DOCTOR":
                    registration.setDoctorId(id);
                    break;
            }
        } else {
            PageInfo<RegistrationForShow> page1 = registrationService.selectPage2(pageNum, pageSize, status);
            return Result.success(page1);
        }
        PageInfo<RegistrationForShow> page = registrationService.selectPage(registration, pageNum, pageSize, status);
        return Result.success(page);
    }
}
