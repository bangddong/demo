package com.example.demo.products;

import com.example.demo.utils.ApiRequestUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.example.demo.utils.ApiUtils.ApiResult;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {

    private final ObjectMapper mapper;

    public static final String PRODUCT_URL = "http://fruit.api.postype.net";

    public ProductService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public List<String> findAll() throws Exception {
        Map<String, String> requestMap = new HashMap<>();

        String token = getToken();
        String url = PRODUCT_URL + "/product";
        requestMap.put("accessToken", token);
        ApiResult<ResponseEntity<String>> response = ApiRequestUtils.requestApi(requestMap, url, HttpMethod.GET);

        return mapper.readValue(response.getResponse().getBody(), new TypeReference<>() {});
    }

    public Optional<ProductDto> findByName(String name) throws Exception {
        checkNotNull(name, "과일명은 필수 입력값입니다.");

        Map<String, String> requestMap = new HashMap<>();
        String token = getToken();

        String url = PRODUCT_URL + "/product?name=" + name;
        requestMap.put("accessToken", token);
        ApiResult<ResponseEntity<String>> response = ApiRequestUtils.requestApi(requestMap, url, HttpMethod.GET);

        return mapper.readValue(response.getResponse().getBody(), new TypeReference<>() {});
    }

    public String getToken() throws Exception {
        ApiResult<ResponseEntity<String>> response = ApiRequestUtils.requestApi(null, PRODUCT_URL + "/token", HttpMethod.GET);
        Map<String, String> resMap = mapper.readValue(response.getResponse().getBody(), new TypeReference<>() {});

        return resMap.get("accessToken");
    }
}
