package com.hac.duohalle.domain.profile.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ProfileChangeRequestDto {

    @Length(max = 35)
    private String bio;

    @Length(max = 50)
    private String profileUrl;

    @Length(max = 50)
    private String occupation;

    private String imageUrl;
}
