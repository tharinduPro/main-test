var exeTimeIndex = 1;
function addExeTime() {
    $("span[name=exeTimeDelSpan]").remove();
    var exeTimeTd1 = $('<td><input type="text" name="executeHour" onblur="checkHour(this)"/>时</td>');

    var exeTimeTd2 = $('<td><input type="text" name="executeMinute" onblur="checkMinute(this)"  />分</td>');

    var delElement = $( '<span class="linkHand" name="exeTimeDelSpan" ><u>删除</u></span>' );
    exeTimeTd2.append( delElement );

    var exeTimeTr =$('<tr></tr>');
    exeTimeTr.append( exeTimeTd1 );
    exeTimeTr.append( exeTimeTd2 );

    var exeTimeTable = $( "#exeTimeTable" );
    exeTimeTable.append( exeTimeTr );
    exeTimeIndex++;

    $("span[name=exeTimeDelSpan]").click( function() {
            var thisTr = $(this).parents("tr:first");
            var prevAdvanceTd = thisTr.prev().children("td:eq(1)");
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
var xpathTrIndex = 1;
function addSubItemXpath() {

    $("span[name=delSpan]").remove();

    var xpathTd1 = $( '<td>主xpath: <input type="text" name="htmlItemXpathArray[' + xpathTrIndex + '].mainXpath"/></td>');

    var xpathTd2 = $( '<td>子xpath: <input type="text" name="htmlItemXpathArray[' + xpathTrIndex + '].subXpath" /></td>');

    var xpathTd3 = $( '<td><span class="linkHand" name="advanceSpan" ><u>高级</u><small>▼</small></span></td>');

    var delElement = $( '<span class="linkHand" name="delSpan" ><u>删除</u></span>' );

    xpathTd3.append( delElement );

    var xpathTr = $('<tr></tr>');
    xpathTr.append( xpathTd1 );
    xpathTr.append( xpathTd2 );
    xpathTr.append( xpathTd3 );

    var xpathTable = $( "#xpathTable" );
    xpathTable.append( xpathTr );
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
            if( xpathTrIndex != 2 ) {
                prevAdvanceTd.append( delElement );
            }
            thisTr.nextAll(":lt(2)").remove();
            thisTr.remove();
            xpathTrIndex--;
    });
}
