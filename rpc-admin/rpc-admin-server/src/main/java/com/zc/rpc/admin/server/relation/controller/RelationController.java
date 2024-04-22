package com.zc.rpc.admin.server.relation.controller;

import com.zc.rpc.admin.bean.DTO.RelationDTO;
import com.zc.rpc.admin.bean.POJO.Line;
import com.zc.rpc.admin.bean.POJO.Node;
import com.zc.rpc.admin.server.relation.service.RelationService;
import com.zc.rpc.admin.utils.constants.HttpCode;
import com.zc.rpc.admin.utils.resp.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-19
 */
@RestController
public class RelationController {

    @Autowired
    private RelationService relationService;

    @GetMapping("/relation")
    public Result<RelationDTO> relation(){
        RelationDTO relationDTO = relationService.getRelation();
        return new Result<>(HttpCode.SUCCESS, "成功", relationDTO);
    }
}
