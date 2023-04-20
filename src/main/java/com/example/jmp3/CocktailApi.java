package com.example.jmp3;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @brief Cocktail API
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
@Component
public class CocktailApi {

    // Cocktail 클러스터 현황 목록 조회
    public void getAccountSeq(String userId, String userName, String userDepartment, String department) {
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
                result = result.concat(line); // 받아온 데이터 결과 저장
            }

            //JSON parser를 만들어 만들어진 문자열 데이터를 객체화
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(result);
            JSONArray parse_result = (JSONArray) obj.get("result");

            // accountSeq 중복 확인을 위해 배열 생성
            List<Integer> seqList = new ArrayList<Integer>();

            for (int i = 0; i < parse_result.size(); i++) {
                Integer seq_num = null;
                JSONObject jsonObj = (JSONObject) parse_result.get(i);
                JSONObject jsonAccount = (JSONObject) jsonObj.get("account");
                String seq = jsonAccount.get("accountSeq").toString();
                seq_num = Integer.parseInt(seq); //accountSeq 값 추출 ex)accountSeq: 4
                if (seqList.indexOf(seq_num) < 0) { // accountSeq가 중복 값이 아니라면
                    seqList.add(seq_num); // 배열에 추가
                    CocktailApi api = new CocktailApi();
                    api.getAccountUsers(userId, userName, userDepartment, department, seq_num); //특정 accountSeq 사용자 목록 조회
                } else { //값이 있으면
                    System.out.println("중복 된 accountSeq임.");
                }
            }
            seqList.clear(); //배열 초기화
            bf.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Cocktail 내 특정 accountSeq 사용자 목록 조회
    public void getAccountUsers(String userId, String userName, String userDepartment, String department, Integer accountSeq) {
        try {
            URL url = new URL("http://api-server:8080/api/account/" + accountSeq + "/users"); //URL 객체 생성

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
            JSONArray parse_result = (JSONArray) obj.get("result");

            boolean addUser = true; // 사용자 추가 여부
            Integer userSeq = null;

            //AD 서버의 userId가 칵테일 유저 목록 중에 있는지 확인
            for (int i = 0; i < parse_result.size(); i++) {
                JSONObject jsonObj = (JSONObject) parse_result.get(i);
                String obj_userId = jsonObj.get("userId").toString(); // userId 추출, ex) 1110000

                if (obj_userId.equals(userId)) { //칵테일 플랫폼에 있는 유저면
                    addUser = false; //사용자 수정
                    String obj_userSeq = jsonObj.get("userSeq").toString(); //userSeq 추출, ex)136
                    userSeq = Integer.parseInt(obj_userSeq);
                    break;
                } else {
                    addUser = true; //사용자 추가
                }
            }

            CocktailApi api = new CocktailApi();
            if (addUser) {
                api.addAccountUsers(userId, userName, userDepartment, department, accountSeq);
            } else {
                api.modifyAccountUsers(userId, userName, userDepartment, userSeq, accountSeq);
            }
            bf.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // 특정 accountSeq에 사용자 추가
    public void addAccountUsers(String userId, String userName, String userDepartment, String department, Integer accountSeq){
        if (userDepartment.equals(department)) { //환경변수 값에 해당하는 (ex. 개발1실) 부서 소속인 사용자만 추가
            JSONObject data = new JSONObject();
            ArrayList rolearr = new ArrayList();
            rolearr.add("DEVOPS");

            data.put("userName", userName);
            data.put("userId", userId);
            data.put("roles", rolearr);
            data.put("userDepartment", userDepartment);

            try {
                String host_url = "http://api-server:8080/api/account/" + accountSeq + "/user";
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
                Integer userSeq = Integer.parseInt(result_userSeq);

                CocktailApi api = new CocktailApi();
                api.modifyUserInactive(userSeq, accountSeq); // 사용자 비활성화

                bf.close();
            } catch (IOException ie) {
                System.out.println(ie.getMessage());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // 특정 accountSeq 사용자 정보(부서) 수정
    public void modifyAccountUsers(String userId, String userName, String userDepartment, Integer userSeq, Integer accountSeq) {
        JSONObject data = new JSONObject();

        ArrayList rolearr = new ArrayList();
        rolearr.add("DEVOPS");

        data.put("userName", userName);
        data.put("userId", userId);
        data.put("roles", rolearr);
        data.put("userDepartment", userDepartment);

        try {
            String host_url = "http://api-server:8080/api/account/" + accountSeq + "/user/" + userSeq;
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

            CocktailApi api = new CocktailApi();
            api.modifyUserInactive(userSeq, accountSeq); // 사용자 비활성화

        } catch (IOException ie) {

        }
    }

    // 특정 accountSeq 사용자 비활성화
    public void modifyUserInactive(Integer userseq, Integer accountSeq) {
        JSONObject data = new JSONObject();

        data.put("inactiveYn", "Y");
        data.put("userSeq", userseq);

        System.out.println(data);

        try {
            String host_url = "http://api-server:8080/api/account/" + accountSeq + "/user/"+ userseq + "/inactive";
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
