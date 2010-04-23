#Include FilePathCopier.ahk
#Include WarWindowsLimit.ahk
#Include MyEclipse.ahk
#Include MyJava.ahk
#SingleInstance force 
#NoEnv  ; Recommended for performance and compatibility with future AutoHotkey releases.
SendMode Input  ; Recommended for new scripts due to its superior speed and reliability.
SetWorkingDir %A_ScriptDir%  ; Ensures a consistent starting directory.
#a::Run http://61.145.199.114
#b::Run http://localhost:8081/dgcredit/login.jsp
#l::Run http://localhost:8081/dgcredit/task2add/list.action
#u::Run http://localhost:8081/netmeeting/manager/preFileUpload
#f::Run D:\ProgramFiles\Net\freegate.exe
#i::Run http://localhost:8081/dgcredit/index/index.action
#j::Run https://ibsbjstar.ccb.com.cn/app/V5/CN/STY1/login.jsp
#s::Run D:\ProgramFiles\System\Close LCD_PConline.exe 
#t::Run D:\ProgramFiles\Net\Thunder Network\Thunder\Program\Thunder.exe
#n::Run http://localhost:8081/netmeeting/manager/index

!d:: Run G:\Download
!e:: Run explorer
!i:: Run E:\Document\Inner
!t:: Run E:\TestWork\Test\src
!w:: Run E:\WorkPlace
!p:: Run D:\ProgramFiles

$F1::Run %A_WinDir%\hh.exe E:\Document\JDK_API_1_6_zh_CN.CHM
$F2::Run E:\TestWork\Test\src\ahk\f2.ahk
$F11::Run E:\TestWork\Test\src\ahk\dotaahk103.ahk


#F1::test()

test() {
    Gui, +ToolWindow 
    Gui, Show, , VimNormal Mode
    return
    ;WinGet, active_id, ID, A
    ;MsgBox, The active window's ID is "%active_id%".
    ;return
}



::/gcp::
    clipboard = KM4Yg4CP7KD9
    Send ^v
    return

::/cmbcard::
    Send 6225887692161199
    return

:::w::
    runSelf()
    return

runSelf() {
    WinGetActiveTitle, title
    pos := RegExMatch( title, "ahk" )
    if( %pos% <> 0 ) {
        ;vim 写入
        send {Escape}:w`n
        sleep 100
        run E:\TestWork\Test\src\ahk\Main.ahk
    }
    else {
        send {Escape}:w`n
    }
    return   
}
