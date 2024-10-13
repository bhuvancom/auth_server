package com.cloud_campus.common.auth_server;

import com.cloud_campus.common.auth_server.config.WithAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Author  : Bhuvaneshvar
 * Project : auth_server
 * Date    : 1:35 pm
 **/
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthTest {
    @Autowired
    private MockMvc mockMvc;


    @Test()
    @WithAuthentication()
    void shouldCallWithAuth() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user/test/{org}", "test_org");
        ResultActions perform = mockMvc.perform(requestBuilder);
        perform.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
    }
    @Test()
    @WithAuthentication(roles = {})
    void shouldCallWithAuthError() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user/test/{org}", "test_org");
        ResultActions perform = mockMvc.perform(requestBuilder);
        perform.andExpect(MockMvcResultMatchers.status().isForbidden()).andDo(MockMvcResultHandlers.print());
    }
    @Test()
    @WithAuthentication(orgId = "abc")
    void shouldCallWithAuthErrorNoAccessToOrg() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user/test/{org}", "test_org");
        ResultActions perform = mockMvc.perform(requestBuilder);
        perform.andExpect(MockMvcResultMatchers.status().isForbidden()).andDo(MockMvcResultHandlers.print());
    }
}
