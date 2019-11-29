package io.dfjinxin.modules.firstpage;

import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.analyse.service.WpBaseIndexValService;
import io.dfjinxin.modules.price.service.PssPriceEwarnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Desc:
 * @Author: z.h.c
 * @Date: 2019/10/09 15:39
 * @Version: 1.0
 */

@RestController
@RequestMapping("price/second")
@Api(tags = "二级页面(商品总览)")
public class SecondPageViewController {

    private static Logger logger = LoggerFactory.getLogger(SecondPageViewController.class);

    @Autowired
    private PssPriceEwarnService pssPriceEwarnService;
    @Autowired
    private WpBaseIndexValService wpBaseIndexValService;

    /**
     * @Desc: 二级页面(商品总览)
     * @Param: 3类商品id[commId]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/11/12 18:52
     */
    @GetMapping("/view/{commId}")
    @ApiOperation(value = "二级页面(商品总览)-展示", notes = "根据3级商品id 获取相应该商品所有4级商品 指标信息 eg:58")
    public R queryIndexTypeByCommId(@PathVariable("commId") Integer commId) {
        logger.info("二级页面(商品总览),req commId:{}", commId);
//        指标类型信息
        List<Map<String, Object>> list = wpBaseIndexValService.secondPageIndexType(commId);
        Map<String, Object> resMap = new HashMap<>();
        for (Map<String, Object> var : list) {
            for (String key : var.keySet()) {
                resMap.put(key, var.get(key));
            }
        }

        //其它数据
        Map<String, Object> zfMap = pssPriceEwarnService.secondPageDetail(commId);
        resMap.putAll(zfMap);
        return R.ok().put("data", resMap);
    }

    /**
     * @Desc: 二级页面(商品总览) -根据3类商品、时间区域、指标类型分页查询规格品指标信息
     * @Param: [commId, indexType, startDate, endDate, pageIndex, pageSize]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/11/29 12:20
     */
    @GetMapping("/indexType/byDate/{commId}")
    @ApiOperation(value = "二级页面(商品总览)-根据3类商品、时间区域、指标类型分页查询规格品指标信息",
            notes = "根据3级商品id 获取指定时间、指标类型规格品指标信息 eg:58")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indexType", value = "指标类型", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "页码", required = false, dataType = "Int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = false, dataType = "Int", paramType = "query")
    })
    public R queryPageIndexValByDate(
            @PathVariable("commId") Integer commId,
            @RequestParam(value = "indexType", required = true) String indexType,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize

    ) {
        Map<String, Object> params = new HashMap() {{
            put("indexType", indexType);
            put("startDate", startDate);
            put("endDate", endDate);
            put("commId", commId);
            put("pageIndex", pageIndex);
            put("pageSize", pageSize);
        }};
        PageUtils page = wpBaseIndexValService.queryPageByDate(params);

        return R.ok().put("page", page);
    }


    /**
     * @Desc: 二级页面(商品总览)-折线图:根据4类商品id、指标类型、指标名称、时间区域统计规格品各频度下各区域的指标信息
     * @Param: [commId, indexType, startDate, endDate]
     * @Return: io.dfjinxin.common.utils.R
     * @Author: z.h.c
     * @Date: 2019/11/29 12:20
     */
    @GetMapping("/lineChart/{commId}")
    @ApiOperation(value = "二级页面(商品总览)-折线图:根据4类商品id、指标类型、指标名称、时间区域统计规格品各频度下各区域的指标信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indexType", value = "指标类型", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", required = true, dataType = "String", paramType = "query"),
    })
    public R queryLineChartByCondition(@PathVariable("commId") Integer commId,
                                  @RequestParam(value = "indexType") String indexType,
                                  @RequestParam(value = "indexName") String indexName,
                                  @RequestParam(value = "startDate") String startDate,
                                  @RequestParam(value = "endDate") String endDate) {

        Map<String, Object> params = new HashMap() {{
            put("indexType", indexType);
            put("indexName", indexName);
            put("startDate", startDate);
            put("endDate", endDate);
            put("commId", commId);
        }};
        Map<String, Object> map = wpBaseIndexValService.queryLineChartByCondition(params);

        return R.ok().put("data", map);
    }


}
