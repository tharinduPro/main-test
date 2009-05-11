<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <title>CAS auto login</title>
        <script type="text/javascript" src="dojo.js"></script>
        <script language="javascript">
            function loginCas(){
                var doc; 
                if (document.all){//IE
                    doc = document.frames["casFrame"].document;
                }else{//Firefox    
                    doc = document.getElementById("casFrame").contentDocument;
                }
                document.getElementById("lt").value = doc.getElementsByName("lt")[0].value;
                document.getElementsByName("submit")[0].click();
            }

            function LoadFrame(strUrl) {
            dojo.xhrGet( {
            // The following URL must match that used to test the server.
            url: strUrl, 
            handleAs: "text",
            timeout: 5000, // Time in milliseconds
            // The LOAD function will be called on a successful response.
            load: function(response, ioArgs) { //  
            var doc; 
            if (document.all){//IE
            doc = document.frames["casFrame"].document;
            }else{//Firefox    
            doc = document.getElementById("casFrame").contentDocument;
            }
            doc.open();
            doc.write(response);
            doc.close();
            return response; // 
            },
            error: function(response, ioArgs) { //
            //console.error("HTTP status code: ", ioArgs.xhr.status); //
            return response; //       
            }
            });
            }
        </script>
    </head>
    <body onload="LoadFrame('<%=request.getAttribute("casService") %>')">
    
            <form id="fm1" action="<%=request.getAttribute("casService") %>" method="post">

                <input id="username" name="username"  type="hidden" value="<%=request.getParameter("userName")%>"/>

                <input id="password" name="password" type="hidden" value="<%=request.getParameter("password")%>"/>
                
                <input type="hidden" name="lt" id="lt" value="" />

                <input type="hidden" name="_eventId" value="submit" />

                <input name="submit" tabindex="4" type="submit" />
            </form>
        正在登录.....
        <input type="button" value="click" onclick="loginCas()" />
        <span><%=request.getAttribute( "hostService" )%></span>
        <iframe name="casFrame" id="casFrame" />
        </body>
    </html>
