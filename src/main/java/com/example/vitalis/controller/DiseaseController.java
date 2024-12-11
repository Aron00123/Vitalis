package com.example.vitalis.controller;

import com.example.vitalis.common.Result;
import com.example.vitalis.entity.Disease;
import com.example.vitalis.entity.Disease;
import com.example.vitalis.service.DiseaseService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/delete")
    public Result deleteById(@RequestBody Map<String, Object> params) {
        diseaseService.deleteById((Integer) params.get("id"));
        return Result.success();
    }

    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestBody Map<String, Object> params) {
        List<Integer> ids = (List<Integer>) params.get("ids");
        diseaseService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping("/update")
    public Result updateById(@RequestBody Disease disease) {
        diseaseService.updateById(disease);
        return Result.success();
    }

    @PostMapping("/selectById")
    public Result selectById(@RequestBody Map<String, Object> params) {
        diseaseService.selectById((Integer) params.get("id"));
        return Result.success();
    }

    @PostMapping("/selectAll")
    public Result selectAll(Disease disease ) {
        List<Disease> list = diseaseService.selectAll(disease);
        return Result.success(list);
    }

    @PostMapping("/selectPage")
    public Result selectPage(@RequestBody Map<String, Object> searchMap) {
        Integer pageNum = (Integer) searchMap.get("pageNum");
        Integer pageSize = (Integer) searchMap.get("pageSize");
        Integer disId = null;
        String id = (String) searchMap.get("id");
        if (!id.isEmpty()) {
            disId = Integer.parseInt(id);
        }
        Disease disease = new Disease();
        disease.setId(disId);
        PageInfo<Disease> page = diseaseService.selectPage(disease, pageNum, pageSize);
        return Result.success(page);
    }

    @PostMapping("/querySearch")
    public Result querySearch(@RequestBody Map<String, Object> searchMap) {
        String queryString = (String) searchMap.get("queryString");
        if (queryString.isEmpty()) {
            List<Disease> list = new ArrayList<Disease>();
            return Result.success(list);
        }
        List<Disease> diseases = diseaseService.querySearch(queryString);
        return Result.success(diseases);
    }

    @PostMapping("/querySearchAdmin")
    public Result querySearchAdmin(@RequestBody Map<String, Object> searchMap) {
        Integer pageNum = (Integer) searchMap.get("pageNum");
        Integer pageSize = (Integer) searchMap.get("pageSize");
        String queryString = (String) searchMap.get("id");
        if (queryString.isEmpty()) {
            List<Disease> list = new ArrayList<Disease>();
            return Result.success(list);
        }
        PageInfo<Disease> diseases = diseaseService.querySearchAdmin(queryString, pageNum, pageSize);
        return Result.success(diseases);
    }
}
