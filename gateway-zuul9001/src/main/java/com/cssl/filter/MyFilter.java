package com.cssl.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class MyFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx= RequestContext.getCurrentContext();
        System.out.println("IPFilter:"+ctx.sendZuulResponse());
        return ctx.sendZuulResponse();
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext ctx= RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String uri = request.getRequestURI();
        if(uri.endsWith("login")){
            System.out.println("放过登录请求！");
            ctx.setSendZuulResponse(true);
            return null;
        }

        String token = request.getHeader("token");
        System.out.println("MyFilter run token:"+token);

        if(token == null || token.isEmpty()){
            System.out.println("Token校验不通过！！！");
            ctx.setResponseStatusCode(403);
            ctx.setSendZuulResponse(false);
            ctx.getResponse().setContentType("text/html;charset=utf-8");
            ctx.setResponseBody("{\"msg\":\"Token为空，不允许访问！\"}");
        }else{
            System.out.println("Token校验通过！");
            ctx.setSendZuulResponse(true);
        }
        return null;
    }
}
