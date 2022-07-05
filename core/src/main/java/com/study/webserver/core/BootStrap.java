package com.study.webserver.core;

import java.util.Scanner;

/**
 * 启动引导
 */
public class BootStrap {
    public static void run() {
        Server server = new Server();
        Scanner scanner = new Scanner(System.in);
        String order = null;
        while (scanner.hasNext()) {
            order = scanner.next();
            if (order.equals("EXIT")) {
                server.close();
                System.exit(0);
            }
        }
    }
}
