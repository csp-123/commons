package com.commons.someothers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import okhttp3.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chishupeng
 * @date 2023/8/17 4:45 PM
 */
public class Test {

    @SneakyThrows
    public static void main(String[] args) {

        String url = "https://test-hwork.haier.net/hip/api/purchaser/user/get-user-info-by-user-code?userCode=";
        String authorization = "Bearer eyJ0eXAiOiJqd3QiLCJhbGciOiJSUzI1NiIsImtpZCI6ImY3NDNibiJ9.eyJURVJNSU5BTC1UWVBFIjoicGMiLCJVU0VSLUVNQUlMIjoiIiwiaXNzIjoiaHdvcmsiLCJVU0VSLU5BTUUiOiIlRTUlQkMlQTAlRTglODklQUYlRTclOEUlODkiLCJVU0VSLVBIT05FIjoiIiwiVVNFUi1ST0xFLU5BTUUiOiJ0ZXJtaW51c19yb2xlXzAwMyIsIlVTRVItV0hPTEUtTkVUIjowLCJVU0VSIjoiMDEwNzU4NjUiLCJVU0VSLVJPTEUiOiJ0ZXJtaW51c19yb2xlXzAwMyIsImlhdCI6MTY5MjI2MDM3NCwiZXhwIjoxNjkyMjY3NTc0fQ.iaBW0TkO7EZFmBNyyNptB00A-h18bMlibDrk8GPHXeXGabklxLa2Jp4DQilGOL_WGIhJKeSggvMCM8L3pgcVAHl07snHItWhGroq68LkuXGUIl-wHISKXH55ojBwl7EIOf6C9QTlWJwWZQr8kJcR7HZaP3EvTXdkKifOb1U9VzWJcJoCxnnBN_AJ5aCz19LQVG0lL7kpHDs8PXLAjtI7wUN61g_nVb_xus6JGqvGwpDNVlFnD95fQ8GnM0Q8NKk9gRtv41t2_6-g_lX3-Yn0xSZiXTCviFBzet0NRuQsg7Or6YXD-ghr-iCEVoY2MstPGzoE5wuUcEbO-yuus70l_A";
        String cookie = "hwork_machine_id=66c2be5b-1753-4668-8063-e85ce4c3adbb; _pk_id.hwork-test.0b1e=b7bedfb3ca93d236.1686996512.; gr_user_id=f6f8684d-e8d6-435f-ad83-c44bf5f67a82; bb1ba6f0337e1e9b_gr_last_sent_cs1=A0064090; ulid=5B576A1B6A6B3EDC4022F45CC6263092; _pk_id.hdshipp_1_hid.0b1e=88e73343a358affe.1691045803.; sidebarStatus=1; 93c2e11b0ba77242_gr_last_sent_cs1=01075865; ae2eee21ce6f705e_gr_last_sent_cs1=A0064079; INGRESSCOOKIE=86ff11cfc19d974a121b91b6e19ae6df|9b4260ce95aaa4c3227e03d0d52bfc92; 8f373f4cb80da3d5_gr_last_sent_cs1=01075865; 8f373f4cb80da3d5_gr_cs1=01075865; ae2eee21ce6f705e_gr_cs1=A0064079; x_i_jwt=eyJhbGciOiJSUzI1NiJ9.eyJhY2NvdW50TnVtYmVyIjoiQTAwNjQwOTAiLCJ1YyI6IjEiLCJpbnN0YW5jZUlkIjoxMjkzNzE3MDU1Njk3NDQ3OTk4LCJsb2dpbk5hbWUiOiJBMDA2NDA5MCIsImRvbWFpbiI6IklETSIsInRlbmFudElkIjoxLCJpZCI6MjMwNTI0MjU1MTExLCJsb2dpblNvdXJjZSI6IjYiLCJqdGkiOiIyMGM0NTBlZC01ZDkwLTRkMTktOWZkOC1lMmJlNjc4MWVhMmEiLCJuYmYiOjE2OTIyNjAxNzQsImV4cCI6MTY5MjI4ODAwMH0.c0g95-KzrFtLSyzkCMqXRHSg_LOcWa4_t1ehMl5VSic9X9gi_HE3bYJVR0k6RGxq-O3-k-O5UZhwm7DJQX8_qWhxpzOiEicKoMGYhw1LZ3FQQcOHioVucMCOU8pAGaakHQToGrmgSVjljWQzoZFKrFbhEXtCLrfkQyGSHqjRhd0sBAk0-Z0e-C4JEZf6Kj1XvkqcGxgm1UTfuGVQBQlVEAOmlIW56OL5Qg6k0gCs0nOsnTSQsW-exl0VIhGIlR5ltjWZ2YZCpmay75tpV870pcdhBmMLxSDMeudqehSNW_ek0H0cL0EmYl05GsIqS3IyypjTlemXqQMNnpl2MQdDQg; x_jwt=eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiQTAwNjQwOTAiLCJuYW1lIjoi6L-f5Lmm5pyLIiwibG9naW5Tb3VyY2UiOiJpYW0ifSwia2V5IjoieC1vYXV0aCIsImF1ZCI6Ingtb2F1dGgiLCJleHAiOjE2OTIzMDMzNzQsImlhdCI6MTY5MjI2MDE3NCwiaXNzIjoieC1vYXV0aCIsInN1YiI6IkEwMDY0MDkwIn0.m8TCsWExfrmGZ_fdP0WXUxNgtF3CFW8bIHCmTe-F-QmXc8SQZI2CxYSyCb9k6SP6czx_ftECK6IYol1icBiR3R_fLiVghVPa-hnNXWSI79yNPqSUCLeOAm3DPRY4T6sq62YGZxSCS1XkQ-8LW3uoi7Z7J0CcECirpmwRfoKXXIOnB-sYLA8SuDK3sA6Aww1yWU6gp9DYiVRnK-GumuaGU6xd5qM3kyo8mSNLC2Uwelsy-_vKlQY4WLJdvWbzMazDLfDEssTugo3yyGsLr0ZaAIpPngDLCeftK7tQlVNlKflBCa3BVemhc4b-NFHbXACTenCe8yxk-WCLNm4OhSkFzA; bb1ba6f0337e1e9b_gr_session_id=c334e8cc-08c1-48e6-86bc-03c6372fbbb4; bb1ba6f0337e1e9b_gr_last_sent_sid_with_cs1=c334e8cc-08c1-48e6-86bc-03c6372fbbb4; bb1ba6f0337e1e9b_gr_cs1=A0064090; 93c2e11b0ba77242_gr_session_id=0d0b21d4-122c-4d71-83e6-be8b2b904a4d; 93c2e11b0ba77242_gr_last_sent_sid_with_cs1=0d0b21d4-122c-4d71-83e6-be8b2b904a4d; 93c2e11b0ba77242_gr_session_id_sent_vst=0d0b21d4-122c-4d71-83e6-be8b2b904a4d; _pk_ses.hdshipp_1_hid.0b1e=1; acw_tc=2f624a3616922603596584193e7fce3728771b3dd241bed46916f094a0719d; 93c2e11b0ba77242_gr_cs1=01075865; hwork_jwt=eyJ0eXAiOiJqd3QiLCJhbGciOiJSUzI1NiIsImtpZCI6ImY3NDNibiJ9.eyJURVJNSU5BTC1UWVBFIjoicGMiLCJVU0VSLUVNQUlMIjoiIiwiaXNzIjoiaHdvcmsiLCJVU0VSLU5BTUUiOiIlRTUlQkMlQTAlRTglODklQUYlRTclOEUlODkiLCJVU0VSLVBIT05FIjoiIiwiVVNFUi1ST0xFLU5BTUUiOiJ0ZXJtaW51c19yb2xlXzAwMyIsIlVTRVItV0hPTEUtTkVUIjowLCJVU0VSIjoiMDEwNzU4NjUiLCJVU0VSLVJPTEUiOiJ0ZXJtaW51c19yb2xlXzAwMyIsImlhdCI6MTY5MjI2MDM3NCwiZXhwIjoxNjkyMjY3NTc0fQ.iaBW0TkO7EZFmBNyyNptB00A-h18bMlibDrk8GPHXeXGabklxLa2Jp4DQilGOL_WGIhJKeSggvMCM8L3pgcVAHl07snHItWhGroq68LkuXGUIl-wHISKXH55ojBwl7EIOf6C9QTlWJwWZQr8kJcR7HZaP3EvTXdkKifOb1U9VzWJcJoCxnnBN_AJ5aCz19LQVG0lL7kpHDs8PXLAjtI7wUN61g_nVb_xus6JGqvGwpDNVlFnD95fQ8GnM0Q8NKk9gRtv41t2_6-g_lX3-Yn0xSZiXTCviFBzet0NRuQsg7Or6YXD-ghr-iCEVoY2MstPGzoE5wuUcEbO-yuus70l_A; hwork_token=eyJ0eXAiOiJqd3QiLCJhbGciOiJSUzI1NiIsImtpZCI6ImY3NDNibiJ9.eyJURVJNSU5BTC1UWVBFIjoicGMiLCJVU0VSLUVNQUlMIjoiIiwiaXNzIjoiaHdvcmsiLCJVU0VSLU5BTUUiOiIlRTUlQkMlQTAlRTglODklQUYlRTclOEUlODkiLCJVU0VSLVBIT05FIjoiIiwiVVNFUi1ST0xFLU5BTUUiOiJ0ZXJtaW51c19yb2xlXzAwMyIsIlVTRVItV0hPTEUtTkVUIjowLCJVU0VSIjoiMDEwNzU4NjUiLCJVU0VSLVJPTEUiOiJ0ZXJtaW51c19yb2xlXzAwMyIsImlhdCI6MTY5MjI2MDM3NCwiZXhwIjoxNjkyMjY3NTc0fQ.iaBW0TkO7EZFmBNyyNptB00A-h18bMlibDrk8GPHXeXGabklxLa2Jp4DQilGOL_WGIhJKeSggvMCM8L3pgcVAHl07snHItWhGroq68LkuXGUIl-wHISKXH55ojBwl7EIOf6C9QTlWJwWZQr8kJcR7HZaP3EvTXdkKifOb1U9VzWJcJoCxnnBN_AJ5aCz19LQVG0lL7kpHDs8PXLAjtI7wUN61g_nVb_xus6JGqvGwpDNVlFnD95fQ8GnM0Q8NKk9gRtv41t2_6-g_lX3-Yn0xSZiXTCviFBzet0NRuQsg7Or6YXD-ghr-iCEVoY2MstPGzoE5wuUcEbO-yuus70l_A; hwork_refresh_token=qL8xYiIggz63VUdMejmQrbAF7KiARDn6+hF3nfqI6agz8DswZ6Z6i4PFirfR2W9E; JSESSIONID=5FBBD3F53A1A7779B6DB8762E497AC65";

        String[] userCodes = new String[]{"00012130","00012613","00575935",
                "00576120","01069188","01075865","01413497","01476787","01493749",
                "01494690","01514874","19003223","20016590","21036381","21048890"
        };
        List<String> planList = new ArrayList<>();
        List<String> requestList = new ArrayList<>();
        List<String> orderList = new ArrayList<>();
        List<String> useList = new ArrayList<>();
        List<String> expList = new ArrayList<>();
        for (String userCode : userCodes) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(url + userCode)
                    .method("GET", null)
                    .addHeader("Access-Control-Allow-Credentials", "true")
                    .addHeader("Authorization", authorization)
                    .addHeader("Pragma", "no-cache")
                    .addHeader("Cookie", cookie)
                    .addHeader("User-Agent", "Apifox/1.0.0 (https://www.apifox.cn)")
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    .build();
            Response response = client.newCall(request).execute();
            JSONObject responseObj = JSON.parseObject(response.body().string());
            if (!responseObj.getBooleanValue("success")) {
                continue;
            }
            JSONObject result = responseObj.getJSONObject("result");
            String userCode1 = result.getString("userCode");
            String orgCode = result.getString("orgCode");
            String orgPath = result.getString("orgPath");

            planList.add(generateSqlForPlan(userCode1,orgCode,orgPath));
            requestList.add(generateSqlForReq(userCode1,orgCode,orgPath));
            orderList.add(generateSqlForOrder(userCode1,orgCode,orgPath));
            useList.add(generateSqlForUse(userCode1,orgCode,orgPath));
            expList.add(generateSqlForExpReq(userCode1,orgCode,orgPath));
        }

