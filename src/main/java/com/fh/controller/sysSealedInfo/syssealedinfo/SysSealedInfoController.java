package com.fh.controller.sysSealedInfo.syssealedinfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.controller.common.DictsUtil;
import com.fh.entity.CommonBase;
import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.entity.PageResult;
import com.fh.service.fhoa.department.DepartmentManager;
import com.fh.service.sysSealedInfo.syssealedinfo.SysSealedInfoManager;
import com.fh.service.sysUnlockInfo.sysunlockinfo.SysUnlockInfoManager;
import com.fh.service.system.user.UserManager;
import com.fh.util.Jurisdiction;
import com.fh.util.PageData;
import com.fh.util.SqlTools;
import com.fh.util.StringUtil;
import com.fh.util.enums.BillType;

import net.sf.json.JSONArray;

/**
 * 说明：业务封存信息 创建人：FH Q313596790 创建时间：2017-06-16
 */
@Controller
@RequestMapping(value = "/syssealedinfo")
public class SysSealedInfoController extends BaseController {

	String menuUrl = "syssealedinfo/list.do"; // 菜单地址(权限用)
	@Resource(name = "syssealedinfoService")
	private SysSealedInfoManager syssealedinfoService;

	@Resource(name = "departmentService")
	private DepartmentManager departmentService;

	@Resource(name = "userService")
	private UserManager userService;

	@Resource(name = "sysunlockinfoService")
	private SysUnlockInfoManager sysUnlockInfoService;

