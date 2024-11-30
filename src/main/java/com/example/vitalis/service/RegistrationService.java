package com.example.vitalis.service;

import cn.hutool.core.collection.CollectionUtil;
import com.example.vitalis.common.enums.RoleEnum;
import com.example.vitalis.entity.Account;
import com.example.vitalis.entity.Registration;
import com.example.vitalis.mapper.RegistrationMapper;
import com.example.vitalis.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 住院登记表业务处理
 **/
@Service
public class RegistrationService {

    @Resource
    private RegistrationMapper registrationMapper;

    /**
     * 新增
     */
    public void add(Registration registration) {
        // 判断该患者是否正在住院中，如果是，不需要生成一条新的住院记录，如果已出院或者没有住院记录，那么就生成一条新的住院记录
        List<Registration> registrations = registrationMapper.selectAll(registration);
        if (CollectionUtil.isEmpty(registrations)) {
            registrationMapper.insert(registration);
        }
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        registrationMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            registrationMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Registration registration) {
        registrationMapper.updateById(registration);
    }

    /**
     * 根据ID查询
     */
    public Registration selectById(Integer id) {
        return registrationMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Registration> selectAll(Registration registration) {
        return registrationMapper.selectAll(registration);
    }

    /**
     * 分页查询
     */
    public PageInfo<Registration> selectPage(Registration registration, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            registration.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Registration> list = registrationMapper.selectAll(registration);
        return PageInfo.of(list);
    }

}