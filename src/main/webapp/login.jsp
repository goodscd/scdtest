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
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script>
		$(function () {
			//使login.jsp始终在顶层窗口中打开
			if(window.top!=window){
				window.top.location=window.location;
			}
			//每次页面刷新后，清空账号文本框中的内容
			$("#loginAct").val("")
			//登录文本框自动获取焦点
			$("#loginAct").focus()
			//当账号，密码输入完成之后，按Enter按钮，也可以执行登录操作,Enter键盘值为13
			$(window).keydown(function (event) {
				if (event.keyCode==13) {
					login()
				}
			})
			//点击button按钮，执行表单提交操作
			$('#btn').click(function () {
				login()
			})
		})
		//定义登录提交的login()方法
		function  login() {
			//将文本中的空格去掉：使用$.trim(文本)
			var loginAct=$.trim($("#loginAct").val());
			var loginPwd=$.trim($("#loginPwd").val());
			//若账号和密码为空,则提示用户，并且不执行表单提交
			if (loginAct == "" || loginPwd == "") {
				$("#msg").html("账号密码不能为空")
				return false;
			}
			//若有数据，则表单提交
			$.ajax({
				url:"settings/user/login.do",
				data:{
					"loginAct":loginAct,
					"loginPwd":loginPwd
				},
				type:"post",
				success:function (data) {
					//data:json数据    json中的数据：{sucess:true/false,msg="登录失败的四种提示")
					//如果登录成功
					if(data.success){
						//登录成功，跳转到index.html
						window.location.href="workbench/index.jsp"
					}else{//登录失败
						$("#msg").html(data.msg)
					}
				}
			})
		}
	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.jsp" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct" name="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd" name="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color:red;"></span>
						
					</div>
					<!--
					  submit被替换为button,通过js来控制表单的提交
					-->
					<button type="button" id="btn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>