	/**
	 * 修改
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/edit")
	public @ResponseBody CommonBase edit() throws Exception {
		CommonBase commonBase = new CommonBase();
		logBefore(logger, Jurisdiction.getUsername() + "修改JgGrid");
		// if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;}
		// //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		if (pd.getString("oper").equals("edit")) {
			if (pd.getString("STATE").equals("0")) {
				String valiType = "";
				String message = "";
				if (pd.getString("BILL_TYPE").equals(BillType.SALLARY_DETAIL.getNameKey())) {// 工资明细
					// 验证工资汇总
					valiType = BillType.SALLARY_SUMMARY.getNameKey();
					message = "当前记录的【工资汇总】业务还没有进行解封,请先对对应的【工资汇总】业务进行解封.";
				} else if (pd.getString("BILL_TYPE").equals(BillType.SALLARY_SUMMARY.getNameKey())) {// 工资汇总
					// 验证工资接口
					valiType = BillType.SALLARY_LISTEN.getNameKey();
					message = "当前记录的【工资接口】业务还没有进行解封,请先对对应的【工资接口】业务进行解封.";
				} else if (pd.getString("BILL_TYPE").equals(BillType.GOLD_DETAIL.getNameKey())) {// 公积金明细
					// 验证公积金汇总
					valiType = BillType.GOLD_SUMMARY.getNameKey();
					message = "当前记录的【公积金汇总】业务还没有进行解封,请先对对应的【公积金汇总】业务进行解封.";
				} else if (pd.getString("BILL_TYPE").equals(BillType.GOLD_SUMMARY.getNameKey())) {// 公积金汇总
					// 验证公积金接口
					valiType = BillType.GOLD_LISTEN.getNameKey();
					message = "当前记录的【公积金接口】业务还没有进行解封,请先对对应的【公积金接口】业务进行解封.";
				} else if (pd.getString("BILL_TYPE").equals(BillType.SECURITY_DETAIL.getNameKey())) {// 社保明细
					// 验证社保汇总
					valiType = BillType.SECURITY_SUMMARY.getNameKey();
					message = "当前记录的【社保汇总】业务还没有进行解封,请先对对应的【社保汇总】业务进行解封.";
				} else if (pd.getString("BILL_TYPE").equals(BillType.SECURITY_SUMMARY.getNameKey())) {// 社保汇总
					// 验证社保接口
					valiType = BillType.SECURITY_LISTEN.getNameKey();
					message = "当前记录的【社保接口】业务还没有进行解封,请先对对应的【社保接口】业务进行解封.";
				}
				pd.put("VALI_TYPE", valiType);
				String valiState = syssealedinfoService.valiState(pd);
				if (valiState != null && valiState.equals("1")) {
					commonBase.setCode(2);
					commonBase.setMessage(message);
					return commonBase;
				}

				if (pd.getString("BILL_TYPE").equals(BillType.SALLARY_LISTEN.getNameKey())
						|| pd.getString("BILL_TYPE").equals(BillType.GOLD_LISTEN.getNameKey())
						|| pd.getString("BILL_TYPE").equals(BillType.SECURITY_LISTEN.getNameKey())) {
					String tableCode = "";
					if (pd.getString("BILL_TYPE").equals(BillType.SALLARY_LISTEN.getNameKey())) {
						tableCode = "TB_STAFF_SUMMY";
					} else if (pd.getString("BILL_TYPE").equals(BillType.GOLD_LISTEN.getNameKey())) {
						tableCode = "TB_HOUSE_FUND_SUMMY";
					} else if (pd.getString("BILL_TYPE").equals(BillType.SECURITY_LISTEN.getNameKey())) {
						tableCode = "TB_SOCIAL_INC_SUMMY";
					} else {
						tableCode = "TB_STAFF_SUMMY";
					}
					pd.put("TABLE_CODE", tableCode);
					List<PageData> listSysUnlockInfo = sysUnlockInfoService.listSyncDelUnlock(pd); // 获取当前接口类型，当前二级单位当前期间当前封存状态为封存的的传输列表
					if(listSysUnlockInfo!=null&&listSysUnlockInfo.size()>0){
						PageData pdItem0=listSysUnlockInfo.get(0);//如果包含其中一条带有凭证号的记录，就可以证明此二级单位所有记录已经生成凭证号，不能再进行解封操作。
						if(StringUtil.isNotEmpty(pdItem0.getString("CERT_CODE"))){
							commonBase.setCode(3);
							commonBase.setMessage("当前封存记录的下的二级单位汇总数据已经生成凭证,不能再进行解封.");
							return commonBase;
						}
						if(StringUtil.isEmpty(pdItem0.getString("REVCERT_CODE"))){//如果已生成冲销凭证号，则历史记录不允许删除，即不再往tb_sys_unlock_info插入记录
							sysUnlockInfoService.save(listSysUnlockInfo);
						}
					}
				}
				syssealedinfoService.edit(pd);
				commonBase.setCode(0);
			}
		}
		/**
		 * 此处为业务错误返回值，例如返回当前删除的信息含有业务关联字段，不能删除，自行设定setCode(返回码，客户端按码抓取并返回提示信息)和setMessage("自定义提示信息，提示给用户的")信息，并由界面进行展示。
		 * 此处不是异常返回的错误信息，异常返回错误信息统一由框架抓取异常。
		 */
		// commonBase.setCode(-1);
		// commonBase.setMessage("当前删除的信息含有业务关联字段，不能删除");
		return commonBase;
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(Page page) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "列表SysSealedInfo");
		// if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		// //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("sysSealedInfo/syssealedinfo/syssealedinfo_list");
		mv.addObject("zTreeNodes", DictsUtil.getDepartmentSelectTreeSource(departmentService));
		mv.addObject("billTypeList", BillType.values());

		String departmentValus = DictsUtil.getDepartmentValue(departmentService);
		String departmentString = ":[All];" + departmentValus;
		mv.addObject("departmentStr", departmentString);

		String userValus = DictsUtil.getSysUserValue(userService);
		String userString = ":[All];" + userValus;
		mv.addObject("userStr", userString);

		StringBuilder sbBillType = new StringBuilder();
		for (BillType billType : BillType.values()) {
			sbBillType.append(billType.getNameKey() + ":" + billType.getNameValue());
			sbBillType.append(';');
		}
		sbBillType.deleteCharAt(sbBillType.length() - 1);
		String billTypeString = ":[All];" + sbBillType.toString();
		mv.addObject("billTypeStr", billTypeString);
		return mv;
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/getPageList")
	public @ResponseBody PageResult<PageData> getPageList(JqPage page) throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "列表Betting");
		PageData pd = this.getPageData();
		/*
		 * String keywords = pd.getString("keywords"); //关键词检索条件 if(null !=
		 * keywords && !"".equals(keywords)){ pd.put("keywords",
		 * keywords.trim()); }
		 */

		String strRptDept = pd.getString("RPT_DEPT"); // 单位检索条件
		if (null != strRptDept && !"".equals(strRptDept)) {
			String RPT_DEPTS[] = strRptDept.split(",");
			pd.put("RPT_DEPTS", RPT_DEPTS);
		}
		String filters = pd.getString("filters"); // 多条件过滤条件
		if (null != filters && !"".equals(filters)) {
			pd.put("filterWhereResult", SqlTools.constructWhere(filters, null));
		}
		page.setPd(pd);
		List<PageData> varList = syssealedinfoService.list(page);
		int records = syssealedinfoService.count(pd);
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		result.setRowNum(page.getRowNum());
		result.setRecords(records);
		result.setPage(page.getPage());
		return result;
	}

	/**
	 * 批量修改
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateAll")
	/* @RequestBody RequestBase<JqGridModel> jqGridModel */
	public @ResponseBody CommonBase updateAll() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "批量删除JgGrid");
		// if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;}
		// //校验权限
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		PageData pd = this.getPageData();
		String strDataRows = pd.getString("DATA_ROWS");
		JSONArray array = JSONArray.fromObject(strDataRows);
		List<PageData> listData = (List<PageData>) JSONArray.toCollection(array, PageData.class);// 过时方法

		if (null != listData && listData.size() > 0) {
			syssealedinfoService.updateAll(listData);
			commonBase.setCode(0);
		}
		return commonBase;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
	}
}
