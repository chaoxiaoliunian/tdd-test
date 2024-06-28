package org.example;

public class Main {
    public static void main(String[] args) {

        String message = "this is a test from wlf!";
        String res = DingTalkUtil.postWithJson(message);
        System.out.println(res);
    }
}