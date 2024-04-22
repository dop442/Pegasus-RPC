package com.zc.rpc.admin.server.test.utils.methodTest;

import com.zc.rpc.admin.server.test.utils.ParameterUtil;
import com.zc.rpc.protocol.RpcProtocol;
import com.zc.rpc.protocol.enumeration.RpcType;
import com.zc.rpc.protocol.header.RpcHeaderFactory;
import com.zc.rpc.protocol.request.RpcRequest;
import com.zc.rpc.protocol.response.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-13
 */
public class TestMethodHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {

    private String serviceName;
    private String group;
    private String version;
    private String methodName;
    private String[] parametersStr;
    private String[] parameterTypesStr;
    @Getter
    private Object result;

    public TestMethodHandler(String serviceName, String group, String version, String methodName, String[] parametersStr, String[] parameterTypesStr) {
        this.serviceName = serviceName;
        this.group = group;
        this.version = version;
        this.methodName = methodName;
        this.parametersStr = parametersStr;
        this.parameterTypesStr = parameterTypesStr;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Object[] parameters = new Object[parametersStr.length];
        Class[] parameterType = new Class[parameterTypesStr.length];
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
        protocol.setHeader(RpcHeaderFactory.getRequestHeader("jdk", RpcType.REQUEST.getType()));
        for (int i = 0; i < parametersStr.length; i++) {
            parameters[i] = ParameterUtil.stringMapParameter(parameterTypesStr[i], parametersStr[i]);
            parameterType[i] = ParameterUtil.stringMapParameterType(parameterTypesStr[i]);
        }
        RpcRequest request = new RpcRequest();
        request.setClassName(serviceName);
        request.setGroup(group);
        request.setMethodName(methodName);
        request.setParameters(parameters);
        request.setParameterTypes(parameterType);
        request.setVersion(version);
        request.setAsync(false);
        request.setOneway(false);
        protocol.setBody(request);
        ctx.writeAndFlush(protocol);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> protocol) throws Exception {
        result = protocol.getBody().getResult();
    }
}
