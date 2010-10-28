#Include FilePathCopier.ahk
#Include MyEclipse.ahk
#Include MyJava.ahk
#SingleInstance force 
#NoEnv  ; Recommended for performance and compatibility with future AutoHotkey releases.
SendMode Input  ; Recommended for new scripts due to its superior speed and reliability.
SetWorkingDir %A_ScriptDir%  ; Ensures a consistent starting directory.
#a::Run http://localhost:8081/cs
#l::Run http://localhost:8081/cs/company/index.action?company.comUserId=6
#u::Run http://localhost:8081/netmeeting/manager/preFileUpload
#i::Run http://localhost:8081/dgcredit/index/index.action
#j::Run https://ibsbjstar.ccb.com.cn/app/V5/CN/STY1/login.jsp
#s::Run D:\ProgramFiles\System\Close LCD_PConline.exe 
#t::Run D:\ProgramFiles\Net\Thunder Network\Thunder\Program\Thunder.exe
#n::Run http://localhost:8081/netmeeting/manager/index
#w::
    Run D:\ProgramFiles\Net\Mozilla Firefox Test\firefox.exe
    Run D:\ProgramFiles\Net\Tencent\QQ\Bin\qq.exe
    EnvGet, JAVA_HOME,JAVA_HOME 
    Run %comspec% /c D:\ProgramFiles\Program\EclipseJ2EE\eclipse\eclipse.exe -vm "%JAVA_HOME%/jre/bin/javaw.exe"
    return

!d:: Run G:\Download
!i:: Run E:\Document\Inner
!t:: Run E:\TestWork\Test\src
!w:: Run E:\WorkPlace
!p:: Run D:\ProgramFiles

$F1::Run %comspec% /c ant -f E:\Workplace\cs\build.xml
#F1::Run %comspec% /k ant -f E:\Workplace\cs\build.xml tomcat-start
$F2::Run E:\TestWork\Test\src\ahk\f2.ahk
$F3::Run %comspec% /c gVim E:\TestWork\Test\src\ahk\Main.ahk


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
