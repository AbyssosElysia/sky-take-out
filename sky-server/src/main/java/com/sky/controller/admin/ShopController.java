package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {

    private static final String key = "SHOP_STATUS";

    @Autowired
    RedisTemplate redisTemplate;

    @PutMapping("/{status}")
    @ApiOperation("设置店铺营业状态")
    public Result setStatus(@PathVariable Integer status) {
        log.info("设置店铺的营业状态为: {}", status == 1 ? "营业中" : "打烊中");
        redisTemplate.opsForValue().set(key, status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result<Integer> getStatus() {
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get(key);
        log.info("获取到的店铺状态为: {}", shopStatus);
        return Result.success(shopStatus);
    }
}
