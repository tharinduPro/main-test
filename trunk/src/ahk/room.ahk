#SingleInstance force 
#NoEnv  ; Recommended for performance and compatibility with future AutoHotkey releases.
SendMode Input  ; Recommended for new scripts due to its superior speed and reliability.

SetTitleMatchMode, 3
goIntoRoom()
{
    MouseGetPos, xpos, ypos 
    ControlGetPos,xctr,yctr,,,List1
    loop
    {
        sleep 1000
        IfWinExist, VS������Ϸƽ̨
        {
            WinClose
            dbClickRoom(xpos,ypos,xctr,yctr)
        }
        IfWinExist, VSClient
        {
            WinClose
            dbClickRoom(xpos,ypos,xctr,yctr)
        }
    }
}

dbClickRoom(xp,yp,xc,yc)
{
        IfWinExist,VS������Ϸƽ̨ -- ��ʽ��-3.0.16
        {
            Xmsg := xp - xc
            Ymsg := yp - yc
            Position:=makeLparam(Xmsg,Ymsg)
            PostMessage, 0x203, 0x1,%Position%,List1 
        }
}


makeLparam(low,high)
{
	result := (((high+1)*0xFFFF) & 0xFFFF0000) + low
	return result
}

$F8::Run E:\TestWork\Test\src\ahk\room.ahk
$F10::goIntoRoom()
