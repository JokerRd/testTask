package com.shortn0tes.springbootapp;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws IOException {
        URL basePath = ClassLoader.getSystemClassLoader().getResource("airports.csv");
        Searcher searcher = new Searcher(new Scanner(System.in).nextLine(),
                args.length != 0 ? args[0] : "",
                basePath);
        searcher.search();
        searcher.printResult();
        System.out.println("Time spent searching: " + searcher.getTimeSearch());
        System.out.println("Number of lines found: " + searcher.getLengthCountStrResult());
    }
}