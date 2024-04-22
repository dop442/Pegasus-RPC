package com.zc.rpc.proxy.api.future;

import com.zc.rpc.protocol.RpcProtocol;
import com.zc.rpc.protocol.request.RpcRequest;
import com.zc.rpc.protocol.response.RpcResponse;
import com.zc.rpc.proxy.api.callback.AsyncRpcCallback;
import com.zc.rpc.proxy.api.threadpool.ClientThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-04
 */
public class RpcFuture extends CompletableFuture<Object> {

    private static final Logger log = LoggerFactory.getLogger(RpcFuture.class);
    private Sync sync;
    private RpcProtocol<RpcRequest> requestRpcProtocol;
    private RpcProtocol<RpcResponse> responseRpcProtocol;
    private long startTime;
    private long responseTimeThreshold = 5000;
    private List<AsyncRpcCallback> pendingCallbacks = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();

    public RpcFuture(RpcProtocol<RpcRequest> requestRpcProtocol) {
        this.sync = new Sync();
        this.requestRpcProtocol = requestRpcProtocol;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public boolean isDone() {
        return sync.isDone();
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        log.info("尝试获取，但此时RpcFuture中为空值，获取操作被阻塞....");
        sync.acquire(-1);
        if (this.responseRpcProtocol!=null) {
            log.info("RpcFuture得到了响应值，可通过get正常获取");
            return this.responseRpcProtocol.getBody().getResult();
        }else {
            return null;
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean success = sync.tryAcquireNanos(-1, unit.toNanos(timeout));
        if (success) {
            if (this.responseRpcProtocol != null) {
                return this.responseRpcProtocol.getBody().getResult();
            }else {
                return null;
            }
        }else {
            StringBuilder builder = new StringBuilder("Timeout exception. Request id:");
            builder.append(this.requestRpcProtocol.getHeader().getRequestId());
            builder.append(". Request class name: ");
            builder.append(this.requestRpcProtocol.getBody().getClassName());
            builder.append(". Request method: ");
            builder.append(this.requestRpcProtocol.getBody().getMethodName());
            throw new RuntimeException(builder.toString());
        }
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    public void done(RpcProtocol<RpcResponse> responseRpcProtocol){
        this.responseRpcProtocol = responseRpcProtocol;
        sync.release(1);
        invokeCallbacks();
        long responseTime = System.currentTimeMillis() - startTime;
        if (responseTime>this.responseTimeThreshold) {
            log.warn("Service response time is too slow. Request id is:{}. Response Time is {}",
                    responseRpcProtocol.getHeader().getRequestId(), responseTime);
        }
    }

    private void runCallback(final AsyncRpcCallback callback){
        final RpcResponse response = this.responseRpcProtocol.getBody();
        ClientThreadPool.submit(()->{
            if (!response.isError()){
                callback.onSuccess(response.getResult());
            }else {
                callback.onException(new RuntimeException("Response error", new Throwable(response.getError())));
            }
        });
    }

    public RpcFuture addCallback(AsyncRpcCallback callback){
        lock.lock();
        try{
            if (isDone()) {
                // 如果提供者给到响应结果
                runCallback(callback);
            } else {
                // 如果提供者还没给到响应结果
                this.pendingCallbacks.add(callback);
            }
        }finally {
            lock.unlock();
        }
        return this;
    }

    public void invokeCallbacks(){
        lock.lock();
        try{
            for (AsyncRpcCallback callback : pendingCallbacks) {
                runCallback(callback);
            }
        }finally {
            lock.unlock();
        }
    }


    static class Sync extends AbstractQueuedSynchronizer{
        private static final long serialVersionUID = 1L;

        private final int done = 1;
        private final int pending = 0;

        @Override
        protected boolean tryAcquire(int arg) {
            return getState() == done;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getState()==pending) {
                if (compareAndSetState(pending, done)) {
                    return true;
                }
            }
            return false;
        }

        public boolean isDone(){
            getState();
            return getState()==done;
        }
    }



}
