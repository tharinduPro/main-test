function getmohustr(str)
{
	var oldstr=str;
	var newstr="";
	for(var i=0;i<=oldstr.length;i++){
		if(oldstr.substr(i,1)=="+"||oldstr.substr(i,1)=="-"||oldstr.substr(i,1)=="*"||oldstr.substr(i,1)=="("||oldstr.substr(i,1)==")"){
			if(i>0){
				newstr+="%"
				newstr+=oldstr.substr(0,i);
				newstr+="%";
			}
			newstr+=oldstr.substr(i,1);
			newstr+=getmohustr(oldstr.substr(i+1));
			oldstr="";	
			break;
		}
	}

	if(oldstr!=""){
		newstr+="%"
		newstr+=oldstr;
		newstr+="%";
	}
	return newstr;
}


//-------------------------------------------------zh-------------
function GetCookie (name){
    var CookieFound = false;
    var start = 0, end = 0, i = 0;

    //Found name in CookieSting
    while( i <= document.cookie.length){
	start = i;
        end = start + name.length;
        if (document.cookie.substring(start,end) == name){
	    CookieFound = true;
	    break;
   	}
	i++;
    }

    //Check when name is found
    if (CookieFound){
	start = end + 1;
	end = document.cookie.indexOf(";",start);
	if (end < start)
	    end = document.cookie.length;
	return unescape(document.cookie.substring(start, end));
    }
    return "";
}

function SetCookie (name, value) {
    document.cookie = name + "=" + escape (value) + ";path=/;";
}

function DeleteCookie (name) {
    var exp = new Date();
    exp.setTime (exp.getTime() - 1);
    document.cookie = name + "=; path=/; expires=" + exp.toGMTString();
}
//-----------------------------------------------------
function TRSSetCookie2(name,value)
{
    var exp = new Date();
    exp.setYear (2050);

if(value==""||value==null){
	TRSDeleteCookie(name);
	return;
}
var argv=TRSSetCookie2.arguments;
var argc=TRSSetCookie2.arguments.length;
var expires=(argv>2)?argv[2]:null;
var path=(argv>3)?argv[3]:"/";
var domain=(argv>4)?argv[4]:null;
var secure=(argv>5)?argv[5]:false;
document.cookie = name + "=" + escape(value)+
((";expires=" +exp.toGMTString()))+
((path == null) ? "":(";path="+path))+
((domain == null)?"":(";domain="+domain))+
((secure == true)?";secure":"");

}

function TRSSetCookie(name,value)
{
if(value==""||value==null){
	TRSDeleteCookie(name);
	return;
}
var argv=TRSSetCookie.arguments;
var argc=TRSSetCookie.arguments.length;
var expires=(argv>2)?argv[2]:null;
var path=(argv>3)?argv[3]:"/";
var domain=(argv>4)?argv[4]:null;
var secure=(argv>5)?argv[5]:false;
document.cookie = name + "=" + escape(value)+
((expires == null) ? "" : (";expires=" +expires.toGMTString()))+
((path == null) ? "":(";path="+path))+
((domain == null)?"":(";domain="+domain))+
((secure == true)?";secure":"");
}

function getCookieVal(offset)
{
var endstr = document.cookie.indexOf(";",offset);
if(endstr == -1){
	endstr = document.cookie.length;
}
return unescape(document.cookie.substring(offset,endstr));
}

function TRSGetCookie(name)
{
var arg=name+"=";
var alen = arg.length;
var clen = document.cookie.length;
var i = 0;
while(i<clen)
{
 var j = i+alen;
 if(document.cookie.substring(i,j) == arg){
 	return getCookieVal (j);
 }
 i = document.cookie.indexOf(" ",i)+1;
 if(i == 0) break;
 } return "";
}

function TRSDeleteCookie(name)
{DeleteCookie (name);return;
var exp = new Date();
exp.setTime(exp.getTime() - 1);
var cval = TRSGetCookie(name);
if(cval != null&&cval!="")
document.cookie = name+"=" + cval +";expires="+exp.toGMTString();
}

//---------------------------------------------------------
function GetDateStr(Word) 
{
	var SQL=Word;
	var FindPression0= new RegExp("[\\\\]","g");
	var FindPression1= new RegExp("[/]","g");

	SQL=SQL.replace(FindPression0,".");
	SQL=SQL.replace(FindPression1,".");
	
	return SQL;
}

//-------------------for netscape-----------------------
function getElmById(doc,aID)
{ 
 	var element = null; 
	
	if(navigator.appName.indexOf("Microsoft")>=0){
		//element = doc.all[aID];
		element = doc.all.item(aID);
	}
	else{
		element = doc.getElementById(aID);
	}
	//Netscape4:	element = doc.layers[aID];

	return element;
} 
function getElmsByTag(doc,aID)
{ 
 	var elements = null; 
	
	if(navigator.appName.indexOf("Microsoft")>=0){

		elements = doc.all.tags(aID);
	}
	else{

		elements = doc.getElementsByTagName(aID);
	}

	return elements;
} 

function getFrameDocument(doc,aID) 
{
	var rv = null; 

	if(navigator.appName.indexOf("Microsoft")>=0){
    		rv = doc.frames[aID].document;
  	} else {
    		rv = doc.getElementById(aID).contentDocument;
  	}
  	return rv;
}
