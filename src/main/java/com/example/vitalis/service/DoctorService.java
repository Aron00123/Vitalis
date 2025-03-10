package com.example.vitalis.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.vitalis.common.enums.ResultCodeEnum;
import com.example.vitalis.entity.*;
import com.example.vitalis.exception.CustomException;
import com.example.vitalis.mapper.AccountMapper;
import com.example.vitalis.mapper.DepartmentMapper;
import com.example.vitalis.mapper.DoctorMapper;
import com.example.vitalis.mapper.RegistrationMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.print.Doc;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class DoctorService {

    @Resource
    private DoctorMapper doctorMapper;
    @Autowired
    private RegistrationMapper registrationMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private AccountMapper accountMapper;

    public void add(Doctor doctor) {
        doctorMapper.insert(doctor);
    }

    public void deleteById(String id) {
        doctorMapper.deleteById(id);
        Account account = accountMapper.selectById(id);
        if (account != null) {
            accountMapper.deleteById(id);
        }
    }

    public void deleteBatch(List<String> ids) {
        for (String id : ids) {
            doctorMapper.deleteById(id);
            Account account = accountMapper.selectById(id);
            if (account != null) {
                accountMapper.deleteById(id);
            }
        }
    }

    public void updateById(Doctor doctor) {
        Doctor doctor1 = doctorMapper.selectById(doctor.getId());
        if (ObjectUtil.isNull(doctor1)) {
            throw new CustomException(ResultCodeEnum.DOCTOR_NOT_EXIST_ERROR);
        }
        doctorMapper.updateById(doctor);
    }

    public Doctor selectById(String id) {
        return doctorMapper.selectById(id);
    }

    public List<Doctor> selectAll(Doctor doctor) {
        List<Doctor> doctorList = doctorMapper.selectAll(doctor);
        for (Doctor doctor1 : doctorList) {
            Department department = departmentMapper.selectById(doctor1.getDepartId());
            doctor1.setDepartmentName(department.getType());
        }
        return doctorList;
    }

    public PageInfo<Doctor> selectPage(Doctor doctor, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Doctor> doctors = doctorMapper.selectAll(doctor);
        for (Doctor doctor1 : doctors) {
            Department department = departmentMapper.selectById(doctor1.getDepartId());
            doctor1.setDepartmentName(department.getType());
        }

        return PageInfo.of(doctors);
    }


    /**
     * 医生挂号页面的分页查询
     */
    public PageInfo<Doctor> selectPage2(String dateStr, Integer departmentId, Integer pageNum, Integer pageSize) throws ParseException, CloneNotSupportedException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 将字符串解析为日期
        Date date = sdf.parse(dateStr);
        // 创建 Calendar 实例
        Calendar calendar = Calendar.getInstance();
        // 设置日期
        calendar.setTime(date);
        // 获取星期几（1=星期日, 2=星期一, ..., 7=星期六）
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // 将数字转换为对应的中文星期几
        String[] weekdays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        String weekday = weekdays[dayOfWeek % 7];
        PageHelper.startPage(pageNum, pageSize);
        //
        Doctor abDoctor = new Doctor();
        abDoctor.setDepartId(departmentId);
        List<Doctor> list = doctorMapper.selectAll(abDoctor);
        List<Doctor> finalDoctorList = new ArrayList<>();
        // 计算查出来的在诊医生剩余多少个号
        for (Doctor dbDoctor : list) {
            if (dbDoctor.getConsultDays() != null && dbDoctor.getConsultDays().contains(weekday)) {
                // 查询出来当天已经挂过该医生号的数量
                Registration registration = new Registration();
                registration.setDoctorId(dbDoctor.getId());
                registration.setAppointDate(dateStr);
                // 用总数量-已经挂过的号数量 = 剩余挂号数量
                List<Registration> registrationList = registrationMapper.selectAll(registration);
                if (dbDoctor.getConsultLimit() - registrationList.size() > -1) {
                    Doctor doctor = (Doctor) dbDoctor.clone();
                    doctor.setConsultLimit(dbDoctor.getConsultLimit() - registrationList.size());
                    finalDoctorList.add(doctor);
                }
            }
        }

        for (Doctor doctor1 : list) {
            Department department = departmentMapper.selectById(doctor1.getDepartId());
            doctor1.setDepartmentName(department.getType());
        }
        return PageInfo.of(finalDoctorList);
    }

    public List<Doctor> querySearch(String queryString) {
        Doctor doctor = new Doctor();
        List<Doctor> doctors = doctorMapper.selectAll(doctor);
        List<Doctor> doctors1 = new ArrayList<>();
        int cnt = 0;
        for (Doctor doctor1 : doctors) {
            if (cnt >= 10) {
                break;
            }
            if (doctor1.getName().contains(queryString)) {
                doctors1.add(doctor1);
                cnt += 1;
            }
        }
        return doctors1;
    }
    
    public PageInfo<Doctor> querySearchAdmin(String queryString, Integer pageNum, Integer pageSize) {
        Doctor doctor = new Doctor();
        doctor.setName(queryString);
        List<Doctor> doctors = doctorMapper.selectAll(doctor);
        List<Doctor> doctors1 = new ArrayList<>();
        for (Doctor doctor1: doctors) {
            if (doctor1.getName().contains(queryString)) {
                doctors1.add(doctor1);
            }
        }
        int total = doctors1.size(); // 总记录数
        int start = (pageNum - 1) * pageSize; // 起始索引
        int end = Math.min(start + pageSize, total); // 结束索引

        // 获取当前页数据
        List<Doctor> pageList = doctors1.subList(start, end);

        // 封装到 PageInfo 中
        PageInfo<Doctor> pageInfo = new PageInfo<>(pageList);
        pageInfo.setTotal(total); // 总记录数
        pageInfo.setPageNum(pageNum); // 当前页
        pageInfo.setPageSize(pageSize); // 每页大小
        pageInfo.setPages((total + pageSize - 1) / pageSize); // 总页数

        return pageInfo;
    }
}
