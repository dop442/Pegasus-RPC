package com.zc.rpc.admin.bean.DTO;

import com.zc.rpc.admin.bean.POJO.Line;
import com.zc.rpc.admin.bean.POJO.Node;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-19
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RelationDTO implements Serializable {

    private static final long serialVersionUID = -4718772478980745136L;

    Set<Node> nodeSet;

    Set<Line> lineSet;

}
