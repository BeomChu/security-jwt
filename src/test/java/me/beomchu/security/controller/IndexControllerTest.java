package me.beomchu.security.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.HandlerResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
class IndexControllerTest {

    @Autowired
    MockMvc mockMvc;


    @Test
    @WithAnonymousUser
    @DisplayName("권한없이 인덱스 페이지 받아볼 수 있음.")
    public void index() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("인덱스페이지다"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    @DisplayName("ROLE_USER는 /manager에 접속할 수 없다")
    public void userToManager() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/manager"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


    @Test
    @WithMockUser(authorities = {"ROLE_MANAGER"})
    @DisplayName("ROLE_MANAGER는 /manager에 접속할 수 있다")
    public void managerToManager() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/manager"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("매니저페이지다"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @DisplayName("ROLE_ADMIN은 /manager에 접속할 수 있다")
    public void adminToManger() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/manager"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("매니저페이지다"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_MANAGER"})
    @DisplayName("ROLE_MANAGER는 /admin에 접속할 수 없다")
    public void managerToAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    @DisplayName("ROLE_ADMIN은 /admin에 접속할 수 있다")
    public void adminToAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("어드민페이지다"));
    }

}