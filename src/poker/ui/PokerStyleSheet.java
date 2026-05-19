package poker.ui;


public class PokerStyleSheet {

    private static final String CSS =
        ".poker-slider .track { -fx-background-color: #225522; -fx-border-color: #448844; }" +
        ".poker-slider .thumb { -fx-background-color: #f5d76e; }" +
        ".poker-slider .thumb:hover { -fx-background-color: #ffe090; }";

    public static String asDataUri() {
        String encoded = CSS
            .replace("%",  "%25")
            .replace(" ",  "%20")
            .replace("#",  "%23")
            .replace(":",  "%3A")
            .replace(";",  "%3B")
            .replace("{",  "%7B")
            .replace("}",  "%7D")
            .replace(".",  "%2E")
            .replace("(",  "%28")
            .replace(")",  "%29")
            .replace("'",  "%27")
            .replace(",",  "%2C")
            .replace(">",  "%3E");
        return "data:text/css," + encoded;
    }
}
