package com.hac.duohalle.domain.index.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.hac.duohalle.domain.account.dto.request.AccountSignUpRequestDto;
import com.hac.duohalle.domain.account.service.AccountService;
import com.hac.duohalle.infra.config.auth.WithMockAccount;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class IndexControllerTest {

    private MockMvc mvc;
    private final WebApplicationContext context;
    private final AccountService accountService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @DisplayName("메인 화면 - 회원")
    @WithMockAccount
    @Test
    void indexForLoginUser() throws Exception {
        mvc.perform(get("/")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("account"))
                .andExpect(authenticated());
    }

    @DisplayName("메인 화면 - 비회원")
    @Test
    void indexForNonMember() throws Exception {
        mvc.perform(get("/")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("error"))
                .andExpect(unauthenticated());
    }

    @DisplayName("로그인")
    @Test
    void login() throws Exception {
        mvc.perform(get("/login")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(unauthenticated());
    }

    @DisplayName("로그인 - 성공")
    @Test
    void loginSuccess() throws Exception {
        // given
        String email = "jackdu@fakeemail.com";
        String password = "fakePassword!";
        String nickname = "jackdu";

        AccountSignUpRequestDto dto = new AccountSignUpRequestDto();
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setNickname(nickname);
        accountService.signUp(dto);

        // then & when
        mvc.perform(post("/login")
                        .param("username", email)
                        .param("password", password)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated());
    }

    @DisplayName("로그인 - 실패")
    @Test
    void loginFail() throws Exception {
        mvc.perform(post("/login")
                        .param("username", "notEmail")
                        .param("password", "fakePassword")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @DisplayName("로그아웃")
    @WithMockAccount
    @Test
    void logout() throws Exception {
        mvc.perform(post("/logout")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());
    }
}