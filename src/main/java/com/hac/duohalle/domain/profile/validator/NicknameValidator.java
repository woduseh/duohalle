package com.hac.duohalle.domain.profile.validator;

import com.hac.duohalle.domain.account.entity.Account;
import com.hac.duohalle.domain.account.repository.AccountRepository;
import com.hac.duohalle.domain.profile.dto.request.NicknameChangeRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class NicknameValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return NicknameChangeRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NicknameChangeRequestDto nicknameForm = (NicknameChangeRequestDto) target;
        Account byNickname = accountRepository.findByNickname(nicknameForm.getNickname());
        if (byNickname != null) {
            errors.rejectValue("nickname", "wrong.value", "입력하신 닉네임을 사용할 수 없습니다.");
        }
    }
}
