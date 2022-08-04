package com.hac.duohalle.domain.account.controller;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.hac.duohalle.domain.account.entity.Account;
import com.hac.duohalle.domain.account.repository.AccountRepository;
import com.hac.duohalle.infra.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class AccountControllerTest {

    private MockMvc mvc;
    private final WebApplicationContext context;
    private final AccountRepository accountRepository;

    @MockBean
    MailService mailService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    public void tearDown() {
        accountRepository.deleteAll();
    }

    @DisplayName("회원 가입 화면")
    @Test
    void signUpViewTest() throws Exception {
        mvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("accountSignUpRequestDto"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원 가입 처리 - 성공")
    @Test
    void signUpSuccessTest() throws Exception {
        // given
        String rightEmail = "jackdu@fakedomain.com"; // 실제 이메일이 도달하는 것을 확인하려면 값을 바꿀 것
        String rightPassword = "Abc12345!";
        String rightNickname = "jackdu";

        // when
        mvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", rightEmail)
                        .param("password", rightPassword)
                        .param("nickname", rightNickname)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(authenticated().withUsername(rightNickname));

        // then
        Account account = accountRepository.findByEmail(rightEmail);
        assertNotNull(account);
        assertNotEquals(account.getPassword(), rightPassword);
        then(mailService).should().sendSignUpConfirmEmail(account);
    }

    @DisplayName("회원 가입 처리 - 입력값 오류")
    @Test
    void signUpFailureTest() throws Exception {
        // given
        String wrongEmail = "123";
        String wrongPassword = "123";
        String wrongNickname = "1";

        // when
        // then
        mvc.perform(post("/sign-up")
                        .param("email", wrongEmail)
                        .param("password", wrongPassword)
                        .param("nickname", wrongNickname)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(unauthenticated());

        // TODO: 회원 가입 실패 시 에러 메시지 화면에 반환하고 해당 값 존재하는지 테스트
    }

    @DisplayName("회원 가입 인증 처리 - 성공")
    @Test
    void confirmSignUpSuccessTest() throws Exception {
        // given
        String rightEmail = "jackdu@fakedomain.com"; // 실제 이메일이 도달하는 것을 확인하려면 값을 바꿀 것
        String rightPassword = "Abc12345!";
        String rightNickname = "jackdu";

        mvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", rightEmail)
                        .param("password", rightPassword)
                        .param("nickname", rightNickname)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        Account account = accountRepository.findByEmail(rightEmail);

        // when - then
        mvc.perform(get("/check-email-token")
                        .param("token", account.getEmailCheckToken())
                        .param("email", rightEmail))
                .andExpect(status().isOk())
                .andExpect(view().name("account/confirm"))
                .andExpect(model().attributeExists("isEmailVerified"))
                .andExpect(model().attribute("isEmailVerified", true));
    }

    @DisplayName("회원 가입 인증 처리 - 실패")
    @Test
    void confirmSignUpFailureTest() throws Exception {
        mvc.perform(get("/check-email-token")
                        .param("checkEmailToken", "wrongToken")
                        .param("email", "wrongEmail"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/confirm"))
                .andExpect(model().attributeExists("isEmailVerified"))
                .andExpect(model().attribute("isEmailVerified", false));
    }
}