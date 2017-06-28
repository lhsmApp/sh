<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<script type="text/javascript" src="static/js/jquery-1.7.2.js"></script>
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/top.jsp"%>
<!-- 树形下拉框start -->
<script type="text/javascript" src="plugins/selectZtree/selectTree.js"></script>
<script type="text/javascript" src="plugins/selectZtree/framework.js"></script>
<link rel="stylesheet" type="text/css" href="plugins/selectZtree/import_fh.css"/>
<script type="text/javascript" src="plugins/selectZtree/ztree/ztree.js"></script>
<link type="text/css" rel="stylesheet" href="plugins/selectZtree/ztree/ztree.css"></link>
<!-- 树形下拉框end -->
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 ，其中包含旧版本（Ace）Jqgrid Css-->
<%@ include file="../../system/index/topWithJqgrid.jsp"%>
<!-- 日期框 -->
<link rel="stylesheet" href="static/ace/css/datepicker.css" />

<!-- 最新版的Jqgrid Css，如果旧版本（Ace）某些方法不好用，尝试用此版本Css，替换旧版本Css -->
<!-- <link rel="stylesheet" type="text/css" media="screen" href="static/ace/css/ui.jqgrid-bootstrap.css" /> -->
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
						<table>
							<tr>
								<td><span
									class="label label-xlg label-success arrowed-right">东部管道</span>
									<!-- arrowed-in-right --> <span
									class="label label-xlg label-yellow arrowed-in arrowed-right"
									id="subTitle" style="margin-left: 2px;">数据配置</span> <span
									style="border-left: 1px solid #e2e2e2; margin: 0px 10px;">&nbsp;</span>

									<button id="btnQuery" class="btn btn-white btn-info btn-sm"
										onclick="showQueryCondi()">
										<i class="ace-icon fa fa-chevron-up bigger-120 blue"></i> <span>隐藏查询</span>
									</button>
									
									<button id="btnCopy" class="btn btn-white btn-info btn-sm" style="margin-left: 10px;"
										onclick="copyData()">
										 <span>复制</span>
									</button></td>
							</tr>
						</table>
					</div>
					<!-- /.page-header -->

					<div class="row">
						<div class="col-xs-12">
							<div class="widget-box" >
								<div class="widget-body">
									<div class="widget-main">
										<form class="form-inline">		
										<table style="margin-top:5px;">
							<tr>
							<td><input name="DEPARTMENT_CODE" id="DEPARTMENT_CODE" type="hidden" value="${pd.DEPARTMENT_CODE }" /></td>
							<td><input name="DNAME" id="DNAME" type="hidden" value="${pd.DNAME }" /></td>
								<td>
											<span>
												<select class="chosen-select form-control" 
													name="TABLE_NO" id=TABLE_NO
													data-placeholder="请选择表明称"
													style="vertical-align: top; height:32px;width: 150px;">
													<option value="">请选择表名称</option>
													<c:forEach items="${listBase}" var="tableName">
														<option value="${tableName.TABLE_NO}"
															<c:if test="${pd.TABLE_NO==tableName.TABLE_NO}">selected</c:if>>${tableName.TABLE_NAME  }</option>
													</c:forEach>
												</select>
											</span>
											</td>			
											<td style="padding-left:5px">
											<div class="selectTree" id="selectTree"></div>
											</td>
											<td style="padding-left:5px">
											<button type="button" class="btn btn-info btn-sm" onclick="tosearch();">
												<i class="ace-icon fa fa-search bigger-110"></i>
											</button>
											</td>
												</tr>
						</table>
										</form>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-12">
							<table id="jqGrid"></table>
							<div id="jqGridPager"></div>
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
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
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
		
	<script type="text/javascript"> 
	
	$(document).ready(function () {
		
		$(top.hangge());//关闭加载状态
		//resize to fit page size
		$(window).on('resize.jqGrid', function () {
			$("#jqGrid").jqGrid( 'setGridWidth', $(".page-content").width());
			$("#jqGrid").jqGrid( 'setGridHeight', $(window).height() - 230);
	    })
		
		$("#jqGrid").jqGrid({
			url: '<%=basePath%>tmplconfig/list.do',
			datatype: "json",
			 colModel: [
				//隐藏where条件
				{ label: '单位编码', name: 'DEPT_CODE', width: 60,hidden : true,editable: true,},
				{ label: '表编码', name: 'TABLE_CODE', width: 60,hidden : true,editable: true,},
				{ label: '列编码', name: 'COL_CODE', width: 60,hidden : true,editable: true,},
				//{ label: '列编码', name: 'DICT_TRANS', width: 60,hidden : true,editable: true,},
			
				{label: '单位',name:'DNAME', width:100}, 
				{ label: '表名', name: 'TABLE_NAME', width: 90},
				{ label: '列编码', name: 'COL_CODE', width: 60},
				{ label: '列名称', name: 'COL_NAME', width: 60,editable: true,},
				{ label: '显示序号', name: 'DISP_ORDER', width: 80,formatter: 'int', sorttype: 'number',editable: true,},
				{ label: '字典翻译', name: 'DICT_TRANS', width: 80,align:'center',editable: true,edittype: 'select',formatter:'select',formatteroptions:{value:"${dictString}"},editoptions:{value:"${dictString}"}},                  
				{ label: '列隐藏', name: 'COL_HIDE', width: 80, editable: true,align:'center',edittype:"checkbox",editoptions: {value:"1:0"},unformat: aceSwitch},                   
				{ label: '列汇总', name: 'COL_SUM', width: 80, editable: true,align:'center',edittype:"checkbox",editoptions: {value:"1:0"},unformat: aceSwitch},                   
				{ label: '列平均值', name: 'COL_AVE', width: 80, editable: true,align:'center',edittype:"checkbox",editoptions: {value:"1:0"},unformat: aceSwitch}                   
			],
			reloadAfterSubmit: true, 
			//viewrecords: true, // show the current page, data rang and total records on the toolbar
			rowNum: 1000,
			sortname: 'DISP_ORDER',
			pager: "#jqGridPager",
			pgbuttons: false,//上下按钮 
			pginput:false,//输入框
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
			rownumbers: true, // show row numbers
            rownumWidth: 35, // the width of the row numbers columns			
	        ondblClickRow: dbClickRow,//双击表格编辑
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
				refreshicon : 'ace-icon fa fa-refresh green',
				view: false,
				viewicon : 'ace-icon fa fa-search-plus grey',
			},
			{
				//edit record form
				//closeAfterEdit: true,
				//width: 700,
				recreateForm: true,
				beforeShowForm :beforeEditOrAddCallback
			},
			 {
				//new record form
				//width: 700,
				closeAfterAdd: true,
				recreateForm: true,
				viewPagerButtons: false,
				//reloadAfterSubmit: true,
				beforeShowForm : beforeEditOrAddCallback,
			    onclickSubmit: function(params, posdata) {
					console.log("onclickSubmit");
                    //console.log(posdata	);
                } , 
                afterSubmit: fn_addSubmit
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
				showQuery: true
				
			}
		);
	
		
		// 批量编辑
        $('#jqGrid').navButtonAdd('#jqGridPager',
        {
            buttonicon: "ace-icon fa fa-pencil-square-o purple",
            title: "批量编辑",
            caption: "",
            position: "last",
            onClickButton: batchEdit
        });
		
     	// 取消批量编辑
        $('#jqGrid').navButtonAdd('#jqGridPager',
        {
            buttonicon: "ace-icon fa fa-undo",
            title: "取消批量编辑",
            caption: "",
            position: "last",
            onClickButton: batchCancelEdit
        });

        //批量保存
        $('#jqGrid').navButtonAdd('#jqGridPager',
        {
     	   /* bigger-150 */
            buttonicon: "ace-icon fa fa-save green",
            title: "批量保存",
            caption: "",
            position: "last",
            onClickButton: batchSave
        });
 	});

	
	
	 //批量编辑
	function batchEdit(e) {
		var grid = $("#jqGrid");
        var ids = grid.jqGrid('getDataIDs');
        console.log("批量编辑"+ids);
        for (var i = 0; i < ids.length; i++) {
            grid.jqGrid('editRow',ids[i]);
        }
    }
	
	//取消批量编辑
	function batchCancelEdit(e) {
		var grid = $("#jqGrid");
        var ids = grid.jqGrid('getDataIDs');
        for (var i = 0; i < ids.length; i++) {
            grid.jqGrid('restoreRow',ids[i]);
        }
    }
	
	//批量保存
	function batchSave(e) {
		 var listData =new Array();
		var ids = $("#jqGrid").jqGrid('getDataIDs');
		//console.log("ids"+ids);
		//遍历访问这个集合  
		var rowData;
		$(ids).each(function (index, id){  
			console.log("index"+index);
			console.log("id"+id);

           $("#jqGrid").saveRow(id, false, 'clientArray');
            
             rowData = $("#jqGrid").getRowData(id);
     		console.log("rowData"+rowData);
            listData.push(rowData);
		}); 
		top.jzts();
		$.ajax({
			type: "POST",
			url: '<%=basePath%>tmplconfig/updateAll.do?',
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
	
	
	
	
	
		//检索
		function tosearch() {
			
			if($("#TABLE_NO").val()==""){
				$("#TABLE_NO").tips({
					side:3,
		            msg:'请选择表名称',
		            bg:'#AE81FF',
		            time:2
		        });
				$("#TABLE_NO").focus();
			return false;
			}
			if($("#selectTree2_input").val()=="请选择"){
				document.getElementById("DEPARTMENT_CODE").value="001"; 
				document.getElementById("DNAME").value="总部"; 
				
			}
			
			var TABLE_NO = $("#TABLE_NO").val(); 
			var DEPARTMENT_CODE = $("#DEPARTMENT_CODE").val(); 
			var TABLE_NAME = $("#TABLE_NO").find("option:selected").text();
			var DNAME = $("#DNAME").val(); 
			$("#jqGrid").jqGrid('setGridParam',{  // 重新加载数据
				url:'<%=basePath%>tmplconfig/getPageList.do?TABLE_NO='+TABLE_NO+'&DEPARTMENT_CODE='+DEPARTMENT_CODE+'&TABLE_NAME='+TABLE_NAME+'&DNAME='+DNAME,  
				datatype:'json',
			      page:1
			}).trigger("reloadGrid");
			
		}  
		
		//显示隐藏查询
		function showQueryCondi(){
			if($(".widget-box").css("display")=="block"){
				$("#btnQuery").find("i").removeClass('fa-chevron-up').addClass('fa-chevron-down');
				$("#btnQuery").find("span").text("显示查询");
				$(window).triggerHandler('resize.jqGrid');
			}
			else{
				$("#btnQuery").find("i").removeClass('fa-chevron-down').addClass('fa-chevron-up');
				$("#btnQuery").find("span").text("隐藏查询");
			}
			$(".widget-box").toggle("fast");
			
		}
		
		//自定义列隐藏样式
		function customFmatterState(cellvalue, options, rowObject){  
			if (cellvalue==1) {
				 return '<span class="label label-important arrowed-in">隐藏</span>';
			} else if(cellvalue==0){
				return '<span class="label label-success arrowed">不隐藏</span>';
			}
		};
		
		//switch element when editing inline
		function aceSwitch( cellvalue, options, cell ) {
			setTimeout(function(){
				$(cell) .find('input[type=checkbox]')
					.addClass('ace ace-switch ace-switch-5')
					.after('<span class="lbl"></span>');
			}, 0);
			console.log(cellvalue);
			console.log(options);
			console.log(cell);
			if (cellvalue=='隐藏') {			
				return 1;
			}else if (cellvalue=='不隐藏') {
				return 0;
			}
		}
		
		
		
		 function unformatSelect(cellvalue, options, cellobject ) {
			 var unformatValue = '';
			 
			 var strs= new Array(); 
			 var str = options.colModel.editoptions.value;
			 strs=str.split(";");

			  $.each(strs, function (index, value)
			  {
				  var lastStrs = new Array();
				  var lastStr = value;
				  lastStrs=lastStr.split(":");
				 
			   if (cellvalue == lastStrs[1])
			   {
			    unformatValue = lastStrs[0];
			    console.log(unformatValue);
			   }
			  });
			  return unformatValue;
		}
		 
		
    
		function initComplete(){
			console.log("下拉树");
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
		
		
        
        function copyData() {
        	var TABLE_NO = $("#TABLE_NO").val(); 
			var DEPARTMENT_CODE = $("#DEPARTMENT_CODE").val(); 
        	 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag = true;
			 diag.Title = "复制";
			 diag.URL = '<%=basePath%>department/listAllDepartmentCopy.do?TABLE_NO='+TABLE_NO+'&DEPARTMENT_CODE='+DEPARTMENT_CODE;
			 diag.Width = 320;
			 diag.Height = 450;
			 diag.CancelEvent = function(){ //关闭事件
				diag.close();
			 };
			 diag.show();
		}
		
		<%-- function getDictList() {
			$.ajax({
				type: "POST",
				url: '<%=basePath%>tmplconfig/getDictList.do?',
		    	dataType:'json',
				cache: false,
				success: function(response){
					if(response.code==0){
						
					}else{
						
					}
				},
		    	error: function(e) {
					$(top.hangge());//关闭加载状态
		    	}
			}); 
			
		} --%>
		var lastSelection;
		function dbClickRow(rowId, rowIndex, colnumIndex, event){ 
			if (rowId && rowId !== lastSelection) {
                var grid = $("#jqGrid");
                grid.jqGrid('restoreRow',lastSelection);
                grid.jqGrid('editRow',rowId, {keys: true} );
                lastSelection = rowId;
            }
			console.log("双击表格");
		}
	
	
 	</script>


</body>
</html>