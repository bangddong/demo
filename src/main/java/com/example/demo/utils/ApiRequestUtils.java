package com.example.demo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.example.demo.utils.ApiUtils.ApiResult;
import static com.example.demo.utils.ApiUtils.success;

public class ApiRequestUtils {

    /**
     * 외부 API를 호출한다.
     * @param requestMap 요청 파라미터
     * @param url 요청 URL
     * @param method HTTP 메소드 타입
     */
    public static ApiResult<ResponseEntity<String>> requestApi(Map<String, String> requestMap, String url, HttpMethod method) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application","json", StandardCharsets.UTF_8));
        if (requestMap != null) {
            httpHeaders.add("Authorization", requestMap.get("accessToken"));
        }

        String params = objectMapper.writeValueAsString(requestMap);

        HttpEntity<String> entity = new HttpEntity<>(params, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, entity, String.class);

        return success(responseEntity);
    }
}
