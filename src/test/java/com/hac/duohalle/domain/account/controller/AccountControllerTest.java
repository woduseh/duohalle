package com.hac.duohalle.domain.account.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @DisplayName("회원 가입 화면")
    @Test
    void signUpViewTest() throws Exception {
        mvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("accountSignUpRequestDto"));
    }

    @DisplayName("회원 가입 처리 - 성공")
    @Test
    void signUpSuccessTest() throws Exception {
        // given
        String rightEmail = "jackdu@fakedomain.com"; // 실제 이메일이 도달하는 것을 확인하려면 값을 바꿀 것
        String rightPassword = "Abc12345!";
        String rightNickname = "jackdu";

        // when
        // then
        mvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", rightEmail)
                        .param("password", rightPassword)
                        .param("nickname", rightNickname)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/sign-in"));
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
                .andExpect(status().isOk());
    }
}