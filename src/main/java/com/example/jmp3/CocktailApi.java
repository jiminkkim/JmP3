package com.example.jmp3;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @section 작성정보
 * - author: 개발3실
 * - version: 1.0
 * - since 2023.04.20.
 * @section Class
 * - Class: CocktailApi
 * @section 수정정보
 * - 수정일: 2023.04.20.
 * - 수정자: 김지민
 * - 수정내용: 최초생성
 */
@Configuration
@Component
public class CocktailApi {

    /**
     * Cocktail의 클러스터 현황 목록을 조회한다. @n
     * 조회된 결과를 통해 AD서버의 사용자를 Cocktail의 다수의 플랫폼에 일괄적으로 추가한다.
     * @param userId String: AD 서버에 등록되어 있는 사용자의 ID이다.
     * @param userName String: AD 서버에 등록되어 있는 사용자의 이름이다.
     * @param userDepartment String: AD 서버에 등록되어 있는 사용자의 부서 명이다.
     * @param department String: Cocktail에 등록할 특정 부서 명이다.
     * @param cocktail_server String: Cocktail API Server 주소이다.
     */
    // Cocktail 클러스터 현황 목록 조회
    public JSONArray getAccountSeq(String cocktail_server) {
        JSONArray parse_result = null;
        try {
            URL url = new URL(cocktail_server + "/api/cluster/v2/conditions"); //URL 객체 생성

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
                result = result.concat(line); // 받아온 데이터 결과 저장
            }

            //JSON parser를 만들어 만들어진 문자열 데이터를 객체화
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(result);
            parse_result = (JSONArray) obj.get("result");

            bf.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return parse_result;
    }

    /**
     * Cocktail 내 특정 플랫폼 사용자 목록을 조회한다. @n
     * @param userId String: AD 서버에 등록되어 있는 사용자의 ID이다.
     * @param userName String: AD 서버에 등록되어 있는 사용자의 이름이다.
     * @param userDepartment String: AD 서버에 등록되어 있는 사용자의 부서 명이다.
     * @param department String: Cocktail에 등록할 특정 부서 명이다.
     * @param accountSeq Integer: Cocktail 내 특정 플랫폼을 가리킨다.
     * @param cocktail_server String: Cocktail API Server 주소이다.
     */
    // Cocktail 내 특정 accountSeq 사용자 목록 조회
    public JSONArray getAccountUsers(Integer accountSeq, String cocktail_server) {
        JSONArray parse_result = null;
        try {
            URL url = new URL(cocktail_server + "/api/account/" + accountSeq + "/users"); //URL 객체 생성

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
                result = result.concat(line); // 받아온 데이터 결과 저장
            }

            //JSON parser를 만들어 만들어진 문자열 데이터를 객체화
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(result);
            parse_result = (JSONArray) obj.get("result");

            bf.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return parse_result;
    }

    /**
     * Cocktail 내 특정 플랫폼에 AD 서버 사용자를 추가한다. @n
     * @param userId String: AD 서버에 등록되어 있는 사용자의 ID이다.
     * @param userName String: AD 서버에 등록되어 있는 사용자의 이름이다.
     * @param userDepartment String: AD 서버에 등록되어 있는 사용자의 부서 명이다.
     * @param department String: Cocktail에 등록할 특정 부서 명이다.
     * @param accountSeq Integer: Cocktail 내 특정 플랫폼을 가리킨다.
     * @param cocktail_server String: Cocktail API Server 주소이다.
     */
    // 특정 accountSeq에 사용자 추가
    public Integer addAccountUsers(String userId, String userName, String userDepartment, String department, Integer accountSeq, String cocktail_server){
        String[] array = department.split(","); // ex) [개발1실, 개발2실, 개발3실]
        Integer userSeq = null;
        for (int i = 0; i < array.length; i++) {
            if (userDepartment.equals(array[i])) { //환경변수 값에 해당하는 (ex. 개발1실) 부서 소속인 사용자만 추가
                JSONObject data = new JSONObject();
                ArrayList rolearr = new ArrayList();
                rolearr.add("DEVOPS");

                data.put("userName", userName);
                data.put("userId", userId);
                data.put("roles", rolearr);
                data.put("userDepartment", userDepartment);

                try {
                    String host_url = cocktail_server + "/api/account/" + accountSeq + "/user";
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
                        result = result.concat(line); // 받아온 데이터 결과 저장
                    }

                    //JSON parser를 만들어 만들어진 문자열 데이터를 객체화
                    Object obj = null;
                    JSONObject jsonObj = null;
                    JSONParser jsonParser = new JSONParser();

                    obj = jsonParser.parse(result); //JSONParser를 통해 Object로 바꾸고
                    jsonObj = (JSONObject) obj; // 이 Object를 다시 JSONObject로 캐스팅

                    Object parse_result = jsonObj.get("result");

                    JSONObject json_result = null;
                    json_result = (JSONObject) parse_result; //Object를 JSONObject로 캐스팅
                    String result_userSeq = json_result.get("userSeq").toString();
                    userSeq = Integer.parseInt(result_userSeq);

                    bf.close();
                } catch (IOException ie) {
                    System.out.println(ie.getMessage());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return userSeq;
    }

    /**
     * Cocktail 내 특정 플랫폼의 사용자 부서 명을 수정한다. @n
     * @param userId String: AD 서버에 등록되어 있는 사용자의 ID이다.
     * @param userName String: AD 서버에 등록되어 있는 사용자의 이름이다.
     * @param userDepartment String: AD 서버에 등록되어 있는 사용자의 부서 명이다.
     * @param userSeq Integer: Cocktail에 등록되어 있는 특정 사용자를 가리킨다.
     * @param accountSeq Integer: Cocktail 내 특정 플랫폼을 가리킨다.
     * @param cocktail_server String: Cocktail API Server 주소이다.
     */
    // 특정 accountSeq 사용자 정보(부서) 수정
    public void modifyAccountUsers(String userId, String userName, String userDepartment, Integer userSeq, Integer accountSeq, String cocktail_server) {
        JSONObject data = new JSONObject();

        ArrayList rolearr = new ArrayList();
        rolearr.add("DEVOPS");

        data.put("userName", userName);
        data.put("userId", userId);
        data.put("roles", rolearr);
        data.put("userDepartment", userDepartment);

        try {
            String host_url = cocktail_server + "/api/account/" + accountSeq + "/user/" + userSeq;
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

    /**
     * Cocktail 내 특정 플랫폼의 사용자를 비 활성화한다. @n
     * @param userSeq Integer: Cocktail에 등록되어 있는 특정 사용자를 가리킨다.
     * @param accountSeq Integer: Cocktail 내 특정 플랫폼을 가리킨다.
     * @param cocktail_server String: Cocktail API Server 주소이다.
     */
    // 특정 accountSeq 사용자 비활성화
    public void modifyUserInactive(Integer userSeq, Integer accountSeq, String cocktail_server) {
        JSONObject data = new JSONObject();

        data.put("inactiveYn", "Y");
        data.put("userSeq", userSeq);

        System.out.println(data);

        try {
            String host_url = cocktail_server + "/api/account/" + accountSeq + "/user/"+ userSeq + "/inactive";
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
