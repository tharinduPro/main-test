var exeTimeIndex = 1;
function addExeTime() {
    $("span[name=exeTimeDelSpan]").remove();
    var exeTimeTd1 = $('<td><input type="text" name="executeTime" /></td>');

    var delElement = $( '<span class="linkHand" name="exeTimeDelSpan" ><u>删除</u></span>' );
    exeTimeTd1.append( delElement );

    var exeTimeTr =$('<tr></tr>');
    exeTimeTr.append( exeTimeTd1 );

    var exeTimeTable = $( "#exeTimeTable" );
    exeTimeTable.append( exeTimeTr );
    $('#exeTimeTable input').ptTimeSelect({setButtonLabel: "设置", minutesLabel: "分钟", hoursLabel: "小时" });
    exeTimeIndex++;

    $("span[name=exeTimeDelSpan]").click( function() {
            var thisTr = $(this).parents("tr:first");
            var prevAdvanceTd = thisTr.prev().children("td:eq(0)");
            if( exeTimeIndex!= 2 ) {
                prevAdvanceTd.append( delElement );
            }
            thisTr.remove();
            exeTimeIndex--;
    });
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
var xpathTrIndex = 0;
function addSubItemXpath() {

    $("span[name=delSpan]").remove();

    
    var xpathTable = $( "#xpathTable" );
    var advanceElement = $( '<span class="linkHand" name="advanceSpan" ><u>高级</u><small>▼</small></span>' );
    xpathTable.find("tr:last > td:last").append( advanceElement );
    var xpathTrAdvance = $( 
        '<tr class="advanceTr">' +
            '<td colspan="3">' +
                '分页参数:<input type="text" name="htmlItemXpathArray[' + xpathTrIndex + '].pageParamName" />' +
                '截取条目关键字:<input type="text" name="htmlItemXpathArray[' + xpathTrIndex + '].keyword" />' +
            '</td>' +
        '</tr>' +
        '<tr class="advanceTr">' +
            '<td colspan="3">' +
                '起始页码:<input type="text" name="htmlItemXpathArray[' + xpathTrIndex + '].pageParamStart" />' +
                '结束页码:<input type="text" name="htmlItemXpathArray[' + xpathTrIndex + '].pageParamEnd" />' +
            '</td>' +
        '</tr>' );

    xpathTable.append( xpathTrAdvance );
    xpathTrAdvance.hide();

    var xpathTd1 = $( '<td>主xpath: <input type="text" name="htmlItemXpathArray[' + xpathTrIndex + '].mainXpath"/></td>');

    var xpathTd2 = $( '<td>子xpath: <input type="text" name="htmlItemXpathArray[' + xpathTrIndex + '].subXpath" /></td>');

    var xpathTd3 = $( '<td></td>');

    var delElement = $( '<span class="linkHand" name="delSpan" ><u>删除</u></span>' );

    xpathTd3.append( delElement );

    var xpathTr = $('<tr></tr>');
    xpathTr.append( xpathTd1 );
    xpathTr.append( xpathTd2 );
    xpathTr.append( xpathTd3 );
    xpathTable.append( xpathTr );
    
    xpathTrIndex++;

    $("span[name=advanceSpan]").toggle(
        function(){
            $(this).parents("tr").nextAll(":lt(2)").show();
            $(this).children("small").text("▲");
        },
        function(){
            $(this).parents("tr").nextAll(":lt(2)").hide();
            $(this).children("small").text("▼");
        }
    );

    $("span[name=delSpan]").click( function() {
            var thisTr = $(this).parents("tr:first");
            var prevAdvanceTd = thisTr.prevAll(":eq(2)").children("td:eq(2)");
        	prevAdvanceTd.find("span").remove();
            if( xpathTrIndex != 1 ) {
                prevAdvanceTd.append( delElement );
            }
            thisTr.prevAll(":lt(2)").remove();
            thisTr.remove();
            xpathTrIndex--;
    });
}
