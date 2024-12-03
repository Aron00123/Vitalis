package com.example.vitalis.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.vitalis.entity.Account;
import com.example.vitalis.exception.CustomException;
import com.example.vitalis.common.enums.ResultCodeEnum;
import com.example.vitalis.mapper.AccountMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AccountService {

    @Resource
    private AccountMapper accountMapper;

    // 登录
    public Account login(Account account) {
        Account account1 = accountMapper.selectById(account.getId());
        if (ObjectUtil.isNull(account1)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account1.getPassword().equals(account.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        return account1;
    }

    // 注册
    public void register(Account account) {
        Account account1 = accountMapper.selectById(account.getId());
        if (ObjectUtil.isNotNull(account1)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        accountMapper.insert(account);
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
}
