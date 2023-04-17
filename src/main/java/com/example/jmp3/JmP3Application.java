package com.example.jmp3;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootApplication
public class JmP3Application {

    public static void main(String[] args) {
        CocktailApi api = new CocktailApi();
        api.addAccountUsers("조세호", "jshsh", "사용자", "경영지원실", "Y");
    }
}
