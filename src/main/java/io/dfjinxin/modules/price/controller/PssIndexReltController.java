package io.dfjinxin.modules.price.controller;

import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.validator.ValidatorUtils;
import io.dfjinxin.modules.price.entity.PssIndexReltEntity;
import io.dfjinxin.modules.price.service.PssIndexReltService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;



/**
 * 
 *
 * @author bourne
 * @email kuibobo@gmail.com
 * @date 2019-09-04 17:32:05
 */
@RestController
@RequestMapping("price/pssindexrelt")
@Api(tags = "指数预测结果")
public class PssIndexReltController {
    @Autowired
    private PssIndexReltService pssIndexReltService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("price:pssindexrelt:list")
    @ApiOperation("列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "indexName", value = "指数名称", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "dateFrom", value = "开始时间", required = false, dataType = "Date", paramType = "query"),
            @ApiImplicitParam(name = "dateTo", value = "结束时间", required = false, dataType = "Date", paramType = "query")
    })
    public R list(@RequestParam(name = "indexName") String indexName,
                  @RequestParam(name = "dateFrom") Date dateFrom,
                  @RequestParam(name = "dateTo") Date dateTo){
        PageUtils page = pssIndexReltService.queryPage(null);

        return R.ok().put("page", page);
    }
}
