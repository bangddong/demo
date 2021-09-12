package com.example.demo.products;

import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ProductRestControllerTest {

    private static final Map<Integer, String> productName = new HashMap<>() {{
        put(1, "배");
        put(2, "토마토");
        put(3, "사과");
        put(4, "바나나");
    }};

    private MockMvc mockMvc;
    private WebApplicationContext ctx;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Autowired
    public ProductRestControllerTest(WebApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(print())
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("과일 목록 조회 테스트")
    void findAllSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/products")
                .accept(MediaType.APPLICATION_JSON)
        );
        result.andDo(print())
                .andDo(document("findAllProducts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response").isArray())
                .andExpect(jsonPath("$.response.length()", is(4)))
                .andExpect(jsonPath("$.response[0]", is("배")))
                .andExpect(jsonPath("$.response[1]", is("토마토")))
                .andExpect(jsonPath("$.response[2]", is("사과")))
                .andExpect(jsonPath("$.response[3]", is("바나나")));
    }

    @Test
    @Order(2)
    @DisplayName("과일 가격 조회 성공 테스트")
    void findByNameSuccessTest() throws Exception {
        for (int i = 1; i < 5; i++) {
            ResultActions result = mockMvc.perform(
                    get(new URI("/products/" + productName.get(i)))
            );
            result.andDo(print())
                    .andDo(document("findByNameProducts"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.response.name").exists())
                    .andExpect(jsonPath("$.response.price").exists());
        }
    }

    @Test
    @Order(3)
    @DisplayName("과일 가격 조회 실패 테스트 (존재하지 않는 상품)")
    void findByNameFailTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/products/" + "옥수수")
        );
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(404)));
    }
}
