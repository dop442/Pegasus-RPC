package com.zc.rpc.test.consumer.handler;

import com.zc.rpc.consumer.common.RpcConsumer;
import com.zc.rpc.consumer.common.context.RpcContext;
import com.zc.rpc.proxy.api.future.RpcFuture;
import com.zc.rpc.protocol.RpcProtocol;
import com.zc.rpc.protocol.header.RpcHeaderFactory;
import com.zc.rpc.protocol.request.RpcRequest;
import com.zc.rpc.proxy.api.callback.AsyncRpcCallback;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-04
 */
@Slf4j
public class RpcConsumerHandlerTest {
    public static void main(String[] args) throws Exception{

    }

//    static RpcConsumer consumer = RpcConsumer.getInstance();
//    static RpcFuture rpcFuture;
//
//    static {
//        try {
//            rpcFuture = consumer.sendRequest(getRpcRequestProtocol());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

//    @Test
//    public void TestSync() throws Exception {
//        RpcConsumer consumer = RpcConsumer.getInstance();
//        RpcFuture future = consumer.sendRequest(getRpcRequestProtocol());
//        log.info("从消费者获得的数据：{}", future.get());
//        consumer.close();
//    }

    @Test
    public void TestAsync() throws Exception{
        RpcConsumer consumer = RpcConsumer.getInstance();
//        consumer.sendRequest(getRpcRequestProtocol());
        RpcFuture future = RpcContext.getContext().getRpcFuture();
        log.info("从服务消费者获取到的数据===>>>" + future.get());
        consumer.close();
    }

    @Test
    public void TestOneway() throws Exception{
        RpcConsumer consumer = RpcConsumer.getInstance();
//        consumer.sendRequest(getRpcRequestProtocol());
        log.info("无需获取返回的结果数据");
        consumer.close();
    }

//    @Test
//    public void TestCallback() throws Exception {
//        RpcConsumer consumer = RpcConsumer.getInstance();
////        RpcFuture rpcFuture = consumer.sendRequest(getRpcRequestProtocol());
//        rpcFuture.addCallback(new AsyncRpcCallback() {
//            @Override
//            public void onSuccess(Object result) {
//                log.info("回调方法从消费者获取的数据：{}", result);
//            }
//
//            @Override
//            public void onException(Exception e) {
//                log.info("抛出了异常：{}", e.toString());
//            }
//        });
//        Thread.sleep(200);
//        consumer.close();
//    }



    private static RpcProtocol<RpcRequest> getRpcRequestProtocol(){
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
        protocol.setHeader(RpcHeaderFactory.getRequestHeader("jdk"));
        RpcRequest request = new RpcRequest();
        request.setClassName("com.zc.rpc.test.api.DemoService");
        request.setGroup("zc");
        request.setMethodName("hello");
        request.setParameters(new Object[]{"hello!zc!"});
        request.setParameterTypes(new Class[]{String.class});
        request.setVersion("1.0.0");
        request.setAsync(false);
        request.setOneway(false);
        protocol.setBody(request);
        return protocol;
    }
}
