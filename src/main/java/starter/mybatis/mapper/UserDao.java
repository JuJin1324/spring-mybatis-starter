package starter.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import starter.mybatis.domain.User;

import java.util.List;
import java.util.UUID;

/**
 * Created by Yoo Ju Jin(jujin@100fac.com)
 * Created Date : 12/28/23
 * Copyright (C) 2023, Centum Factorial all rights reserved.
 */

@Repository
@Mapper
public interface UserDao {
    List<User> findById(@Param("id") Long id);

    List<User> findByUuid(@Param("uuid") UUID uuid);
}
