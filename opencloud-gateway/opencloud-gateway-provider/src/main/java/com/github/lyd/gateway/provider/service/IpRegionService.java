package com.github.lyd.gateway.provider.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.lang.reflect.Method;


/**
 * IpRegion服务类
 */
@Slf4j
@Service
public class IpRegionService {
    DbConfig config = null;
    DbSearcher searcher = null;

    /**
     * 初始化IP库
     * 网上早springboot中使用的工具类,能用但是不可取,性能会有问题,每次都会重新加载db文件,增加IO读取和执行效率。
     * 这里使用bean的方法一次初始化db
     */
    @PostConstruct
    public void init() {
        try {
            // 因为jar无法读取文件,复制创建临时文件
            String tmpDir = System.getProperties().getProperty("java.io.tmpdir");
            String dbPath = tmpDir + "ip2region.db";
            log.info("init ip region db path [{}]", dbPath);
            File file = new File(dbPath);
            FileUtils.copyInputStreamToFile(IpRegionService.class.getClassLoader().getResourceAsStream("data/ip2region.db"), file);
            config = new DbConfig();
            searcher = new DbSearcher(config, dbPath);
            log.info("bean [{}]",config);
            log.info("bean [{}]",searcher);
        } catch (Exception e) {
            log.error("init ip region error:{}", e);
        }
    }


    /**
     * 解析IP
     *
     * @param ip
     * @return
     */
    public String getRegion(String ip) {
        try {
            //db
            if (searcher == null) {
                log.error("DbSearcher is null");
                return null;
            }
            long startTime = System.currentTimeMillis();
            //查询算法
            int algorithm = DbSearcher.MEMORY_ALGORITYM; //B-tree
            //DbSearcher.BINARY_ALGORITHM //Binary
            //DbSearcher.MEMORY_ALGORITYM //Memory
            //define the method
            Method method = null;
            switch (algorithm) {
                case DbSearcher.BTREE_ALGORITHM:
                    method = searcher.getClass().getMethod("btreeSearch", String.class);
                    break;
                case DbSearcher.BINARY_ALGORITHM:
                    method = searcher.getClass().getMethod("binarySearch", String.class);
                    break;
                case DbSearcher.MEMORY_ALGORITYM:
                    method = searcher.getClass().getMethod("memorySearch", String.class);
                    break;
            }

            DataBlock dataBlock = null;
            if (Util.isIpAddress(ip) == false) {
                log.error("Error: Invalid ip address");
            }
            dataBlock = (DataBlock) method.invoke(searcher, ip);
            String result = dataBlock.getRegion();
            long endTime = System.currentTimeMillis();
            log.debug("region use time[{}] result[{}]",endTime-startTime,result);
            return result;

        } catch (Exception e) {
            log.error("Error:{}", e);
        }
        return null;
    }
}
