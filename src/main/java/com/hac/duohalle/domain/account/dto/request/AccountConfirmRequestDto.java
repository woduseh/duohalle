package com.hac.duohalle.domain.account.dto.request;

import lombok.Data;

@Data
public class AccountConfirmRequestDto {

    private String token;
    private String email;
}
