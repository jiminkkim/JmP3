package com.example.jmp3;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class ADserver {
    public void getUserAD() {
        try {
            URL url = new URL("http://101.55.69.58:30001/users"); //URL 객체 생성
            BufferedReader bf;
            String line = "";
            String result = "";

            //정보를 받아옴
            bf = new BufferedReader(new InputStreamReader(url.openStream()));

            //버퍼에 있는 정보를 하나의 문자열로 변환.
            while ((line = bf.readLine()) != null) {
                result = result.concat(line);
//                System.out.println(result); //받아온 데이터를 확인해봄
            }

            //JSON parser를 만들어 만들어진 문자열 데이터를 객체화 함.
            Object obj = null;
//            JSONObject jsonObj = null;
            JSONParser parser = new JSONParser();

            obj = parser.parse(result);
//            System.out.println(obj);
            JSONArray temp = (JSONArray) obj;

            for (int i = 0; i < temp.size(); i++) {
                JSONObject jsonObj = (JSONObject) temp.get(i);
                System.out.println(jsonObj.get("firstName"));
            }
            //여기까지
            bf.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
