~Esc::
    if vimEnabled is not number
         vimEnabled = 0 

    ;排除vim使用
    WinGetActiveTitle, title
    pos := RegExMatch( title, "GVIM" )

    if ( vimEnabled == 0 && pos==0) {
         vimEnabled = 1 
        vimize()
         send {Escape}
     }
    return

~i:: 
    if ( vimEnabled == 1 ) {
        vimEnabled = 0 
        unvimize()
        send {Backspace}
    }
    return



#IFWinExist vimOn
    ; Multiples
    1:: num = %num%1
    2:: num = %num%2
    3:: num = %num%3
    4:: num = %num%4
    5:: num = %num%5
    6:: num = %num%6
    7:: num = %num%7
    8:: num = %num%8
    9:: num = %num%9
    0:: num = %num%0

    h::Send, {Left}
    j::Send, {Down}
    k::Send, {Up}
    l::Send, {Right}

    $::Send, {End}
    ^::Send, {Home}

    ^b::Send, {PgUp}
    ^f::Send, {PgDn}

    u::Send,^z

    ;删除一行
    :*Z?:dd::
        send,{End}+{Home}^x
        return

#IfWinExist

vimize()
{
  Gui 11:Show, Minimize, vimOn ; Hide,
}

unvimize()
{
  Gui 11:Destroy
}
