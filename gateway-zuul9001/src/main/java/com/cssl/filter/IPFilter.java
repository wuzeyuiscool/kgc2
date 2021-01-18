package com.cssl.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Component
public class IPFilter extends ZuulFilter {

    Logger logger= LoggerFactory.getLogger(getClass());

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx= RequestContext.getCurrentContext();
        HttpServletRequest req=ctx.getRequest();
        String ipAddr=this.getIpAddr(req);
        System.out.println("请求IP地址为："+ipAddr);
        logger.info("请求IP地址为：[{}]",ipAddr);
        //配置本地IP白名单，生产环境可放入数据库或者redis中
        List<String> ips=new ArrayList<String>();
        ips.add("127.0.0.1");
        ips.add("192.168.174.1");

        if(!ips.contains(ipAddr)){
            System.out.println("IP地址校验不通过！！！");
            ctx.setResponseStatusCode(401);
            ctx.setSendZuulResponse(false);
            ctx.getResponse().setContentType("text/html;charset=utf-8");
            ctx.setResponseBody("{\"msg\":\"IP地址不允许访问！\"}");
        }else{
            System.out.println("IP地址校验通过！");
            ctx.setSendZuulResponse(true);
        }

        return null;
    }

    /**
     * 获取Ip地址
     * @param request
     * @return
     */
    public  String getIpAddr(HttpServletRequest request){

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
