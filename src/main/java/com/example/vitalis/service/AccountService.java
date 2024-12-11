package com.example.vitalis.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.vitalis.entity.Account;
import com.example.vitalis.entity.Doctor;
import com.example.vitalis.entity.Patient;
import com.example.vitalis.exception.CustomException;
import com.example.vitalis.common.enums.ResultCodeEnum;
import com.example.vitalis.mapper.AccountMapper;
import com.example.vitalis.mapper.DoctorMapper;
import com.example.vitalis.mapper.PatientMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.HashMap;
import java.util.Objects;

@Service
public class AccountService {

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private DoctorMapper doctorMapper;

    @Resource
    private PatientMapper patientMapper;

    // 登录
    public HashMap<String, Object> login(Account account) {
        Account account1 = accountMapper.selectById(account.getId());
        if (ObjectUtil.isNull(account1)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account1.getPassword().equals(account.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }

        HashMap<String, Object> accountInfo = new HashMap<String, Object>();
        accountInfo.put("account", account1);
        if (Objects.equals(account1.getRole(), "ADMIN")) {
            HashMap<String, Object> temp = new HashMap<>();
            temp.put("id", account.getId());
            accountInfo.put("roleInfo", temp);
            return accountInfo;
        }
        switch (account1.getRole()) {
            case "DOCTOR":
                accountInfo.put("roleInfo", doctorMapper.selectById(account.getId()));
                break;
            case "PATIENT":
                accountInfo.put("roleInfo", patientMapper.selectById(account.getId()));
                break;
            default:
                break;
        }
        return accountInfo;
    }

    // 注册
    public void register(Account account , Patient patient) {
        Account account1 = accountMapper.selectById(account.getId());
        if (ObjectUtil.isNotNull(account1)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        accountMapper.insert(account);
        if (Objects.equals(account.getRole(), "PATIENT")) {
            patientMapper.insert(patient);
        }
    }

    // 修改密码
    public void updatePassword(Account account) {
        Account account1 = accountMapper.selectById(account.getId());
        if (ObjectUtil.isNull(account1)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        accountMapper.updateById(account);
    }

    public Account selectById(String id) {
        Account account = accountMapper.selectById(id);
        if (ObjectUtil.isNull(account)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        return account;

    }

    public List<Account> selectAll(Account account) {
        return accountMapper.selectAll(account);
    }

    public void deleteById(String id) {
        accountMapper.deleteById(id);
    }


}
