package com.chryl.util;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.chryl.service.mq.SmsService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * Created by Chr.yl on 2020/6/27.
 *
 * @author Chr.yl
 */
@Slf4j
public class SmsUtil {

    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "LTAI4G8NbYFkjB6BThA1Dy4P";
    static final String accessKeySecret = "QGz2MeWdrskfSdtnqiHJrinoxTiI0m";

    /**
     * 发送短信
     *
     * @param phoneNumbers  要发送短信的手机号
     * @param signName      短信签名(必须使用前面申请通过的)
     * @param templateCode  短信模板(必须申请通过的)
     * @param templateParam 模板中 ${code} 等参数位置的内容  ,注意是json格式的 "code":"123456"
     * @return
     * @throws ClientException
     */
    public static SendSmsResponse sendSms(String phoneNumbers,
                                          String signName,
                                          String templateCode,
                                          String templateParam) throws ClientException {

        try {
            //可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            //必填:待发送手机号
            request.setPhoneNumbers(phoneNumbers);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(signName);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(templateCode);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam(templateParam);

            //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");

            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId("yourOutId");

            //hint 此处可能会抛出异常，注意catch
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if (!"OK".equals(sendSmsResponse.getCode())) {
                log.info("发送短信出错,{}", sendSmsResponse);
                throw new RuntimeException(sendSmsResponse.getMessage());
            }
            return sendSmsResponse;
        } catch (Exception e) {
            log.info("发送短信出错,{}", e);
            throw new RuntimeException("发送短信失败");
        }

    }


    /**
     * 查询
     *
     * @param bizId       流水号
     * @param phoneNumber 手机号
     * @param sendDate    yyyyMMdd
     * @param currentPage 当前页
     * @param pageSize    每页记录数
     * @return
     * @throws ClientException
     */
    public static QuerySendDetailsResponse querySendDetails(String bizId,
                                                            String phoneNumber,
                                                            String sendDate,
                                                            Long currentPage,
                                                            Long pageSize) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber(phoneNumber);
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
//        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
//        request.setSendDate(ft.format(new Date()));
        request.setSendDate(sendDate);
        //必填-页大小
        request.setPageSize(pageSize);
        //必填-当前页码从1开始计数
        request.setCurrentPage(currentPage);

        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

        return querySendDetailsResponse;
    }

    public static void main(String[] args) {


        Param param = new Param();
        try {
            sendSms("13287983898", "浪维", "SMS_193509750", JSON.toJSONString(param));
            System.out.println("发送成功");
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }


    @Data
    static class Param {
        private String code;

        public Param() {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < 6; i++) {

                stringBuilder.append(new Random().nextInt(9) + 1);
            }
            this.code = stringBuilder.toString();
        }
    }
}
