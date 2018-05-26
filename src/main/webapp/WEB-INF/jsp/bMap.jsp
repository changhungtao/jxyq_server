<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%String s=(String)session.getAttribute("latlng");
if (s == null) response.sendRedirect("b.jsp");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">  
<head>     
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />     
<style type="text/css">       
        html { height: 100%; }
        body { height: 100%; margin: 0px; padding: 0px;background-color: #d4e4f6;  }
        #map_canvas { width: 100%; height: 800px; margin: 0px auto; }
</style>
    
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=D5d3bd3df8e28e478bc0e4c1821de909"></script>
<script type="text/javascript">
var map;
var marker;
var i=14;
var mess="<%=s%>";
var b=mess.split(",");

function onload(){
    var lHeight = document.body.clientHeight;
    document.getElementById("map_canvas").style.height=lHeight;
    map = new BMap.Map("map_canvas");
    var point=new BMap.Point(b[1], b[0]);
    map.centerAndZoom(point,20);
    map.addControl(new BMap.MapTypeControl());
    marker=new BMap.Marker(point);
    map.addOverlay(marker);
    marker.setAnimation(BMAP_ANIMATION_BOUNCE)
    var geocoder= new BMap.Geocoder();
    geocoder.getLocation(point, function(rs) {
        if (rs) {
            document.getElementById("location").innerText=rs.address;
		} else {
			alert("不能解析的地址 ");
		}
	});
}

function fd() {
	i++;
	if (i>18) i=18;
	map.setZoom(i);
}

function sx(){
	i--;
	if (i<6) i=6;
	map.setZoom(i);
}

function changemap(s){
	window.location.href=s;
}
</script>

</head>   
<body onload="onload()"> 
<div style="float:right;">
<input type="button" onclick="fd()" value="放大"/>
<input type="button" onclick="sx()" value="缩小" />
<input type="button" onclick="changemap('bMap')" value="百度地图"/>
<input type="button" onclick="changemap('gMap')" value="谷歌地图" />
</div>
<div align="center" id="location"></div>
<div id="map_canvas"></div>
</body> 
</html>
