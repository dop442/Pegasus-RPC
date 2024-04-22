package com.zc.rpc.admin.server.weight.contorller;

import com.zc.rpc.admin.bean.DTO.WeightProviderDTO;
import com.zc.rpc.admin.bean.DTO.WeightProviderListDTO;
import com.zc.rpc.admin.bean.POJO.ProviderWeight;
import com.zc.rpc.admin.server.weight.service.WeightService;
import com.zc.rpc.admin.utils.constants.HttpCode;
import com.zc.rpc.admin.utils.resp.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-19
 */
@RestController
@RequestMapping("/weight")
public class WeightController {


    @Autowired
    private WeightService weightService;


    @PostMapping("/update")
    public Result<WeightProviderDTO> updateWeight(
            @RequestParam("serviceName")String serviceName,
            @RequestParam("group") String group,
            @RequestParam("version") String version,
            @RequestParam("id") String id,
            @RequestParam("weight") int weight
    ) throws Exception {
        WeightProviderDTO weightDTO = new WeightProviderDTO(serviceName, group, version, weight);
        weightService.updateWeight(serviceName, group, version, id, weight);
        return new Result<>(HttpCode.SUCCESS, "成功", weightDTO);
    }

    @PostMapping("/delete")
    public Result<Map<String, String>> deleteWeight(
            @RequestParam("serviceName")String serviceName,
            @RequestParam("group") String group,
            @RequestParam("version") String version,
            @RequestParam("id") String id
    ) throws Exception {
        weightService.updateWeight(serviceName, group, version, id, 0);
        Map<String, String> map = new HashMap<>();
        map.put("serviceName", serviceName);
        map.put("serviceGroup", group);
        map.put("serviceVersion", version);
        return new Result<>(HttpCode.SUCCESS, "成功", map);
    }

    @PostMapping("/query")
    public Result<WeightProviderListDTO> queryWeight(
            @RequestParam("serviceName")String serviceName,
            @RequestParam("group") String group,
            @RequestParam("version") String version
    ) throws Exception {

        List<ProviderWeight> providerList = weightService.queryWeight(serviceName, group, version);
        return new Result<>(HttpCode.SUCCESS, "成功", new WeightProviderListDTO(serviceName, group, version, providerList));
    }

//    @PostMapping("/create")
//    public Result<String> createWeight(
//            @RequestParam("serviceName")String serviceName,
//            @RequestParam("group") String group,
//            @RequestParam("version") String version,
//            @RequestParam("id") String id,
//            @RequestParam("ip") String ip,
//            @RequestParam("weight") int weight
//    ){
//        return new Result<>(HttpCode.SUCCESS, "成功", "id:"+id+" 权重更新成功");
//    }



}
