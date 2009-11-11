;
; AutoHotkey Version: 1.x
; Language:       English
; Platform:       Win9x/NT
; Author:         A.N.Other <myemail@nowhere.com>
;
; Script Function:
;	Template script (you can customize this template by editing "ShellNew\Template.ahk" in your Windows folder)
;

#NoEnv  ; Recommended for performance and compatibility with future AutoHotkey releases.
SendMode Input  ; Recommended for new scripts due to its superior speed and reliability.
SetWorkingDir %A_ScriptDir%  ; Ensures a consistent starting directory.
#a::Run http://localhost/dgcredit/type2add/preAdd.action
#b::Run http://localhost/dgcredit/task2add/preAdd.action
#i::Run http://localhost/dgcredit/index/index.action
#e::Run explorer 
#n::
    Run net start EvtEng
    Run net start RegSrvc
    Run net start WLANKEEPER
    return

#j::Run https://ibsbjstar.ccb.com.cn/app/V5/CN/STY1/login.jsp
#s::Run D:\ProgramFiles\System\Close LCD_PConline.exe 
#t::
    Run explorer E:\TestWork\Test\src 
    return

#p::Run C:\WINDOWS\system32\WindowsPowerShell\v1.0\powershell.exe 

#1::Run explorer E:\Document\Inner
