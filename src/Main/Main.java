package Main;

import IO.NIOTest;

public class Main {
    public static void main(String[] args){
        System.out.println("test");
        NIOTest nioTest = new NIOTest();
        nioTest.startServer();
    }
}
