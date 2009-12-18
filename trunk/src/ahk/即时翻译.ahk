AutoHotKey是一個類似Linux上shell script的軟體, 或者簡單來說, 他是一個設定快速鍵的軟體,
有興趣的話可以看一下這個介紹, http://blog.nownews.com/jazzdog/textview.php?file=54048
接下來我要講的是, 如何使用Alt-1當成查單字的快速鍵.

我使用的朗文字典是: Longman Dictionary of Contemporary English
(除此之外, 我仍有MED, CALD, ONAD等字典, 不過ahk script寫起來差不多)
由於內建的隨指隨查只有在IE or MS word等才能用, 而且常浮動一個視窗
(不浮動就會藏在後面...Orz)
我的需求是: 看到單字->叫出字典->打單字查詢
因此有了這個短短的script供大家參考.

;look up in Longman
!1::;我使用Alt-1當作快速鍵, 因為右手點滑鼠兩下後讓字反白, 左手按快速鍵很快
MouseClick, left ;讓滑鼠連點兩次, 反白單字
MouseClick, left
Send ^c ;複製此字
;叫出朗文字典
IfWinExist,Longman Dictionary of Contemporary English
WinActivate
else
RUN "D:\Program Files\Longman\ldoce4v2\ldoce4.exe" ;這地方隨個人更改
WinWait, Longman Dictionary of Contemporary English UPDATED EDITION,
;連點兩下滑鼠讓輸入列反白, 然後輸入單字...
;不點兩下滑鼠只用鍵盤輸入, 字典常會不理人...
MouseClick, left, 152, 142
MouseClick, left, 152, 142
Sleep, 100
Send %Clipboard% {Enter};把字貼上後按enter...

return

