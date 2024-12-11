package com.example.vitalis.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.vitalis.common.Result;
import com.example.vitalis.common.enums.ResultCodeEnum;
import com.example.vitalis.entity.Account;
import com.example.vitalis.entity.Patient;
import com.example.vitalis.entity.DoctorPatientAccount;
import com.example.vitalis.service.AccountService;
import javax.annotation.Resource;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Map;


/**
 * 基础前端接口
 */
@RestController
public class WebController {

    @Resource
    private AccountService accountService;

    @GetMapping("/")
    public Result hello() {
        return Result.success("访问成功");
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        if (ObjectUtil.isEmpty(account.getId()) || ObjectUtil.isEmpty(account.getPassword())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }

        return Result.success(accountService.login(account));
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody DoctorPatientAccount patient) {
        if (StrUtil.isBlank(patient.getId()) || StrUtil.isBlank(patient.getPassword())
                || ObjectUtil.isEmpty(patient.getRole())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        Account account = new Account();
        account.setId(patient.getId());account.setPassword(patient.getPassword());account.setNewPassword("");account.setRole(patient.getRole());
        Patient patient1 = new Patient(patient.getId(), patient.getGender(), patient.getName(), patient.getAge(), patient.getAddress(), patient.getPhone(), patient.getEmergencyPhone());
        accountService.register(account, patient1);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody DoctorPatientAccount account) {
        if (StrUtil.isBlank(account.getId()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getNewPassword())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        accountService.updatePassword(account.getAccount());
        return Result.success();
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadToImgBB(@RequestParam("smfile") MultipartFile file) {
        try {
            // 创建 RestTemplate
            RestTemplate restTemplate = new RestTemplate();

            // 构建请求体
            LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("key", "a5f0117086c7c8b6bf47abfe7520ecb3");
            body.add("image", Base64.getEncoder().encodeToString(file.getBytes())); // 文件内容需要 Base64 编码

            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body);

            // 发起请求到 ImgBB API
            ResponseEntity<String> response = restTemplate.postForEntity("https://api.imgbb.com/1/upload", requestEntity, String.class);

            // 返回 ImgBB 的响应
            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "message", "上传失败",
                    "error", e.getMessage()
            ));
        }
    }
}
