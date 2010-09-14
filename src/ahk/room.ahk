#SingleInstance force 
#NoEnv  ; Recommended for performance and compatibility with future AutoHotkey releases.
SendMode Input  ; Recommended for new scripts due to its superior speed and reliability.

SetTitleMatchMode, 3
runIntoRoom := true 
goIntoRoom()
{
    MouseGetPos, xpos, ypos,,vsHWND,2 
    ControlGetPos,xctr,yctr,,,List1
    loop
    {
        sleep 1000
        IfWinExist, VS竞技游戏平台
        {
            WinClose
        }
        IfWinExist, VSClient
        {
            WinClose
        }
        dbClickRoom(xpos,ypos,xctr,yctr,vsHWND)
    }
}

dbClickRoom(xp,yp,xc,yc,hwnd)
{
        IfWinExist,VS竞技游戏平台 -- 正式版-3.0.16
        {
            xmsg := xp - xc
            ymsg := yp - yc
            position:=makeLparam(xp,yp)
            PostMessage, 0x200,0x1,%position%,List1 
            PostMessage, 0x203,0x1,%position%,List1 
        }
}


makeLparam(low,high)
{
	result := (((high+1)*0xFFFF) & 0xFFFF0000) + low
	return result
}

$F8::Run E:\TestWork\Test\src\ahk\room.ahk
$F10::goIntoRoom()
