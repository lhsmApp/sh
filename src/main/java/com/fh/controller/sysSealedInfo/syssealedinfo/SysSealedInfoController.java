package com.fh.controller.sysSealedInfo.syssealedinfo;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.fh.controller.base.BaseController;
import com.fh.entity.CommonBase;
import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.entity.PageResult;
import com.fh.util.AppUtil;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.SqlTools;
import com.fh.util.Jurisdiction;
import com.fh.util.Tools;
import com.fh.util.enums.BillType;
import com.fh.service.sysSealedInfo.syssealedinfo.SysSealedInfoManager;

/** 
 * 说明：业务封存信息
 * 创建人：FH Q313596790
 * 创建时间：2017-06-16
 */
@Controller
@RequestMapping(value="/syssealedinfo")
public class SysSealedInfoController extends BaseController {
	
	String menuUrl = "syssealedinfo/list.do"; //菜单地址(权限用)
	@Resource(name="syssealedinfoService")
	private SysSealedInfoManager syssealedinfoService;
	
//	/**修改
//	 * @param
//	 * @throws Exception
//	 */
//	@RequestMapping(value="/edit")
//	public ModelAndView edit() throws Exception{
//		CommonBase commonBase = new CommonBase();
//		logBefore(logger, Jurisdiction.getUsername()+"修改SysSealedInfo");
//		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
//		ModelAndView mv = this.getModelAndView();
//		PageData pd = new PageData();
//		pd = this.getPageData();
//		syssealedinfoService.edit(pd);
//		commonBase.setCode(0);
//		mv.addObject("msg","success");
//		mv.setViewName("save_result");
//		return mv;
//	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public @ResponseBody CommonBase edit() throws Exception{
		CommonBase commonBase = new CommonBase();
		logBefore(logger, Jurisdiction.getUsername()+"修改JgGrid");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		if(pd.getString("oper").equals("edit")){
			syssealedinfoService.edit(pd);
			commonBase.setCode(0);
		}
		
		
		/**此处为业务错误返回值，例如返回当前删除的信息含有业务关联字段，不能删除，自行设定setCode(返回码，客户端按码抓取并返回提示信息)和setMessage("自定义提示信息，提示给用户的")信息，并由界面进行展示。
		 * 此处不是异常返回的错误信息，异常返回错误信息统一由框架抓取异常。
		 */
		//commonBase.setCode(-1);
		//commonBase.setMessage("当前删除的信息含有业务关联字段，不能删除");
		return commonBase;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表SysSealedInfo");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		List<String> deptList = syssealedinfoService.deptList();//单位列表
		mv.setViewName("sysSealedInfo/syssealedinfo/syssealedinfo_list");
		mv.addObject("deptList", deptList);
		mv.addObject("billTypeList", BillType.values());
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/getPageList")
	public @ResponseBody PageResult<PageData> getPageList(JqPage page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表Betting");
		PageData pd = new PageData();
		pd = this.getPageData();
//		String keywords = pd.getString("keywords");				//关键词检索条件
//		if(null != keywords && !"".equals(keywords)){
//			pd.put("keywords", keywords.trim());
//		}
		
		String RPT_DEPT = pd.getString("RPT_DEPT");				//单位检索条件
		if(null != RPT_DEPT && !"".equals(RPT_DEPT)){
			pd.put("RPT_DEPT", RPT_DEPT.trim());
		}
		String BILL_TYPE = pd.getString("BILL_TYPE");				//单位检索条件
		if(null != BILL_TYPE && !"".equals(BILL_TYPE)){
			pd.put("BILL_TYPE", BILL_TYPE.trim());
		}
		String STATUS = pd.getString("STATUS");				//单位检索条件
		if(null != STATUS && !"".equals(STATUS)){
			pd.put("STATUS", STATUS.trim());
		}
		String filters = pd.getString("filters");				//多条件过滤条件
		if(null != filters && !"".equals(filters)){
			pd.put("filterWhereResult", SqlTools.constructWhere(filters,null));
		}
		
		page.setPd(pd);
		List<PageData> varList = syssealedinfoService.list(page);	//列出Betting列表
		int records = syssealedinfoService.count(pd);
		
		
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		result.setRowNum(page.getRowNum());
		result.setRecords(records);
		result.setPage(page.getPage());
		
		return result;
	}
	
	/**单位列表
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deptList")
	@ResponseBody
	public List<String> deptList() throws Exception{
		
		
		List<String>	deptList = syssealedinfoService.deptList();
		
		return deptList;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
