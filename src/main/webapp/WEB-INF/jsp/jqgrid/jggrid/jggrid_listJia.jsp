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
						<!-- <h1>
							东部管道
							<small>
								<i class="ace-icon fa fa-angle-double-right"></i>
								成本核算
							</small>
						</h1> -->
						<table >
							<tr>
								<td>
									<span class="label label-xlg label-success arrowed-right">东部管道</span>
									<!-- arrowed-in-right -->
									<span class="label label-xlg label-yellow arrowed-in arrowed-right" id="subTitle" style="margin-left:2px;">成本核算</span>
								</td>
							</tr>
						</table>
					</div><!-- /.page-header -->
				
					<div class="row">
						<div class="col-xs-12">
							<!-- PAGE CONTENT BEGINS -->
							<!-- <div class="well well-sm">
								You can have a custom jqGrid download here:
								<a href="http://www.trirand.com/blog/?page_id=6" target="_blank">
									
									<i class="fa fa-external-link bigger-110"></i>
								</a>
							</div> -->
						
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
		/* $.jgrid.defaults.width = 780;*/
		//$.jgrid.defaults.styleUI = 'Bootstrap'; 
		$(top.hangge());//关闭加载状态
		
		//resize to fit page size
		$(window).on('resize.jqGrid', function () {
			$("#jqGrid").jqGrid( 'setGridWidth', $(".page-content").width());
			//console.log("ccc"+$("iframe").height());
			$("#jqGrid").jqGrid( 'setGridHeight', $(window).height() - 200);
	    })
		
		$("#jqGrid").jqGrid({
			<%-- url: '<%=basePath%>static/data/data.json', --%>
			url: '<%=basePath%>jqgridJia/getPageList.do',
			datatype: "json",
			 colModel: [
				{label: ' ',name:'myac',index:'', width:80, fixed:true, sortable:false, resize:false,
					formatter:'actions', 
					formatoptions:{ 
                        onSuccess: function(response) {
							
                        	//var jsonResponse = $.parseJSON(response.responseText);
							//console.log(response.responseJSON);
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
                        	//console.log("afterSave");
                        	$("#jqGrid").trigger("reloadGrid");
                        },
					
						keys:true,
						//delbutton: false,//disable delete button
						delOptions:{
							recreateForm: true, 
							beforeShowForm:beforeDeleteCallback,
							afterSubmit: function(response, postData) {
								if(response.responseJSON.code==0){
									console.log("sss");
									return [true];
								}else{
									console.log("rsdf");
									return [false, response.responseJSON.message];
								}
                            },
						}
						//editformbutton:true, editOptions:{recreateForm: true, beforeShowForm:beforeEditCallback}
						 
						/* editformbutton:true, 
						editOptions:{
							onclickSubmit: function(params, posdata) {
								console.log("submit");
                                console.log(posdata	);
                            } , 
                            afterSubmit: fn_editSubmit
						} */ 
						
						//editformbutton:true,
						/*editOptions: {  //编辑操作，这个很重要，实现编辑时传送参数什么的。  
                            //reloadAfterSubmit: true,
                             editData: {  
                                editkey: function () {  
                                    var sel_id = $('#TblClassTypeId').jqGrid('getGridParam', 'selrow');  
                                    var value = $('#TblClassTypeId').jqGrid('getCell', sel_id, 'Id_Key');  
                                    return value;  
                                }  
                            }   
						}*/
					}
				},
				{label: 'id',name:'ID',index:'',key: true, width:30, sorttype:"int", editable: false},
				{ label: 'Category Name', name: 'CATEGORYNAME', width: 75,editable: true,editoptions:{size:"20",maxlength:"30"} },
				{ label: 'Product Name', name: 'PRODUCTNAME', width: 90,editable: true,editoptions:{size:"20",maxlength:"30"} },
				{ label: 'Country', name: 'COUNTRY', width: 100,editable: true,edittype:"select",editoptions:{value:"FE:FedEx;IN:InTime;TN:TNT;AR:ARAMEX"} },
				{ label: 'Price', name: 'PRICE', width: 80, formatter: 'number',sorttype: 'number',summaryTpl: "sum: {0}", summaryType: "sum",editable: true},
				// sorttype is used only if the data is loaded locally or loadonce is set to true
				{ label: 'Quantity', name: 'QUANTITY', width: 80, sorttype: 'integer',editable: true }                   
			],
			reloadAfterSubmit: true, 
			//caption: "jqGrid with inline editing",
			//autowidth:true,
			//shrinkToFit:true,
			viewrecords: true, // show the current page, data rang and total records on the toolbar
			rowNum: 10,
			//loadonce: true, // this is just for the demo
			//height: '100%', 
			
			sortname: 'PRODUCTNAME',
			
			footerrow: true,
			userDataOnFooter: true, // the calculated sums and/or strings from server are put at footer row.
			grouping: true,
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
            },
            
            /* groupingView : { 
				 groupField : ['name'],
				 groupDataSorted : true,
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
	        editurl: "<%=basePath%>jqgridJia/edit.do"//nothing is saved
		});
		
		$(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size
	
		//navButtons
		jQuery("#jqGrid").jqGrid('navGrid',"#jqGridPager",
			{ 	//navbar options
				edit: false,
				editicon : 'ace-icon fa fa-pencil blue',
				add: true,
				addicon : 'ace-icon fa fa-plus-circle purple',
				del: true,
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
				//delete record form
				recreateForm: true,
				beforeShowForm : beforeDeleteCallback,
				onClick : function(e) {
					<%-- console.log("BatchDelete");
					//alert(1);
					var ids = grid.jqGrid('getDataIDs');
					var strIds="";
					for (var i = 0; i < ids.length; i++) {
						if(strIds=='')
							strIds+= ids[i];
						else
							strIds+= ','+ids[i];
                		//grid.jqGrid('editRow',ids[i]);
            		}
					$.ajax({
						type: "POST",
						url: '<%=basePath%>jqgridJia/deleteAll.do?tm='+new Date().getTime(),
				    	data: {DATA_IDS:str},
						dataType:'json',
						//beforeSend: validateData,
						cache: false,
						success: function(data){
							 
						}
					}); --%>
				}
			},
			{
				//search form
				recreateForm: true,
				afterShowSearch: beforeSearchCallback,
				afterRedraw: function(){
					style_search_filters($(this));
				}
				,
				multipleSearch: true,
				/**
				multipleGroup:true,
				showQuery: true
				*/
			}
		);
	
	});

 	</script>
</body>
</html>