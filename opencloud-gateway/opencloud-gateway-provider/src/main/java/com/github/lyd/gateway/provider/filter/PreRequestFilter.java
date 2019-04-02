package com.github.lyd.gateway.provider.filter;

import com.github.lyd.common.gen.SnowflakeIdGenerator;
import com.github.lyd.gateway.client.constants.GatewayContants;
import com.github.lyd.gateway.provider.service.GatewayAccessLogsService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class PreRequestFilter extends OncePerRequestFilter {
    private SnowflakeIdGenerator snowflakeIdGenerator;
    private RedisTemplate redisTemplate;
    private GatewayAccessLogsService gatewayAccessLogsService;

    public PreRequestFilter(SnowflakeIdGenerator snowflakeIdGenerator, RedisTemplate redisTemplate, GatewayAccessLogsService gatewayAccessLogsService) {
        this.snowflakeIdGenerator = snowflakeIdGenerator;
        this.redisTemplate = redisTemplate;
        this.gatewayAccessLogsService = gatewayAccessLogsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        log.debug("==> PreFilter");
        Date start = new Date();
        log.debug("==> start [{}] path[{}]", start.getTime(),httpServletRequest.getRequestURI());
        try {
            Long requestId = snowflakeIdGenerator.nextId();
            httpServletRequest.setAttribute(GatewayContants.PRE_REQUEST_ID, String.valueOf(requestId));
            Map<String, Object> map = Maps.newHashMap();
            map.put("accessId", requestId);
            map.put("requestTime", new Date());
            // 3分钟过期
            String key = GatewayContants.PRE_REQUEST_ID_CACHE_PREFIX + requestId;
            // 放入redis缓存
            redisTemplate.opsForValue().set(key, map, 3, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("访问日志异常:{}", e);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        Date end = new Date();
        log.debug("==> end [{}] use [{}] httpStatus=[{}]", end.getTime(), end.getTime() - start.getTime(),httpServletResponse.getStatus());
        if (httpServletResponse.getStatus() == 200) {
            gatewayAccessLogsService.saveLogs(httpServletRequest,httpServletResponse);
        }
    }
}
