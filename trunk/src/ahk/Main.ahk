#Include FilePathCopier.ahk
#Include WarWindowsLimit.ahk
#Include MyEclipse.ahk
#Include MyJava.ahk
#SingleInstance force 
#NoEnv  ; Recommended for performance and compatibility with future AutoHotkey releases.
SendMode Input  ; Recommended for new scripts due to its superior speed and reliability.
SetWorkingDir %A_ScriptDir%  ; Ensures a consistent starting directory.
#a::Run http://localhost:8081/dgcredit/task2add/preAdd.action
#b::Run http://localhost:8081/dgcredit//creditQuery/creditQueryAction!creditQuery.action
#l::Run http://localhost:8081/dgcredit/task2add/list.action
#u::Run http://localhost:8081/dgcredit/userManager/list.action
#f::Run D:\ProgramFiles\Net\freegate.exe
#i::Run http://localhost:8081/dgcredit/index/index.action
#j::Run https://ibsbjstar.ccb.com.cn/app/V5/CN/STY1/login.jsp
#m::Run D:\ProgramFiles\Program\MyEclipse 7.0M1\eclipse\eclipse.exe
#s::Run D:\ProgramFiles\System\Close LCD_PConline.exe 
#t::Run D:\ProgramFiles\Net\Thunder Network\Thunder\Program\Thunder.exe
#v::Run G:\Games\VS\VSLoader.exe
#p::Run C:\WINDOWS\system32\WindowsPowerShell\v1.0\powershell.exe
#q::Run D:\ProgramFiles\Net\Tencent\QQ\Bin\qq.exe

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
