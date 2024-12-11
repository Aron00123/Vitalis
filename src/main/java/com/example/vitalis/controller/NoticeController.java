package com.example.vitalis.controller;

import com.example.vitalis.common.Result;
import com.example.vitalis.entity.Notice;
import com.example.vitalis.entity.Notice;
import com.example.vitalis.entity.Patient;
import com.example.vitalis.service.NoticeService;
import com.example.vitalis.service.PatientService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    @PostMapping("/add")
    public Result add(@RequestBody Notice notice) {
        noticeService.add(notice);
        return Result.success();
    }

    @PostMapping("/delete")
    public Result deleteById(@RequestBody Map<String, Object> params) {
        // 使用 @RequestBody Map<String, Object> 替代 @PathVariable
        String id = (String) params.get("id");
        noticeService.deleteById(id);
        return Result.success();
    }    

    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestBody Map<String, Object> params) {
        List<String> ids = (List<String>) params.get("ids");
        noticeService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping("/update")
    public Result updateById(@RequestBody Notice notice) {
        noticeService.updateById(notice);
        return Result.success();
    }

    @PostMapping("/selectById")
    public Result selectById(@RequestBody Map<String, Object> params) {
        // 使用 @RequestBody Map<String, Object> 替代 @PathVariable
        String id = (String) params.get("id");
        Notice notice = noticeService.selectById(id);
        return Result.success(notice);
    }


    @PostMapping("/selectAll")
    public Result selectAll(Notice notice) {
        List<Notice> list = noticeService.selectAll(notice);
        return Result.success(list);
    }
    
    @PostMapping("/selectPage")
    public Result selectPage(@RequestBody Map<String, Object> params) {
        // 获取 notice 参数，假设 params 中包含 notice 对象的相关字段
        Notice notice = new Notice();
        notice.setId((String) params.get("id")); // 假设 params 中有 id 字段，根据实际情况修改
        
        // 获取分页参数，提供默认值
        Integer pageNum = (Integer) params.getOrDefault("pageNum", 1);  // 默认值为 1
        Integer pageSize = (Integer) params.getOrDefault("pageSize", 10); // 默认值为 10
    
        // 调用 service 层进行分页查询
        PageInfo<Notice> page = noticeService.selectPage(notice, pageNum, pageSize);
        return Result.success(page);
    }

    @PostMapping("/querySearch")
    public Result querySearch(@RequestBody Map<String, Object> params) {
        String queryString = (String) params.get("queryString");
        List<Notice> list = noticeService.querySearch(queryString);
        return Result.success(list);
    }

    @PostMapping("/querySearchAdmin")
    public Result querySearchAdmin(@RequestBody Map<String, Object> params) {
        Integer pageNum = (Integer) params.get("pageNum");
        Integer pageSize = (Integer) params.get("pageSize");
        String queryString = (String) params.get("id");
        PageInfo<Notice> page = noticeService.querySearchAdmin(queryString, pageNum, pageSize);
        return Result.success(page);
    }
    
    
}
