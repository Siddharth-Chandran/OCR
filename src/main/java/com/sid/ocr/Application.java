package com.sid.ocr;


import com.sid.ocr.businesslogic.BusinessLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
public class Application implements CommandLineRunner{

    @Autowired
    BusinessLogic businessLogic;

    public static int fileCount = 0;
    public static Map<String, List<String>> alertDetails = new HashMap<>();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public static List<String> lexicons = Arrays.asList(
            "Mafia",
            "stock is going up",
            "buy stock",
            "will go down",
            "dance",
            "what",
            "the"
    );

    @Override
    public void run(String... strings) throws Exception {
        businessLogic.processImages();
    }
}
