package com.example.jmp3;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

@Component
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
    public void getAccountUsers(String userId, String userName, String userDepartment, String department) {
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

            boolean addUser = true;
            Integer userSeq = null;
            for (int i = 0; i < parse_result.size(); i++) { //넘어온 userId가 칵테일 유저 목록 중에 있는지 반복문 돌려서 확인
                JSONObject jsonObj = (JSONObject) parse_result.get(i);
                String obj_userId = jsonObj.get("userId").toString(); //ex) 1110000

                if (obj_userId.equals(userId)) { //칵테일 플랫폼에 있는 유저인지
                    addUser = false; //사용자 수정
                    String obj_userSeq = jsonObj.get("userSeq").toString(); //ex)136
                    userSeq = Integer.parseInt(obj_userSeq);
                    break;
                } else {
                    addUser = true; //사용자 추가
                }
            }

            //AD userId랑 비교
            CocktailApi api = new CocktailApi();
            if (addUser) {
                api.addAccountUsers(userId, userName, userDepartment, department);
            } else {
                api.modifyAccountUsers(userId, userName, userDepartment, userSeq);
            }
            bf.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void addAccountUsers(String userId, String userName, String userDepartment, String department){
        if (userDepartment.equals(department)) { //환경변수 값(개발1실) 부서 소속인 사용자만 추가
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

                //response
                InputStream is = conn.getInputStream();
                BufferedReader bf = new BufferedReader(new InputStreamReader(is));

                String line = "";
                String result = "";
                //버퍼에 있는 정보 확인
                while ((line = bf.readLine()) != null) {
                    result = result.concat(line);
                }

                //JSON parser를 만들어 만들어진 문자열 데이터를 객체화 함
                Object obj = null;
                JSONObject jsonObj = null;
                JSONParser jsonParser = new JSONParser();

                obj = jsonParser.parse(result); //JSONParser를 통해 Object로 바꾸고
                jsonObj = (JSONObject) obj; // 이 Object를 다시 JSONObject로 캐스팅함

                Object parse_result = jsonObj.get("result");

                JSONObject json_result = null;
                json_result = (JSONObject) parse_result; //Object를 JSONObject로 캐스팅
                String result_userSeq = json_result.get("userSeq").toString();
                Integer userSeq = Integer.parseInt(result_userSeq);

                CocktailApi api = new CocktailApi();
                api.modifyUserInactive(userSeq);

                bf.close();
            } catch (IOException ie) {
                System.out.println(ie.getMessage());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void modifyAccountUsers(String userId, String userName, String userDepartment, Integer userSeq) {
        // 사용자 부서가 변경된 경우, 비활성 사용자로 변경하고 부서명은 AD서버 것으로 업데이트 한다.
        JSONObject data = new JSONObject();

        ArrayList rolearr = new ArrayList();
        rolearr.add("DEVOPS");

        data.put("userName", userName);
        data.put("userId", userId);
        data.put("roles", rolearr);
        data.put("userDepartment", userDepartment);

//        System.out.println(data);

        try {
            String host_url = "http://api-server:8080/api/account/1/user/" + userSeq;
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
//            System.out.println("응답 메시지 수정메시지: " + returnMsg);
            CocktailApi api = new CocktailApi();
            api.modifyUserInactive(userSeq);

        } catch (IOException ie) {

        }
    }
    public void modifyUserInactive(Integer userseq) {
        JSONObject data = new JSONObject();

        data.put("inactiveYn", "Y");
        data.put("userSeq", userseq);

        System.out.println(data);

        try {
            String host_url = "http://api-server:8080/api/account/1/user/"+ userseq + "/inactive";
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
