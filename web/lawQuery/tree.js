
function gotochannel(aEvent)
{
	var myEvent = aEvent ? aEvent : window.event;
	var srcElement=null;
	if(navigator.appName.indexOf("Microsoft")>=0){
		srcElement=myEvent.srcElement;
	}
	else{
		srcElement=myEvent.target;
	}

	var id="chk"+srcElement.id;

	var allCheckBox = getElmsByTag(document,"input");

	for (var i = 0 ; i < allCheckBox.length ; i ++)
	{
		if(allCheckBox[i].type!="checkbox")continue;
		if (allCheckBox[i].id.substr(0,3) != "chk")continue;

		if(allCheckBox[i].name.indexOf(allCheckBox[id].name)==0)allCheckBox[i].checked = 1;
		else allCheckBox[i].checked = 0;
	}
	allCheckBox[id].checked=1;
	
	ClearNodes();
	top_channels=AddNodeStr(srcElement.id,top_channels);
	var idstr="chk"+srcElement.id;
	top_nodenames=AddNodeStr(allCheckBox[idstr].name,top_nodenames);
	
	if(top_channels==",2,"){
		top_channels="";
	}
	
	TRSSetCookie("channels",top_channels);
	TRSSetCookie("nodenames",top_nodenames);
}


function AddNodeStr(id,s)
{
	var sss=s;
	var str=','+id+',';
	if(sss=="")sss=str;
	else if(sss.indexOf(str)<0)sss=sss+id+",";
	return sss;
}
function DelNodeStr(id,s)
{
	var sss=s;
	var str=','+id+',';
	var pos=sss.indexOf(str);
	if(pos>=0){
		var str1=sss.substr(0,pos);
		var str2=sss.substr(pos+str.length);
		
		sss=str1+","+str2;
		if(sss==",")sss="";
	}
	return sss;
}
function DelChildNames(name)
{
	var FindPression= new RegExp(",","g");
	var ss1,ss2;
	ss1 = top_nodenames.split(FindPression);
	ss2 = top_channels.split(FindPression);

	var i;
	var names="";
	var channels="";
	for(i=0;i<ss1.length;i++){
		if(ss1[i]=="")continue;
		if(ss1[i].indexOf(name)==0&&ss1[i]!=name)continue;
		if(names=="")names=",";
		names=names+ss1[i]+",";
		if(channels=="")channels=",";
		channels=channels+ss2[i]+",";
	}
	top_nodenames=names;
	top_channels=channels;
}

