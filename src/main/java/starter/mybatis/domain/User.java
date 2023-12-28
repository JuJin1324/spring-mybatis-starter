package starter.mybatis.domain;

import lombok.*;

import java.util.UUID;

/**
 * Created by Yoo Ju Jin(jujin@100fac.com)
 * Created Date : 12/28/23
 * Copyright (C) 2023, Centum Factorial all rights reserved.
 */

@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public class User {
    private Long id;
    private UUID uuid;
    private PhoneNumber phoneNumber;
}
