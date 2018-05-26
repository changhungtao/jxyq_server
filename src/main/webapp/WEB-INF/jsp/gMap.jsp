<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%String s=(String)session.getAttribute("latlng");
if (s == null) response.sendRedirect("b.jsp");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">  
<head>     
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />     
<style type="text/css">       
html { height: 100% }      
body { height: 100%; margin: 0; padding: 0 }
#map_canvas {width: 100%; height: 100% }
a:link{text-decoration:none ;color:blue ;}
a:visited {text-decoration:none ; color:blue;}
a:hover {text-decoration:none ; color:red;}
a:active {text-decoration:none ; color:black;}
</style>
    
 <script type="text/javascript" src="http://ditu.google.cn/maps/api/js?key=AIzaSyCDf1paOmvAsETs28due5kszrzCgCGsXgs&sensor=false"></script>
   
 <script type="text/javascript">
 var i=14;
 var map; 
 var mess="<%=s%>";
 //var mess="34.26667, 108.95000";
 var b=mess.split(","); 
 //alert(b[0]);
 var mylatlng= new google.maps.LatLng(b[0], b[1]);   
 function initialize() {   
       var mapOptions = {           
       center: mylatlng, 
       overviewMapControl:false,
       mapTypeId: google.maps.MapTypeId.ROADMAP,
       disableDefaultUI:true,
       draggable:false,
       panControl:false,
       scrollwheel:false,          
       zoom: 14         };         
      map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
      var marker = new google.maps.Marker({
          map: map,
          position: mylatlng

    });
       codeLatLng(mylatlng);
              } 
        function codeLatLng(latlng) 
        { 
        var geocoder = new google.maps.Geocoder(); 
        geocoder.geocode({'latLng': latlng}, function(results, status) { 
              if (status == google.maps.GeocoderStatus.OK) {  
                     if (results[0]) {  
             
             document.getElementById("location").innerText=results[0].formatted_address; 
           
             map.setZoom(14);
           // infowindow.open(map, marker);    
                 }     
                   } 
                   else {        
                       document.getElementById("location").innerText="不能解析的地址 ";       }  
                             }); 
                             }
      function fd()
      {
      i++;
      if (i>18) i=18;
      map.setZoom(i);
      } 
          function sx()
      {
      i--;
      if (i<6) i=6;
      map.setZoom(i);
      }

   function changemap(s){
   	window.location.href=s;
   }

   </script>   
   </head>   
   <body onload="initialize()">
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
