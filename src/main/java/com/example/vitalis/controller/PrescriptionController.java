package com.example.vitalis.controller;

import com.example.vitalis.common.Result;
import com.example.vitalis.entity.Prescription;
import com.example.vitalis.service.PrescriptionService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/prescription")
public class PrescriptionController {

    @Resource
    private PrescriptionService prescriptionService;

    @PostMapping("/add")
    public Result add(@RequestBody Prescription prescription) {
        prescriptionService.add(prescription);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        prescriptionService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        prescriptionService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Prescription prescription) {
        prescriptionService.updateById(prescription);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        prescriptionService.selectById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Prescription prescription) {
        List<Prescription> list = prescriptionService.selectAll(prescription);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Prescription prescription,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Prescription> page = prescriptionService.selectPage(prescription, pageNum, pageSize);
        return Result.success(page);
    }
}
