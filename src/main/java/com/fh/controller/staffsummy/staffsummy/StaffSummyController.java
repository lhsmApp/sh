package com.fh.controller.staffsummy.staffsummy;

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
import com.fh.controller.common.TmplUtil;
import com.fh.entity.CommonBase;
import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.entity.PageResult;
import com.fh.entity.StaffDetailModel;
import com.fh.entity.SysSealed;
import com.fh.entity.system.User;
import com.fh.util.AppUtil;
import com.fh.util.Const;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.SqlTools;
import com.fh.util.Jurisdiction;
import com.fh.util.Tools;
import com.fh.util.date.DateFormatUtils;
import com.fh.util.date.DateUtils;
import com.fh.util.enums.BillType;
import com.fh.util.enums.DurState;

import net.sf.json.JSONArray;

import com.fh.service.fhoa.department.impl.DepartmentService;
import com.fh.service.staffsummy.staffsummy.StaffSummyManager;
import com.fh.service.sysSealedInfo.syssealedinfo.impl.SysSealedInfoService;
import com.fh.service.system.dictionaries.impl.DictionariesService;
import com.fh.service.tmplConfigDict.tmplconfigdict.impl.TmplConfigDictService;
import com.fh.service.tmplconfig.tmplconfig.impl.TmplConfigService;

/** 
 * 说明：工资汇总
 * 创建人：zhangxiaoliu
 * 创建时间：2017-07-07
 */
@Controller
@RequestMapping(value="/staffsummy")
public class StaffSummyController extends BaseController {
	
	String menuUrl = "staffsummy/list.do"; //菜单地址(权限用)
	@Resource(name="staffsummyService")
	private StaffSummyManager staffsummyService;
	@Resource(name="tmplconfigService")
	private TmplConfigService tmplconfigService;
	@Resource(name="syssealedinfoService")
	private SysSealedInfoService syssealedinfoService;
	@Resource(name="tmplconfigdictService")
	private TmplConfigDictService tmplconfigdictService;
	@Resource(name="dictionariesService")
	private DictionariesService dictionariesService;
	@Resource(name="departmentService")
	private DepartmentService departmentService;

	//表名
	String TableName = "tb_staff_summy";
	//枚举类型  1工资明细,2工资汇总,3公积金明细,4公积金汇总,5社保明细,6社保汇总,7工资接口,8公积金接口,9社保接口
	String TypeCode = BillType.SALLARY_SUMMARY.getNameKey();
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表StaffSummy");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		PageData pd = this.getPageData();
		
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("staffsummy/staffsummy/staffsummy_list");
		
		TmplUtil tmpl = new TmplUtil(tmplconfigService, tmplconfigdictService, dictionariesService, departmentService);
		String jqGridColModel = tmpl.generateStructure(TableName, "001001", 1);
		
		//SqlUserdata = tmpl.getSqlUserdata();
		//默认值
		//DefaultValueList = tmpl.getDefaultValueList();
		//字典
		//DicList = tmpl.getDicList();
		//前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		//ColumnsList = tmpl.getColumnsList();
		
		mv.addObject("jqGridColModel", jqGridColModel);
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/getPageList")
	public @ResponseBody PageResult<PageData> getPageList(JqPage page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表StaffSummy");
		PageData pd = new PageData();
		pd = this.getPageData();
		//
		String UserCode = pd.getString("UserCode");	
		if(null != UserCode && !"".equals(UserCode)){
			pd.put("UserCode", UserCode.trim());
		}
		//多条件过滤条件
		String filters = pd.getString("filters");
		if(null != filters && !"".equals(filters)){
			pd.put("filterWhereResult", SqlTools.constructWhere(filters,null));
		}
		//页面显示数据的年月
		//pd.put("SystemDateTime", SystemDateTime);
		//页面显示数据的二级单位
		//pd.put("DepartCode", DepartCode);
		page.setPd(pd);
		//List<PageData> varList = staffdetailService.JqPage(page);	//列出Betting列表
		//int records = staffdetailService.countJqGridExtend(page);
		PageData userdata = null;
		//if(SqlUserdata!=null && !SqlUserdata.toString().trim().equals("")){
			//底行显示的求和与平均值字段
		//	pd.put("Userdata", SqlUserdata.toString());
		//	userdata = staffdetailService.getFooterSummary(page);
		//}
		
		PageResult<PageData> result = new PageResult<PageData>();
		//result.setRows(varList);
		//result.setRowNum(page.getRowNum());
		//result.setRecords(records);
		//result.setPage(page.getPage());
		//result.setUserdata(userdata);
		
		return result;
	}
	
	 /**上报
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/report")
	public @ResponseBody CommonBase report() throws Exception{
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "report")){return null;} //校验权限	
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		
		String checkState = CheckState(this.getPageData());
		if(checkState!=null && checkState.trim() != ""){
			commonBase.setCode(2);
			commonBase.setMessage(checkState);
		} else {
			User user = (User) Jurisdiction.getSession().getAttribute(Const.SESSION_USERROL);
			String userId = user.getUSER_ID();
           String time = DateUtils.getCurrentTime(DateFormatUtils.DATE_FORMAT2);
			
			SysSealed item = new SysSealed();
			item.setBILL_CODE(" ");
			//item.setRPT_DEPT(DepartCode);
			//item.setRPT_DUR(SystemDateTime);
			item.setRPT_USER(userId);
			item.setRPT_DATE(time);//YYYY-MM-DD HH:MM:SS
			item.setBILL_TYPE(TypeCode.toString());// 枚举  1工资明细,2工资汇总,3公积金明细,4公积金汇总,5社保明细,6社保汇总,7工资接口,8公积金接口,9社保接口
			item.setSTATE(DurState.Sealed.getNameKey());// 枚举  1封存,0解封
			syssealedinfoService.report(item);
			commonBase.setCode(0);
		}
		return commonBase;
	}
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/summary")
	public @ResponseBody CommonBase summary() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "delete")){return null;} //校验权限	
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		
		PageData pd = this.getPageData();
		String checkState = CheckState(pd);
		if(checkState!=null && checkState.trim() != ""){
			commonBase.setCode(2);
			commonBase.setMessage(checkState);
		} else {
			Object DATA_ROWS = pd.get("DATA_ROWS");
			String json = DATA_ROWS.toString();  
	        JSONArray array = JSONArray.fromObject(json);  
	        List<StaffDetailModel> listData = (List<StaffDetailModel>) JSONArray.toCollection(array,StaffDetailModel.class);
	        if(null != listData && listData.size() > 0){
				//staffdetailService.deleteAll(listData);
				commonBase.setCode(0);
			}
		}
		return commonBase;
	}
	
	private String CheckState(PageData pd) throws Exception{
		String strRut = "当前期间已封存！";
		//pd.put("RPT_DEPT", DepartCode);
		//pd.put("RPT_DUR", SystemDateTime);
		pd.put("BILL_TYPE", TypeCode);// 枚举  1工资明细,2工资汇总,3公积金明细,4公积金汇总,5社保明细,6社保汇总,7工资接口,8公积金接口,9社保接口
		String State = syssealedinfoService.getState(pd);
		if(State!=null && State.equals(DurState.Release.getNameKey())){// 枚举  1封存,0解封
			strRut = "";
		}
		return strRut;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
