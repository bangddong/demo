package com.example.demo.products;

import com.example.demo.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.demo.utils.ApiUtils.ApiResult;
import static com.example.demo.utils.ApiUtils.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductRestController {

    private final ProductService productService;

    @GetMapping
    public ApiResult<List<String>> findAll() throws Exception {
        return success(productService.findAll());
    }

    @GetMapping(path = "{name}")
    public ApiResult<ProductDto> findByName(@PathVariable String name) throws Exception {
        return success(productService.findByName(name).orElseThrow(() ->
                new NotFoundException("can not find product Id " + name)));
    }
}
