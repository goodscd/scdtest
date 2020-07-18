<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" +
request.getServerName() + ":" + request.getServerPort() +
request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>"/>
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<%--分页查询的组件--%>
	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>



	<script type="text/javascript">

	$(function(){
		//控制创建模态窗口的开关
		$("#addBtn").click(function () {
			//年-月-日前端框架所提供的日期选择器
			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});
			/* 1.使用js代码控制模态窗口的开启
                 操作模态窗口的方式：需要操作的模态窗口的对象，调用modal方法
                  为该方法传递参数，show表示	：打开模态窗口  hide表示：关闭模态窗口
               2.在模态窗口打开之前，先将所有者的信息步入select下拉框之中
            */
			$.get("workbench/activity/getUserList.do",function (data) {
				$("#create-owner").empty()
				$.each(data,function (i,n) {
					$("#create-owner").append("<option value='"+n.id+"'>"+n.name+"</option>");
				})
			})
			//让下拉框默认值为当前登录用户的name
			var id="${sessionScope.user.id}";
			$("#create-owner").val(id);
			//打开模态窗口
			$("#createActivityModal").modal("show");
		})
		/* 添加下拉框列表的取值,添加操作：*/
		$("#savebtn").click(function () {
			if ($.trim($("#create-name").val()) == "") {
				alert("市场活动名称不能为空")
				return;
			}
			$.ajax({
				url:"workbench/activity/save.do",
				data:{
					"owner":$("#create-owner").val(),
					"name":$("#create-name").val(),
					"startDate":$("#create-startDate").val(),
					"endDate":$("#create-endDate").val(),
					"cost":$("#create-cost").val(),
					"description":$("#create-description").val(),
				},
				type:"post",
				dataType:"json",
				success:function(data){
					/*
                       从后端获取到的json数据为：  data:{success:true/false}
                       若为true,则说明添加成功，若为false，则说明添加失败
                        */
					if (data.success) {
						//重置from表单
						/*
                           jquery对象和dom对象的转化：
                              jquery对象实际上是dom对象的数组
                              即jquery转dom:dom对象=jquery[0]
                              dom对象转jquery对象：
                              jquery对象=$(dom)
                            */
						$("#activityAddFrom")[0].reset();
						//添加成功，关闭模态窗口
						$("#createActivityModal").modal("hide")
						/*$("#activityPage").bs_pagination('getOption', 'currentPage')代表当前页
						* $("#activityPage").bs_pagination('getOption', 'rowsPerPage')为每次维持多少条记录数*/
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


					}else{
						alert("添加市场活动失败")
					}
				}
			})
		})
		/**修改市场活动列表*/
		/*
		  打开模态窗口：data-toggle="modal" data-target="#editActivityModal"
		  有我们自己控制模态窗口的打开与关闭
		  */
		$("#editBtn").click(function () {
			//选中列表中的数据进行修改
			var $xz=$("input[name=xz]:checked");
			if ($xz.length==0) {
				alert("请选中要修改的数据选项")
			}else if($xz.length>1){
				alert("一次只能修改一条记录")
			}else{//说明选中了一条，进行ajax操作
				var id=$xz.val()
				$.ajax({
					url:"workbench/activity/getUserListAndActivity.do",
					data:{
						id:id
					},
					type:"post",
					dataType:"json",
					success:function(data){
						/*data中的json数据为    user表中的数据的list集合   activity表中的list集合*/
						$("#edit-owner").empty();
						$.each(data.uList,function (i,n) {
							$("#edit-owner").append("<option value='"+n.id+"'>"+n.name+"</option>")
						})
						var activity=data.activity;
						$("#edit-name").val(activity.name)
						$("#edit-startDate").val(activity.startDate)
						$("#edit-endDate").val(activity.endDate)
						$("#edit-cost").val(activity.cost)
						$("#edit-description").val(activity.description)
						$("#edit-id").val(activity.id)
					}
				})
				//添加完所有信息之后，打开模态窗口
				$("#editActivityModal").modal("show");
			}


		})
		//点击更新按钮，执行修改操作
		$("#updatebtn").click(function () {
			$.ajax({
				url:"workbench/activity/update.do",
				data:{
					"id":$("#edit-id").val(),
					"owner":$("#edit-owner").val(),
					"name":$("#edit-name").val(),
					"startDate":$("#edit-startDate").val(),
					"endDate":$("#edit-endDate").val(),
					"cost":$("#edit-cost").val(),
					"description":$("#edit-description").val(),
				},
				type:"post",
				dataType:"json",
				success:function(data){
					/*
                       从后端获取到的json数据为：  data:{success:true/false}
                       若为true,则说明添加成功，若为false，则说明添加失败
                        */
					if (data.success) {
						//重置from表单
						/*
                           jquery对象和dom对象的转化：
                              jquery对象实际上是dom对象的数组
                              即jquery转dom:dom对象=jquery[0]
                              dom对象转jquery对象：
                              jquery对象=$(dom)
                            */
						/*$("#activityAddFrom")[0].reset();*/
						//修改成功，关闭模态窗口
						$("#editActivityModal").modal("hide")
						$("input[name=xz]").prop("checked",false)
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage'),$("#activityPage").bs_pagination('getOption', 'rowsPerPage'))
					}else{
						alert("修改市场活动失败")
					}
				}
			})

		})

		$("#searchbtn").click(function () {
			/*点击查询按钮的时候，将搜索框中的内容保存到隐藏域之中*/
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));
			/*
            点击查询按钮局部刷新市场活动列表
            */
			pageList(1,2)

		})
		//在市场活动页面加载之前，将数据铺到市场活动列表之中
		pageList(1,2)
		//复选框全选的按钮
		$("#checkbox").click(function () {
			$("input[name=xz]").prop("checked",this.checked)
		})
		/*
		  动态生成的元素不能用普通绑定事件的形式来操作
		  动态生成的元素以on方法的形式来触发事件
		  语法：$("需要绑定元素的外层有效元素).on("绑定事件,需要绑定的jquery对象，回调函数"）
		*/
		$("#activityBody").on("click",$("input[name=xz]"),function () {
			//标签选择器，若全部选中，则#checkbox选中，比较选中的数量是否和复选框的数量相同
			$("#checkbox").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);
		})
		//执行删除操作：
		$("#detletBtn").click(function () {
			var $xz=$("input[name=xz]:checked");
			if ($xz.length==0) {
				alert("请选中你要删除的数据")
			}else{
				if (confirm("确定要删除选中的记录吗？")){
					var param="";
					for(var i=0;i<$xz.length;i++){
						param+="id="+$($xz[i]).val();
						if(i<$xz.length-1){
							param+="&";
						}
					}
					$.ajax({
						url:"workbench/activity/delete.do",
						data:param,
						type:"post",
						dataType:"json",
						success:function(data){
							//data={success:true}
							if (data.success) {
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'))
								alert("删除成功")
								//刷新页面

							}else{
								alert(data.msg)
							}
						}
					})
				}

			}

		})


	});
	//分页操作的方法，pageNo为页码，pagesize为每页显示的记录条数
	function  pageList(pageNo,pageSize) {
		//每次查询前，将全选框的选中去掉
		$("#checkbox").prop("checked",false)
		/*查询前，将隐藏域中的信息显示在搜索框中*/
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#hidden-endDate").val($.trim($("#hidden-endDate").val()));
		$.ajax({
			url:"workbench/activity/pageList.do",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"startDate":$.trim($("#search-startDate").val()),
				"endDate":$.trim($("#search-endDate").val())
			},
			type:"get",
			dataType:"json",
			//从后台返回的json数据为：{total:100,"dataList":json数组}
			//向市场活动列表中步入activity表中的数据
			success:function(data){
				$("#activityBody").empty();
				$.each(data.dataList,function (i,n) {
					//所拼接的列表格式
					<%--<tr class="active">
                   <td><input type="checkbox" /></td>
                   "<td><a style='text-decoration: none; cursor: pointer;' onclick=\"window.location.href='workbench/activity/detail.do?id="+n.id+"';\">"+n.name+"</a></td>"
                   <td>zhangsan</td>
                   <td>2020-10-10</td>
                   <td>2020-10-20</td>
                   </tr>--%>
					var id="f4d6ad920e014d9198385e0dd470fb82"
					$("#activityBody").append("<tr class='active'>" +
							                     "<td><input type='checkbox' name='xz' value='"+n.id+"' /></td>   " +
							                     "<td><a style='text-decoration: none; cursor: pointer;' onclick=\"window.location.href='workbench/activity/detail.do?id="+n.id+"';\">"+n.name+"</a></td>" +
							                     "<td>"+n.owner+"</td>" +
						                     	 "<td>"+n.startDate+"</td>" +
							                     "<td>"+n.endDate+"</td>" +
							                   "</tr>")

				})


				//计算总页数
				var totalPages=data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;
				/*数据处理完毕后，结合分页查询，对前端展示分页信息*/
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数
					visiblePageLinks: 3, // 显示几个卡片
					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,
					/*该回调函数是在点击分页组件的时候触发的*/
					onChangePage : function(event, data){
						//pageList为执行分页操作的方法名
						pageList(data.currentPage , data.rowsPerPage);
					}
				});

			}
		})

	}
	
