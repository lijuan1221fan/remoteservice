package com.visionvera.remoteservice.controller;


import com.visionvera.remoteservice.service.AreaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/area")
public class AreaController {

    @Resource
    private AreaService areaService;

    @RequestMapping(value = "/getArea", method = RequestMethod.GET)
    public Map<String, Object> getArea(String id) {
        return areaService.getArea(id);
    }
}
