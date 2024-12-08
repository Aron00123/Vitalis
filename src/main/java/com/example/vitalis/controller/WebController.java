package com.example.vitalis.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.vitalis.common.Result;
import com.example.vitalis.common.enums.ResultCodeEnum;
import com.example.vitalis.common.enums.RoleEnum;
import com.example.vitalis.entity.Account;
import com.example.vitalis.entity.Doctor;
import com.example.vitalis.entity.Patient;
import com.example.vitalis.entity.PatientAccount;
import com.example.vitalis.service.AccountService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;


/**
 * 基础前端接口
 */
@RestController
public class WebController {

    @Resource
    private AccountService accountService;

    @GetMapping("/")
    public Result hello() {
        return Result.success("访问成功");
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        if (ObjectUtil.isEmpty(account.getId()) || ObjectUtil.isEmpty(account.getPassword())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }

        return Result.success(accountService.login(account));
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody PatientAccount patient) {
        if (StrUtil.isBlank(patient.getId()) || StrUtil.isBlank(patient.getPassword())
                || ObjectUtil.isEmpty(patient.getRole())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        Account account = new Account();
        account.setId(patient.getId());account.setPassword(patient.getPassword());account.setNewPassword("");account.setRole(patient.getRole());
        Patient patient1 = new Patient(patient.getId(), patient.getGender(), patient.getName(), patient.getAge(), patient.getAddress(), patient.getPhone(), patient.getEmergencyPhone());
        accountService.register(account, patient1);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody PatientAccount account) {
        if (StrUtil.isBlank(account.getId()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getNewPassword())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        accountService.updatePassword(account.getAccount());
        return Result.success();
    }

}
