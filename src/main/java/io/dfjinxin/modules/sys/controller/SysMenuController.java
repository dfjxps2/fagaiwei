/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.controller;

import io.dfjinxin.common.annotation.SysLog;
import io.dfjinxin.common.exception.RRException;
import io.dfjinxin.common.utils.Constant;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.modules.sys.entity.GovRootMenuEntity;
import io.dfjinxin.modules.sys.entity.SysMenuEntity;
import io.dfjinxin.modules.sys.service.ShiroService;
import io.dfjinxin.modules.sys.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import io.dfjinxin.common.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 系统菜单
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/menu")
@Api(tags = "SysMenuController", description = "菜单管理")
public class SysMenuController {
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private ShiroService shiroService;

	/**
	 * 导航菜单
	 */
	@GetMapping("/nav")
	public R nav(){
		List<SysMenuEntity> menuList = sysMenuService.getUserMenuList(1);
		Set<String> permissions = shiroService.getUserPermissions("1");
		return R.ok().put("menuList", menuList).put("permissions", permissions);
	}

	/**
	 * 所有菜单列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:menu:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysMenuService.queryPage(params);
		return R.ok().put("page", page);
	}

	/**
	 * 选择菜单(添加、修改菜单)
	 */
	@GetMapping("/select")
	@RequiresPermissions("sys:menu:select")
	public R select(){
		//查询列表数据
		List<SysMenuEntity> menuList = sysMenuService.queryNotButtonList();

		//添加顶级菜单
		SysMenuEntity root = new SysMenuEntity();
		root.setMenuId(0);
		root.setMenuName("一级菜单");
		root.setPareMenuId(-1);
		root.setMenuState(1);
		menuList.add(root);

		return R.ok().put("menuList", menuList);
	}

	/**
	 * 菜单信息
	 */
	@GetMapping("/info")
	@RequiresPermissions("sys:menu:info")
	public R info(@RequestParam("menuId") int menuId){
		SysMenuEntity menu = sysMenuService.getById(menuId);
		return R.ok().put("menu", menu);
	}

	/**
	 * 保存或修改菜单
	 */
	@SysLog("保存菜单")
	@PostMapping("/saveOrUpdate")
	@RequiresPermissions("sys:menu:save")
	public R save(@RequestBody SysMenuEntity menu){
		//数据校验
		verifyForm(menu);
        if( menu.getMenuId() ==0){
			sysMenuService.save(menu);
		}else {
			sysMenuService.updateById(menu);
		}

		return R.ok();
	}


	/**
	 * 删除
	 */
	@SysLog("删除菜单")
	@PostMapping("/delete")
	@RequiresPermissions("sys:menu:delete")
	@ApiOperation("删除菜单")
	public R delete( @RequestParam(value = "mid[]") int[] menuId){

		//判断是否有子菜单或按钮
		for(int d : menuId){
			List<SysMenuEntity> menuList = sysMenuService.queryListParentId(d);
			if(menuList.size() > 0){
				return R.error("请先删除子菜单");
			}
			sysMenuService.delete(d);
		}

		return R.ok();
	}

	/**
	 * 菜单下拉框
	 */
	@GetMapping("/userMenuInfo")
	public R userMenuInfo( ){
		List<Map<String,Object>> menu = sysMenuService.serMenuInfo();
		return R.ok().put("menu", menu);
	}

	/**
	 * 验证参数是否正确
	 */
	private void verifyForm(SysMenuEntity menu){
		if(StringUtils.isBlank(menu.getMenuName())){
			throw new RRException("菜单名称不能为空");
		}

		if(menu.getMenuType()!=1 && menu.getPareMenuId() ==0){
			throw new RRException("上级菜单不能为空");
		}

		//菜单
		if(menu.getMenuType() != Constant.MenuType.BUTTON.getValue()){
			if(StringUtils.isBlank(menu.getMenuRouter())){
				throw new RRException("菜单路由不能为空");
			}
		}

		//上级菜单类型
		int parentType = Constant.MenuType.CATALOG.getValue();
		if(menu.getPareMenuId() != 0){
			SysMenuEntity parentMenu = sysMenuService.getById(menu.getPareMenuId());
			parentType = parentMenu.getMenuType();
		}

		//目录、菜单
		if(menu.getMenuType() == Constant.MenuType.CATALOG.getValue() ||
				menu.getMenuType() == Constant.MenuType.MENU.getValue()){
			if(parentType != Constant.MenuType.CATALOG.getValue()){
				throw new RRException("上级菜单只能为目录类型");
			}
			return ;
		}

		//按钮

	}
}
