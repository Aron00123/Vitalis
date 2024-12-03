package com.example.vitalis.mapper;

import com.example.vitalis.entity.Account;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface AccountMapper {

    int insert(Account account);

    int deleteById(String id);

    int updateById(Account account);

    Account selectById(String id);

    List<Account> selectAll(Account account);
}
