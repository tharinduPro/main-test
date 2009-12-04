; dotaahk_v103
;
; AutoHotkey Version: 1.x
; Language:       English
; Platform:       WinNT
; Author:         autohotkey@rn <zhang00000@gmail.com>
;
; Script Function:
; An AutoHotkey Script for dota!
; gong cheng cai liao ge zhong bei ju

;不使用操作系统的环境变量
;recommended for all new scripts
#NoEnv

#SingleInstance force

;Switches to the SendInput method 
SendMode Input

;设置脚本的所在目录为工作目录
SetWorkingDir %A_ScriptDir%

;Sets coordinate mode for various commands to be relative to either the active window or the screen.
CoordMode, Pixel

;Jumps to the specified label and continues execution until Return is encountered.
Gosub,f_IniRead

Gosub,f_SetHotkey0
If sg1
	Gosub,f_SetHotkey1
If sg2
	Gosub,f_SetHotkey2
If sg4
	Gosub,f_SetHotkey4
If sg5
	Gosub,f_Sethotkey5
If sg6
	Hotkey,Lwin,SC1004
Haystack = abcdefghijklmnopqrstuvwxyz1234567890`-=[]\;',./
;DllCall("ClipCursor", "UInt", 0)
Suspend on
Send, {[ Up}
Send, {] Up}
loop,
{
	winwaitactive,A
    WinGet,hWnd,ID,A
	loop,
    {   
        WinGetPos,,,Width,Height,A
        if(Width>400 and Height>300)
            break
        sleep 1000
    }
    suspend off
    If sg3
		gosub,f_hbon
	loop,
	{
		ifwinactive,A
			sleep 1000
		else
			break
	}
	DllCall("ClipCursor", "UInt", 0)
    suspend on
}


f_SetHotkey0:
{
	If rhk
		HotKey, %Rhk%, SC1000
    If Shk
		HotKey, %Shk%, f_Suspend
	return
}
f_SetHotkey1:
{
	If Item7
    {
        HotKey, $%Item7%, f_Item7
        HotKey, $+%Item7%, s_Item7
		Hotkey, $^%Item7%, f_null
    }
    If Item8
    {
        HotKey, $%Item8%, f_Item8
        HotKey, $+%Item8%, s_Item8
		Hotkey, $^%Item8%, f_null
    }
    If Item4
    {
        HotKey, $%Item4%, f_Item4
        HotKey, $+%Item4%, s_Item4
		Hotkey, $^%Item4%, f_null
    }
    If Item5
    {
        HotKey, $%Item5%, f_Item5
        HotKey, $+%Item5%, s_Item5
		Hotkey, $^%Item5%, f_null
    }
    If Item1
    {
       	HotKey, $%Item1%, f_Item1
      	HotKey, $+%Item1%, s_Item1
		Hotkey, $^%Item1%, f_null
    }
    If Item2
    {
        HotKey, $%Item2%, f_Item2
        HotKey, $+%Item2%, s_Item2
		Hotkey, $^%Item2%, f_null
    }
    If Mhk
    {
        HotKey, $%Mhk%, f_Move
        HotKey, $+%Mhk%, s_Move
		Hotkey, $^%Mhk%, f_null
    }
    Return
}
f_SetHotkey2:
{
	If Thk1
        HotKey, $%Thk1%, f_Text1
	If Thk2
        HotKey, $%Thk2%, f_Text2
	If Thk3
        HotKey, $%Thk3%, f_Text3
	If Thk4
        HotKey, $%Thk4%, f_Text4		
	If Thk5
        HotKey, $%Thk5%, f_Text5
	If Thk6
        HotKey, $%Thk6%, f_Text6
	If Thk7
        HotKey, $%Thk7%, f_Text7
	If Thk8
        HotKey, $%Thk8%, f_Text8
	If Thk9
        HotKey, $%Thk9%, f_Text9
	return	
}
f_SetHotkey4:
{
	If mlon
        HotKey, $%mlon%, SC1001
	If mlof
        HotKey, $%mlof%, SC1002
	return	
}
f_SetHotkey5:
{
	HotKey, WheelUp, f_wlup ,off
	HotKey, WheelDown, f_wldn ,off
	If mshk
		HotKey, $%mshk%, SC1003
	return	
}


f_Item7:
{
    IfInString, Haystack, %Item7%
    {
        if IME_CHECK()
            send,%Item7%
        else
            send,{Numpad7}{bs}{%Item7%}
        return
    }
    else
        send,{Numpad7}
    return
}
s_Item7:
{
    IfInString, Haystack, %Item7%
    {
        if IME_CHECK()
            send,+%Item7%
        else
            send,+{Numpad7}+{%Item7%}
        return
    }
    else
        send,+{Numpad7}
    return
}




f_Item8:
{
    IfInString, Haystack, %Item8%
    {
        if IME_CHECK()
            send,%Item8%
        else
            send,{Numpad8}{bs}{%Item8%}
        return
    }
    else
        send,{Numpad8}
    return
}
s_Item8:
{
    IfInString, Haystack, %Item8%
    {
        if IME_CHECK()
            send,+%Item8%
        else
            send,+{Numpad8}+{%Item8%}
        return
    }
    else
        send,+{Numpad8}
    return
}



f_Item4:
{
    IfInString, Haystack, %Item4%
    {
        if IME_CHECK()
            send,%Item4%
        else
            send,{Numpad4}{bs}{%Item4%}
        return
    }
    else
        send,{Numpad4}
    return
}
s_Item4:
{
    IfInString, Haystack, %Item4%
    {
        if IME_CHECK()
            send,+%Item4%
        else
            send,+{Numpad4}+{%Item4%}
        return
    }
    else
        send,+{Numpad4}
    return
}



f_Item5:
{
    IfInString, Haystack, %Item5%
    {
        if IME_CHECK()
            send,%Item5%
        else
            send,{Numpad5}{bs}{%Item5%}
        return
    }
    else
        send,{Numpad5}
    return
}
s_Item5:
{
    IfInString, Haystack, %Item5%
    {
        if IME_CHECK()
            send,+%Item5%
        else
            send,+{Numpad5}+{%Item5%}
        return
    }
    else
        send,+{Numpad5}
    return
}



f_Item1:
{
    IfInString, Haystack, %Item1%
    {
        if IME_CHECK()
            send,%Item1%
        else
            send,{Numpad1}{bs}{%Item1%}
        return
    }
    else
        send,{Numpad1}
    return
}
s_Item1:
{
    IfInString, Haystack, %Item1%
    {
        if IME_CHECK()
            send,+%Item1%
        else
            send,+{Numpad1}+{%Item1%}
        return
    }
    else
        send,+{Numpad1}
    return
}



f_Item2:
{
    IfInString, Haystack, %Item2%
    {
        if IME_CHECK()
            send,%Item2%
        else
            send,{Numpad2}{bs}{%Item2%}
        return
    }
    else
        send,{Numpad2}
    return
}
s_Item2:
{
    IfInString, Haystack, %Item2%
    {
        if IME_CHECK()
            send,+%Item2%
        else
            send,+{Numpad2}+{%Item2%}
        return
    }
    else
        send,+{Numpad2}
    return
}

f_Move:
{
    IfInString, Haystack, %mhk%
    {
        if IME_CHECK()
            send,{%mhk%}
        else
            send,{m}{bs}{%mhk%}
        return
    }
    else
        send,{m}
    return
}
s_Move:
{
    IfInString, Haystack, %mhk%
    {
        if IME_CHECK()
            send,{+%mhk%}
        else
            send,+{m}+{%mhk%}
        return
    }
    else
        send,+{m}
    return
}


f_text1:
{
temp=%clipboard%
clipboard=%Text1%
Send,{Enter}
sleep 50
Send ^v
sleep 50
Send,{Enter}
clipboard=%temp%
return
}
f_text2:
{
temp=%clipboard%
clipboard=%Text2%
Send,{Enter}
sleep 50
Send ^v
sleep 50
Send,{Enter}
clipboard=%temp%
return
}
f_text3:
{
temp=%clipboard%
clipboard=%Text3%
Send,{Enter}
sleep 50
Send ^v
sleep 50
Send,{Enter}
clipboard=%temp%
return
}
f_text4:
{
temp=%clipboard%
clipboard=%Text4%
Send,{Enter}
sleep 50
Send ^v
sleep 50
Send,{Enter}
clipboard=%temp%
return
}
f_text5:
{
temp=%clipboard%
clipboard=%Text5%
Send,{Enter}
sleep 50
Send ^v
sleep 50
Send,{Enter}
clipboard=%temp%
return
}
f_text6:
{
temp=%clipboard%
clipboard=%Text6%
Send,{Enter}
sleep 50
Send ^v
sleep 50
Send,{Enter}
clipboard=%temp%
return
}
f_text7:
{
temp=%clipboard%
clipboard=%Text7%
Send,{Enter}
sleep 50
Send ^v
sleep 50
Send,{Enter}
clipboard=%temp%
return
}
f_text8:
{
temp=%clipboard%
clipboard=%Text8%
Send,{Enter}
sleep 50
Send ^v
sleep 50
Send,{Enter}
clipboard=%temp%
return
}
f_text9:
{
temp=%clipboard%
clipboard=%Text9%
Send,{Enter}
sleep 50
Send ^v
sleep 50
Send,{Enter}
clipboard=%temp%
return
}

SC1000::
    ;Does nothing except mark the current subroutine as being exempt from suspension.
	Suspend permit
	Reload
	return
SC1001::
    hwnd := WinExist( "A" )
	If hwnd
    {
        VarSetCapacity(lpRect, 16, 0)
        DllCall("GetClientRect", "UInt", hwnd, "UInt", &lpRect)
        DllCall("ClientToScreen", "Uint", hWnd, "Uint", &lpRect)
        DllCall("ClientToScreen", "Uint", hWnd, "Uint", &lpRect+8)
        DllCall("ClipCursor", "UInt", &lpRect)
    }
Return
SC1002::
    Suspend permit
    DllCall("ClipCursor", "UInt", 0)
Return
SC1003::
	Suspend permit
	Hotkey , WheelUp ,Toggle
	Hotkey , WheelDown ,Toggle
return
SC1004::
	suspend permit
return
f_wlup:
{
	send,{=}
	return
}
f_wldn:
{
	send,{-}
	return
}
f_Suspend:
{	
    ;On: Suspends(挂起) all hotkeys and hotstrings except those explained the Remarks section.
    suspend on
	return
}

f_IniRead:
{
    SettingFile =%A_ScriptDir%\dotaahk.ini
    if FileExist(SettingFile)
    {
        IniRead, Rhk, %A_ScriptDir%\dotaahk.ini, 1, Reload
        IniRead, Shk, %A_ScriptDir%\dotaahk.ini, 1, Suspend
				
		IniRead, sg1, %A_ScriptDir%\dotaahk.ini, Itemkeys, Enabled
        IniRead, Item1, %A_ScriptDir%\dotaahk.ini, Itemkeys, Item1
        IniRead, Item2, %A_ScriptDir%\dotaahk.ini, Itemkeys, Item2
        IniRead, Item4, %A_ScriptDir%\dotaahk.ini, Itemkeys, Item4
        IniRead, Item5, %A_ScriptDir%\dotaahk.ini, Itemkeys, Item5
		IniRead, Item7, %A_ScriptDir%\dotaahk.ini, Itemkeys, Item7
        IniRead, Item8, %A_ScriptDir%\dotaahk.ini, Itemkeys, Item8
        IniRead, Mhk, %A_ScriptDir%\dotaahk.ini, Itemkeys, Move
        
		IniRead, sg2, %A_ScriptDir%\dotaahk.ini,  Textsend, Enabled
		IniRead, Thk1, %A_ScriptDir%\dotaahk.ini, Textsend, Hotkey1
        IniRead, Thk2, %A_ScriptDir%\dotaahk.ini, Textsend, Hotkey2
        IniRead, Thk3, %A_ScriptDir%\dotaahk.ini, Textsend, Hotkey3
        IniRead, Thk4, %A_ScriptDir%\dotaahk.ini, Textsend, Hotkey4
        IniRead, Thk5, %A_ScriptDir%\dotaahk.ini, Textsend, Hotkey5
		IniRead, Thk6, %A_ScriptDir%\dotaahk.ini, Textsend, Hotkey6
		IniRead, Thk7, %A_ScriptDir%\dotaahk.ini, Textsend, Hotkey7
		IniRead, Thk8, %A_ScriptDir%\dotaahk.ini, Textsend, Hotkey8
		IniRead, Thk9, %A_ScriptDir%\dotaahk.ini, Textsend, Hotkey9
		IniRead, Text1, %A_ScriptDir%\dotaahk.ini, Textsend, Text1
        IniRead, Text2, %A_ScriptDir%\dotaahk.ini, Textsend, Text2
        IniRead, Text3, %A_ScriptDir%\dotaahk.ini, Textsend, Text3
        IniRead, Text4, %A_ScriptDir%\dotaahk.ini, Textsend, Text4
        IniRead, Text5, %A_ScriptDir%\dotaahk.ini, Textsend, Text5
		IniRead, Text6, %A_ScriptDir%\dotaahk.ini, Textsend, Text6
		IniRead, Text7, %A_ScriptDir%\dotaahk.ini, Textsend, Text7
		IniRead, Text8, %A_ScriptDir%\dotaahk.ini, Textsend, Text8
		IniRead, Text9, %A_ScriptDir%\dotaahk.ini, Textsend, Text9
		
		IniRead, sg3, %A_ScriptDir%\dotaahk.ini, Healthbar, Enabled
		
		IniRead, sg4, %A_ScriptDir%\dotaahk.ini, MouseLocker, Enabled
		IniRead, Mlon, %A_ScriptDir%\dotaahk.ini, Mouselocker, On
        IniRead, Mlof, %A_ScriptDir%\dotaahk.ini, Mouselocker, Off
		
		IniRead, sg5, %A_ScriptDir%\dotaahk.ini, MouseScroll, Enabled
		IniRead, Mshk, %A_ScriptDir%\dotaahk.ini, MouseScroll, Hotkey0
		
		IniRead, sg6, %A_ScriptDir%\dotaahk.ini, Winkeykiller, Enabled

    }
    else
    {
        MsgBox,Dotaahk.ini miss!
        Exitapp
    }
    Return
}

f_hbon:
{
    Send, {[ Up}
    Send, {] Up}
    Send, {[ Down}
    Send, {] Down}
    send,{bs}{bs}
    return			
}

IME_CHECK()
{
	WinGet,hWnd,ID,A
	IMEWnd:=DllCall("imm32\ImmGetDefaultIMEWnd", Uint,hWnd, Uint)
	DetectHiddenWindows , ON
	SendMessage 0x283,0x005,"",,ahk_id %IMEWnd%
	DetectHiddenWindows , OFF
	return ErrorLevel
}

f_null:
{
	return
}

