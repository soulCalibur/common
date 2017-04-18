/*
 * @Project Name: ht_common
 * @File Name: ZuulFilter.java
 * @Package Name: com.ht.test.cloud.proxy.filter
 * @Date: 2017-4-14下午3:47:06
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.test.cloud.proxy.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-4-14下午3:47:06
 * @see
 */
@Component
public class MyFilter extends ZuulFilter{
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        Object accessToken = request.getParameter("token");
        System.out.println("业务健全Token为：>>"+accessToken);
        if(accessToken == null) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            try {
                ctx.getResponse().getWriter().write("未登录系统 加上token参数就登录了");
            }catch (Exception e){}
            return null;
        }
        return null;
    }
}