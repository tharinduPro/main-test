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
#a::Run http://localhost:8081/dgcredit/type2add/preAdd.action
#b::Run http://localhost:8081/dgcredit/task2add/preAdd.action
#l::Run http://localhost:8081/dgcredit/task2add/list.action
#i::Run http://localhost:8081/dgcredit/index/index.action
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

$F1::Run %A_WinDir%\hh.exe E:\Document\JDK_API_1_6_zh_CN.CHM
$F2::Run E:\TestWork\Test\src\ahk\f2.ahk
$F11::Run E:\TestWork\Test\src\ahk\dotaahk103.ahk

/*
 * BoD winsupermaximize v1.01.
 *
 * This program and its source are in the public domain.
 * Contact BoD@JRAF.org for more information.
 *
 * Version history:
 * 2008-05-12: v1.01
 * 2008-05-10: v1.00
 */

#SingleInstance ignore

/*
 * Tray menu.
 */
Menu, tray, NoStandard
Menu, tray, Add, Super maximize window, menuSuperMaximize
Menu, tray, Add, About..., menuAbout
Menu, tray, Add, Exit, menuExit
Menu, tray, Default, Super maximize window

/*
 * Bind to Win-F11.
 */
#F11::superMaximize()


menuAbout:
	MsgBox, 8256, About, BoD winsupermaximize v1.01.`n`nThis program and its source are in the public domain.`nContact BoD@JRAF.org for more information.
return

menuExit:
	ExitApp
return

menuSuperMaximize:
	Send !{Tab} ; go to previously active window (the currently active window is the taskbar !)
	Sleep, 200
	superMaximize()
return


/*
 * Super Maximizes the currently active window.
 */
superMaximize() {
	global

	WinActive("A")
	WinGet, winId, ID

	if (isSuperMaximized_%winId% = 1) {
		; already supermaximized: we restore the window
		WinSet, Style, +0x800000
		WinMove, , , orig_%winId%_x, orig_%winId%_y, orig_%winId%_width, orig_%winId%_height
		if (orig_%winId%_wasMaximized) {
			WinMaximize
		}
		isSuperMaximized_%winId% = 0
	} else {
		; not supermaximized: we supermaximize it
		WinGet, orig_%winId%_wasMaximized, MinMax
		if (orig_%winId%_wasMaximized = 1) {
			WinRestore
		}
		WinGetPos, orig_%winId%_x, orig_%winId%_y, orig_%winId%_width, orig_%winId%_height ; store the old bounds
		WinSet, Style, -0x800000
		WinMove, , , 0, -4, A_ScreenWidth, A_ScreenHeight + 4 - 1 ; 1 pixel less, to be able to use the auto-hide taskbar
		isSuperMaximized_%winId% = 1
	}
}
