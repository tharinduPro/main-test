/*天堂之门 menk33@163.com 2008年11月5日
用法：自定义热键来调用此脚本。如果当前窗口是帮助，则在隐藏和显示侧边栏间切换；如果存在未激活，则激活；如果未启动，则启动后使其侧边栏显示。
说明：模拟鼠标点击隐藏和显示按钮然后向下还原再最大化窗口。默认33秒检查窗口是否存在，否就退出脚本。
*/
SendMode Input
#SingleInstance,Force ;Force 让脚本能重载，下面的切换显示和隐藏侧边栏才能生效。
Menu,Tray,Icon,Shell32.dll,23
#Persistent ;使此脚本持续运行直到ExitApp

IfWinActive,ahk_class HH Parent
  Gosub 显隐侧边栏 ;实现切换
Else
{
  IfWinExist,ahk_class HH Parent
    WinActivate
  Else
    {
     ;Run,%A_WinDir%\hh.exe %A_Desktop%\AutoHotkey.chm ;自定义帮助的路径
     WinWait,ahk_class HH Parent,,3 ;确保下面能顺利操作
     WinActivate ;激活确保下面点击成功
     WinGet,控件列表,ControlList
     IfNotInString,控件列表,HH SizeBar1 ;判断侧边栏是否在控件列表中
       Gosub 显隐侧边栏
    }
}
SetTimer,检查,33000
检查:
IfWinExist,ahk_class HH Parent
  Return
Else ExitApp

显隐侧边栏:
CoordMode,mouse,relative
Click,18,47 ;点显示/隐藏侧边栏的按钮
WinRestore
Winmaximize
Return

