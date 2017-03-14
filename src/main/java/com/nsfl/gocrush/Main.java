package com.nsfl.gocrush;

import static spark.Spark.get;

public class Main {

    public static void main(String[] args) {

        get("/", (req, res) -> {
            return "Hello World!";
        });
    }

}
