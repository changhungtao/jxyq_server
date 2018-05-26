<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.LinkedList"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.jxyq.bbs.util.XMLHelper"%>
<%@page import="com.jxyq.bbs.client.Client"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>

<title>登录论坛</title>
</head>
<body>
<%
String username = (String)request.getAttribute("username");
String password = (String)request.getAttribute("password");
String error_msg = (String)request.getAttribute("error_msg");
if(error_msg != null){
    System.out.println(error_msg);
}else{
    System.out.println(username);
    System.out.println(password);
	Client uc = new Client();
	String $uc_syn_logout = uc.uc_user_synlogout();
	out.println($uc_syn_logout);
    System.out.println("退出成功" + $uc_syn_logout);

	String result = uc.uc_user_login(username, password);
	LinkedList<String> result_list = XMLHelper.uc_unserialize(result);
	if(result_list.size()>0){
		int $auth_uid = Integer.parseInt(result_list.get(0));
		String $auth_username = result_list.get(1);
		String $auth_password = result_list.get(2);
		String $auth_email = result_list.get(3);
		if($auth_uid > 0) {
			response.addHeader("P3P","CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");

			String $uc_syn_login = uc.uc_user_synlogin($auth_uid);
			out.println($uc_syn_login);
            System.out.println("登录成功" + $uc_syn_login);

			Cookie auth = new Cookie("auth", uc.uc_authcode($auth_password + "\t" + $auth_uid, "ENCODE"));
			auth.setMaxAge(31536000);
			response.addCookie(auth);

			Cookie user = new Cookie("uchome_loginuser", URLEncoder.encode($auth_username, "utf-8"));
			response.addCookie(user);
			response.setHeader("refresh","1; url=http://203.171.231.92:88/");
		} else if($auth_uid == -1) {
			System.out.println("用户不存在,或者被删除");
		} else if($auth_uid == -2) {
			System.out.println("密码错");
		} else {
			System.out.println("未定义");
		}
	}else{
		System.out.println(result);
	}
}
%>
</body>
</html>