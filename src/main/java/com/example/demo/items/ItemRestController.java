package com.example.demo.items;

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
@RequestMapping("items")
public class ItemRestController {

    private final ItemService itemService;

    @GetMapping
    public ApiResult<List<String>> findAll() throws Exception {
        return success(itemService.findAll());
    }

    @GetMapping(path = "{name}")
    public ApiResult<ItemDto> findByName(@PathVariable String name) throws Exception {
        return success(itemService.findByName(name).orElseThrow(() ->
                new NotFoundException("can not find item Id " + name)));
    }
}
