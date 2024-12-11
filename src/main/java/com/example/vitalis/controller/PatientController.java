package com.example.vitalis.controller;

import com.example.vitalis.common.Result;
import com.example.vitalis.entity.Patient;
import com.example.vitalis.entity.Patient;
import com.example.vitalis.service.PatientService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Resource
    private PatientService patientService;

    @PostMapping("/add")
    public Result add(@RequestBody Patient patient) {
        patientService.add(patient);
        return Result.success();
    }

    @PostMapping("/delete")
    public Result deleteById(@RequestBody Map<String, Object> params) {
        // 使用 @RequestBody Map<String, Object> 替代 @PathVariable
        String id = (String) params.get("id");
        patientService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestBody Map<String, Object> params) {
        List<String> ids = (List<String>) params.get("ids");
        patientService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping("/update")
    public Result updateById(@RequestBody Patient patient) {
        patientService.updateById(patient);
        return Result.success();
    }

    @PostMapping("/selectById")
    public Result selectById(@RequestBody Map<String, Object> params) {
        // 使用 @RequestBody Map<String, Object> 替代 @PathVariable
        String id = (String) params.get("id");
        Patient patient = patientService.selectById(id);
        return Result.success(patient);
    }    

    @PostMapping("/selectAll")
    public Result selectAll(Patient patient ) {
        List<Patient> list = patientService.selectAll(patient);
        return Result.success(list);
    }

    @PostMapping("/selectPage")
    public Result selectPage(@RequestBody Map<String, Object> params) {
        // 获取 patient 参数，假设 params 中包含 patient 对象的相关字段
        String id = (String) params.get("userId");
        if (id.isEmpty()) {
            id = null;
        }
        Patient patient = patientService.selectById(id);
        
        // 获取分页参数，提供默认值
        Integer pageNum = (Integer) params.getOrDefault("pageNum", 1);  // 默认值为 1
        Integer pageSize = (Integer) params.getOrDefault("pageSize", 10); // 默认值为 10
    
        // 调用 service 层进行分页查询
        PageInfo<Patient> page = patientService.selectPage(patient, pageNum, pageSize);
        return Result.success(page);
    }

    @PostMapping("/querySearch")
    public Result querySearch(@RequestBody Map<String, Object> params) {
        String queryString = (String) params.get("queryString");
        List<Patient> list = patientService.querySearch(queryString);
        return Result.success(list);
    }

    @PostMapping("/querySearchAdmin")
    public Result querySearchAdmin(@RequestBody Map<String, Object> params) {
        Integer pageNum = (Integer) params.get("pageNum");
        Integer pageSize = (Integer) params.get("pageSize");
        String queryString = (String) params.get("id");
        PageInfo<Patient> page = patientService.querySearchAdmin(queryString, pageNum, pageSize);
        return Result.success(page);
    }
    
}
