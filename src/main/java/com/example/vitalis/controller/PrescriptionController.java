package com.example.vitalis.controller;

import com.example.vitalis.common.Result;
import com.example.vitalis.entity.Doctor;
import com.example.vitalis.entity.Patient;
import com.example.vitalis.entity.Prescription;
import com.example.vitalis.service.DoctorService;
import com.example.vitalis.service.PatientService;
import com.example.vitalis.service.PrescriptionService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prescription")
public class PrescriptionController {

    @Resource
    private PrescriptionService prescriptionService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private DoctorService doctorService;

    @PostMapping("/add")
    public Result add(@RequestBody Prescription prescription) {
        prescriptionService.add(prescription);
        return Result.success();
    }

    @PostMapping("/delete")
    public Result deleteById(@RequestBody Map<String, Object> params) {
        // 使用 @RequestBody Map<String, Object> 替代 @PathVariable
        Integer id = (Integer) params.get("id");
        prescriptionService.deleteById(id);
        return Result.success();
    }
    

    @PostMapping("/delete/batch")
    public Result deleteBatch(@RequestBody Map<String, Object> params) {
        List<Integer> ids = (List<Integer>) params.get("ids");
        prescriptionService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping("/update")
    public Result updateById(@RequestBody Prescription prescription) {
        prescriptionService.updateById(prescription);
        return Result.success();
    }

    @PostMapping("/selectById")
    public Result selectById(@RequestBody Map<String, Object> params) {
        // 使用 @RequestBody Map<String, Object> 替代 @PathVariable
        Integer id = (Integer) params.get("id");
        return Result.success(prescriptionService.selectById(id));
    }

    @PostMapping("/selectAll")
    public Result selectAll(Prescription prescription) {
        List<Prescription> list = prescriptionService.selectAll(prescription);
        return Result.success(list);
    }

    @PostMapping("/selectPage")
    public Result selectPage(@RequestBody Map<String, Object> params) {
        // 获取 prescription 参数，假设 params 中包含 prescription 对象的相关字段
        Prescription prescription = new Prescription();
        prescription.setPrescriptionId((Integer) params.get("id")); // 假设 params 中有 id 字段
        
        // 获取分页参数，提供默认值
        Integer pageNum = (Integer) params.getOrDefault("pageNum", 1);  // 默认值为 1
        Integer pageSize = (Integer) params.getOrDefault("pageSize", 10); // 默认值为 10
    
        // 调用 service 层进行分页查询
        PageInfo<Prescription> page = prescriptionService.selectPage(prescription, pageNum, pageSize);
        return Result.success(page);
    }
    

    @PostMapping("/selectAndView")
    public Result selectAndView(@RequestBody Map<String, Object>params) {
        Integer id = (Integer) params.get("id");
        Prescription prescription = prescriptionService.selectById(id);
        Patient patient = patientService.selectById(prescription.getPatientId());
        Doctor doctor = doctorService.selectById(prescription.getDoctorId());
        Map<String, Object> map = new HashMap<>();
        map.put("prescription", prescription);
        map.put("patient", patient);
        map.put("doctor", doctor);
        return Result.success(map);
    }

    @PostMapping("/selectPageForAdmin")
    public Result selectPageForAdmin(@RequestBody Map<String, Object> params) {
        // 获取 prescription 参数，假设 params 中包含 prescription 对象的相关字段
        Prescription prescription = new Prescription();
        Integer preId = null;
        String id = (String) params.get("id");
        if (!id.isEmpty()) {
            preId = Integer.parseInt(id);
        }
        prescription.setPrescriptionId(preId); // 假设 params 中有 id 字段
        // 获取分页参数，提供默认值
        Integer pageNum = (Integer) params.getOrDefault("pageNum", 1);  // 默认值为 1
        Integer pageSize = (Integer) params.getOrDefault("pageSize", 10); // 默认值为 10

        // 调用 service 层进行分页查询
        PageInfo<Map<String, Object>> page = prescriptionService.selectPageForAdmin(prescription, pageNum, pageSize);
        return Result.success(page);
    }
}
