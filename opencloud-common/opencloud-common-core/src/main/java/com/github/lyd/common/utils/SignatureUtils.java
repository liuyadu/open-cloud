package com.github.lyd.common.utils;

import com.github.lyd.common.constants.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author liuyadu
 */
@Slf4j
public class SignatureUtils {

    /**
     * 5分钟有效期
     */
    private final static long MAX_EXPIRE = 5 * 60;

    public static void main(String[] args) {
        //参数签名算法测试例子
        HashMap<String, String> signMap = new HashMap<String, String>();
        signMap.put("clientId", "gateway");
        signMap.put("signType", "SHA256");
        signMap.put("timestamp", DateUtils.getTimestampStr());
        signMap.put("nonce", "d3c6fcd551104c53b4ccde31059d815a");
        String sign = SignatureUtils.getSign(signMap, "123456");
        System.out.println("签名结果:" + sign);
        signMap.put("sign", sign);
        System.out.println(SignatureUtils.validateSign(signMap, "123456"));
    }

    /**
     * 验证参数
     *
     * @param paramsMap
     * @throws Exception
     */
    public static void validateParams(Map<String, String> paramsMap) throws Exception {
        Assert.notNull(paramsMap.get(CommonConstants.SIGN_CLIENT_ID_KEY), "clientId不能为空");
        Assert.notNull(paramsMap.get(CommonConstants.SIGN_NONCE_KEY), "nonce不能为空");
        Assert.notNull(paramsMap.get(CommonConstants.SIGN_TIMESTAMP_KEY), "timestamp不能为空");
        Assert.notNull(paramsMap.get(CommonConstants.SIGN_SIGN_TYPE_KEY), "signType不能为空");
        Assert.notNull(paramsMap.get(CommonConstants.SIGN_SIGN_KEY), "sign不能为空");
        if (!SignatureUtils.SignType.contains(paramsMap.get(CommonConstants.SIGN_SIGN_TYPE_KEY))) {
            throw new IllegalArgumentException(String.format("signType必须为:%s,%s", SignatureUtils.SignType.MD5, SignatureUtils.SignType.SHA256));
        }
        try {
            DateUtils.parseDate(paramsMap.get(CommonConstants.SIGN_TIMESTAMP_KEY), "yyyyMMddHHmmss");
        } catch (ParseException e) {
            throw new IllegalArgumentException("timestamp格式必须为:yyyyMMddHHmmss");
        }
    }

    /**
     * @param paramMap     必须包含
     * @param clientSecret
     * @return
     */
    public static boolean validateSign(Map<String, String> paramMap, String clientSecret) {
        try {
            validateParams(paramMap);
            String sign = paramMap.get(CommonConstants.SIGN_SIGN_KEY);
            String timestamp = paramMap.get(CommonConstants.SIGN_TIMESTAMP_KEY);
            Long clientTimestamp = Long.parseLong(timestamp);
            //判断时间戳 timestamp=201808091113
            if ((DateUtils.getTimestamp() - clientTimestamp) > MAX_EXPIRE) {
                log.debug("validateSign fail timestamp expire");
                return false;
            }
            //重新生成签名
            String signNew = getSign(paramMap, clientSecret);
            //判断当前签名是否正确
            if (signNew.equals(sign)) {
                return true;
            }
        } catch (Exception e) {
            log.error("validateSign error:{}", e.getMessage());
            return false;
        }
        return false;
    }


    /**
     * 得到签名
     *
     * @param paramMap     参数集合不含clientSecret
     *                     必须包含clientId=客户端ID
     *                     signType = SHA256|MD5 签名方式
     *                     timestamp=时间戳
     *                     nonce=随机字符串
     * @param clientSecret 验证接口的clientSecret
     * @return
     */
    public static String getSign(Map<String, String> paramMap, String clientSecret) {
        if (paramMap == null) {
            return "";
        }
        //排序
        Set<String> keySet = paramMap.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        String signType = paramMap.get(CommonConstants.SIGN_SIGN_TYPE_KEY);
        SignType type = null;
        if (StringUtils.isNotBlank(signType)) {
            type = SignType.valueOf(signType);
        }
        if (type == null) {
            type = SignType.MD5;
        }
        for (String k : keyArray) {
            if (k.equals(CommonConstants.SIGN_SIGN_KEY) || k.equals(CommonConstants.SIGN_CLIENT_SECRET_KEY)) {
                continue;
            }
            if (paramMap.get(k).trim().length() > 0) {
                // 参数值为空，则不参与签名
                sb.append(k).append("=").append(paramMap.get(k).trim()).append("&");
            }
        }
        //暂时不需要个人认证
        sb.append(CommonConstants.SIGN_CLIENT_SECRET_KEY+"=").append(clientSecret);
        String signStr = "";
        //加密
        switch (type) {
            case MD5:
                signStr = EncryptUtils.md5Hex(sb.toString()).toLowerCase();
                break;
            case SHA256:
                signStr = EncryptUtils.sha256Hex(sb.toString()).toLowerCase();
                break;
            default:
                break;
        }
        return signStr;
    }


    public enum SignType {
        MD5,
        SHA256;

        public static boolean contains(String type) {
            for (SignType typeEnum : SignType.values()) {
                if (typeEnum.name().equals(type)) {
                    return true;
                }
            }
            return false;
        }
    }

}
