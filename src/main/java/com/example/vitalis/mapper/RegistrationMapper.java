package com.example.vitalis.mapper;

import com.example.vitalis.entity.Registration;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface RegistrationMapper {

    int insert(Registration registration);

    int deleteById(Integer id);

    int updateById(Registration registration);

    Registration selectById(Integer registrationId);

    List<Registration> selectAll(Registration registration);
}
