package com.housaire;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.route.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * Desc:
 *
 * @author: Zhang Kai
 * @email: <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @date: 2020/5/18
 */
public class GatewayZuulFilter extends RibbonRoutingFilter {

    public GatewayZuulFilter(ProxyRequestHelper helper, RibbonCommandFactory<?> ribbonCommandFactory) {
        super(helper, ribbonCommandFactory);
    }

    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        if (!ctx.getRequest().getRequestURI().startsWith("/api/gateway")) {
            return false;
        }
        return (ctx.getRouteHost() == null && ctx.sendZuulResponse());
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        this.helper.addIgnoredHeaders();
        try {
            RibbonCommandContext commandContext = buildCommandContext(context);
            ClientHttpResponse response = forward(commandContext);
            setResponse(response);
            return response;
        }
        catch (ZuulException ex) {
            ex.printStackTrace();
            context.set("error.status_code", ex.nStatusCode);
            context.set("error.message", ex.errorCause);
            context.set("error.exception", ex);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            context.set("error.status_code",
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            context.set("error.exception", ex);
        }
        return null;
    }

    @Override
    protected RibbonCommandContext buildCommandContext(RequestContext context) {
        HttpServletRequest request = context.getRequest();

        MultiValueMap<String, String> headers = this.helper
                .buildZuulRequestHeaders(request);
        MultiValueMap<String, String> params = this.helper
                .buildZuulRequestQueryParams(request);
        String verb = getVerb(request);
        InputStream requestEntity = getRequestBody(request);
        if (request.getContentLength() < 0) {
            context.setChunkedRequestBody();
        }

        String serviceId = (String) context.get("serviceId");
        Boolean retryable = (Boolean) context.get("retryable");

        String target = params.getFirst("target");
        String channelFlag = getChannelFlag(params.getFirst("serviceParams"));

        String uri = target.replaceAll("\\.", "/");

        // remove double slashes
        uri = "/" + uri.replace("//", "/");

        return new RibbonCommandContext(channelFlag, verb, uri, retryable, headers, params,
                requestEntity);
    }

    private String getChannelFlag(String serviceParams){
        String channelFlag = "";
        JSONObject json = JSON.parseObject(serviceParams);
        if(json.containsKey("channelFlag")){
            channelFlag = (String)json.get("channelFlag");
        }
        return channelFlag;
    }
}
