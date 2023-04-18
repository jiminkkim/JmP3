package com.example.jmp3;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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
    public void addAccountUsers(String userId, String userName, String userDepartment){

        if (userDepartment == "사업본부") {
            JSONObject data = new JSONObject();
            ArrayList rolearr = new ArrayList();
            rolearr.add("DEVOPS");

            data.put("userName", userName);
            data.put("userId", userId);
            data.put("roles", rolearr);
            data.put("userDepartment", userDepartment);

            try {
                String host_url = "http://api-server:8080/api/account/1/user";
                HttpURLConnection conn = null;

                URL url = new URL(host_url);
                conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                //request header set
                conn.setRequestProperty("user-id", "1");
                conn.setRequestProperty("user-role", "ADMIN");

                conn.setDoOutput(true);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

                bw.write(data.toString());
                bw.flush();
                bw.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String returnMsg = in.readLine();
                System.out.println("응답 메시지: " + returnMsg);
            } catch (IOException ie) {

            }
        }
    }

    public void modifyAccountUsers() {
        JSONObject data = new JSONObject();

        ArrayList rolearr = new ArrayList();
        rolearr.add("DEVOPS");

        data.put("userName", "조세호");
        data.put("userId", 223333);
        data.put("roles", rolearr);
        data.put("userDepartment", "개발1실");

        System.out.println(data);

        try {
            String host_url = "http://api-server:8080/api/account/1/user/136";
            HttpURLConnection conn = null;

            URL url = new URL(host_url);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            //request header set
            conn.setRequestProperty("user-id", "1");
            conn.setRequestProperty("user-role", "ADMIN");

            conn.setDoOutput(true);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

            bw.write(data.toString());
            bw.flush();
            bw.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String returnMsg = in.readLine();
            System.out.println("응답 메시지: " + returnMsg);
        } catch (IOException ie) {

        }
    }
    public void modifyUserInactive() {
        JSONObject data = new JSONObject();


        data.put("inactiveYn", "Y");
        data.put("userSeq", 136);

        System.out.println(data);

        try {
            String host_url = "http://api-server:8080/api/account/1/user/136/inactive";
            HttpURLConnection conn = null;

            URL url = new URL(host_url);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            //request header set
            conn.setRequestProperty("user-id", "3");
            conn.setRequestProperty("user-role", "SYSTEM");

            conn.setDoOutput(true);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

            bw.write(data.toString());
            bw.flush();
            bw.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String returnMsg = in.readLine();
            System.out.println("응답 메시지: " + returnMsg);
        } catch (IOException ie) {

        }
    }
}
