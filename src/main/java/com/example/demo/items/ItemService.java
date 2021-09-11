package com.example.demo.items;

import com.example.demo.errors.GetAccessTokenException;
import com.example.demo.utils.ApiRequestUtils;
import com.example.demo.utils.ApiUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class ItemService {

    private final ObjectMapper mapper;

    public static final String ITEM_URL = "http://vegetable.api.postype.net";

    public ItemService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public List<String> findAll() throws Exception {
        Map<String, String> requestMap = new HashMap<>();

        String token = getToken();
        String url = ITEM_URL + "/item";
        requestMap.put("accessToken", token);
        ApiUtils.ApiResult<ResponseEntity<String>> response = ApiRequestUtils.requestApi(requestMap, url, HttpMethod.GET);

        return mapper.readValue(response.getResponse().getBody(), new TypeReference<>() {});
    }

    public Optional<ItemDto> findByName(String name) throws Exception {
        checkNotNull(name, "채소명은 필수 입력값입니다.");

        Map<String, String> requestMap = new HashMap<>();
        String token = getToken();

        String url = ITEM_URL + "/item?name=" + name;
        requestMap.put("accessToken", token);
        ApiUtils.ApiResult<ResponseEntity<String>> response = ApiRequestUtils.requestApi(requestMap, url, HttpMethod.GET);

        return mapper.readValue(response.getResponse().getBody(), new TypeReference<>() {});
    }

    public String getToken() throws Exception {
        ApiUtils.ApiResult<ResponseEntity<String>> response = ApiRequestUtils.requestApi(null, ITEM_URL + "/token", HttpMethod.GET);
        HttpHeaders headers = response.getResponse().getHeaders();
        if (headers.get("Set-Cookie") == null) {
            throw new GetAccessTokenException("인증토큰 발급 실패");
        }
        String cookie = headers.get("Set-Cookie").toString();
        cookie = cookie.substring(cookie.indexOf("=") + 1,cookie.indexOf(";"));

        return cookie;
    }
}
