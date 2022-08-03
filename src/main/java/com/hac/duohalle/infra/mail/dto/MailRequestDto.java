package com.hac.duohalle.infra.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailRequestDto {
    private String address;
    private String title;
    private String message;

    @Override
    public String toString() {
        return "MailRequestDto{" +
                "address='" + address + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
