/*天堂之门 menk33@163.com 2008年10月23日
功能：在下面匹配的窗口类集合激活并按F2重命名时，自动不选中扩展名。当然前提是你没勾隐藏已知文件类型的扩展名。
用法：可在开机就运行的脚本中用$F2::热键调用，还可加#IfWinNotActive判断等。
感谢 Helfee 和写了 BetterRename.ahk 的 Adam Pash 以及 Jameson。
*/
#SingleInstance,Ignore
SendMode Input
SetTitleMatchMode RegEx ;让下面的 ahk_class 能用正则表达式来判断窗口类

;下面的窗口类依次为：Win+D后的桌面、桌面、我的电脑、资源管理器、另存为等
IfWinActive,ahk_class (WorkerW|Progman|CabinetWClass|ExploreWClass|#32770)
{
备份=%ClipboardAll%
Clipboard=
Send,^c ;取得文件或文件夹的完全路径
ClipWait,3
  If ErrorLevel
    {
     MsgBox,复制3秒超时
     ExitApp
    }
Send,{F2}
  If NOT InStr(FileExist(Clipboard),"D") ;如果不是文件夹。是用剪贴板取得的完全路径其对应的文件，由此文件属性字串是否含字母 D 来判断的。
    {
     IfNotInString,Clipboard,.lnk ;如果不是快捷方式。因为快捷方式默认不显示扩展名，所以用剪贴板里的完全路径来判断。注意：未判断文件名里存在另一个“.lnk”的情况，因为概率实在是微乎其微！
       {
        StringGetPos,点位,Clipboard,.,R ;如果有多个“.”，取右边首个点的位置。以0开始
        左移位数:=StrLen(Clipboard)-点位
        Send,+{Left %左移位数%} ;按住Shift再左移
       }
    }
Clipboard=%备份%
}
Else Send,{F2}
ExitApp 

