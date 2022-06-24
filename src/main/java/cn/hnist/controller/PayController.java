package cn.hnist.controller;

import cn.hnist.pojo.OrderInfo;
import cn.hnist.pojo.ResultInfo;
import cn.hnist.service.OrderService;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 支付控制器
 */
@Controller
@RequestMapping("/user")
public class PayController {

    @Autowired
    OrderService orderService;

    @RequestMapping("/alipay/{ID}")
    public void aliPay(@PathVariable("ID") String order_id, HttpServletResponse response)
            throws IOException {

        AlipayClient alipayClient = new
                DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do",
                "2021000121604466",
                "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCFi839WdDDbW/l/pIshWFBfM1DPjsTFHpMaRTUPCWcK0012dEkCLLqJ9DjUcafBlvNducAuYLmDNJk5qt0gVa3nIyf6q3iYAZ2gQx9TTKmoBStl0AQKQbpWqjeu0B9an0vhrfzk1A25GgjPJplUkCvEjr/984BoEDrs/2It1D7kEpocDCb7tKNOgaoJtYGpO+ng31TLQEVhnYunKPKaPJw9zeeq6YnluHBhNs213qQJunMfwsiPySgdKQHRaWfUIocMj0CADVrBsG3jBwRJlPTiefJQefPh4Tlb+wbRKbakSKXReGcL8LglHWGCAd+onQViEZM3Durf6wIPBcrrmexAgMBAAECggEALtpy5c0qGjZ+CNI81JPjIEe5+DqQHUyU79P6M8/6XgUvLJKD9sF6i/52IhrhtY9UjAyIVU/AphefhTkMtG5gT0EEcWKzqPWJru/twtRfzefh+VlUZ5cNTre5AZPut1GyJAd+6F0pymhuRNaRULT/7qxVuNJnG1jvDgLzE5FFP0zJn1pvsMjIUXJcn1jZNE9jeMW9tcmg4uEocpJYTSdlfodKXlpNTQKM+3k31x0mb+smPFsi6Du657QcWlDi85t4CrtdTCmtXJGrB8odqF+ib9I62s0DJZ/esTm6TvUSCUczCcQrxjCp09MRbnTIDGf8eIAWBzeRFnTyWuFm+PQctQKBgQC6RXrr/3oK4XSgo+NVXAPvwihhF53grk+RB6wFxHyEXYOPcGGjlOho7kXMcvEVn4ZuZxmqNugmh8BLUxMlZEHn8qQ7b8aLuYQOKZ0nO9QQGDs6iWpP0G/3k2gc1t8gA24T8WL90bh85g87nrP7EvDOujU2YEYNtJKukUo9KpVuAwKBgQC3iZ9WMGLR6VsXjHg566H4fXmW3acUPUixyJ3RAq/dq882JvSg7QXlFD5MDaDVzYEYE6W30I5NyZ7py42pYw5qKC5EHLgalm7JlUqPfN7517DvtPL+odAJNJaHNmtfPp+KfWmaYa49p/yOP2T9E4Clt/8Hg8b/ewc2XEJ5Ja6vOwKBgAaDr4zZXndK8z06/hRaJ3kXHTpoDQlyjy3PSuDYwlIOwy8dCw0hhA9HZgGEc5ICBj2ONYOScAQI81yvBxhjrTpjbgL2Icd5MXkdU3WvzfbSIiAl9iXccE4ERbvcd+kytYLVClU+JGmZ0iaPolPBkdJpCLtiGpEYI5kfU61X8+npAoGAXJ8eAs/ePtKFvseLsKciERW5M5JeC8+1jMqSIfU6LSLUKh7UmxWiQO3LVnQWM29G85COs8mS1tylzpSDw/SSRK5MUuEvY3OBK0ek3e6FVA4uY3msmyWrp8mbIVcsLl+bAbeygE9FMCZDRMHnQ/QAmyDUmcU9upYqmJtMjHigxBsCgYAtRyxVo+4AAV7N2WVz1vkRbl/5kKPUw4FWkG44jyfZq7LQA2dwFM4w3KZjCYh/5z+CH1GaWiGqeNWa46RAJYnF105jdnRVVgTWOSRccZOIh3Ir10k8JT2xTUpJWZrG/aE1qEX0bJoi6dPy3CxZ1xN6AXHkRTz9h36hpAbciitbwg==",
                "json",
                "utf-8",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmyj6eopSZM0564p0OW0pImF54QJ59siEUJREdnmoz58dQ45c8h2OaJcEAlUkWHNNzpfJGCb1wACXYKzRryVVS3kPZdrOBfoqqMgHxWE3G0i8eMnZwYWAYKYEa7mW1/kPxJFxL1cOfab7EsJ4pr1vEDN4k0mpqUdRR/3JQAyFUg3mXBJvAm9dCF9XHl49ebvljHIfGabYITlAOr3bg3H5exDXGquEchAcVjxtRl827UvUxuNIkXDUOMcwtlE8MQUabH2xEG4TOi/tILi/gsUlXAjf1sbwTeAcsfF0hDMaH0tJyP3DHMEsPT6R7Vwe3NFlJhBNGBvZ4WPgTxXZ4zbOswIDAQAB",
                "RSA2");

