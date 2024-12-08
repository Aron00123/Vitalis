package com.example.vitalis.controller;

import com.example.vitalis.common.Result;
import com.example.vitalis.entity.Doctor;
import com.example.vitalis.service.DoctorService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Resource
    private DoctorService doctorService;

    @PostMapping("/add")
    public Result add(@RequestBody Doctor doctor) {
        doctorService.add(doctor);
        return Result.success();
    }

    @PostMapping("/delete/{id}")
    public Result deleteById(@PathVariable String id) {
        doctorService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<String> ids) {
        doctorService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping("/update")
    public Result updateById(@RequestBody Doctor doctor) {
        doctorService.updateById(doctor);
        return Result.success();
    }

    @PostMapping("/selectById/{id}")
    public Result selectById(@PathVariable String id) {
        Doctor doctor = doctorService.selectById(id);
        return Result.success(doctor);
    }

    @PostMapping("/selectAll")
    public Result selectAll(Doctor doctor) {
        List<Doctor> list = doctorService.selectAll(doctor);
        return Result.success(list);
    }

    @PostMapping("/selectPage")
    public Result selectPage(Doctor doctor,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Doctor> page = doctorService.selectPage(doctor, pageNum, pageSize);
        return Result.success(page);
    }

    @PostMapping("/selectPage2")
    public Result selectPage2(@RequestBody Map<String, Object> params) throws ParseException, CloneNotSupportedException {
        String dateStr = (String) params.get("dateStr");
        Integer departmentId = (Integer) params.get("departmentId");
        Integer pageNum = (Integer) params.get("pageNum");
        Integer pageSize = (Integer) params.get("pageSize");

        PageInfo<Doctor> page = doctorService.selectPage2(dateStr, departmentId, pageNum, pageSize);
        return Result.success(page);
    }
}