function ClearNodes()
{
	top_channels="";
	top_nodenames="";
}
function ShowAllNodes()
{
	var FindPression= new RegExp(",","g");
	var ss;
	ss = top_channels.split(FindPression);

	var i;
	for(i=0;i<ss.length;i++){
		if(ss[i]=="")continue;
		var id="chk"+ss[i];
		checkitem2(id,1);
	}
}
function RelplaceChannels(id)//将一个节点的channelid替换为所有子节点的channelid列表
{
	var allCheckBox = getElmsByTag(document,"input");

	for (var i = 0 ; i < allCheckBox.length ; i ++)
	{
		if(allCheckBox[i].type!="checkbox")continue;
		if (allCheckBox[i].id.substr(0,3) != "chk")continue;
		if(id==allCheckBox[i].id)continue;
		
		if(allCheckBox[i].name.indexOf(allCheckBox[id].name)==0){
			if(allCheckBox[i].name.length-allCheckBox[id].name.length==4){
				top_channels=AddNodeStr(allCheckBox[i].id.substring(3),top_channels);
				top_nodenames=AddNodeStr(allCheckBox[i].name,top_nodenames);
			}
		}
	}

}
function checkitem(id)
{
	var allCheckBox = getElmsByTag(document,"input");
	var checked = allCheckBox[id].checked;
	

	if(checked){
		top_channels=AddNodeStr(id.substring(3),top_channels);
		top_nodenames=AddNodeStr(allCheckBox[id].name,top_nodenames);
	}
	else {
		top_channels=DelNodeStr(id.substring(3),top_channels);
		top_nodenames=DelNodeStr(allCheckBox[id].name,top_nodenames);
	}
	DelChildNames(allCheckBox[id].name);//即使未显示节点也可正确处理

	for (var i = 0 ; i < allCheckBox.length ; i ++)
	{
		if(allCheckBox[i].type!="checkbox")continue;
		if (allCheckBox[i].id.substr(0,3) != "chk")continue;
		if(id==allCheckBox[i].id)continue;
		
		if(allCheckBox[id].name.length>=14&&allCheckBox[i].name.length==10){//点击小类时，1类及其子类取消
			var str=allCheckBox[id].name.substr(0,10);
			var str2=allCheckBox[i].name.substr(0,10);
			if(str!=str2){
				top_channels=DelNodeStr(allCheckBox[i].id.substring(3),top_channels);
				top_nodenames=DelNodeStr(allCheckBox[i].name,top_nodenames);
				DelChildNames(allCheckBox[i].name);
				checkitem2(allCheckBox[i].id,0);
			}
		}
		if(checked){
			if(allCheckBox[id].name.length==10&&allCheckBox[i].name.length==10&&allCheckBox[i].checked==0){//点击1类时，小类取消
				var str=allCheckBox[id].name.substr(0,10);
				var str2=allCheckBox[i].name.substr(0,10);
				if(str!=str2){
					DelChildNames(allCheckBox[i].name);
					checkitem2(allCheckBox[i].id,0);
				}
			}
		}
		if(checked==0){
		     if(allCheckBox[id].name.indexOf(allCheckBox[i].name)==0){//父节点,肯定处于显示状态
			if(allCheckBox[i].checked){
				RelplaceChannels(allCheckBox[i].id);
				top_channels=DelNodeStr(id.substring(3),top_channels);
				top_nodenames=DelNodeStr(allCheckBox[id].name,top_nodenames);
			}
				
			top_channels=DelNodeStr(allCheckBox[i].id.substring(3),top_channels);
			top_nodenames=DelNodeStr(allCheckBox[i].name,top_nodenames);
		     }
		}
	}

	checkitem2(id,checked);
	
	TRSSetCookie("channels",top_channels);
	TRSSetCookie("nodenames",top_nodenames);
	//alert(top_nodenames);
}
function checkitem2(id,checked)//仅显示
{
	var allCheckBox = getElmsByTag(document,"input");
	if(allCheckBox[id]==null)return;


	for (var i = 0 ; i < allCheckBox.length ; i ++)
	{
		if(allCheckBox[i].type!="checkbox")continue;
		if (allCheckBox[i].id.substr(0,3) != "chk")continue;
		
		if(checked){
			if(allCheckBox[i].name.indexOf(allCheckBox[id].name)==0){//本、子
				allCheckBox[i].checked = checked;
			}
		}
		else{
			if(allCheckBox[i].name.indexOf(allCheckBox[id].name)==0){//本、子
				allCheckBox[i].checked = 0;
			}
			if(allCheckBox[id].name.indexOf(allCheckBox[i].name)==0){//父
				allCheckBox[i].checked = 0;
			}
		}
	}
}

function ClickItem(srcElementid)
{
	var id="chk"+srcElementid;
	var srcElement=getElmById(document,id);

	//red
	var links=document.links;
	var link;
	if(srcElement.tagName=="a"||srcElement.tagName=="A"){
		for(var i=0;i<links.length;i++){
			link=links.item(i,0);
			link.style.color="";
			
		}
	   srcElement.style.color="red";
	   
	}

	//-------------------------------------

	var allCheckBox = getElmsByTag(document,"input");
	for (var i = 0 ; i < allCheckBox.length ; i ++)
	{
		if(allCheckBox[i].type!="checkbox")continue;
		if (allCheckBox[i].id.substr(0,3) != "chk")continue;

		if(allCheckBox[i].name.indexOf(allCheckBox[id].name)==0)allCheckBox[i].checked = 1;
		else allCheckBox[i].checked = 0;
	}
	allCheckBox[id].checked=1;
	
	ClearNodes();
	top_channels=AddNodeStr(srcElementid,top_channels);

	var idstr=srcElement.id;
	top_nodenames=AddNodeStr(allCheckBox[idstr].name,top_nodenames);
	
	if(top_channels==",2,"){
		top_channels="";
	}
	
	TRSSetCookie("channels",top_channels);
	TRSSetCookie("nodenames",top_nodenames);
}

