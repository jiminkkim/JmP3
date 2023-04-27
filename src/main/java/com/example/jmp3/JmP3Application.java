package com.example.jmp3;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class JmP3Application implements CommandLineRunner {

    @Value("${user.department}")
    private String department;

    @Value("${adserver.address}")
    private String ad_server;

    @Value("${cocktail.address}")
    private String cocktail_server;

    @Autowired
    ADserver adserver;

    @Autowired
    CocktailApi api;

    public static void main(String[] args) throws IOException {
        SpringApplication.run(JmP3Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String userId = null;
        String userName = null;
        String userDepartment = null;
        JSONArray temp = null;
        temp = adserver.getUserAD();

        for (int i = 0; i < temp.size(); i++) {
            JSONObject jsonObj = (JSONObject) temp.get(i);

            JSONObject attributes = (JSONObject) jsonObj.get("attributes");
            if (attributes != null) {
                JSONArray jsonArray = (JSONArray) attributes.get("LDAP_ENTRY_DN");
                String str = jsonArray.get(0).toString();
                String[] array = str.split(","); // ex) CN=1110000,OU=개발1실,---
                String dpt = array[1]; // ex) OU=개발1실
                String[] dpt_array = dpt.split("="); // ex) 개발1실

                userId = jsonObj.get("username").toString(); // ex) 1110000
                userName = jsonObj.get("lastName").toString() + jsonObj.get("firstName").toString(); // ex) 고하은
                userDepartment = dpt_array[1]; // ex) 개발1실

                accountSeq(userId, userName, userDepartment);
            }
        }
    }
    public void accountSeq(String userId, String userName, String userDepartment) {
            JSONArray clusterSeq = null;
            clusterSeq = api.getAccountSeq(cocktail_server);

            //accountSeq 중복 확인을 위해 배열 생성
            List<Integer> seqList = new ArrayList<Integer>();

            for (int i = 0; i < clusterSeq.size(); i++) {
                Integer seq_num = null;
                JSONObject jsonObj = (JSONObject) clusterSeq.get(i);
                JSONObject jsonAccount = (JSONObject) jsonObj.get("account");
                String seq = jsonAccount.get("accountSeq").toString();
                seq_num = Integer.parseInt(seq); //accountSeq 값 추출 ex)accountSeq: 4
                if (seqList.indexOf(seq_num) < 0) { // accountSeq가 중복 값이 아니라면
                    seqList.add(seq_num); // 배열에 추가
                    accountUser(userId, userName, userDepartment, seq_num);
                } else { //값이 있으면
                }
            }
            seqList.clear(); //배열 초기화
    }

    public void accountUser(String userId, String userName, String userDepartment, Integer accountSeq) {
            JSONArray parse_result = null;
            parse_result = api.getAccountUsers(accountSeq, cocktail_server);

            boolean addUser = false; // 사용자 추가 여부
            boolean userInactive = false; // 사용자 비활성화 여부
            Integer userSeq = null;

            //AD 서버의 userId가 칵테일 유저 목록 중에 있는지 확인
            for (int i = 0; i < parse_result.size(); i++) {
                JSONObject jsonObj = (JSONObject) parse_result.get(i);
                String obj_userId = jsonObj.get("userId").toString(); // userId 추출, ex) 1110000

                if (obj_userId.equals(userId)) { //칵테일 플랫폼에 있는 유저면
                    addUser = false; //사용자 수정
                    String obj_userSeq = jsonObj.get("userSeq").toString(); //userSeq 추출, ex)136
                    userSeq = Integer.parseInt(obj_userSeq);
                    String obj_userDepartment = jsonObj.get("userDepartment").toString(); //userDepartment 추출, ex) 개발3실

                    if (!obj_userDepartment.equals(userDepartment)) { //부서가 변경됐다면
                        userInactive = true;
                    }
                    break;
                } else {
                    addUser = true; //사용자 추가
                    userInactive = false;
                }
            }

            if (addUser) {
                UserAdd(userId, userName, userDepartment, accountSeq, userSeq); //사용자 추가
            } else if (userInactive){
                UserModify(userId, userName, userDepartment, accountSeq, userSeq); //사용자 수정
            }
    }
    public void UserAdd(String userId, String userName, String userDepartment, Integer accountSeq, Integer userSeq) {
        api.addAccountUsers(userId, userName, userDepartment, department, accountSeq, cocktail_server);
        api.modifyUserInactive(userSeq, accountSeq, cocktail_server);
    }

    public void UserModify(String userId, String userName, String userDepartment, Integer accountSeq, Integer userSeq) {
        api.modifyAccountUsers(userId, userName, userDepartment, userSeq, accountSeq, cocktail_server);
        api.modifyUserInactive(userSeq, accountSeq, cocktail_server);
    }
}
