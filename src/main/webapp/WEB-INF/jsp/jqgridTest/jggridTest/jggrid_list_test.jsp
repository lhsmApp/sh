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
</head>
<body class="no-skin">
	<div class="main-container" id="main-container">
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<!-- /section:settings.box -->
					<div class="page-header">
						<h1>
							东部管道 <small> <i class="ace-icon fa fa-angle-double-right"></i>
								成本核算
							</small>
						</h1>
					</div>
					<!-- /.page-header -->
					
					
					<!-- 检索  -->
						<table style="margin-top:5px;">
							<tr>
								<td>
									<div class="nav-search">
										<span class="input-icon">
											<input type="text" placeholder="这里输入关键词" class="nav-search-input" id="keywords" autocomplete="off" name="keywords" value="${pd.keywords }" placeholder="这里输入关键词"/>
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
									</div>
								</td>
								
								<td style="vertical-align:top;padding-left:2px"><a class="btn btn-light btn-xs" onclick="tosearch();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
							</tr>
						</table>
						<!-- 检索  -->
					
					

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

		<a href="#" id="btn-scroll-up"
			class="btn-scroll-up btn btn-sm btn-inverse"> <i
			class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a> <br /> <br /> <input class="btn btn-default" type="button"
			value="Get Selected Rows" onclick="getSelectedRows()" /> <br /> <br />

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
		<script src="static/ace/js/ace/elements.spinner.js"></script>

	<script type="text/javascript"> 
	$(document).ready(function () {
		
		//创建一个input输入框
		function myelem (value, options) {
		var el = document.createElement("input");
		el.type="text";
		$(el).ace_spinner({value:0,min:0,max:200,step:10, btn_up_class:'btn-info' , btn_down_class:'btn-info'});
		/* .closest('.ace-spinner')
		.on('changed.fu.spinbox', function(){
			//alert($('#spinner1').val())
			return $(el).val();
		});  */
		/* el.value = value;
		return el; */
		}
	/* 	 */
		//获取值
		function myvalue(elem) {
		return $(elem).val();
		}
		
		
		
		$(top.hangge());//关闭加载状态
		
		//resize to fit page size
		$(window).on('resize.jqGrid', function () {
			$("#jqGrid").jqGrid( 'setGridWidth', $(".page-content").width());
			//console.log("ccc"+$("iframe").height());
			//$("#jqGrid").jqGrid( 'setGridHeight', $(window).height() - 138);
	    })
	    
		
		$("#jqGrid").jqGrid({	
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
			subGridRowExpanded: function (subgridDivId, rowId) {
				var subgridTableId = subgridDivId + "_t";
				$("#" + subgridDivId).html("<table id='" + subgridTableId + "'></table>");
				$("#" + subgridTableId).jqGrid({
					datatype: 'json',
					url: '<%=basePath%>jqgriddetail/detailList.do?ParentId='+rowId,
					colNames: ['Name','Qty','SaleDate'],
					colModel: [
						{ name: 'NAME', width: 50 },
						{ name: 'QTY', width: 150 },
						{ name: 'SALEDATE', width: 50 }
					]
				});
			},
		
			<%-- url: '<%=basePath%>static/data/data.json', --%>
			url: '<%=basePath%>jqgrid/getPageList.do',
			editurl: '<%=basePath%>jqgrid/edit.do',
			datatype: "json",
			 colModel: [
				  {label: "Edit Actions", name: "actions", width: 100,formatter: "actions",
                     formatoptions: {
                         keys: true,
                         editOptions: {},
                         addOptions: {},
                         delOptions: {}
                     }       
                 }, 
                 { label: 'id', name: 'ID',key: true, width: 75, editable: true, hidden: true },
				{ label: 'Category Name', name: 'CATEGORYNAME', width: 75, editable: true,
                	// stype defines the search type control - in this case HTML select (dropdownlist)
                     stype: "select",
                     // searchoptions value - name values pairs for the dropdown - they will appear as options
                     searchoptions: { value: ":[All];aa:aa;Beverages:Beverages;Condiments:Condiments;Confections:Confections;Dairy Products:Dairy Products" }

                	 
				
                	 },
			
				
				
				
				{ label: 'Product Name', name: 'PRODUCTNAME', width: 90, editable: true },
				{ label: 'Country', name: 'COUNTRY', width: 100, editable: true },
				{ label: 'Price', name: 'PRICE', width: 80, sorttype: 'integer',summaryTpl: "Sum: {0}", // set the summary template to show the group summary
                    summaryType: "sum", // set the formula to calculate the summary type
                    formatter:'number', editable: true },
				// sorttype is used only if the data is loaded locally or loadonce is set to true
				{ label: 'Quantity', name: 'QUANTITY', width: 80, sorttype: 'number',summaryType: 'sum', formatter:'number', editable: true,edittype:'custom', editoptions:{custom_element: myelem, custom_value:myvalue} }                   
			],
			caption: "jqGrid with inline editing",
			
            onSelectRow: editRow, // the javascript function to call on row click. will ues to to put the row in edit mode

			
			viewrecords: true,
			//sortname : 'CATEGORYNAME',
			

			//autowidth:true,
			//shrinkToFit:true,
			rowNum: 30,
			//loadonce: true, // this is just for the demo
			height: 340, 

            //shrinkToFit: false, // must be set with frozen columns, otherwise columns will be shrank to fit the grid width
				/* rowList : [20,30,50],
                rownumbers: true, 
                rownumWidth: 25,  */
                
            multiselect: true,//check选择框
			pager: "#jqGridPager",
			//合计
			footerrow: true, // set a footer row
			userDataOnFooter: true, // the calculated sums and/or strings from server are put at footer row.
			//分组
			 grouping: true,
			groupingView: {
				groupField: ["CATEGORYNAME"],
				groupColumnShow: [true],
				groupText: ["<b>{0}</b>"],
				groupOrder: ["asc"],
				groupSummary: [true],
				groupCollapse: false
			}, 
			
			
			loadComplete : function() {
				var table = this;
				setTimeout(function(){
					styleCheckbox(table);
					updateActionIcons(table);
					updatePagerIcons(table);
					enableTooltips(table);
				}, 0);
			}
		});
		
		//导航栏添加，编辑，删除等
		$('#jqGrid').navGrid('#jqGridPager',
                // the buttons to appear on the toolbar of the grid
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
			
			
			
			// options for the Edit Dialog
                {
                    editCaption: "The Edit Dialog",
                    recreateForm: true,
					checkOnUpdate : true,
					checkOnSubmit : true,
                    closeAfterEdit: true,
                    errorTextFormat: function (data) {
                        return 'Error: ' + data.responseText
                    }
                },
                // options for the Add Dialog
                {
                    closeAfterAdd: true,
                    recreateForm: true,
                    errorTextFormat: function (data) {
                        return 'Error: ' + data.responseText
                    }
                },
                // options for the Delete Dailog
                {
                    errorTextFormat: function (data) {
                        return 'Error: ' + data.responseText
                    }
                });
		
		// add first custom button
        $('#jqGrid').navButtonAdd('#jqGridPager',
            {
                buttonicon: "ui-icon-mail-closed",
                title: "Edit All",
                caption: "Edit All",
                position: "last",
                onClickButton: startEdit
            });
		
        /// add second custom button
        $('#jqGrid').navButtonAdd('#jqGridPager',
             {
                 buttonicon: "ui-icon-pencil",
                 title: "Save All",
                 caption: "Save All",
                 position: "last",
                 onClickButton: saveRows
             });
    
      /// add second custom button
        $('#jqGrid').navButtonAdd('#jqGridPager',
             {
                 buttonicon: "ui-icon-pencil",
                 title: "Del",
                 caption: "Del",
                 position: "last",
                 onClickButton: delSelectedRows
             });
		
		//冻结列
		// $("#jqGrid").jqGrid("setFrozenColumns");
		//多表头
		$('#jqGrid').setGroupHeaders(
                {
                    useColSpanStyle: true,
                    groupHeaders: [
                        { "numberOfColumns": 2, "titleText": "Headers", "startColumnName": "CATEGORYNAME" },
                        { "numberOfColumns": 3, "titleText": "Details", "startColumnName": "COUNTRY" }]
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
	
	});
	
	//点击行，行内编辑
	var lastSelection;
	function editRow(id) {
        if (id && id !== lastSelection) {
            var grid = $("#jqGrid");
            grid.jqGrid('restoreRow',lastSelection);
            grid.jqGrid('editRow',id, {keys:true, focusField: 4});
            lastSelection = id;
        }
    }
	
	function delSelectedRows() {
        var grid = $("#jqGrid");
        var rowKey = grid.getGridParam("selrow");

        if (!rowKey)
            alert("No rows are selected");
        else {
            var selectedIDs = grid.getGridParam("selarrrow");
           /*   var columnCodes=[];//初始化一个数组
            $(selrow).each(function (index, ID) {//遍历每个id  找到每个data  并把属性加到初始化数组里
                var rowData = grid.jqGrid("getRowData", ID);
                columnCodes.push(rowData.columnCode);
            });  */
            
            var result = "";
            for (var i = 0; i < selectedIDs.length; i++) {
                result += selectedIDs[i] + ",";
            }

            alert(result);
            
           <%--  $.ajax({
				type: "POST",
				url: '<%=basePath%>jqgrid/deleteAll.do',
		    	data: {DATA_IDS:result},
				dataType:'json',
				//beforeSend: validateData,
				cache: false,
				success: function(data){
					 $.each(data.list, function(i, list){
							nextPage(${page.currentPage});
					 });
				}
			}); --%>
            
            
            
        }                
    }
	
	
	 function tosearch() {
			var keywords = $("#keywords").val();
			$("#jqGrid").jqGrid('setGridParam',{  // 重新加载数据
				url:'<%=basePath%>jqgrid/getPageList.do?keywords='+keywords,  
				datatype:'json',
			      page:1
			}).trigger("reloadGrid");
			
		}  
	
	function startEdit() {
        var grid = $("#jqGrid");
        var ids = grid.jqGrid('getDataIDs');

        for (var i = 0; i < ids.length; i++) {
            grid.jqGrid('editRow',ids[i]);
        }
    };
    
    function saveRows() {
        var grid = $("#jqGrid");
        var ids = grid.jqGrid('getDataIDs');

        
        var rowData = $("#jqGrid").jqGrid("getRowData");
         $.ajax({
        	//type: "POST",
        	url: '<%=basePath%>jqgrid/edit.do',
        	data: {rowData:rowData, oper:"edit"},
        	dataType:'json',
        	//traditional: true,//必须指定为true
			cache: false,
			success: function(data){
				for (var i = 0; i < ids.length; i++) {
		            //grid.jqGrid('saveRow', ids[i]);
		            grid.jqGrid('restoreRow', ids[i]);
		        }
			 }	
		}); 
         
         
         
       /*  //检索
 		function tosearch(){
 			top.jzts();
 			$("#Form").submit();
 		}  */
       
        
    }

 	</script>
</body>
</html>