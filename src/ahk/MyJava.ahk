:*:;sop::
    sop = System.out.println(  );
    clipboard = %sop%
    send ^v
    loop 3 {
        send {Left down}
    }
    return

:*:;fore::
    Var =
    (
        for( : ) {

        }
    )
    clipboard = %Var%
    send ^v
    loop 2 {
        send {Up down}
    }
    loop 3 {
        send {Right down}
    }
    return

:*:;main::
    Var =
    (
        public static void main( String[] args ) {

        }
    )
    clipboard = %Var%
    send ^v
    loop 1 {
        send {Up down}
    }
    send {Tab}
    return