        OrderInfo orderInfo = orderService.findOrderInfoById(order_id);

        String out_trade_no = order_id;
        out_trade_no = URLDecoder.decode(out_trade_no, "UTF-8");
        String total_amount = String.valueOf(orderInfo.getTotal_price() + orderInfo.getTransit_price());
        total_amount = URLDecoder.decode(total_amount, "UTF-8");
        String subject = String.format("天天生鲜%s", order_id);
        subject = URLDecoder.decode(subject, "UTF-8");
        String body = "";
        body = URLDecoder.decode(body, "UTF-8");

        //创建API对应的request
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl("http://81.69.56.215/user/order");
        alipayRequest.setNotifyUrl("http://81.69.56.215/payOk");
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\"" + out_trade_no + "\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":" + total_amount + "," +
                "    \"subject\":\"" + subject + "\"," +
                "    \"body\":\"" + body + "\"" +
                "    }" +
                "  }");
        //填充业务参数
        String form = "";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(form);//直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();
    }

    /**
     * ajax响应订单支付状态查询
     */
    @RequestMapping(value = "/order/check", method = RequestMethod.POST)
    public ResultInfo check(String order_id, HttpSession session) throws AlipayApiException, UnsupportedEncodingException, InterruptedException {
        ResultInfo info = new ResultInfo();

        AlipayClient alipayClient = new
                DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do",
                "2021000121604466",
                "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCFi839WdDDbW/l/pIshWFBfM1DPjsTFHpMaRTUPCWcK0012dEkCLLqJ9DjUcafBlvNducAuYLmDNJk5qt0gVa3nIyf6q3iYAZ2gQx9TTKmoBStl0AQKQbpWqjeu0B9an0vhrfzk1A25GgjPJplUkCvEjr/984BoEDrs/2It1D7kEpocDCb7tKNOgaoJtYGpO+ng31TLQEVhnYunKPKaPJw9zeeq6YnluHBhNs213qQJunMfwsiPySgdKQHRaWfUIocMj0CADVrBsG3jBwRJlPTiefJQefPh4Tlb+wbRKbakSKXReGcL8LglHWGCAd+onQViEZM3Durf6wIPBcrrmexAgMBAAECggEALtpy5c0qGjZ+CNI81JPjIEe5+DqQHUyU79P6M8/6XgUvLJKD9sF6i/52IhrhtY9UjAyIVU/AphefhTkMtG5gT0EEcWKzqPWJru/twtRfzefh+VlUZ5cNTre5AZPut1GyJAd+6F0pymhuRNaRULT/7qxVuNJnG1jvDgLzE5FFP0zJn1pvsMjIUXJcn1jZNE9jeMW9tcmg4uEocpJYTSdlfodKXlpNTQKM+3k31x0mb+smPFsi6Du657QcWlDi85t4CrtdTCmtXJGrB8odqF+ib9I62s0DJZ/esTm6TvUSCUczCcQrxjCp09MRbnTIDGf8eIAWBzeRFnTyWuFm+PQctQKBgQC6RXrr/3oK4XSgo+NVXAPvwihhF53grk+RB6wFxHyEXYOPcGGjlOho7kXMcvEVn4ZuZxmqNugmh8BLUxMlZEHn8qQ7b8aLuYQOKZ0nO9QQGDs6iWpP0G/3k2gc1t8gA24T8WL90bh85g87nrP7EvDOujU2YEYNtJKukUo9KpVuAwKBgQC3iZ9WMGLR6VsXjHg566H4fXmW3acUPUixyJ3RAq/dq882JvSg7QXlFD5MDaDVzYEYE6W30I5NyZ7py42pYw5qKC5EHLgalm7JlUqPfN7517DvtPL+odAJNJaHNmtfPp+KfWmaYa49p/yOP2T9E4Clt/8Hg8b/ewc2XEJ5Ja6vOwKBgAaDr4zZXndK8z06/hRaJ3kXHTpoDQlyjy3PSuDYwlIOwy8dCw0hhA9HZgGEc5ICBj2ONYOScAQI81yvBxhjrTpjbgL2Icd5MXkdU3WvzfbSIiAl9iXccE4ERbvcd+kytYLVClU+JGmZ0iaPolPBkdJpCLtiGpEYI5kfU61X8+npAoGAXJ8eAs/ePtKFvseLsKciERW5M5JeC8+1jMqSIfU6LSLUKh7UmxWiQO3LVnQWM29G85COs8mS1tylzpSDw/SSRK5MUuEvY3OBK0ek3e6FVA4uY3msmyWrp8mbIVcsLl+bAbeygE9FMCZDRMHnQ/QAmyDUmcU9upYqmJtMjHigxBsCgYAtRyxVo+4AAV7N2WVz1vkRbl/5kKPUw4FWkG44jyfZq7LQA2dwFM4w3KZjCYh/5z+CH1GaWiGqeNWa46RAJYnF105jdnRVVgTWOSRccZOIh3Ir10k8JT2xTUpJWZrG/aE1qEX0bJoi6dPy3CxZ1xN6AXHkRTz9h36hpAbciitbwg==",
                "json",
                "utf-8",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmyj6eopSZM0564p0OW0pImF54QJ59siEUJREdnmoz58dQ45c8h2OaJcEAlUkWHNNzpfJGCb1wACXYKzRryVVS3kPZdrOBfoqqMgHxWE3G0i8eMnZwYWAYKYEa7mW1/kPxJFxL1cOfab7EsJ4pr1vEDN4k0mpqUdRR/3JQAyFUg3mXBJvAm9dCF9XHl49ebvljHIfGabYITlAOr3bg3H5exDXGquEchAcVjxtRl827UvUxuNIkXDUOMcwtlE8MQUabH2xEG4TOi/tILi/gsUlXAjf1sbwTeAcsfF0hDMaH0tJyP3DHMEsPT6R7Vwe3NFlJhBNGBvZ4WPgTxXZ4zbOswIDAQAB",
                "RSA2");

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

        String out_trade_no = order_id;
        out_trade_no = URLDecoder.decode(out_trade_no, "UTF-8");
        request.setBizContent("{" +
                "\"out_trade_no\":\"" + out_trade_no + "\"," +
                "  }");
        while (true) {
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            String code = response.getCode();
            String tradeStatus = response.getTradeStatus();
            if ("40004".equals(code)) {
                Thread.sleep(3000);
            }
            if ("10000".equals(code)) {
                break;
            }
        }

        info.setFlag(true);
        return info;
    }

}
