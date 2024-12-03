package com.example.vitalis.controller;

import com.example.vitalis.common.Result;
import com.example.vitalis.entity.Disease;
import com.example.vitalis.service.DiseaseService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/disease")
public class DiseaseController {

    @Resource
    private DiseaseService diseaseService;

    @PostMapping("/add")
    public Result add(@RequestBody Disease disease) {
        diseaseService.add(disease);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        diseaseService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        diseaseService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result updateById(@RequestBody Disease disease) {
        diseaseService.updateById(disease);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        diseaseService.selectById(id);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Disease disease ) {
        List<Disease> list = diseaseService.selectAll(disease);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Disease disease,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Disease> page = diseaseService.selectPage(disease, pageNum, pageSize);
        return Result.success(page);
    }
}
