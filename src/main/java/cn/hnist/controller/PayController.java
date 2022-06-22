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
                "j3oqnLkONi0i+Vuggyj9AoFRfuheEQpTK3hArN8mAoDjc0FvZb0jPAM806UCR4pmESKpRdv0/3Wc4iDdAoGAFh9+uAzjI2fKg5QY/hsUvANpMaZ6c4f7aN/3zqWvTvi7/xybj1McmB+rFLo9J9wWCRCGl96beYWZxiGYOe7KLxszQjmUIBx7ehJs0Uqh2jAspt4nUFtoLfIdZ1C5RfWVLuW/xRfizTawTnSwl4y2bPxCx2jE4kNNsVPyRmZ95tECgYBQMzB0401HIWNsR6j8ej9M2x1+/f2Pj0dE+yQpW71H9RAVGqQjjZgfy5wlUYPejSRVTEunzprEWos2b2pWJVm/dKt2F3oIFb5Qs4Yuzj/vSWHw+41RjhniMMwIImfEfVHuiIEXKCh5G9wLFXROSd5ULXxriycF3/rE4BsK3Hg3HA==",
                "json",
                "utf-8",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoGaKMmnTIzQCdIthV2PTKguhR35kaMYIi+bK/8ocW1BWH9EzgvwQmTUl5C4fzPYNOgewVVjhzqOAgJzUIk+jZMPCIyL2swSpFtIDizQTQltVT7jG7P0ykgspPxPCRbQWjhsJcgwAfhAVbwPL447yb9aqDwkiXhieABq1zc+Isp7SmEJSi01Dq/yo9QGfERsKX1toyYvqnFZMOHJ+uMenVQ9qOFK05J+nQ3s+/DHzZY4fDpPi7dJtTH24l0hG3cLgBuJE8HT8dNiaE+hPSvd9i7QfK2dJiOzDoBEHm6ry9yv0i73tLng9F2/t6e8iJpBG1zltwb7MT8Sx+J/Ceqr31QIDAQAB",
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
        alipayRequest.setReturnUrl("http://localhost/user/order");
        alipayRequest.setNotifyUrl("http://localhost/payOk");
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
                "2016092900626932",
                "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCzA277CLCcwH5Fuul9k6HyokP9uRCQ/gqFxJCTbmckGxif4R4oqb95FNqqa9ufMcQfGQYHjv96PnpYgpPMY2yAPTQVzefqWb8eZzeRCFwDFjBvB5jjl470LPFuQAmAB0JiVEcBN7aS2E9L5CCs1nAyj4IHQuz5yulSlXrhkA8/iR1Bk4LPjUTNw5JoktmjFOzfZT83ZS/Ro2pSVy6xRHoXwpvBaY+ap1gTO0M/j3jb/yXTHGbowB7BgxIvmDz32pLjMDLdFhQZOsas7Y1IMngf6BiKUeHUt/c7QrOQCpCYpJxoNvZXkx93F04EKc8xG/LDuxxvAsfFjQ/2dreEPsLrAgMBAAECggEAKl+gsIlhDOm/ug/BXzlH92ATigZ5dmi74ughdNSIU5KaZJXsBqIPscCO9hIr3WXxT48jzzw5Gx2Y5kSaaaILN9vE7Xhc8ismQzMZgxogdZaWBmIc/ZqNexz8VSWRWh3dx9q9J/uhGuda9dLdLSxhSIaRt9hOsd4nFqZNfQEVqusLJL5zpfccFNH31N6fXFeQeIDwwgQxZvPsbqoViPE5YquD6CC5AabaG0cfiJFlbylAReoGGTioR0AWNZPVv8FSylAuZT007jmxRj5r0F46LLLpKhexAzjAunjC/yA4BpQWsKi2BioaoFRpwum96baSD16JkZcJZJbLFZZAFJL6MQKBgQDp2imxr6K+FfAT/eDf2iBt9dAKgWov/jUbwT3mdN+pyia8/X0VytOX8kb5XpuI1f7mPXnCicveFNAuMGl1APf4U6eOqqIZNeeoFeGIGcrO3QM55C0Rc+EBwaAwqh4t2a5euUXRtSIwfjPPvP1KM1yWISrd/8ctBWC1eaOUp5gK4wKBgQDD967bInpcAuTtDQEVefW4UqEup71jrcimjBtGkZleOX6FA+jCtnco/6YalphANyIrf53KSgRI3Hu/trypku37OHfYzKgwwsWap8CWWvoX8YDaAwVkUp5bW0dtbjZUeY0mYuSOnUHfVl+bAMzCdGhQ5yMZSSbtarRvzWImh0A+WQKBgQDR9Y9Duna6hgXUXTf+o+BD+dR86i7Fwk46iiiidkKuNhLL0ifxXfbWUK+Ki3fHxqKiY4WjtYZ18WfsYIj+hQ0eRzWEcQ0y52QDVQn14Xpmeqa/3m7cYZcetZK2vrZz4iKdZ2kZXLDCqzyngn5DlZPGMydCxLg2YsKujPTirgpjkwKBgDlW96rW48aYcjOKYo7OKL/OLathGWFZMCTLSg/T+nQ/jm2NP0X57dPpOcj9136yoyTnKlxOXAKVwP/PkFRt0YR4eVjOSUg7NWaTPx6/b/4hg/pGeAVqxzsj8jpqzSrahhp9RTMVgHjA5SiNi1w6g2totjRn2yQM3M754YY0fO/BAoGAc+ifvL6ugRGX6PekqmwLwZ/+0wre2vRPwgdGfElZh8alR3reYyTvGRfCvMxuWpbQB9JgXs3D/Cw0KRY8fAJEQOHvSJ2mXgPI2L7XQ5eIPHj+H8PnxVcovwdEJwSGwRN3Z2tMl3TVzjVzuRnbHure/fgKB+Ret03FRTNhSFH/igk=",
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
