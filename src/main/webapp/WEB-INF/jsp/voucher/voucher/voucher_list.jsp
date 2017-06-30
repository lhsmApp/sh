<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 ，其中包含旧版本（Ace）Jqgrid Css-->
<%@ include file="../../system/index/topWithJqgrid.jsp"%>
<!-- 日期框 -->
<link rel="stylesheet" href="static/ace/css/datepicker.css" />

<!-- 最新版的Jqgrid Css，如果旧版本（Ace）某些方法不好用，尝试用此版本Css，替换旧版本Css -->
<!-- <link rel="stylesheet" type="text/css" media="screen" href="static/ace/css/ui.jqgrid-bootstrap.css" /> -->

<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
<!-- 树形下拉框start -->
<script type="text/javascript" src="plugins/selectZtree/selectTree.js"></script>
<script type="text/javascript" src="plugins/selectZtree/framework.js"></script>
<link rel="stylesheet" type="text/css" href="plugins/selectZtree/import_fh.css"/>
<script type="text/javascript" src="plugins/selectZtree/ztree/ztree.js"></script>
<link type="text/css" rel="stylesheet" href="plugins/selectZtree/ztree/ztree.css"></link>
<!-- 树形下拉框end -->
<style>
.page-header {
	padding-top: 9px;
	padding-bottom: 9px;
	margin: 0 0 8px;
}
</style>
</head>
<body class="no-skin">
	<div class="main-container" id="main-container">
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<!-- /section:settings.box -->
					<div class="page-header">
						<span class="label label-xlg label-success arrowed-right">财务核算</span>
						<!-- arrowed-in-right -->
						<span
							class="label label-xlg label-yellow arrowed-in arrowed-right"
							id="subTitle" style="margin-left: 2px;">凭证传输</span> <span
							style="border-left: 1px solid #e2e2e2; margin: 0px 10px;">&nbsp;</span>

						<button id="btnQuery" class="btn btn-white btn-info btn-sm"
							onclick="showQueryCondi()">
							<i class="ace-icon fa fa-chevron-down bigger-120 blue"></i> <span>显示查询</span>
						</button>
						<div class="pull-right">
							<span class="green middle bolder">凭证数据类型: &nbsp;</span>

							<div class="btn-toolbar inline middle no-margin">
								<div data-toggle="buttons" class="btn-group no-margin">
									<label class="btn btn-sm btn-primary active"> <span
										class="bigger-110">工资</span> <input type="radio" value="1" />
									</label> <label class="btn btn-sm btn-primary"> <span
										class="bigger-110">社保</span> <input type="radio" value="2" />
									</label> <label class="btn btn-sm btn-primary"> <span
										class="bigger-110">公积金</span> <input type="radio" value="3" />
									</label>
								</div>
							</div>
						</div>
					</div>
					<!-- /.page-header -->

					<div class="row">
						<div class="col-xs-12">
							<div class="widget-box" style="display: none;">
								<div class="widget-body">
									<div class="widget-main">
										<form class="form-inline">
											<span class="input-icon"> <input
												id="form-field-icon-1" type="text" placeholder="这里输入关键词">
												<i class="ace-icon fa fa-leaf blue"></i>
											</span>
											<span> 
												<select
													class="chosen-select form-control" name="BELONG_AREA"
													id="belong_area" data-placeholder="请选择所属区域"
													style="vertical-align: top; height: 32px; width: 150px;">
														<option value="" selected>请选择国家</option>
														<option value="">USA</option>
														<c:forEach items="${areaList}" var="area">
															<option value="${area.BIANMA }"
																<c:if test="${pd.BELONG_AREA==area.BIANMA}">selected</c:if>>${area.NAME }</option>
														</c:forEach>
												</select>
											</span>
											<span>
												<div class="selectTree" id="selectTree"></div>
											</span>
											<button type="button" class="btn btn-info btn-sm">
												<i class="ace-icon fa fa-search bigger-110"></i>
											</button>
										</form>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-12">
							<div class="tabbable">
								<ul class="nav nav-tabs padding-18">
									<li class="active"><a data-toggle="tab" href="#voucherTransfer">
											<i class="green ace-icon fa fa-user bigger-120"></i> 凭证数据传输
									</a></li>

									<li><a data-toggle="tab" href="#voucherMgr"> <i
											class="orange ace-icon fa fa-rss bigger-120"></i> 凭证管理
									</a></li>
								</ul>
								<div class="tab-content no-border ">
									<table id="jqGrid"></table>
									<div id="jqGridPager"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<a href="#" id="btn-scroll-up"
			class="btn-scroll-up btn btn-sm btn-inverse"> <i
			class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>
	</div>


	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>

	<!-- 最新版的Jqgrid Js，如果旧版本（Ace）某些方法不好用，尝试用此版本Js，替换旧版本JS -->
	<!-- <script src="static/ace/js/jquery.jqGrid.min.js" type="text/javascript"></script>
	<script src="static/ace/js/grid.locale-cn.js" type="text/javascript"></script> -->

	<!-- 旧版本（Ace）Jqgrid Js -->
	<script src="static/ace/js/jqGrid/jquery.jqGrid.src.js"></script>
	<script src="static/ace/js/jqGrid/i18n/grid.locale-cn.js"></script>
	<!-- 删除时确认窗口 -->
	<script src="static/ace/js/bootbox.js"></script>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<!-- JqGrid统一样式统一操作 -->
	<script type="text/javascript" src="static/js/common/jqgrid_style.js"></script>
	<script type="text/javascript"
		src="static/js/common/cusElement_style.js"></script>
	<script type="text/javascript" src="static/js/util/toolkit.js"></script>
	<script src="static/ace/js/ace/ace.widget-box.js"></script>
	<script type="text/javascript"> 
	var jqGridColModelSub;
	$(document).ready(function () {
		$(top.hangge());//关闭加载状态
		//dropDownStyle();
		
		//前端数据表格界面字段,动态取自tb_tmpl_config_detail，根据当前单位编码及表名获取字段配置信息
	    var jqGridColModel = eval("(${jqGridColModel})");//此处记得用eval()行数将string转为array
	    jqGridColModelSub = eval("(${jqGridColModelSub})");
		
		//resize to fit page size
		$(window).on('resize.jqGrid', function () {
			$("#jqGrid").jqGrid( 'setGridWidth', $(".page-content").width());
			$("#jqGrid").jqGrid( 'setGridHeight', $(window).height() - 213);
	    });
		
		//初始化当前选择凭证类型
		if('${pd.which}'!=""){
			$('[data-toggle="buttons"] .btn').each(function(index, data){
				var target = $(this).find('input[type=radio]');
				$(this).removeClass('active');
				var which = parseInt(target.val());
				if(which=='${pd.which}'){
					$(this).addClass('active');
				}
			});
		} 
	    
		//凭证类型变化
		$('[data-toggle="buttons"] .btn').on('click', function(e){
			var target = $(this).find('input[type=radio]');
			var which = parseInt(target.val());
			console.log(which);
			if(which!='${pd.which}'){
				window.location.href="<%=basePath%>voucher/list.do?TABLE_CODE="+which;
			}
		});
		
		//tab页切换
		$('.nav-tabs li').on('click', function(e){
			if($(this).hasClass('active')) return;
			var target = $(this).find('a');
			var voucherType;
			//data:{VOUCHER_TYPE:voucherType,TABLE_CODE:'${pd.which}'},
			if(target.attr('href')=='#voucherTransfer'){
				voucherType=1;
				$("#jqGrid").jqGrid("setGridParam",{postData:{"VOUCHER_TYPE":voucherType,"TABLE_CODE":'${pd.which}'}});
				$("#jqGrid").trigger("reloadGrid");  
			}else{
				voucherType=2;
				$("#jqGrid").jqGrid("setGridParam",{postData:{"VOUCHER_TYPE":voucherType,"TABLE_CODE":'${pd.which}'}});
				$("#jqGrid").trigger("reloadGrid");  
			}
		});
		
		$("#jqGrid").jqGrid({
			url: '<%=basePath%>voucher/getPageList.do',
			postData:{"VOUCHER_TYPE":1,"TABLE_CODE":'${pd.which}'},
			datatype: "json",
			colModel: jqGridColModel,
			reloadAfterSubmit: true, 
			//autowidth:true,
			shrinkToFit:false,
			viewrecords: true, // show the current page, data rang and total records on the toolbar
			rowNum: 10,
			rowList:[10,20,30,100000],
			//height: '100%', 
			width:'100%',
			sortname: 'BILL_CODE',
			
			//footerrow: true,
			//userDataOnFooter: true, // the calculated sums and/or strings from server are put at footer row.
			/*grouping: true,
			 groupingView: {
                groupField: ["CATEGORYNAME"],
                groupColumnShow: [true],
                groupText: ["<b>{0}</b>"],
                groupOrder: ["desc"],
                groupDataSorted : false,
                groupSummary: [true],
                groupCollapse: false,
                plusicon : 'fa fa-chevron-down bigger-110',
				minusicon : 'fa fa-chevron-up bigger-110'
            }, */
			
			pager: "#jqGridPager",
			loadComplete : function() {
				var table = this;
				setTimeout(function(){
					styleCheckbox(table);
					updateActionIcons(table);
					updatePagerIcons(table);
					enableTooltips(table);
				}, 0);
			},
			
			altRows: true,
			//toppager: true,
			rownumbers: true, // show row numbers
            rownumWidth: 35, // the width of the row numbers columns
			
			multiselect: true,
			//multikey: "ctrlKey",
	        multiboxonly: true,
	        //editurl: "<%=basePath%>jqgridJia/edit.do",//nothing is saved
	        
	      	//subgrid options
			subGrid : true,
			//subGridModel: [{ name : ['No','Item Name','Qty'], width : [55,200,80] }],
			//datatype: "xml",
			subGridOptions : {
				plusicon : "ace-icon fa fa-plus center bigger-110 blue",
				minusicon  : "ace-icon fa fa-minus center bigger-110 blue",
				openicon : "ace-icon fa fa-chevron-right center orange"
			},
			//for this example we are using local data
			subGridRowExpanded: showChildGrid,
		});
		
		$(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size
	
		//navButtons
		jQuery("#jqGrid").jqGrid('navGrid',"#jqGridPager",
			{ 	//navbar options
				edit: false,
				editicon : 'ace-icon fa fa-pencil blue',
				add: false,
				addicon : 'ace-icon fa fa-plus-circle purple',
				del: false,
				delicon : 'ace-icon fa fa-trash-o red',
				search: true,
				searchicon : 'ace-icon fa fa-search orange',
				refresh: true,
				refreshicon : 'ace-icon fa fa-refresh blue',
				view: false,
				viewicon : 'ace-icon fa fa-search-plus grey',
			},
			{
				//edit record form
			},
			{
				//new record form
			},
			{
				//delete record form
			},
			{
				//search form
				recreateForm: true,
				afterShowSearch: beforeSearchCallback,
				afterRedraw: function(){
					style_search_filters($(this));
				},
				multipleSearch: true,
				//multipleGroup:true,
				showQuery: false
			}
		);

       //批量传输
       $('#jqGrid').navButtonAdd('#jqGridPager',
       {
    	   /* bigger-150 */
           buttonicon: "ace-icon fa fa-cloud-upload green",
           title: "批量上传",
           caption: "",
           position: "last",
           onClickButton: batchSave
       });
	});

	//批量传输
	function batchSave(e) {
		var listData =new Array();
		var ids = $("#jqGrid").jqGrid('getDataIDs');
		console.log(ids);
		//遍历访问这个集合  
		var rowData;
		$(ids).each(function (index, id){  
            $("#jqGrid").saveRow(id, false, 'clientArray');
             rowData = $("#jqGrid").getRowData(id);
            listData.push(rowData);
		});
		top.jzts();
		$.ajax({
			type: "POST",
			url: '<%=basePath%>jqgridJia/updateAll.do?',
	    	//data: rowData,//可以单独传入一个对象，后台可以直接通过对应模型接受参数。但是传入Array（listData）就不好用了，所以传list方式需将List转为Json字符窜。
			//data: '{"rows":listData}',
			data:{DATA_ROWS:JSON.stringify(listData)},
	    	dataType:'json',
			cache: false,
			success: function(response){
				if(response.code==0){
					$("#jqGrid").trigger("reloadGrid");  
					$(top.hangge());//关闭加载状态
					$("#subTitle").tips({
						side:3,
			            msg:'保存成功',
			            bg:'#009933',
			            time:3
			        });
				}else{
					$(top.hangge());//关闭加载状态
					$("#subTitle").tips({
						side:3,
			            msg:'保存失败,'+response.responseJSON.message,
			            bg:'#cc0033',
			            time:3
			        });
				}
			},
	    	error: function(e) {
				$(top.hangge());//关闭加载状态
	    	}
		});
    }
	
	//显示明细信息
	// the event handler on expanding parent row receives two parameters
    // the ID of the grid tow  and the primary key of the row
    function showChildGrid(parentRowID, parentRowKey) {
    	console.log(parentRowID+"  "+parentRowKey);
        var childGridID = parentRowID + "_table";
        var childGridPagerID = parentRowID + "_pager";
     // send the parent row primary key to the server so that we know which grid to show
     	var parentRowData=$("#jqGrid").jqGrid('getRowData',parentRowKey);
     	var tableCode="TB_HOUSE_FUND_DETAIL";
        var childGridURL = '<%=basePath%>voucher/getDetailList.do?BILL_CODE='+parentRowData.BILL_CODE+'&TABLE_CODE='+tableCode+'';
        //childGridURL = childGridURL + "&parentRowID=" + encodeURIComponent(parentRowKey)

        // add a table and pager HTML elements to the parent grid row - we will render the child grid here
        $('#' + parentRowID).append('<table id=' + childGridID + '></table><div id=' + childGridPagerID + ' class=scroll></div>');
        $("#" + childGridID).jqGrid({
            url: childGridURL,
            mtype: "GET",
            datatype: "json",
            page: 1,
            colModel: jqGridColModelSub,
            //width: '100%',
            height: '100%',
            shrinkToFit:true,
            autowidth:true,
            //pager: "#" + childGridPagerID,
            loadComplete : function() {
				var table = this;
				setTimeout(function(){
					styleCheckbox(table);
					updateActionIcons(table);
					updatePagerIcons(table);
					enableTooltips(table);
				}, 0);
			},
			gridComplete:function(){
			    //$("#" + childGridID).parents(".ui-jqgrid-bdiv").css("overflow-x","hidden");
		    }
        });
	}

	
	//日期格式化
    function formateDate(value, row, index) {
        var formateNewDate=toolkit.dateFormat(new Date(value),"yyyy-MM-dd")
        return formateNewDate;
    }
    
  	//日期反格式化  
	function unformateDate(cellValue, options, rowObject){  
	    var updateDate = new Date(cellValue);  
	    return updateDate;  
	}
  	
	//创建一个input输入框
	function myelem (value, options) {
		var el = document.createElement("input");
		el.type="number";
		el.value = value;
		return el;
		/* $(el).ace_spinner({value:0,min:0,max:200,step:10, btn_up_class:'btn-info' , btn_down_class:'btn-info'});
			return el; */
	}
/* 	 */
	//获取值
	function myvalue(elem) {
		return $(elem).val();
	}
	
	//显示隐藏查询
	function showQueryCondi(){
		if($(".widget-box").css("display")=="block"){
			$("#btnQuery").find("i").removeClass('fa-chevron-up').addClass('fa-chevron-down');
			$("#btnQuery").find("span").text("显示查询");
			
		}
		else{
			$("#btnQuery").find("i").removeClass('fa-chevron-down').addClass('fa-chevron-up');
			$("#btnQuery").find("span").text("隐藏查询");
		}
		$(".widget-box").toggle("fast");
		//$(window).triggerHandler('resize.jqGrid');
	}
	
	//加载单位树
	function initComplete(){
		//下拉树
		var defaultNodes = {"treeNodes":${zTreeNodes}};
		//绑定change事件
		$("#selectTree").bind("change",function(){
			console.log($(this));
			if(!$(this).attr("relValue")){
		      //  top.Dialog.alert("没有选择节点");
		    }else{
				//alert("选中节点文本："+$(this).attr("relText")+"<br/>选中节点值："+$(this).attr("relValue"));
				$("#DEPARTMENT_CODE").val($(this).attr("relValue"));
				$("#DNAME").val($(this).attr("relText"));
		    }
		});
		//赋给data属性
		$("#selectTree").data("data",defaultNodes);  
		$("#selectTree").render();
		$("#selectTree2_input").val("${'0'==depname?'请选择':depname}");
	}
 	</script>
</body>
</html>