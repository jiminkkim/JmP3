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
        getAccountSeq();
    }
    public static void getAccountSeq() {
        try {
            URL url = new URL("http://api-server:8080/api/account/1/users"); //URL 객체 생성

            //HTTP Connection 구하기
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //요청 방식 설정 GET
            conn.setRequestMethod("GET");

            //연결 타임아웃 설정
            conn.setConnectTimeout(3000); //3초
            //읽기 타임아웃 설정
            conn.setReadTimeout(3000); //3초

            //request header set
            conn.setRequestProperty("user-id", "1");
            conn.setRequestProperty("user-role", "ADMIN");

            conn.connect();

            //response
            InputStream is = conn.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));

            String line = "";
            String result = "";
            //버퍼에 있는 정보 확인
            while ((line = bf.readLine()) != null) {
                result = result.concat(line);
                //System.out.println(result); //받아온 데이터 확인
            }

            //JSON parser를 만들어 만들어진 문자열 데이터를 객체화 함
//             JSONParser parser = new JSONParser();
//             JSONObject obj = (JSONObject) parser.parse(result);
//             JSONArray parse_result = (JSONArray) obj.get("result");

//             for (int i = 0; i < parse_result.size(); i++) {
//                 JSONObject jsonObj = (JSONObject) parse_result.get(i);
//                 System.out.println(jsonObj);
//             }
            bf.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
