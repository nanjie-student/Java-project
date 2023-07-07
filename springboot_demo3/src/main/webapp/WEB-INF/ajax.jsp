<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>您好Springboot</title>
<!-- 引入函数类库 -->
<script src="../js/jquery-3.4.1.min.js"></script>
<script type="text/javascript">
	
	//1.让页面全部加载完成  函数式编程
	$(function(){
		
	//1.$.get(url地址,传递的参数,回调函数,返回值类型)  
	//$.post()   $.getJSON  $.ajax
	$.ajax({
		url:	"/findAjax",	//url地址
		method: "get",			//请求类型
		data: {id:1,name:"tomcat"},  //请求参数
		success: function(data){
			for(let user of data){
				//console.log(user);
				let id = user.id;
				let name = user.name;
				let age = user.age;
				let sex = user.sex;
				let tr = "<tr align='center'><td>"+id+"</td><td>"+name+"</td><td>"+age+"</td><td>"+sex+"</td></tr>"
				$("#table1").append(tr);
			}
		},
		error:   function(data){
			alert("请求失败");
		},
		cache:   true,		//默认条件下缓存是开启的  false
		async:	 true		//默认就是异步
	})
	
		
		
		//关于参数写法  2种 
		//1.JSON格式{id:1,name:"tomcat"}   
		//2.字符串拼接 id=1&name="tomcat"
		$.get("/findAjax3333333333333",{id:1},function(data){
			//循环遍历方式3  of
			for(let user of data){
				//console.log(user);
				let id = user.id;
				let name = user.name;
				let age = user.age;
				let sex = user.sex;
				let tr = "<tr align='center'><td>"+id+"</td><td>"+name+"</td><td>"+age+"</td><td>"+sex+"</td></tr>"
				$("#table1").append(tr);
			}
			
			//循环遍历方式2  in
			/* for(let index in data){
				let user = data[index];
				console.log(user);
			} */
			
			//console.log(data);
			//循环遍历方式1
			/* for(let i=0;i<data.length;i++){
				let user = data[i];
				let id = user.id;
				let name = user.name;
				console.log(id+":"+name);
			} */
		})
	})
	
	
	
</script>
</head>
<body>
	<table id="table1"  border="1px" width="65%" align="center">
		<tr>
			<td colspan="6" align="center"><h3>学生信息</h3></td>
		</tr>
		<tr>
			<th>编号</th>
			<th>姓名</th>
			<th>年龄</th>
			<th>性别</th>
		</tr>
	</table>
</body>
</html>