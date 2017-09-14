package com.sid.ocr;


import com.sid.ocr.businesslogic.BusinessLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static int fileCount = 0;
    public static Map<String, List<String>> alertDetails = new HashMap<>();
    public static boolean refreshThread = true;

    @Autowired
    BusinessLogic businessLogic;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        if (refreshThread) {
            refreshThread = false;
            businessLogic.processImages();
            refreshThread = true;
        }
    }

    public static List<String> lexicons = Arrays.asList(
            "Mafia",
            "stock",
            "buy stock",
            "will go down",
            "look hot",
            "going up",
            "loser",
            "funding",
            "investment",
            "microsoft",
            "Pole and Santa Claus",
            "and",
            "dance"

    );
}
