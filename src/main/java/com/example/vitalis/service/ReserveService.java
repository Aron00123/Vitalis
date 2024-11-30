package com.example.vitalis.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.example.vitalis.common.enums.CallEnum;
import com.example.vitalis.common.enums.RoleEnum;
import com.example.vitalis.entity.Account;
import com.example.vitalis.entity.Doctor;
import com.example.vitalis.entity.Reserve;
import com.example.vitalis.entity.User;
import com.example.vitalis.exception.CustomException;
import com.example.vitalis.mapper.DoctorMapper;
import com.example.vitalis.mapper.ReserveMapper;
import com.example.vitalis.mapper.UserMapper;
import com.example.vitalis.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 预约挂号表业务处理
 **/
@Service
public class ReserveService {

    @Resource
    private ReserveMapper reserveMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private DoctorMapper doctorMapper;

    /**
     * 新增
     */
    public void add(Reserve reserve) {
        reserve.setTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        // 判断一下该用户当天有没有对该医生挂过号
        List<Reserve> reserves = reserveMapper.selectAll(reserve);
        if (CollectionUtil.isNotEmpty(reserves)) {
            throw new CustomException("-1", "您今天已经挂号该医生的号了，请勿重复挂号");
        }
        // 判断一下余额够不够
        User user = userMapper.selectById(reserve.getUserId());
        Doctor doctor = doctorMapper.selectById(reserve.getDoctorId());
        if (user.getAccount() < doctor.getPrice()) {
            throw new CustomException("-1", "您的余额不足，请在个人中心充值");
        }
        reserve.setStatus(CallEnum.STATUS_NO.status);
        reserveMapper.insert(reserve);

        // 扣除余额
        user.setAccount(user.getAccount() - doctor.getPrice());
        userMapper.updateById(user);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        Reserve reserve = reserveMapper.selectById(id);
        reserveMapper.deleteById(id);
        User user = userMapper.selectById(reserve.getUserId());
        Doctor doctor = doctorMapper.selectById(reserve.getDoctorId());
        user.setAccount(user.getAccount() + doctor.getPrice());
        userMapper.updateById(user);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            reserveMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Reserve reserve) {
        reserveMapper.updateById(reserve);
    }

    /**
     * 根据ID查询
     */
    public Reserve selectById(Integer id) {
        return reserveMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Reserve> selectAll(Reserve reserve) {
        return reserveMapper.selectAll(reserve);
    }

    /**
     * 分页查询
     */
    public PageInfo<Reserve> selectPage(Reserve reserve, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            reserve.setUserId(currentUser.getId());
        }
        if (RoleEnum.DOCTOR.name().equals(currentUser.getRole())) {
            reserve.setDoctorId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Reserve> list = reserveMapper.selectAll(reserve);
        return PageInfo.of(list);
    }

}