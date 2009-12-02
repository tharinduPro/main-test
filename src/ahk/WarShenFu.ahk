;当准备刷符的时候，可以在聊天那里输入-1来启动启动，输入-1off来关闭
;----------------------------------------------------------------
;主程序
;----------------------------------------------------------------
Gosub, f_EnvironmentSetting ;脚本环境设置
Gosub, ShuaFu ;刷符，每两分钟发送提示信息

;----------------------------------------------------------------
;主程序结束
;----------------------------------------------------------------


;----------------------------------------------------------------
;子程序定义
;----------------------------------------------------------------
;设置脚本环境
f_EnvironmentSetting:
{
#SingleInstance force
#NoEnv
#IfWinActive, Warcraft III ahk_class Warcraft III ;仅在war3下有效
SendMode, Play
SetMouseDelay, 0
SetKeyDelay, 0
Return
}

;上面的都是参考别人的代码，主要是下面的

ShuaFu:
{
::-1::
SetTimer, sf,120000
send,{BS}{BS}setSFon{Enter}
return

::-1off::
SetTimer, sf,off
send,{BS}{BS}{BS}{BS}{BS}setSFoff{Enter};由于不知道怎么直接send中文，所以直接用英文提示
return
}

sf:
{
send,{Enter}CheckIt{Enter}
Return
}