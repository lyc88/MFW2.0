package com.zhm.Filter;

import com.alibaba.fastjson.JSON;

import com.zhm.entity.ParamDetail;
import com.zhm.entity.Params;
import com.zhm.service.ParamService;
import com.zhm.util.Constant;
import com.zhm.util.DateUtil;
import com.zhm.util.JsonUtil;
import com.zhm.util.PropertiesUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by 赵红明 on 2019/6/24.
 * 项目启动数据初始化拦截器
 * 就是项目
 *
 */
@Component
public class InitParamStartupRunner implements CommandLineRunner {

    private static Logger logger= LoggerFactory.getLogger(InitParamStartupRunner.class);

    @Autowired
    private ParamService paramService;

    @Autowired
    private StringRedisTemplate rt;

    @Override
    public void run(String... args) throws Exception {
        /**
         * 用于项目启动的时候一些数据的初始化，比如系统参数存储在redis中。
         */
        //所有主参数
        List<Params> paramsList = paramService.findAll();
        logger.info("主参数"+ JsonUtil.toJson(paramsList));
        saveParamsToRedis(paramsList);
        // 所有子参
        List<ParamDetail> paramDetails = paramService.findAllDetails();
        logger.info("子参数"+ JsonUtil.toJson(paramDetails));
        saveParamsDetailToRedis(paramDetails);

        logger.info("当前项目启动时间是: {}", DateUtil.dateToString(new Date()));
    }
    /**
     * 总参 存在redis中
     *
     * @return void
     * @throws
     * @author bob
     * @Param [paramsList]
     * @date 2018/6/15 16:20
     */
    private void saveParamsToRedis(List<Params> paramsList) {
        if (CollectionUtils.isNotEmpty(paramsList)) {
            HashMap<String, String> map = new HashMap<>(paramsList.size());
            Params params;
            for (int i = 0; i < paramsList.size(); i++) {
                params = paramsList.get(i);
                map.put(params.getParamCode(), JSON.toJSONString(params));
            }
            rt.opsForHash().putAll(PropertiesUtil.getRedisPrefixKey() + Constant.PARENT_PARAMS, map);
        }
    }
    /**
     * 子参 存在redis中
     *
     * @return void
     * @throws
     * @author bob
     * @Param [paramDetails]
     * @date 2018/6/15 16:20
     */
    private void saveParamsDetailToRedis(List<ParamDetail> paramDetails) {
        if (CollectionUtils.isNotEmpty(paramDetails)) {
            ParamDetail paramDetail;
            Map<String, Map<String, String>> mapHashMap = new HashMap<>();
            Map<String, String> map;
            String detailCode;
            String redisKey;
            for (int i = 0; i < paramDetails.size(); i++) {
                paramDetail = paramDetails.get(i);
                detailCode = paramDetail.getParamDetailCode();
                redisKey = PropertiesUtil.getRedisPrefixKey() + Constant.SON_PARAMS +  paramDetail.getParamCode();
                map = mapHashMap.get(redisKey);
                if (MapUtils.isEmpty(map)) {
                    map = new LinkedHashMap<>();
                }
                map.put(detailCode, JSON.toJSONString(paramDetail));
                mapHashMap.put(redisKey, map);
            }

            saveMapToRedis(mapHashMap);
        }
    }

    /**
     * 先清缓存再插入
     * @author      bob
     * @Param       [mapHashMap]
     * @return      void
     * @exception
     * @date        2018/6/15 17:59
     */
    private void saveMapToRedis(Map<String,Map<String,String>> mapHashMap) {
        mapHashMap.forEach((key, value) ->{
            rt.delete(key);
            rt.opsForHash().putAll(key, value);
        });
    }

}
