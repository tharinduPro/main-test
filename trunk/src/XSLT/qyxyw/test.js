function test( url , xslt ) {
    document.getElementById( "url" ).value = encodeURI(url);
    document.getElementById( "xslt" ).value = xslt;
    document.getElementById( "form1" ).action= "http://localhost/WebTest/struts2/HelloWorld.action";
    document.getElementById( "form1" ).submit();
}
