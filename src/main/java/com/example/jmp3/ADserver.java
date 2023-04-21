package com.example.jmp3;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @brief ADserver
 * @section 작성정보
 * - author: 개발3실
 * - version: 1.0
 * - since 2023.04.20.
 * @section Class
 * - Class: ADserver
 * @section 수정정보
 * - 수정일: 2023.04.20.
 * - 수정자: 김지민
 * - 수정내용: 최초생성
 */
@EnableScheduling
@Configuration
@Component
public class ADserver {
    @Value("${user.department}")
    private String department;

    @Autowired
    CocktailApi cocktailApi;

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정마다 실행
    public void getUserAD() {
        String userId = null;
        String userName = null;
        String userDepartment = null;

        try {
            URL url = new URL("http://101.55.69.58:30001/users"); //URL 객체 생성
            BufferedReader bf;
            String line = "";
            String result = "";

            //정보를 받아옴
            bf = new BufferedReader(new InputStreamReader(url.openStream()));

            //버퍼에 있는 정보를 하나의 문자열로 변환.
            while ((line = bf.readLine()) != null) {
                result = result.concat(line); // 받아온 데이터 결과 저장
            }

            //JSON parser를 만들어 만들어진 문자열 데이터를 객체화
            Object obj = null;
            JSONParser parser = new JSONParser();

            obj = parser.parse(result);
            JSONArray temp = (JSONArray) obj;

            for (int i = 0; i < temp.size(); i++) {
                JSONObject jsonObj = (JSONObject) temp.get(i);

                JSONObject attributes = (JSONObject) jsonObj.get("attributes");
                JSONArray jsonArray = (JSONArray) attributes.get("LDAP_ENTRY_DN");
                String str = jsonArray.get(0).toString();
                String[] array = str.split(","); // ex) CN=1110000,OU=개발1실,---
                String dpt = array[1]; // ex) OU=개발1실
                String[] dpt_array = dpt.split("="); // ex) 개발1실

                userId = jsonObj.get("username").toString(); // ex) 1110000
                userName = jsonObj.get("lastName").toString() + jsonObj.get("firstName").toString(); // ex) 고하은
                userDepartment = dpt_array[1]; // ex) 개발1실

                // Cocktail 클러스터 현황 목록 조회
                cocktailApi.getAccountSeq(userId, userName, userDepartment, department);
            }
            bf.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
