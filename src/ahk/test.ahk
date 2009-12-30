#Include FilePathCopier.ahk
#Include WarWindowsLimit.ahk
#Include MyVim.ahk
#SingleInstance force 
#NoEnv  ; Recommended for performance and compatibility with future AutoHotkey releases.
SendMode Input  ; Recommended for new scripts due to its superior speed and reliability.
SetWorkingDir %A_ScriptDir%  ; Ensures a consistent starting directory.
#a::Run http://localhost:8081/dgcredit/type2add/preAdd.action
#b::Run http://localhost:8081/dgcredit//creditQuery/creditQueryAction!creditQuery.action
#l::Run http://localhost:8081/dgcredit/task2add/list.action
#u::Run http://localhost:8081/dgcredit/userManager/list.action
#f::Run D:\ProgramFiles\Net\fg690p.exe
#i::Run http://localhost:8081/dgcredit/index/index.action
#j::Run https://ibsbjstar.ccb.com.cn/app/V5/CN/STY1/login.jsp
#m::Run D:\ProgramFiles\Program\MyEclipse 7.0M1\eclipse\eclipse.exe
#s::Run D:\ProgramFiles\System\Close LCD_PConline.exe 
#t::Run D:\ProgramFiles\Net\Thunder Network\Thunder\Program\Thunder.exe
#v::Run G:\Games\VS\VSLoader.exe
#p::Run C:\WINDOWS\system32\WindowsPowerShell\v1.0\powershell.exe
#q::Run D:\ProgramFiles\Net\Tencent\QQ\Bin\qq.exe
#w::limitWarWindow()
` & #e::run D:\ProgramFiles\Net\Mozilla Firefox\firefox.exe

`::
    WinMaximize A   ; Maximize the active/foreground window.
    Send `` ; Make the release of Numpad0 produce a Numpad0 keystroke. See comment below.
    return

#1::startTomcat()

startTomcat() {
    ;Run D:\ProgramFiles\Program\Tomcat6\bin\startup.bat
    ;run D:\ProgramFiles\Program\Groovy-1.6.5\bin\startGroovy.bat
}

!d:: Run G:\Download
!e:: Run explorer
!i:: Run E:\Document\Inner
!t:: Run E:\TestWork\Test\src
!w:: Run E:\WorkPlace
!p:: Run D:\ProgramFiles

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

    ;WinExist("A") is a fast way to get the ID of the active window
	WinActive("A")

    ;Retrieves the specified window's unique ID, process ID, process name
    ;WinGet, OutputVar [, Cmd, WinTitle, WinText, ExcludeTitle, ExcludeText]
    ;OutputVar:The name of the variable in which to store the result of Cmd.
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
        ;Retrieves the minimized/maximized state for a window.
            ;-1: The window is minimized (WinRestore can unminimize it).
            ;1: The window is maximized (WinRestore can unmaximize it).
            ;0: The window is neither minimized nor maximized.
		WinGet, orig_%winId%_wasMaximized, MinMax
        ;如果是最大化则恢复
		if (orig_%winId%_wasMaximized = 1) {
			WinRestore
		}
        ;记录x,y和长宽
		WinGetPos, orig_%winId%_x, orig_%winId%_y, orig_%winId%_width, orig_%winId%_height ; store the old bounds

        ;Remove window that has a thin-line border.
		WinSet, Style, -0x800000

		WinMove, , , 0, -4, A_ScreenWidth, A_ScreenHeight + 4 - 1 ; 1 pixel less, to be able to use the auto-hide taskbar
		isSuperMaximized_%winId% = 1
	}
}


#F1::test()

test() {
    Gui, +ToolWindow 
    Gui, Show, , VimNormal Mode
    return
    ;WinGet, active_id, ID, A
    ;MsgBox, The active window's ID is "%active_id%".
    ;return
}


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
        run E:\TestWork\Test\src\ahk\test.ahk
    }
    else {
        send {Escape}:w`n
    }
    return   
}

::/sop::
    sop = System.out.println( " );
    clipboard = %sop%
    send ^v
    loop 3 {
        send {Left down}
    }
    return

::/gcp::
    clipboard = KM4Yg4CP7KD9
    Send ^v
    return



