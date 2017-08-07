package com.fh.controller.staffsummy.staffsummy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.fh.controller.base.BaseController;
import com.fh.controller.common.BillCodeUtil;
import com.fh.controller.common.DictsUtil;
import com.fh.controller.common.TmplUtil;
import com.fh.entity.CommonBase;
import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.entity.PageResult;
import com.fh.entity.SysSealed;
import com.fh.entity.TableColumns;
import com.fh.entity.TmplConfigDetail;
import com.fh.entity.system.User;
import com.fh.util.Const;
import com.fh.util.DateUtil;
import com.fh.util.PageData;
import com.fh.util.SqlTools;
import com.fh.util.collectionSql.GroupUtils;
import com.fh.util.collectionSql.GroupUtils.GroupBy;
import com.fh.util.Jurisdiction;
import com.fh.util.date.DateFormatUtils;
import com.fh.util.date.DateUtils;
import com.fh.util.enums.BillNumType;
import com.fh.util.enums.BillState;
import com.fh.util.enums.BillType;
import com.fh.util.enums.DurState;

import net.sf.json.JSONArray;

import com.fh.service.fhoa.department.impl.DepartmentService;
import com.fh.service.glzrzx.glzrzx.impl.GlZrzxService;
import com.fh.service.staffDetail.staffdetail.StaffDetailManager;
import com.fh.service.staffsummy.staffsummy.StaffSummyManager;
import com.fh.service.sysBillnum.sysbillnum.SysBillnumManager;
import com.fh.service.sysConfig.sysconfig.SysConfigManager;
import com.fh.service.sysSealedInfo.syssealedinfo.impl.SysSealedInfoService;
import com.fh.service.system.dictionaries.impl.DictionariesService;
import com.fh.service.system.user.UserManager;
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
	@Resource(name="staffdetailService")
	private StaffDetailManager staffdetailService;
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
	@Resource(name="sysconfigService")
	private SysConfigManager sysConfigManager;
	@Resource(name="sysbillnumService")
	private SysBillnumManager sysbillnumService;
	@Resource(name = "userService")
	private UserManager userService;
	@Resource(name = "glzrzxService")
	private GlZrzxService glzrzxService;

	//表名
	String TableNameBase = "tb_staff_summy";
	String TableNameDetail = "tb_staff_detail";
	//枚举类型  1工资明细,2工资汇总,3公积金明细,4公积金汇总,5社保明细,6社保汇总,7工资接口,8公积金接口,9社保接口
	String TypeCode = BillType.SALLARY_SUMMARY.getNameKey();
	//显示结构的单位
    String ShowDepartCode = "01001";
	// 查询表的主键字段，作为标准列，jqgrid添加带__列，mybaits获取带__列
	private List<String> keyListBase = Arrays.asList("BILL_CODE", "BUSI_DATE", "DEPT_CODE", "USER_CATG", "USER_GROP", "CUST_COL7");
    
    //汇总字段
    List<String> SumField = Arrays.asList("BUSI_DATE", "DEPT_CODE", "USER_CATG", "USER_GROP", "CUST_COL7");
    String SumFieldToString = tranferSumFieldToString();
	
	//页面显示数据的年月
	String SystemDateTime = "";
	//底行显示的求和与平均值字段
	StringBuilder SqlUserdata = new StringBuilder();
	//表结构  
	Map<String, TableColumns> map_HaveColumnsList = new HashMap<String, TableColumns>();
	// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
	Map<String, TmplConfigDetail> map_SetColumnsList = new HashMap<String, TmplConfigDetail>();
	
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
		//当前期间,取自tb_system_config的SystemDateTime字段
		SystemDateTime = sysConfigManager.currentSection(pd);
		mv.addObject("SystemDateTime", SystemDateTime);
		
		mv.addObject("zTreeNodes", DictsUtil.getDepartmentSelectTreeSource(departmentService));
		
		TmplUtil tmpl = new TmplUtil(tmplconfigService, tmplconfigdictService, dictionariesService, departmentService,userService, keyListBase);
		String jqGridColModel = tmpl.generateStructureNoEdit(TableNameBase, ShowDepartCode);

		//底行显示的求和与平均值字段
		SqlUserdata = tmpl.getSqlUserdata();
		//表结构  
		map_HaveColumnsList = tmpl.getHaveColumnsList();
		// 前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
		map_SetColumnsList = tmpl.getSetColumnsList();
		
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
		//多条件过滤条件
		String filters = pd.getString("filters");
		if(null != filters && !"".equals(filters)){
			pd.put("filterWhereResult", SqlTools.constructWhere(filters,null));
		}
		//
		String DepartCode = pd.getString("DepartCode");	
		if(null != DepartCode && !"".equals(DepartCode)){
			pd.put("DepartCode", DepartCode.trim());
		}
		//页面显示数据的年月
		pd.put("SystemDateTime", SystemDateTime);
		//类型
		pd.put("TypeCode", TypeCode);
		pd.put("DurState", DurState.Sealed.getNameKey());
		page.setPd(pd);
		List<PageData> varList = staffsummyService.JqPage(page);	//列出Betting列表
		int records = staffsummyService.countJqGridExtend(page);
		PageData userdata = null;
		if(SqlUserdata!=null && !SqlUserdata.toString().trim().equals("")){
			//底行显示的求和与平均值字段
			pd.put("Userdata", SqlUserdata.toString());
			userdata = staffsummyService.getFooterSummary(page);
		}
		
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		result.setRowNum(page.getRowNum());
		result.setRecords(records);
		result.setPage(page.getPage());
		result.setUserdata(userdata);
		
		return result;
	}

	/**明细显示结构
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getDetailColModel")
	public @ResponseBody CommonBase getDetailColModel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"getDetailColModel");
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		
		PageData pd = this.getPageData();
		String DEPT_CODE = (String) pd.get("DATA_DEPT_CODE");
		TmplUtil tmpl = new TmplUtil(tmplconfigService, tmplconfigdictService, dictionariesService, departmentService,userService);
		String detailColModel = tmpl.generateStructureNoEdit(TableNameDetail, DEPT_CODE);

		commonBase.setCode(0);
		commonBase.setMessage(detailColModel);
		
		return commonBase;
	}

	/**明细数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getDetailList")
	public @ResponseBody PageResult<PageData> getDetailList() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"getDetailList");
		PageData pd = this.getPageData();
		
		List<PageData> varList = staffdetailService.getDetailList(pd);	//列出Betting列表
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		
		return result;
	}
	
	 /**上报
	 * @param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/report")
	public @ResponseBody CommonBase report() throws Exception{
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "report")){return null;} //校验权限	
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);

		User user = (User) Jurisdiction.getSession().getAttribute(Const.SESSION_USERROL);
		String userId = user.getUSER_ID();
        String time = DateUtils.getCurrentTime(DateFormatUtils.DATE_FORMAT2);

		PageData pd = this.getPageData();
		Object DATA_ROWS_REPORT = pd.get("DATA_ROWS_REPORT");
		String json = DATA_ROWS_REPORT.toString();  
        JSONArray array = JSONArray.fromObject(json);  
        
		List<PageData> listTransferData = (List<PageData>) JSONArray.toCollection(array, PageData.class);// 过时方法
		if (null != listTransferData && listTransferData.size() > 0) {
			List<SysSealed> listSysSealed = new ArrayList<SysSealed>();
			// 将获取的字典数据进行分组
			Map<String, List<PageData>> mapListTransferData = GroupUtils.group(listTransferData,
					new GroupBy<String>() {
						@Override
						public String groupby(Object obj) {
							PageData d = (PageData) obj;
							return d.getString("DEPT_CODE__"); // 分组依据为DEPT_CODE
						}
					});
			for (Map.Entry<String, List<PageData>> entry : mapListTransferData.entrySet()) {
				SysSealed item = new SysSealed();
				// item.setBILL_CODE(pageData.getString("BILL_CODE"));
				item.setBILL_CODE(" ");
				item.setRPT_DEPT(entry.getKey());
				List<PageData> listItem = entry.getValue();
				PageData pgItem = listItem.get(0);
				item.setRPT_DUR(pgItem.getString("BUSI_DATE__"));
				item.setRPT_USER(userId);
				item.setRPT_DATE(time);// YYYY-MM-DD HH:MM:SS
				item.setBILL_TYPE(TypeCode.toString());
				item.setSTATE(DurState.Sealed.getNameKey());// 枚举  1封存,0解封
				listSysSealed.add(item);
			}
			syssealedinfoService.insertBatch(listSysSealed);
			commonBase.setCode(0);
		}
		return commonBase;
	}
	
	 /**汇总
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/summaryDepartString")
	public @ResponseBody CommonBase summaryDepartString() throws Exception{
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "delete")){return null;} //校验权限	
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		List<PageData> listTo = new ArrayList<PageData>();
		PageData pdBillNum=new PageData();

		/***************获取最大单号及更新最大单号********************/
		String billNumType = BillNumType.YGGZ;
		String month = DateUtil.getMonth();
		pdBillNum.put("BILL_CODE", billNumType);
		pdBillNum.put("BILL_DATE", month);
		PageData pdBillNumResult=sysbillnumService.findById(pdBillNum);
		if(pdBillNumResult == null){
			pdBillNumResult = new PageData();
		}
		Object objGetNum = pdBillNumResult.get("BILL_NUMBER");
		if(!(objGetNum != null && !objGetNum.toString().trim().equals(""))){
			objGetNum = 0;
		}
		int getNum = (int) objGetNum;
		int billNum=getNum;
		/***************************************************/

		/***************去掉重复的单位编码********************/
		PageData getPd = this.getPageData();
		Object DATA_DEPART = getPd.get("DATA_DEPART");
		String[] listDATA_DEPART = DATA_DEPART.toString().split(",");  
		List<String> list = Arrays.asList(listDATA_DEPART);
        Set<String> set = new HashSet<String>(list);
        String [] listDepart=(String[])set.toArray(new String[0]);
		/***************************************************/
        
        if(null != listDepart && listDepart.length > 0){
        	for(String depart : listDepart){
        		//判断汇总信息为未上报
    			SysSealed itemLast = new SysSealed();
    			//item.setBILL_CODE(summy.getBILL_CODE());
    			itemLast.setRPT_DEPT(depart);
    			itemLast.setRPT_DUR(SystemDateTime);
    			itemLast.setBILL_TYPE(TypeCode.toString());// 枚举  1工资明细,2工资汇总,3公积金明细,4公积金汇总,5社保明细,6社保汇总,7工资接口,8公积金接口,9社保接口
    			String checkStateLast = CheckStateLast(itemLast);
    			if(checkStateLast!=null && !checkStateLast.trim().equals("")){
    				commonBase.setCode(2);
    				commonBase.setMessage(checkStateLast);
    				break;
    			}
    			//判断明细信息为已上报
    			SysSealed itemBefore = new SysSealed();
    			//itemBefore.setBILL_CODE(summy.getBILL_CODE());
    			itemBefore.setRPT_DEPT(depart);
    			itemBefore.setRPT_DUR(SystemDateTime);
    			itemBefore.setBILL_TYPE(BillType.SALLARY_DETAIL.getNameKey().toString());// 枚举  1工资明细,2工资汇总,3公积金明细,4公积金汇总,5社保明细,6社保汇总,7工资接口,8公积金接口,9社保接口
    			String checkStateBefore = CheckStateBefore(itemBefore);
    			if(checkStateBefore!=null && !checkStateBefore.trim().equals("")){
    				commonBase.setCode(2);
    				commonBase.setMessage(checkStateBefore);
    				break;
    			}
                //获取单位已有的汇总信息
    			Map<String, String> mapHave = new HashMap<String, String>();
    			mapHave.put("BUSI_DATE", SystemDateTime);
    			mapHave.put("DEPT_CODE", depart);
    			List<PageData> getHaveDate = staffsummyService.getHave(mapHave);
    			//获取单位重新汇总信息
    			List<TableColumns> tableDetailColumns = tmplconfigService.getTableColumns(TableNameDetail);
    			Map<String, String> mapSave = new HashMap<String, String>();
    			mapSave.put("BUSI_DATE", SystemDateTime);
    			mapSave.put("DEPT_CODE", depart);
    			mapSave.put("GroupbyFeild", SumFieldToString);
    			
    			//获取汇总的select的字段
    			String SelectFeild = TmplUtil.getSumFeildSelect(SumField, tableDetailColumns);

    			mapSave.put("SelectFeild", SelectFeild);
    			List<PageData> getSaveDate = staffdetailService.getSum(mapSave);
    			
    			List<PageData> listAdd = getListTo(getHaveDate, getSaveDate);

                ////根据DEPT_CODE从tb_gl_zrzx表里获取ZRZX_CODE，赋值给汇总保存数据
                //String strZRZC_CODE = "";
    			//List<ZrzxModel> listZRZC_CODE = glzrzxService.findDeptFromZrzx(depart);
                //if(listZRZC_CODE!=null && listZRZC_CODE.size()>0){
                //	strZRZC_CODE = listZRZC_CODE.get(0).getZRZX_CODE();
                //}
    			
    			for(PageData addTo : listAdd){
    				Object getBILL_CODE = addTo.get("BILL_CODE");
    				if(!(getBILL_CODE != null && !getBILL_CODE.toString().trim().equals(""))){
    					billNum++;
    					getBILL_CODE = BillCodeUtil.getBillCode(billNumType, month, billNum);
    				}
                    addTo.put("BILL_CODE", getBILL_CODE);
                    addTo.put("BUSI_DATE", SystemDateTime);
                    addTo.put("DEPT_CODE", depart);
                    addTo.put("BILL_STATE", BillState.Normal.getNameKey());
            		User user = (User) Jurisdiction.getSession().getAttribute(Const.SESSION_USERROL);
                    addTo.put("BILL_USER", user.getUSER_ID());
                    addTo.put("BILL_DATE", DateUtil.getTime());
                    addTo.put("ZRZC_CODE", "");
                    addTo.put("ESTB_DEPT", Jurisdiction.getCurrentDepartmentID());
                    
                    //更新明细单号的条件
                    StringBuilder updateFilter = new StringBuilder();
                    for(String field : SumField){
                    	if(updateFilter != null && !updateFilter.toString().trim().equals("")){
                        	updateFilter.append(" and ");
                    	}
                    	updateFilter.append(field + " = '" + addTo.getString(field) + "' ");
                    }
	    			addTo.put("updateFilter", updateFilter);
                    //添加未设置字段默认值
	    			TmplUtil.setModelDefault(addTo, map_HaveColumnsList);
    				listTo.add(addTo);
    			}
        	}
		}
		//单号没变化，pdBillNum为null，不更新数据库单号
		if(getNum == billNum){
			pdBillNum = null;
		} else {
			pdBillNum.put("BILL_NUMBER", billNum);
		}
        if(commonBase.getCode() == -1){
			staffsummyService.summaryModelList(listTo, pdBillNum);
			commonBase.setCode(0);
        }
		return commonBase;
	}
	
	private List<PageData> getListTo(List<PageData> listHave, List<PageData> listSave){
		List<String> listNotSetCode = new ArrayList<String>();
	    if(listSave!=null && listSave.size()>0){
			for(PageData eachSave : listSave){
			    //先清除汇总生成数据的单号
				eachSave.remove("BILL_CODE");
				//在根据汇总字段匹配设置单号
			    if(listHave!=null && listHave.size()>0){
					for(PageData eachHave : listHave){
						Object getBILL_CODE = eachHave.get("BILL_CODE");
						if(getBILL_CODE!=null && !getBILL_CODE.toString().equals("")){
							Boolean bol = true;
							for(String field : SumField){
								String strHave = (String) eachHave.get(field);
								if(strHave == null) strHave = "";
								String strSave = (String) eachSave.get(field);
								if(strSave == null) strSave = "";
								if(!strHave.equals(strSave)){
									bol = false;
								}
							}
							if(bol){
								eachSave.put("BILL_CODE", getBILL_CODE);
							} else {
							    listNotSetCode.add(getBILL_CODE.toString());
							}
						}
					}
			    }
			}
			//未匹配的单号和没有单号的记录设置
			for(PageData eachSave : listSave){
				if(!(listNotSetCode!=null && listNotSetCode.size()>0)){
					break;
				}
				Object getBILL_CODE = eachSave.get("BILL_CODE");
				if(!(getBILL_CODE != null && !getBILL_CODE.toString().trim().equals(""))){
					eachSave.put("BILL_CODE", listNotSetCode.get(0));
					listNotSetCode.remove(0);
				}
			}
	    }
		return listSave;
	}
	
	private String CheckStateLast(SysSealed item) throws Exception{
		String strRut = "单位：" + item.getRPT_DEPT() + "汇总期间已封存！";
		String State = syssealedinfoService.getStateFromModel(item);
		if(!DurState.Sealed.getNameKey().equals(State)){// 枚举  1封存,0解封
			strRut = "";
		}
		return strRut;
	}
	private String CheckStateBefore(SysSealed item) throws Exception{
		String strRut = "单位：" + item.getRPT_DEPT() + "明细期间未封存！";
		String State = syssealedinfoService.getStateFromModel(item);
		if(DurState.Sealed.getNameKey().equals(State)){// 枚举  1封存,0解封
			strRut = "";
		}
		return strRut;
	}
	
	private String tranferSumFieldToString(){
		StringBuilder ret = new StringBuilder();
		for(String field : SumField){
			if(!ret.toString().trim().equals("")){
				ret.append(",");
			}
			ret.append(field);
		}
		return ret.toString();
	}
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
