﻿<%@ page language="java" contentType="text/html; charset=UTF-8"
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
	<!-- 下拉框 -->
	<link rel="stylesheet" href="static/ace/css/chosen.css" />
	<!-- jsp文件头和头部 ，其中包含旧版本（Ace）Jqgrid Css-->
	<%@ include file="../../system/index/topWithJqgrid.jsp"%>
	<!-- 日期框 -->
	<link rel="stylesheet" href="static/ace/css/datepicker.css" />
	
	<!-- 最新版的Jqgrid Css，如果旧版本（Ace）某些方法不好用，尝试用此版本Css，替换旧版本Css -->
	<!-- <link rel="stylesheet" type="text/css" media="screen" href="static/ace/css/ui.jqgrid-bootstrap.css" /> -->
	<style>
		.page-header{
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
									id="subTitle" style="margin-left: 2px;">业务封存</span> <span
									style="border-left: 1px solid #e2e2e2; margin: 0px 10px;">&nbsp;</span>

									<button id="btnQuery" class="btn btn-white btn-info btn-sm"
										onclick="showQueryCondi()">
										<i class="ace-icon fa fa-chevron-down bigger-120 blue"></i> <span>显示查询</span>
									</button></td>
							</tr>
						</table>
					</div>
					<!-- /.page-header -->
			
						<div class="row">
						<div class="col-xs-12">
							<div class="widget-box" style="display: none;">
								<div class="widget-body">
									<div class="widget-main">
										<!-- <p class="alert alert-info">Nunc aliquam enim ut arcu.</p> -->
										<form class="form-inline">
											
											<span>
												<select class="chosen-select form-control" 
													name="RPT_DEPT" id="RPT_DEPT"
													data-placeholder="请选单位"
													style="vertical-align: top; height:32px;width: 150px;">
													<option value=""></option>
													<option value="">全部</option>
													<c:forEach items="${deptList}" var="dept">
														<option value="${dept}"
															<c:if test="${pd.RPT_DEPT==dept}">selected</c:if>>${dept }</option>
													</c:forEach>
												</select>
											</span>
											<span>
												<select class="chosen-select form-control" 
													name="BILL_TYPE" id="BILL_TYPE"
													data-placeholder="请选类型"
													style="vertical-align: top; height:32px;width: 150px;">
													<option value=""></option>
													<option value="">全部</option>
													<c:forEach items="${billTypeList}" var="billType">
														<option value="${billType.nameValue}"
															<c:if test="${pd.BILL_TYPE==billType.nameValue}">selected</c:if>>${billType.nameValue}</option>
													</c:forEach>
												</select>
											</span>
											
											<span>
												<select class="chosen-select form-control" 
													name="STATUS" id="STATUS"
													data-placeholder="请选状态"
													style="vertical-align: top; height:32px;width: 150px;">
													<option value=""></option>
													<option value="0">解封</option>
													<option value="1">封存</option>
													
												</select>
											</span>
											<button type="button" class="btn btn-info btn-sm" onclick="tosearch();">
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
						    <table id="jqGrid"></table>
						    <div id="jqGridPager"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
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
		
	<script type="text/javascript"> 
	$(document).ready(function () { 
		$(top.hangge());//关闭加载状态
		 
		//resize to fit page size
		$(window).on('resize.jqGrid', function () {
			$("#jqGrid").jqGrid( 'setGridWidth', $(".page-content").width());
			$("#jqGrid").jqGrid( 'setGridHeight', $(window).height() - 200);
	    })
		
		$("#jqGrid").jqGrid({
			url: '<%=basePath%>syssealedinfo/getPageList.do',
			datatype: "json",
			 colModel: [
				{label: ' ',name:'myac',index:'', width:40, fixed:true, sortable:false, resize:false,
					formatter:'actions', 
					formatoptions:{ 
                        onSuccess: function(response) {
							
							if(response.responseJSON.code==0){
								return [true];
							}else{
								return [false, response.responseJSON.message];
							}
							
                            /* if (jsonResponse.State != 'Success') {
                                return [false, jsonResponse.ResponseMessage];
                            } else {
                                return [true];
                            }  */                    
                        },
                        onError :function(rowid, res, stat, err) {
                        	if(err!=null)
                        		console.log(err);
                        },
                        
                        afterSave:function(rowid, res){
                        	console.log("afterSave");
                        	$("#jqGrid").trigger("reloadGrid");
                        },
					
						keys:true,
					    delbutton: false,//disable delete button
					}
				},
				{label: '单据编码',name:'BILL_CODE',index:'',key: true, width:100},
				{ label: '单据单位', name: 'RPT_DEPT', width: 90,formatter:customFmatterDept},
				{ label: '单据单位', name: 'NAME', width: 90},
				{ label: '单据期间', name: 'RPT_DUR', width: 75},
				{ label: '上传人', name: 'RPT_USER', width: 75},
				{ label: '上传时间', name: 'RPT_DATE', width: 80, formatter: 'data'},
				// sorttype is used only if the data is loaded locally or loadonce is set to true
				{ label: '单据类型', name: 'BILL_TYPE_TR', width: 80,align:'center'},                  
				{ label: '状态', name: 'STATE', width: 80, editable: true,align:'center',formatter: customFmatterState,edittype:"checkbox",editoptions: {value:"1:0"},unformat: aceSwitch}                   
			],
			reloadAfterSubmit: true, 
			viewrecords: true, // show the current page, data rang and total records on the toolbar
			rowNum: 10,
			sortname: 'BILL_CODE',
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
			rownumbers: true, // show row numbers
            rownumWidth: 35, // the width of the row numbers columns			
			multiselect: true,
	        multiboxonly: true,
	        editurl: "<%=basePath%>syssealedinfo/edit.do",//nothing is save
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

	//switch element when editing inline
	function aceSwitch( cellvalue, options, cell ) {
		setTimeout(function(){
			$(cell) .find('input[type=checkbox]')
				.addClass('ace ace-switch ace-switch-5')
				.after('<span class="lbl"></span>');
		}, 0);
	}
	
	 //批量编辑
	function batchEdit(e) {
		var grid = $("#jqGrid");
        var ids = grid.jqGrid('getDataIDs');
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
			url: '<%=basePath%>syssealedinfo/updateAll.do?',
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
			var RPT_DEPT = $("#RPT_DEPT").val();
			var STATUS = $("#STATUS").val();
			var BILL_TYPE = $("#BILL_TYPE").val();
			$("#jqGrid").jqGrid('setGridParam',{  // 重新加载数据
				url:'<%=basePath%>syssealedinfo/getPageList.do?RPT_DEPT='+RPT_DEPT+'&STATUS='+STATUS+'&BILL_TYPE='+BILL_TYPE,  
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
		
		
		function customFmatterDept(cellvalue, options, rowObject){  
			
			
				return '${deptList}';
			
		};
		
		
		function customFmatterState(cellvalue, options, rowObject){  
			if (cellvalue==1) {
				 return '<span class="label label-important arrowed-in">封存</span>';
			} else {
				return '<span class="label label-success arrowed">解封</span>';
			}
		};
   
	
	
	
 	</script>
</body>
</html>