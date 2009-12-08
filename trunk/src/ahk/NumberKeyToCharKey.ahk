;直接的一种方法：q::Numpad7(小键盘7)，按q就是Numpad7了
;不过这就影响聊天啦，在AutoHotkey中在热键前加~原来键的功能不会被屏蔽，
;比如说~q::Numpad7，就会发送q7。所以我们这样改下
~q::
	Send, {Numpad7}{BS}
	return