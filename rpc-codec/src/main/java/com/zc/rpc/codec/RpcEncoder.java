package com.zc.rpc.codec;

import com.zc.rpc.common.utils.SerializationUtils;
import com.zc.rpc.protocol.RpcProtocol;
import com.zc.rpc.protocol.header.RpcHeader;
import com.zc.rpc.serialization.api.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @Description 数据的编码类，将数据转换成二进制流
 * @Author Schrodinger's Cobra
 * @Date 2024-01-17
 */
@Slf4j
public class RpcEncoder
        extends MessageToByteEncoder<RpcProtocol<Object>>
        implements RpcCodec {
    @Override
    protected void encode(ChannelHandlerContext ctx,
                          RpcProtocol<Object> msg,
                          ByteBuf byteBuf) throws Exception {
        RpcHeader header = msg.getHeader();
        // 魔数
        byteBuf.writeShort(header.getMagic());
        // 报文类型
        byteBuf.writeByte(header.getMsgType());
        // 状态
        byteBuf.writeByte(header.getStatus());
        // 消息 ID
        byteBuf.writeLong(header.getRequestId());
        String serializationType = header.getSerializationType();
        Serialization serialization = getJdkSerialization(serializationType);
        log.info("通过SPI获取序列化方式成功....");
        // 序列化类型
        byteBuf.writeBytes(SerializationUtils
                .paddingString(serializationType)
                .getBytes(StandardCharsets.UTF_8));
        byte[] data = serialization.serialize(msg.getBody());
        log.info("请求体的数据字节长度为 :{}", data.length);
        // 具体数据
        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);
    }
}