</script>
</head>
<body>
    <%--加入隐藏域--%>
    <input type="hidden" id="hidden-name">
    <input type="hidden" id="hidden-owner">
    <input type="hidden" id="hidden-startDate">
    <input type="hidden" id="hidden-endDate">
	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" id="activityAddFrom" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner" name="owner">
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name" name="name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" name="startDate">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" name="endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost" name="cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description" name="description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" id="savebtn"   class="btn btn-primary"  data-dismiss="modal">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						<%--隐藏的id,用来修改表中的数据--%>
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control"  name="owner" id="edit-owner">
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" name="name" id="edit-name" >
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 time control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" name="startDate" id="edit-startDate" >
							</div>
							<label for="edit-endTime" class="col-sm-2 time  control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" name="endDate" id="edit-endDate" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default"  data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary"  id="updatebtn" data-dismiss="modal">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name" name="name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner" name="owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate"  name="startDate"/>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon ">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate" name="endDate">
				    </div>
				  </div>
				  
				  <button type="button" id="searchbtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
					<%--
					   data-toggle="modal":
					        表示触发该按钮，将要打开一个模态窗口
					   data-target="#createActivityModal"：表示打开的模态窗口的目标
					  将<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#createActivityModal">用js代码来替换
					--%>
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn" ><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="detletBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox"  id="checkbox"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>