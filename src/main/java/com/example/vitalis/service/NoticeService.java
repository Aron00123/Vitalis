package com.example.vitalis.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.vitalis.common.enums.ResultCodeEnum;
import com.example.vitalis.entity.Notice;
import com.example.vitalis.entity.Notice;
import com.example.vitalis.exception.CustomException;
import com.example.vitalis.mapper.NoticeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {

    @Resource
    private NoticeMapper noticeMapper;

    public void add(Notice notice) {
        List<Notice> noticeList = noticeMapper.selectAll(new Notice());
        String id;
        if (noticeList.isEmpty()) {
            id = "0";
        } else {
            id = String.valueOf(Integer.parseInt(noticeList.get(noticeList.size() - 1).getId()) + 1);
        }
        notice.setId(id);
        noticeMapper.insert(notice);
    }

    public void deleteById(String id) {
        noticeMapper.deleteById(id);
    }

    public void deleteBatch(List<String> ids) {
        for (String id : ids) {
            noticeMapper.deleteById(id);
        }
    }

    public void updateById(Notice notice) {
        Notice notice1 = noticeMapper.selectById(notice.getId());
        if (ObjectUtil.isNull(notice1)) {
            throw new CustomException(ResultCodeEnum.NOTICE_NOT_EXIST_ERROR);
        }
        noticeMapper.updateById(notice);
    }

    public Notice selectById(String id) {
        return noticeMapper.selectById(id);
    }

    public List<Notice> selectAll(Notice notice) {
        return noticeMapper.selectAll(notice);
    }

    public PageInfo<Notice> selectPage(Notice notice, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Notice> notices = noticeMapper.selectAll(notice);
        return PageInfo.of(notices);
    }

    public List<Notice> querySearch(String queryString) {
        Notice notice = new Notice();
        List<Notice> notices = noticeMapper.selectAll(notice);
        List<Notice> notices1 = new ArrayList<>();
        int cnt = 0;
        for (Notice notice1 : notices) {
            if (cnt >= 10) {
                break;
            }
            if (notice1.getTitle().contains(queryString)) {
                notices1.add(notice1);
                cnt += 1;
            }
        }
        return notices1;
    }

    public PageInfo<Notice> querySearchAdmin(String queryString, Integer pageNum, Integer pageSize) {
        Notice notice = new Notice();
        notice.setTitle(queryString);
        List<Notice> notices = noticeMapper.selectAll(notice);
        List<Notice> notices1 = new ArrayList<>();
        for (Notice notice1: notices) {
            if (notice1.getTitle().contains(queryString)) {
                notices1.add(notice1);
            }
        }
        int total = notices1.size(); // 总记录数
        int start = (pageNum - 1) * pageSize; // 起始索引
        int end = Math.min(start + pageSize, total); // 结束索引

        // 获取当前页数据
        List<Notice> pageList = notices1.subList(start, end);

        // 封装到 PageInfo 中
        PageInfo<Notice> pageInfo = new PageInfo<>(pageList);
        pageInfo.setTotal(total); // 总记录数
        pageInfo.setPageNum(pageNum); // 当前页
        pageInfo.setPageSize(pageSize); // 每页大小
        pageInfo.setPages((total + pageSize - 1) / pageSize); // 总页数

        return pageInfo;
    }
}