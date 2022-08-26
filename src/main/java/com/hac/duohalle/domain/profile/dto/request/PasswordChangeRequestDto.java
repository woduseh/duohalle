package com.hac.duohalle.domain.profile.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PasswordChangeRequestDto {

    @Length(min = 8, max = 50)
    private String newPassword;

    @Length(min = 8, max = 50)
    private String newPasswordConfirm;

}
