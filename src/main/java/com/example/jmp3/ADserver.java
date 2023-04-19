package com.example.jmp3;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class ADserver {
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

                JSONObject attributes = (JSONObject) jsonObj.get("attributes");
                JSONArray jsonArray = (JSONArray) attributes.get("LDAP_ENTRY_DN");
//                System.out.println(jsonArray.get(0));
                String str = jsonArray.get(0).toString();
                String[] array = str.split(","); //CN=1110000,OU=경영지원실,---
                String dpt = array[1]; //OU=경영지원실
                String[] dpt_array = dpt.split("="); // 경영지원실

                userId = jsonObj.get("username").toString(); // userId 1110000
                userName = jsonObj.get("lastName").toString() + jsonObj.get("firstName").toString(); // userName 고하은
                userDepartment = dpt_array[1]; // userDepartment 경영지원실

                // Cocktail 사용자 조회해서 비교
//                CocktailApi api = new CocktailApi();
//                boolean addUser = api.getAccountUsers(userId);
//                if (addUser) {
//                    api.addAccountUsers(userId, userName, userDepartment);
//                } else {
//                    System.out.println("오류임");
////                    api.modifyAccountUsers(userId, userName, userDepartment);
//                }
            }
            //여기까지
            bf.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
