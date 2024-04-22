package com.zc.rpc.codec;

import com.zc.rpc.common.utils.SerializationUtils;
import com.zc.rpc.constants.RpcConstants;
import com.zc.rpc.protocol.RpcProtocol;
import com.zc.rpc.protocol.enumeration.RpcType;
import com.zc.rpc.protocol.header.RpcHeader;
import com.zc.rpc.protocol.request.RpcRequest;
import com.zc.rpc.protocol.response.RpcResponse;
import com.zc.rpc.serialization.api.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description 数据的解码类，将二进制流转化为对应数据的类
 * @Author Schrodinger's Cobra
 * @Date 2024-01-17
 */
public class RpcDecoder extends ByteToMessageDecoder implements RpcCodec {

    private static final Logger log = LoggerFactory.getLogger(RpcDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in,
                          List<Object> out)
            throws Exception {
        if (in.readableBytes()< RpcConstants.HEADER_TOTAL_LEN) {
            return;
        }
        in.markReaderIndex();
        short magic = in.readShort();
        if (magic!= RpcConstants.MAGIC) {
            throw new IllegalArgumentException("magic number is illegal, "+ magic);
        }
        byte msgType = in.readByte();
        byte status = in.readByte();
        long requestId = in.readLong();
        ByteBuf serializationTypeByteBuf = in.readBytes(
                SerializationUtils.MAX_SERIALIZATION_TYPE_COUNT);
        String serializationType = SerializationUtils
                .subString(serializationTypeByteBuf.toString(CharsetUtil.UTF_8));
        int dataLength = in.readInt();
        if (in.readableBytes()<dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        RpcType msgTypeEnum = RpcType.findByType(msgType);
        if (msgTypeEnum == null) {
            return;
        }
        RpcHeader header = new RpcHeader();
        header.setMagic(magic);
        header.setStatus(status);
        header.setRequestId(requestId);
        header.setSerializationType(serializationType);
        header.setMsgLen(dataLength);
        header.setMsgType(msgType);
        //TODO Serialization是拓展点
        Serialization serialization = getJdkSerialization(serializationType);
        switch (msgTypeEnum){
            case REQUEST:
            case HEARTBEAT_FROM_CONSUMER:
            case HEARTBEAT_TO_PROVIDER:
                RpcRequest request = serialization.deserialize(data, RpcRequest.class);
                if (request != null) {
                    RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(request);
                    log.info("解码得到的Request数据====>>>header：{}，body：{}", protocol.getHeader().toString(), protocol.getBody().toString());
                    out.add(protocol);
                }
                break;
            case RESPONSE:
            case HEARTBEAT_TO_CONSUMER:
            case HEARTBEAT_FROM_PROVIDER:
                RpcResponse response = serialization.deserialize(data, RpcResponse.class);
                if (response != null) {
                    RpcProtocol<RpcResponse> protocol = new RpcProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(response);
                    log.info("解码得到的Response数据====>>>header：{}，body：{}", protocol.getHeader().toString(), protocol.getBody().toString());
                    out.add(protocol);
                }
                break;
        }


    }
}
