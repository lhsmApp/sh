package com.fh.controller.dictTransfer;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.controller.common.DictsUtil;
import com.fh.controller.common.GenerateTransferData;
import com.fh.controller.common.TmplUtil;
import com.fh.entity.CommonBase;
import com.fh.entity.JqGridModel;
import com.fh.entity.JqPage;
import com.fh.entity.Page;
import com.fh.entity.PageResult;
import com.fh.entity.SysSealed;
import com.fh.entity.TableColumns;
import com.fh.entity.system.User;
import com.fh.service.fhoa.department.DepartmentManager;
import com.fh.service.sysConfig.sysconfig.SysConfigManager;
import com.fh.service.sysSealedInfo.syssealedinfo.SysSealedInfoManager;
import com.fh.service.system.dictionaries.DictionariesManager;
import com.fh.service.tmplConfigDict.tmplconfigdict.TmplConfigDictManager;
import com.fh.service.tmplconfig.tmplconfig.impl.TmplConfigService;
import com.fh.service.voucher.voucher.VoucherManager;
import com.fh.util.AppUtil;
import com.fh.util.Const;
import com.fh.util.Jurisdiction;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.SqlTools;
import com.fh.util.date.DateUtils;
import com.fh.util.enums.BillType;
import com.fh.util.enums.TransferOperType;

import net.sf.json.JSONArray;

/**
 * 字典数据传输
 * 
 * @ClassName: VoucherController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author jiachao
 * @date 2017年6月29日
 *
 */
@Controller
@RequestMapping(value = "/dictTransfer")
public class DictTransferController extends BaseController {

	String menuUrl = "voucher/list.do"; // 菜单地址(权限用)
	@Resource(name = "dictionariesService")
	private DictionariesManager dictionariesService;

	@Resource(name = "tmplconfigService")
	private TmplConfigService tmplconfigService;

	@Resource(name = "sysconfigService")
	private SysConfigManager sysConfigManager;
	
	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("dictTransfer/dictTransfer_list");
		// 此处放当前页面初始化时用到的一些数据，例如搜索的下拉列表数据，所需的字典数据、权限数据等等。
		mv.addObject("dicTypeList", dictionariesService.listSubDictByParentId("0"));
		return mv;
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value = "/getPageList")
	public @ResponseBody PageResult<PageData> getPageList() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "字典传输列表");
		PageData pd = new PageData();
		pd = this.getPageData();
		
		String keywords = pd.getString("keywords"); // 关键词检索条件
		if (null != keywords && !"".equals(keywords)) {
			pd.put("keywords", keywords.trim());
		}
		String filters = pd.getString("filters"); // 多条件过滤条件
		if (null != filters && !"".equals(filters)) {
			pd.put("filterWhereResult", SqlTools.constructWhere(filters, null));
		}
		//page.setPd(pd);
		List<PageData> varList = dictionariesService.listAll(pd);
		//int records = dictionariesService.count(pd);
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		//result.setRowNum(page.getRowNum());
		//result.setRecords(records);
		//result.setPage(page.getPage());
		return result;
	}

	/**
	 * 批量修改
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/dictTransfer")
	public @ResponseBody CommonBase dictTransfer() throws Exception {
		logBefore(logger, Jurisdiction.getUsername() + "凭证传输");
		CommonBase commonBase = new CommonBase();
		commonBase.setCode(-1);
		PageData pd = this.getPageData();
		String strDataRows = pd.getString("DATA_ROWS");
		JSONArray array = JSONArray.fromObject(strDataRows);
		@SuppressWarnings("unchecked")
		List<PageData> listTransferData = (List<PageData>) JSONArray.toCollection(array, PageData.class);// 过时方法
		try {
			if (null != listTransferData && listTransferData.size() > 0) {
				/********************** 生成字典传输数据 ************************/
				List<TableColumns> tableColumns = new ArrayList<TableColumns>();
				TableColumns tableColumn=new TableColumns();
				tableColumn.setColumn_name("BIANMA");
				tableColumn.setColumn_type("VARCHAR");
				TableColumns tableColumn1=new TableColumns();
				tableColumn1.setColumn_name("NAME");
				tableColumn1.setColumn_type("VARCHAR");
				tableColumns.add(tableColumn);
				tableColumns.add(tableColumn1);
				GenerateTransferData generateTransferData = new GenerateTransferData();
				String transferData = generateTransferData.generateVoucherData(tableColumns, listTransferData, "3630100020",
						TransferOperType.INSERT, "EMPLGRP");
				String a=transferData.replaceAll("<Keys/>", "").replaceAll("<KeyValue/>", "");
				// 执行上传FIMS
				Service service = new Service();
				Call call = (Call) service.createCall();
				pd.put("KEY_CODE", "JSynFactTableData");
				String strUrl = sysConfigManager.getSysConfigByKey(pd);
				URL url = new URL(strUrl);
				call.setTargetEndpointAddress(url);
				call.setOperationName(new QName("http://JSynFactTableData.j2ee", "synFactData"));
				call.setUseSOAPAction(true);
				String message = (String) call.invoke(new Object[] { a });
				System.out.println(message);
				if (message =="true") {
					/******************************************************/
					commonBase.setCode(0);
				}
			}
		} catch (Exception ex) {
			commonBase.setMessage(ex.toString());
		}
		return commonBase;
	}
}
