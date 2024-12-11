package com.example.vitalis.controller;

import com.example.vitalis.common.Result;
import com.example.vitalis.entity.Department;
import com.example.vitalis.entity.Department;
import com.example.vitalis.entity.Patient;
import com.example.vitalis.service.DepartmentService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;

    @PostMapping("/add")
    public Result add(@RequestBody Department department) {
        departmentService.add(department);
        return Result.success();
    }

    @PostMapping("/delete")
    public Result deleteById(@RequestBody Map<String, Object> params) {
        // 修改点：使用 @RequestBody Map<String, Object> 替代 @PathVariable
        Integer id = (Integer) params.get("id");
        departmentService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestBody Map<String, Object> params) {
        List<Integer> ids = (List<Integer>) params.get("ids");
        departmentService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping("/update")
    public Result updateById(@RequestBody Department department) {
        departmentService.updateById(department);
        return Result.success();
    }

    @PostMapping("/selectById")
    public Result selectById(@RequestBody Map<String, Object> params) {
        // 修改点：使用 @RequestBody Map<String, Object> 替代 @PathVariable
        Integer id = (Integer) params.get("id");
        departmentService.selectById(id);
        return Result.success();
    }

    @PostMapping("/selectAll")
    public Result selectAll(Department department ) {
        List<Department> list = departmentService.selectAll(department);
        return Result.success(list);
    }

    @PostMapping("/selectPage")
    public Result selectPage(@RequestBody Map<String, Object> params) {
        // 修改点：使用 @RequestBody Map<String, Object> 替代多个参数
        Department department = new Department();
        department.setDepartId((Integer) params.get("id")); // 确保 params 中有 id 字段
        Integer pageNum = (Integer) params.get("pageNum");
        Integer pageSize = (Integer) params.get("pageSize");
        
        PageInfo<Department> page = departmentService.selectPage(department, pageNum, pageSize);
        return Result.success(page);
    }

    @PostMapping("/querySearch")
    public Result querySearch(@RequestBody Map<String, Object> params) {
        String queryString = (String) params.get("queryString");
        List<Department> list = departmentService.querySearch(queryString);
        return Result.success(list);
    }

    @PostMapping("/querySearchAdmin")
    public Result querySearchAdmin(@RequestBody Map<String, Object> params) {
        Integer pageNum = (Integer) params.get("pageNum");
        Integer pageSize = (Integer) params.get("pageSize");
        String queryString = (String) params.get("id");
        PageInfo<Department> page = departmentService.querySearchAdmin(queryString, pageNum, pageSize);
        return Result.success(page);
    }
}
