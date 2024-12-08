package com.example.vitalis.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.vitalis.entity.Account;
import com.example.vitalis.entity.Doctor;
import com.example.vitalis.entity.Patient;
import com.example.vitalis.entity.PatientAccount;
import com.example.vitalis.exception.CustomException;
import com.example.vitalis.common.enums.ResultCodeEnum;
import com.example.vitalis.mapper.AccountMapper;
import com.example.vitalis.mapper.DoctorMapper;
import com.example.vitalis.mapper.PatientMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AccountService {

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private DoctorMapper doctorMapper;

    @Resource
    private PatientMapper patientMapper;

    // 登录
    public PatientAccount login(Account account) {
        Account account1 = accountMapper.selectById(account.getId());
        if (ObjectUtil.isNull(account1)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account1.getPassword().equals(account.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        Patient patient = patientMapper.selectById(account.getId());
        PatientAccount patientAccount = new PatientAccount();
        patientAccount.setByAccountAndPatient(account1, patient);
        return patientAccount;
    }

    // 注册
    public void register(Account account , Patient patient) {
        Account account1 = accountMapper.selectById(account.getId());
        if (ObjectUtil.isNotNull(account1)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        accountMapper.insert(account);
        switch (account.getRole()){
            case "PATIENT":
                patientMapper.insert(patient);
                break;
            default:
                throw new CustomException(ResultCodeEnum.PARAM_ROLE_ERROR);
        }
    }

    // 修改密码
    public void updatePassword(Account account) {
        Account account1 = accountMapper.selectById(account.getId());
        if (ObjectUtil.isNull(account1)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account1.getPassword().equals(account.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
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
