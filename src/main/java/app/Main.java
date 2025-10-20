package app;

import app.config.ApplicationConfig;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        ApplicationConfig.startServer(7070);
    }
}