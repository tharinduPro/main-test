var i = 1;
function addExeTime() {
    var exeTimeTd1 = document.createElement("td");
    exeTimeTd1.innerHTML = '<input type="text" name="executeHour" onblur="checkHour(this)"/>时';

    var trId = "tr"+i;
    var trIdStr = "'" + trId + "'";
    var exeTimeTableStr= "'" + "exeTimeTable" + "'";
    var exeTimeTd2 = document.createElement("td");
    exeTimeTd2.innerHTML = '<input type="text" name="executeMinute" onblur="checkMinute(this)"  />分 <input type="button" value="删除" onclick="delTrItem(' + exeTimeTableStr + ',' +  trIdStr + ')" />';

    var exeTimeTr = document.createElement("tr");
    exeTimeTr.id = trId;
    exeTimeTr.appendChild( exeTimeTd1 );
    exeTimeTr.appendChild( exeTimeTd2 );

    var exeTimeTable = document.getElementById( "exeTimeTable" );
    exeTimeTable.appendChild( exeTimeTr );
    i++;
}

function delTrItem( tableId,trId ) {
    var Table = document.getElementById( tableId );
    var trObj = document.getElementById( trId );
    Table.removeChild( trObj );
}

function checkHour( obj ) {
    regxp = /^([0-1]?[0-9]{1}|2[0-3]{1})$/;
    if( !regxp.test( obj.value  ) ) {
        obj.value="";
    }
}

function checkMinute( obj ) {
    regxp = /^([0-5]{1}[0-9]{1})$/;
    if( !regxp.test( obj.value  ) ) {
        obj.value="";
    }
}
xpathTrIndex=1
function addSubItemXpath() {
    var xpathTd1 = document.createElement("td");
    var space = "&nbsp;&nbsp;";
    for( i = 1; i < xpathTrIndex; i++ ) {
        space = space + "&nbsp;&nbsp;";
    }
    xpathTd1.innerHTML = space + '主xpath: <input type="text" name="htmlItemXpathArray[' + xpathTrIndex + '].mainXpath""/>';

    var trId = "xpathtr"+xpathTrIndex;
    var trIdStr = "'" + trId + "'";
    var xpathTableStr= "'" + "xpathTable" + "'";
    var xpathTd2 = document.createElement("td");
    xpathTd2.innerHTML = '子xpath: <input type="text" name="htmlItemXpathArray[' + xpathTrIndex + '].subXpath" /> <input type="button" value="删除" onclick="delTrItem(' + xpathTableStr + ',' +  trIdStr + ')" />';;

    var xpathTr = document.createElement("tr");
    xpathTr.id = trId;
    xpathTr.appendChild( xpathTd1 );
    xpathTr.appendChild( xpathTd2 );

    var xpathTable = document.getElementById( "xpathTable" );
    xpathTable.appendChild( xpathTr );
    xpathTrIndex++;
}

function advanceOption( obj, trId ) {
    var trObj = document.getElementById( trId );
    alert( trObj.lastChild.firstChild.innerHTML );
}
