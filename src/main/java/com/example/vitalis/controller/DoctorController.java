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

    @PostMapping("/delete")
    public Result deleteById(@RequestBody Map<String, Object> params) {
        // 修改点：使用 @RequestBody Map<String, Object> 替代 @PathVariable
        String id = (String) params.get("id");
        doctorService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestBody Map<String, Object> params) {
        List<String> ids = (List<String>) params.get("ids");
        doctorService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping("/update")
    public Result updateById(@RequestBody Doctor doctor) {
        doctorService.updateById(doctor);
        return Result.success();
    }

    @PostMapping("/selectById")
    public Result selectById(@RequestBody Doctor doctor) {
        Doctor getDoctor = doctorService.selectById(doctor.getId());
        return Result.success(getDoctor);
    }

    @PostMapping("/selectAll")
    public Result selectAll(Doctor doctor) {
        List<Doctor> list = doctorService.selectAll(doctor);
        return Result.success(list);
    }

    @PostMapping("/selectPage")
    public Result selectPage(@RequestBody Map<String, Object> params) {
        // 获取 doctor 参数
        Doctor doctor = new Doctor();
        String id = (String) params.get("userId");
        if (id == null || id.isEmpty()) {
            id = null;
        }
        doctor.setId(id); // 假设 params 中包含 id，可以根据实际情况修改
        
        // 获取分页参数
        Integer pageNum = (Integer) params.getOrDefault("pageNum", 1);  // 默认值为 1
        Integer pageSize = (Integer) params.getOrDefault("pageSize", 10); // 默认值为 10
        // 调用 service 层获取分页结果
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

    @PostMapping("/querySearch")
    public Result querySearch(@RequestBody Map<String, Object> params) {
        String queryString = (String) params.get("queryString");
        List<Doctor> list = doctorService.querySearch(queryString);
        return Result.success(list);
    }

    @PostMapping("/querySearchAdmin")
    public Result querySearchAdmin(@RequestBody Map<String, Object> params) {
        Integer pageNum = (Integer) params.get("pageNum");
        Integer pageSize = (Integer) params.get("pageSize");
        String queryString = (String) params.get("id");
        PageInfo<Doctor> page = doctorService.querySearchAdmin(queryString, pageNum, pageSize);
        return Result.success(page);
    }
}
