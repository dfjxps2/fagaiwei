package io.dfjinxin.modules.analyse.controller;

import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.analyse.service.WpCommIndexValService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 价格监测子系统-价格分析-多维分析
 *
 * @author z.h.c
 * @email z.h.c@126.com
 * @date 2019-09-10 15:28:19
 */
@RestController
@RequestMapping("analyse/multidimensional")
@Api(tags = "MultiAnalyseController", description = "价格分析-多维分析")
public class MultiAnalyseController {

    @Autowired
    private WpCommIndexValService wpCommIndexValService;

    @GetMapping("/detail/{commId}")
    @ApiOperation("多维分析详情页-根据3级商品id 获取相应该商品所有4级商品 指标信息")
    public R queryIndexTypeByCommId(@PathVariable("commId") Integer commId) {

        List<Map<String, Object>> map  = wpCommIndexValService.queryLevel4CommInfo(commId);
        return R.ok().put("data", map);
    }
}