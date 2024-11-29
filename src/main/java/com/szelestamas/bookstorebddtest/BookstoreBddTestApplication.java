package com.szelestamas.bookstorebddtest;

import io.cucumber.core.cli.Main;

public class BookstoreBddTestApplication {

    public static void main(String[] args) {
        Main.main(concatArgs(new String[] {
                "classpath:featuretest",
                "--glue", "com.szelestamas.bookstorebddtest",
                "--plugin",  "pretty",
                "--plugin", "html:reports/cucumber.html",
                "--plugin", "json:reports/cucumber.json",
                "--snippets", "camelcase",
        }, args));
    }

    private static String[] concatArgs(final String[] a, final String[] b) {
        final int aLen = a.length;
        final int bLen = b.length;
        final String[] c = new String[aLen + bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }
}
