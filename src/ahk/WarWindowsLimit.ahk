#T::
        hwnd := WinExist( "Warcraft III ahk_class Warcraft III" )
        If ( hwnd != "0" )
        {
                SysGet, CaptionHeight, 4 ; SM_CYCAPTION
                SysGet, BorderHeight, 7 ; SM_CXDLGFRAME

                VarSetCapacity(lpRect, 16, 0)
                DllCall("GetWindowRect", "UInt", hwnd, "UInt", &lpRect)

        ; typedef struct _RECT {
                lpRect_left := NumGet(lpRect, 0, "Int")
                lpRect_top := NumGet(lpRect, 4, "Int")
                lpRect_right := NumGet(lpRect, 8, "Int")
                lpRect_bottom := NumGet(lpRect, 12, "Int")
        ; }

                lpRect_left := lpRect_left + BorderHeight + 1
                lpRect_top := lpRect_top + CaptionHeight + BorderHeight + 1
                lpRect_right := lpRect_right - BorderHeight - 1
                lpRect_bottom := lpRect_bottom - BorderHeight - 1

        ; typedef struct _RECT {
                NumPut(lpRect_left, lpRect, 0, "Int")
                NumPut(lpRect_top, lpRect, 4, "Int")
                NumPut(lpRect_right, lpRect, 8, "Int")
                NumPut(lpRect_bottom, lpRect, 12, "Int")
        ; }

                DllCall("ClipCursor", "UInt", &lpRect)
        }
Return