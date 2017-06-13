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
</head>
<body class="no-skin">
	<div class="main-container" id="main-container">
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<!-- /section:settings.box -->
					<div class="page-header">
						<h1>
							东部管道
							<small>
								<i class="ace-icon fa fa-angle-double-right"></i>
								成本核算
							</small>
						</h1>
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
		
	<script type="text/javascript"> 
	$(document).ready(function () {
		/* $.jgrid.defaults.width = 780;*/
		//$.jgrid.defaults.styleUI = 'Bootstrap'; 
		$(top.hangge());//关闭加载状态
		
		//resize to fit page size
		$(window).on('resize.jqGrid', function () {
			$("#jqGrid").jqGrid( 'setGridWidth', $(".page-content").width());
			//console.log("ccc"+$("iframe").height());
			//$("#jqGrid").jqGrid( 'setGridHeight', $(window).height() - 138);
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
                        	$("#jqGrid").trigger("reloadGrid");
                        },
					
						keys:true,
						//delbutton: false,//disable delete button
						delOptions:{recreateForm: true, beforeShowForm:beforeDeleteCallback},
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
			//rowNum: 30,
			//loadonce: true, // this is just for the demo
			height: '100%', 
			
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
			
			multiselect: true,
			//multikey: "ctrlKey",
	        multiboxonly: true,
	        editurl: "<%=basePath%>jqgridJia/edit.do"//nothing is saved
		});
		
		$(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size
	
		//it causes some flicker when reloading or navigating grid
		//it may be possible to have some custom formatter to do this as the grid is being created to prevent this
		//or go back to default browser checkbox styles for the grid
		function styleCheckbox(table) {
		/**
			$(table).find('input:checkbox').addClass('ace')
			.wrap('<label />')
			.after('<span class="lbl align-top" />')
	
	
			$('.ui-jqgrid-labels th[id*="_cb"]:first-child')
			.find('input.cbox[type=checkbox]').addClass('ace')
			.wrap('<label />').after('<span class="lbl align-top" />');
		*/
		}
		
	
		//unlike navButtons icons, action icons in rows seem to be hard-coded
		//you can change them like this in here if you want
		function updateActionIcons(table) {
			/**
			var replacement = 
			{
				'ui-ace-icon fa fa-pencil' : 'ace-icon fa fa-pencil blue',
				'ui-ace-icon fa fa-trash-o' : 'ace-icon fa fa-trash-o red',
				'ui-icon-disk' : 'ace-icon fa fa-check green',
				'ui-icon-cancel' : 'ace-icon fa fa-times red'
			};
			$(table).find('.ui-pg-div span.ui-icon').each(function(){
				var icon = $(this);
				var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
				if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
			})
			*/
		}
		
		//replace icons with FontAwesome icons like above
		function updatePagerIcons(table) {
			var replacement = 
			{
				'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
				'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
				'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
				'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
			};
			$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
				var icon = $(this);
				var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
				
				if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
			})
		}
	
		function enableTooltips(table) {
			$('.navtable .ui-pg-button').tooltip({container:'body'});
			$(table).find('.ui-pg-div').tooltip({container:'body'});
		}
		
		function style_delete_form(form) {
			var buttons = form.next().find('.EditButton .fm-button');
			buttons.addClass('btn btn-sm btn-white btn-round').find('[class*="-icon"]').hide();//ui-icon, s-icon
			buttons.eq(0).addClass('btn-danger').prepend('<i class="ace-icon fa fa-trash-o"></i>');
			buttons.eq(1).addClass('btn-default').prepend('<i class="ace-icon fa fa-times"></i>')
		}
		
		function beforeDeleteCallback(e) {
			var form = $(e[0]);
			if(form.data('styled')) return false;
			
			form.closest('.ui-jqdialog').find('.ui-jqdialog-titlebar').wrapInner('<div class="widget-header" />')
			style_delete_form(form);
			
			form.data('styled', true);
		}
		
		function fn_editSubmit(response,postdata){
			console.log("ssss");
			var json=response.responseText;
			alert(json);//显示返回值
			}
	
	});

 	</script>
</body>
</html>