        System.out.println("-- plan");
        planList.forEach(System.out::println);
        System.out.println("-- request");
        requestList.forEach(System.out::println);
        System.out.println("-- order");
        orderList.forEach(System.out::println);
        System.out.println("-- use");
        useList.forEach(System.out::println);
        System.out.println("-- exception");
        expList.forEach(System.out::println);


    }


    public static String generateSqlForPlan(String userCode, String orgCode, String orgPath) {
        return String.format(" update purchase_plan set department_code_path = '%s' where applicant_code = '%s' and department_code = '%s';", orgPath, userCode, orgCode);
    }

    public static String generateSqlForReq(String userCode, String orgCode, String orgPath) {
        return String.format(" update purchase_request set department_code_path = '%s' where applicant_code = '%s' and department_code = '%s';", orgPath, userCode, orgCode);
    }

    public static String generateSqlForOrder(String userCode, String orgCode, String orgPath) {
        return String.format(" update purchase_order set department_code_path = '%s' where applicant_code = '%s' and department_code = '%s';", orgPath, userCode, orgCode);
    }

    public static String generateSqlForUse(String userCode, String orgCode, String orgPath) {
        return String.format(" update use_order set department_code_path = '%s' where use_user_code = '%s' and use_department_code = '%s';", orgPath, userCode, orgCode);
    }

    /**
     * todo
     * @param userCode
     * @param orgCode
     * @param orgPath
     * @return
     */
    public static String generateSqlForExpReq(String userCode, String orgCode, String orgPath) {

        return String.format(" update purchase_order_exception_request set department_code_path = '%s' where use_user_code = '%s' and use_department_code = '%s';", orgPath, userCode, orgCode);
    }
}
