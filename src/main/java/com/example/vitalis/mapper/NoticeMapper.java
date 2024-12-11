package com.example.vitalis.mapper;

import com.example.vitalis.entity.*;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface NoticeMapper {
    int insert(Notice notice);

    int deleteById(String id);

    int updateById(Notice notice);

    Notice selectById(String id);

    List<Notice> selectAll(Notice notice);
}
