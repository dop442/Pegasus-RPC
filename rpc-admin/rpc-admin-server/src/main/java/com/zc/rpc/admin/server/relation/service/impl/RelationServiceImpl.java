package com.zc.rpc.admin.server.relation.service.impl;

import com.zc.rpc.admin.bean.DTO.RelationDTO;
import com.zc.rpc.admin.bean.POJO.Line;
import com.zc.rpc.admin.bean.POJO.Node;
import com.zc.rpc.admin.server.relation.service.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-04
 */
@Service
public class RelationServiceImpl implements RelationService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public RelationDTO getRelation() {
        Set<String> keys = redisTemplate.keys("consumer:*");
        Set<Node> nodeSet = new HashSet<>();
        Set<Line> lineSet = new HashSet<>();
        for (String key : keys) {
            String[] split = key.split(":");
            String consumer = split[1];
            String provider = split[2];
            nodeSet.add(new Node("consumer",consumer));
            nodeSet.add(new Node( "provider", provider));
            lineSet.add(new Line(consumer, provider));
        }
        Map<String,String> nodeRelation = new HashMap<>();
        Iterator<Node> nodeIterator = nodeSet.iterator();
        int consumerNum = 0;
        int providerNum = 0;
        while (nodeIterator.hasNext()){
            Node next = nodeIterator.next();
            if (next.id.equals("consumer")){
                consumerNum++;
                next.id = next.id + consumerNum;
                nodeRelation.put(next.text, next.id);
            }else {
                providerNum++;
                next.id = next.id + providerNum;
                nodeRelation.put(next.text, next.id);
            }
        }
        for (Line next : lineSet) {
            next.from = nodeRelation.get(next.from);
            next.to = nodeRelation.get(next.to);
        }
        return new RelationDTO(nodeSet, lineSet);
    }

}
