;��׼��ˢ����ʱ�򣬿�����������������-1����������������-1off���ر�
;----------------------------------------------------------------
;������
;----------------------------------------------------------------
Gosub, f_EnvironmentSetting ;�ű���������
Gosub, ShuaFu ;ˢ����ÿ�����ӷ�����ʾ��Ϣ

;----------------------------------------------------------------
;���������
;----------------------------------------------------------------


;----------------------------------------------------------------
;�ӳ�����
;----------------------------------------------------------------
;���ýű�����
f_EnvironmentSetting:
{
#SingleInstance force
#NoEnv
#IfWinActive, Warcraft III ahk_class Warcraft III ;����war3����Ч
SendMode, Play
SetMouseDelay, 0
SetKeyDelay, 0
Return
}

;����Ķ��ǲο����˵Ĵ��룬��Ҫ�������

ShuaFu:
{
::-1::
SetTimer, sf,120000
send,{BS}{BS}setSFon{Enter}
return

::-1off::
SetTimer, sf,off
send,{BS}{BS}{BS}{BS}{BS}setSFoff{Enter};���ڲ�֪����ôֱ��send���ģ�����ֱ����Ӣ����ʾ
return
}

sf:
{
send,{Enter}CheckIt{Enter}
Return
}