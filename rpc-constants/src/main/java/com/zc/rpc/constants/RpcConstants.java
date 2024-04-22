package com.zc.rpc.constants;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-01-17
 */
public class RpcConstants {

    public static final short MAGIC = 0x10;

    public static final int HEADER_TOTAL_LEN = 32;

    public static final int SERVICE_WEIGHT_MIN = 1;

    public static final int SERVICE_WEIGHT_MAX = 100;

    public static final int DEFAULT_RETRY_INTERVAL = 1000;

    public static final int DEFAULT_RETRY_TIMES = 3;

    public static final String REFLECT_TYPE_JDK = "jdk";

    public static final String REFLECT_TYPE_CGLIB = "cglib";

    public static final String SERIALIZATION_JDK = "jdk";

    public static final String SERIALIZATION_PROTOSTUFF = "protostuff";

    public static final String INIT_METHOD_NAME = "init";

    public static final String SERVICE_LOAD_BALANCER_RANDOM = "random";

    public static final String SERVICE_ENHANCED_LOAD_BALANCER_PREFIX = "enhanced_";

    public static final String CODEC_ENCODER = "encoder";

    public static final String CODEC_DECODER = "decoder";

    public static final String CODEC_HANDLER = "handler";
    public static final String CODEC_SERVER_IDLE_HANDLER = "server-idle-handler";
    public static final String CODEC_CLIENT_IDLE_HANDLER = "client-idle-handler";
    public static final String HEARTBEAT_PONG = "pong";
    public static final String HEARTBEAT_PING = "ping";
}
