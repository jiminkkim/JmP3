package com.example.jmp3;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class CocktailApi {
    public void getAccountSeq() {
        try {
            URL url = new URL("http://api-server:8080/api/cluster/v2/conditions"); //URL 객체 생성

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
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(result);
            JSONArray parse_result = (JSONArray) obj.get("result");

            for (int i = 0; i < parse_result.size(); i++) {
                JSONObject jsonObj = (JSONObject) parse_result.get(i);
//                System.out.println(jsonObj);
                System.out.println("clusterId: " + jsonObj.get("clusterId") + ", clusterSeq: " + jsonObj.get("clusterSeq") + ", accountSeq: " + jsonObj.get("accountSeq"));
            }
            bf.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void getAccountUsers() {
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
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(result);
            JSONArray parse_result = (JSONArray) obj.get("result");

            for (int i = 0; i < parse_result.size(); i++) {
                JSONObject jsonObj = (JSONObject) parse_result.get(i);
                System.out.println(jsonObj);
            }
            bf.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void addAccountUsers(String name, String id, String role, String job, String Yn) {
        try {
            URL url = new URL("http://api-server:8080/api/account/1/users"); //URL 객체 생성
            JSONObject body = new JSONObject();
            body.put("userName", name);
            body.put("userId", id);
            body.put("userRole", role);
            body.put("userJob", job);
            body.put("inactiveYn", Yn);

            HttpURLConnection conn = null;

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("Accept", "application/json"); //api 리턴값을 json으로 받을 경우
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(body.toString());
            bw.flush();
            bw.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String output;
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void modifyAccountUsers() {
        try {
            URL url = new URL("http://api-server:8080/api/account/1/user/2220000"); //URL 객체 생성
            JSONObject body = new JSONObject();
            body.put("userName", "조시우");
            body.put("userRole", "user");
            body.put("userJob", "경영지원실");
            body.put("inactiveYn", "N");

            HttpURLConnection conn = null;

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("Accept", "application/json"); //api 리턴값을 json으로 받을 경우
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(body.toString());
            bw.flush();
            bw.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String output;
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void modifyUserInactive() {
        try {
            URL url = new URL("http://api-server:8080/api/account/1/user/2220000/inactive"); //URL 객체 생성
            JSONObject body = new JSONObject();
            body.put("inactiveYn", "Y");

            HttpURLConnection conn = null;

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("Accept", "application/json"); //api 리턴값을 json으로 받을 경우
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(body.toString());
            bw.flush();
            bw.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String output;
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
