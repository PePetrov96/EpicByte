package com.project.EpicByte.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MyErrorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHandleError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/error"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }
}