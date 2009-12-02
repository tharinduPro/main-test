;¿¨¶û×¨ÓÃ
K3 = eee
#Persistent
#SingleInstance, Force
DetectHiddenWindows, On
SetDefaultMouseSpeed, 0
WinGet,HWND,ID,Warcraft III ahk_class Warcraft III
SetTimer, Timer_¼ì²â, 1000

IME_CHECK(HWND)
{
AAA := DllCall("imm32\ImmGetDefaultIMEWnd", Uint, HWND, Uint)
SendMessage, 0x283, 0x005,,,ahk_id %AAA%
Return ErrorLevel
}

~*Enter::
Sleep, 30
Timer_¼ì²â:
If (WinActive("ahk_id "HWND) && !IME_CHECK(HWND))
        Suspend, Off
Else
        Suspend, On
Return

#F10::
Suspend, Permit
ExitApp






$q::
K3 = qqq
Send, %K3%
return

$w::
K3 = www
Send, %K3%
Return

$e::
K3 = eee
Send, %K3%
Return

$r::
Clipboard=-invokelist
Send,{Enter}^v{Enter}
Return

$y::        KAEL(0x383503,0x393504,"y","qqq")
$v::        KAEL(0x8AA780,0x88A779,"v","qqw")
$x::        KAEL(0x59534E,0x59524E,"x","qww")
$c::        KAEL(0x3F342E,0x3D332E,"c","www")
$d::        KAEL(0x3F5996,0x4A6698,"d","wee")
;$t::       KAEL(0x162779,0x152778,"t","eee")        6.60µÄÌì»ð
$t::        KAEL(0x0D89B9,0x0D44A9,"t","eee")
$f::        KAEL(0x010379,0x020479,"f","qee")
$g::        KAEL(0x700519,0x6F041A,"g","qqe")
$b::        KAEL(0x030D46,0x030D44,"b","qwe")


$z::
If (A_PriorHotkey = A_ThisHotkey && A_TimeSincePriorHotkey < 400)
{
        MouseGetPos, X, Y
        Send, {Click 35,70}
        MouseMove, X, Y
}
Else
        KAEL(0x160607,0x160707,"z","wwe")
Return
KAEL(S1,S2,K1,K2)
{
global K3
C1 := GET_COLOR(1089,824)
C2 := GET_COLOR(1159,824)
C3 := GET_COLOR(1228,893)
If (C1=S1 || C2=S2)
        Send, %K1%
Else If C3=0x24323E
        Send, %K2%r%K3%
Return
}
GET_COLOR(X,Y,Time=5)
{
        Loop, %Time%
        {
        PixelGetColor, C, %X%, %Y%
        If C<>0x000000
                Return C
        }
}