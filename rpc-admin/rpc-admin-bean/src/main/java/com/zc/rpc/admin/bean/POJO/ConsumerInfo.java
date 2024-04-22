package com.zc.rpc.admin.bean.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerInfo implements Serializable {
    private static final long serialVersionUID = 4911361851278111706L;

    public String consumerName;

    public List<String> providerList;

}
