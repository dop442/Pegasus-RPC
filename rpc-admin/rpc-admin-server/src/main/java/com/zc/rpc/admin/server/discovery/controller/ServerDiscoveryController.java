package com.zc.rpc.admin.server.discovery.controller;

import com.zc.rpc.admin.bean.DTO.PageDTO;
import com.zc.rpc.admin.bean.DTO.ServiceDetailInfoDTO;
import com.zc.rpc.admin.bean.DTO.ServiceListDTO;
import com.zc.rpc.admin.bean.POJO.ServerInfo;
import com.zc.rpc.protocol.meta.ServiceInfo;
import com.zc.rpc.admin.server.discovery.service.ServerDiscoveryService;
import com.zc.rpc.admin.utils.constants.HttpCode;
import com.zc.rpc.admin.utils.resp.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-06
 */
@RestController
public class ServerDiscoveryController {

    @Autowired
    private ServerDiscoveryService serverDiscoveryService;

    @PostMapping("/service/detail")
    public Result<ServiceDetailInfoDTO> getServiceDetail(
            @RequestParam("serviceName")String serviceName,
            @RequestParam("group")String group,
            @RequestParam("version")String version
    ) throws Exception {
        ServiceDetailInfoDTO serviceDetailInfo = serverDiscoveryService.getServiceDetail(serviceName, group, version);
        return new Result<>(HttpCode.SUCCESS, "成功", serviceDetailInfo);
    }

    @GetMapping("/service/all")
    public Result<ServiceListDTO> getAllService() throws Exception {
        List<ServiceInfo> serviceInfoList = serverDiscoveryService.getAllService();
        ServiceListDTO serviceListDTO = ServiceListDTO.builder()
                .serviceList(serviceInfoList)
                .count(serviceInfoList.size())
                .build();
        return new Result<>(HttpCode.SUCCESS, "成功", serviceListDTO);
    }


    @PostMapping("/service/query")
    public Result<PageDTO<ServerInfo>> queryService(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("serviceName")String serviceName
    ) throws Exception {
        PageDTO<ServerInfo> serviceListPage = serverDiscoveryService.getServiceListPage(serviceName, size, page);
        return new Result<>(HttpCode.SUCCESS, "成功", serviceListPage);
    }


    @PostMapping("/service/remove")
    public Result<String> removeService(
            @RequestParam("serviceName")String serviceName,
            @RequestParam("group")String group,
            @RequestParam("version")String version,
            @RequestParam("id")String id) throws Exception {
        serverDiscoveryService.removeService(serviceName, group, version, id);
        return new Result<>(HttpCode.SUCCESS, "移除成功", serviceName);
    }


}
