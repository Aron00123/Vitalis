package com.example.vitalis.controller;

import com.example.vitalis.common.Result;
import com.example.vitalis.entity.Medicine;
import com.example.vitalis.service.MedicineService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/medicine")
public class MedicineController {
    @Resource
    private MedicineService medicineService;

    @PostMapping("/add")
    public Result add(@RequestBody Medicine medicine) {
        medicineService.add(medicine);
        return Result.success();
    }

    @PostMapping("/delete")
    public Result deleteById(@RequestBody Map<String, Object> params) {
        // 修改点：使用 @RequestBody Map<String, Object> 替代 @PathVariable
        String id =  (String) params.get("id");
        medicineService.deleteById(id);
        return Result.success();
    }
    

    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestBody Map<String, Object> params) {
        List<String> ids = (List<String>) params.get("ids");
        medicineService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping("/update")
    public Result updateById(@RequestBody Medicine medicine) {
        medicineService.updateById(medicine);
        return Result.success();
    }

    @PostMapping("/selectById")
    public Result selectById(@RequestBody Map<String, Object> params) {
        // 修改点：使用 @RequestBody Map<String, Object> 替代 @PathVariable
        String id = (String) params.get("id");
        medicineService.selectById(id);
        return Result.success();
    }    

    @PostMapping("/selectAll")
    public Result selectAll(Medicine medicine ) {
        List<Medicine> list = medicineService.selectAll(medicine);
        return Result.success(list);
    }

    @PostMapping("/selectPage")
    public Result selectPage(@RequestBody Map<String, Object> searchMap) {
        Integer pageNum = (Integer) searchMap.get("pageNum");
        Integer pageSize = (Integer) searchMap.get("pageSize");
        String id = (String) searchMap.get("id");
        if (id.isEmpty()) {
            id = null;
        }
        Medicine medicine = new Medicine();
        medicine.setId(id);
        PageInfo<Medicine> page = medicineService.selectPage(medicine, pageNum, pageSize);
        return Result.success(page);
    }

    @PostMapping("/querySearch")
    public Result querySearch(@RequestBody Map<String, Object> searchMap) {
        String queryString = (String) searchMap.get("queryString");
        if (queryString.isEmpty()) {
            List<Medicine> list = new ArrayList<Medicine>();
            return Result.success(list);
        }
        List<Medicine> medicines = medicineService.querySearch(queryString);
        return Result.success(medicines);
    }

    @PostMapping("/querySearchAdmin")
    public Result querySearchAdmin(@RequestBody Map<String, Object> searchMap) {
        Integer pageNum = (Integer) searchMap.get("pageNum");
        Integer pageSize = (Integer) searchMap.get("pageSize");
        String queryString = (String) searchMap.get("id");
        if (queryString.isEmpty()) {
            List<Medicine> list = new ArrayList<Medicine>();
            return Result.success(list);
        }
        PageInfo<Medicine> medicines = medicineService.querySearchAdmin(queryString, pageNum, pageSize);
        return Result.success(medicines);
    }
}
