package starter.mybatis.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Yoo Ju Jin(jujin@100fac.com)
 * Created Date : 12/28/23
 * Copyright (C) 2023, Centum Factorial all rights reserved.
 */

@AllArgsConstructor
@Getter
public class PhoneNumber {
    private String countryCode;
    private String number;
}
