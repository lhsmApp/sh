package com.fh.controller.jqgrid;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.JqPage;
import com.fh.entity.PageResult;
import com.fh.service.betting.betting.BettingManager;
import com.fh.util.Jurisdiction;
import com.fh.util.PageData;

/**
 * 
* @ClassName: JgGridController
* @Description: TODO(这里用一句话描述这个类的作用)
* @author jiachao
* @date 2017年6月6日
*
 */
@Controller
@RequestMapping(value="/jqgrid")
public class JgGridController extends BaseController {
	@Resource(name="bettingService")
	private BettingManager bettingService;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(JqPage page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表Betting");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		//List<PageData> varList = bettingService.list(page);	//列出Betting列表
		mv.setViewName("jqgrid/test");
		//mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/getPageList")
	public @ResponseBody PageResult<PageData> getPageList(JqPage page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表Betting");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		//List<PageData> varList = bettingService.list(page);	//列出Betting列表
		
		/*List<PageData> varList = bettingService.list(page);	//列出Betting列表
		int records = bettingService.countOperation(operation);
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		result.setRecords(records);*/
		
		
		List<PageData> varList = new ArrayList<PageData>();	//列出Betting列表
		PageData pageData=new PageData();
		pageData.put("CategoryName", "Beverages");
		pageData.put("ProductName", "Steeleye Stout");
		pageData.put("Country", "UK");
		pageData.put("Price", "1008.0000");
		pageData.put("Quantity", "65");
		varList.add(pageData);
		
		PageData pageData1=new PageData();
		pageData1.put("CategoryName", "Beverages");
		pageData1.put("ProductName", "Laughing Lumberjack Lager");
		pageData1.put("Country", "USA");
		pageData1.put("Price", "140.0000");
		pageData1.put("Quantity", "10");
		varList.add(pageData1);
		
		PageData pageData2=new PageData();
		pageData2.put("CategoryName", "Beverages");
		pageData2.put("ProductName", "Lakkalik\u00f6\u00f6ri");
		pageData2.put("Country", "USA");
		pageData2.put("Price", "2160.0000");
		pageData2.put("Quantity", "120");
		varList.add(pageData2);
		
		PageData pageData3=new PageData();
		pageData3.put("CategoryName", "Beverages");
		pageData3.put("ProductName", "Lakkalik\u00f6\u00f6ri");
		pageData3.put("Country", "USA");
		pageData3.put("Price", "2160.0000");
		pageData3.put("Quantity", "120");
		varList.add(pageData3);
		
		PageData pageData4=new PageData();
		pageData4.put("CategoryName", "Beverages");
		pageData4.put("ProductName", "Lakkalik\u00f6\u00f6ri");
		pageData4.put("Country", "USA");
		pageData4.put("Price", "2160.0000");
		pageData4.put("Quantity", "120");
		varList.add(pageData4);
		
		PageData pageData5=new PageData();
		pageData5.put("CategoryName", "Beverages");
		pageData5.put("ProductName", "Lakkalik\u00f6\u00f6ri");
		pageData5.put("Country", "USA");
		pageData5.put("Price", "2160.0000");
		pageData5.put("Quantity", "120");
		varList.add(pageData5);
		
		PageData pageData6=new PageData();
		pageData6.put("CategoryName", "Beverages");
		pageData6.put("ProductName", "Lakkalik\u00f6\u00f6ri");
		pageData6.put("Country", "USA");
		pageData6.put("Price", "2160.0000");
		pageData6.put("Quantity", "120");
		varList.add(pageData6);
		
		PageData pageData7=new PageData();
		pageData7.put("CategoryName", "Beverages");
		pageData7.put("ProductName", "Lakkalik\u00f6\u00f6ri");
		pageData7.put("Country", "USA");
		pageData7.put("Price", "2160.0000");
		pageData7.put("Quantity", "120");
		varList.add(pageData7);
		
		PageData pageData8=new PageData();
		pageData8.put("CategoryName", "Beverages");
		pageData8.put("ProductName", "Lakkalik\u00f6\u00f6ri");
		pageData8.put("Country", "USA");
		pageData8.put("Price", "2160.0000");
		pageData8.put("Quantity", "120");
		varList.add(pageData8);
		
		PageData pageData9=new PageData();
		pageData9.put("CategoryName", "Beverages");
		pageData9.put("ProductName", "Lakkalik\u00f6\u00f6ri");
		pageData9.put("Country", "USA");
		pageData9.put("Price", "2160.0000");
		pageData9.put("Quantity", "120");
		varList.add(pageData9);
		
		PageData pageData10=new PageData();
		pageData10.put("CategoryName", "Beverages");
		pageData10.put("ProductName", "Lakkalik\u00f6\u00f6ri");
		pageData10.put("Country", "USA");
		pageData10.put("Price", "2160.0000");
		pageData10.put("Quantity", "120");
		varList.add(pageData10);
		
		PageData pageData11=new PageData();
		pageData11.put("CategoryName", "Beverages");
		pageData11.put("ProductName", "Lakkalik\u00f6\u00f6ri");
		pageData11.put("Country", "USA");
		pageData11.put("Price", "2160.0000");
		pageData11.put("Quantity", "120");
		varList.add(pageData11);
		
		//int records = bettingService.countOperation(operation);
		PageResult<PageData> result = new PageResult<PageData>();
		result.setRows(varList);
		result.setRecords(31);
		//mv.addObject("varList", varList);
		//mv.addObject("pd", pd);
		//mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return result;
	}
}
