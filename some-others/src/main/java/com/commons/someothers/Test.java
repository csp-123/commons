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

        String url = "https://hwork.haier.net/hip/api/purchaser/user/get-user-info-by-user-code?userCode=";
        String authorization = "Bearer eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJURVJNSU5BTC1UWVBFIjoicGMiLCJVU0VSLUVNQUlMIjoid2FuZ2h1aS5wc2lAaGFpZXIuY29tIiwiaXNzIjoiaHdvcmstcHJvZCIsIlVTRVItTkFNRSI6IiVFNyU4RSU4QiVFOCVCRSU4OSIsIlVTRVItUEhPTkUiOiIxODY2MTY1MDYyMCIsIlVTRVItUk9MRS1OQU1FIjoiJUU5JTgwJTlBJUU3JTk0JUE4JUU4JUE3JTkyJUU4JTg5JUIyIiwiVVNFUi1XSE9MRS1ORVQiOjAsIlVTRVIiOiIyMDAxNjU5MCIsIlVTRVItUk9MRSI6Inp4cHRyeSIsImlhdCI6MTY5MzQ4MDQ3NSwiZXhwIjoxNjk0MDg1Mjc1fQ.MRCliHA0yUHN4ZQP7Nl-qeWKBphQ4QuvKXnMceP8rFk";
        String cookie = "gr_user_id=f6f8684d-e8d6-435f-ad83-c44bf5f67a82; bb1ba6f0337e1e9b_gr_last_sent_cs1=A0064090; 8f373f4cb80da3d5_gr_last_sent_cs1=01075865; INGRESSCOOKIE=90d4814dd3dea525b1d28d3d999f3d6f; _pk_id.hwork.cd42=d57df4dc3d887344.1692351299.; hwork_machine_id=bcd30b42-023c-45fa-856f-ba8f83f76051; _pk_id.hdshipp_1_hid.cd42=c3d96f90eea5c8a2.1692351321.; ulid=3946CEB5236568BA60A30ED628C21930; 93c2e11b0ba77242_gr_last_sent_cs1=01075865; 8f373f4cb80da3d5_gr_cs1=01075865; 93c2e11b0ba77242_gr_cs1=01075865; x_i_jwt=eyJhbGciOiJSUzI1NiJ9.eyJhY2NvdW50TnVtYmVyIjoiQTAwNjQwOTAiLCJ1YyI6IjEiLCJpbnN0YW5jZUlkIjoxMjkzNzE3MDU1Njk3NDQ3OTk4LCJsb2dpbk5hbWUiOiJBMDA2NDA5MCIsImRvbWFpbiI6IklETSIsInRlbmFudElkIjoxLCJpZCI6MjMwNTI0MjU1MTExLCJsb2dpblNvdXJjZSI6IjYiLCJqdGkiOiI0MzE4MzQyMS03OGY0LTQyNTMtYTdkMC0zMTMzNThmYmY4ZDMiLCJuYmYiOjE2OTM0NTI4MDgsImV4cCI6MTY5MzQ5NzYwMH0.R5t_GXOMQIodfFxiJdo3oJR8C7Yc_Ee2Ex0YeyoEZAH5pje0bxzHFEhAgZ9abkbMvoSQssEyOtVDj0MaAgdhe_P44cmc2Qkx7IJMeGHPPGF10LJ1cAX5uDKDRt6FTS7muo8920y6bZhdNuxhSVcw2_FqZEdn2QDa-XNzg4C5OJMMzgJgRfqpRcXgufvgN29IlCrHUgqQzCUOjbC95kIdPi3gu7EnhfF8yflcQsw1VeRus6l_onLpMDEOBUf0iJY9dTHJMXdLzPRAdvAdYGcnxV5NV0neKyTuo-4Gzb2r9D-Zs7Vcek2swl3GIqPdOxutvcbcGBcb5Bh-AuoC-2f92A; x_jwt=eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiQTAwNjQwOTAiLCJuYW1lIjoi6L-f5Lmm5pyLIiwibG9naW5Tb3VyY2UiOiJpYW0ifSwia2V5IjoieC1vYXV0aCIsImF1ZCI6Ingtb2F1dGgiLCJleHAiOjE2OTM0OTYwMDgsImlhdCI6MTY5MzQ1MjgwOCwiaXNzIjoieC1vYXV0aCIsInN1YiI6IkEwMDY0MDkwIn0.lZm5E3STdRD82GrfhfZbFsZ_jTjizOCxwW9KdDZWQoUSueIsUDox2q16jIIO1JMRhp279zKaPOG59xvcQXwgb_XWNUbe5GIqjk5DdjQsx71BNBSBeIkpSpLExXhjrVaBvGCWABagw5bEBIlYqnMuwdorcT00i80NCWXZtr6y18vf_YIuL79aUYRXsOvkA0L9NqjF6WnvqJ6m3-MHrXhDkcjdRQpukpX5T6s6O0uodbzMspLXIbbnIvuXlOSDeFoKsmuiRv3TB1QhuzCV7UZQl4jqT4EDPx0xbcHOrDwb6v4Qs6LNTW6mgx7RNcFqbHoa7x3En8nAo107iIUh5yzEhQ; bb1ba6f0337e1e9b_gr_cs1=A0064090; acw_tc=2f624a3e16934803494341768e776d70105149ca97e13a21ea47bd8b739b32; ae2eee21ce6f705e_gr_last_sent_sid_with_cs1=40edc9ac-51bf-4ef8-aaf5-a065eda9fa28; ae2eee21ce6f705e_gr_last_sent_cs1=20016590; ae2eee21ce6f705e_gr_session_id=40edc9ac-51bf-4ef8-aaf5-a065eda9fa28; ae2eee21ce6f705e_gr_session_id_sent_vst=40edc9ac-51bf-4ef8-aaf5-a065eda9fa28; _pk_ses.hwork.cd42=1; ae2eee21ce6f705e_gr_cs1=20016590; hwork_jwt=eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJURVJNSU5BTC1UWVBFIjoicGMiLCJVU0VSLUVNQUlMIjoid2FuZ2h1aS5wc2lAaGFpZXIuY29tIiwiaXNzIjoiaHdvcmstcHJvZCIsIlVTRVItTkFNRSI6IiVFNyU4RSU4QiVFOCVCRSU4OSIsIlVTRVItUEhPTkUiOiIxODY2MTY1MDYyMCIsIlVTRVItUk9MRS1OQU1FIjoiJUU5JTgwJTlBJUU3JTk0JUE4JUU4JUE3JTkyJUU4JTg5JUIyIiwiVVNFUi1XSE9MRS1ORVQiOjAsIlVTRVIiOiIyMDAxNjU5MCIsIlVTRVItUk9MRSI6Inp4cHRyeSIsImlhdCI6MTY5MzQ4MDQ3NSwiZXhwIjoxNjk0MDg1Mjc1fQ.MRCliHA0yUHN4ZQP7Nl-qeWKBphQ4QuvKXnMceP8rFk; hwork_token=eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJURVJNSU5BTC1UWVBFIjoicGMiLCJVU0VSLUVNQUlMIjoid2FuZ2h1aS5wc2lAaGFpZXIuY29tIiwiaXNzIjoiaHdvcmstcHJvZCIsIlVTRVItTkFNRSI6IiVFNyU4RSU4QiVFOCVCRSU4OSIsIlVTRVItUEhPTkUiOiIxODY2MTY1MDYyMCIsIlVTRVItUk9MRS1OQU1FIjoiJUU5JTgwJTlBJUU3JTk0JUE4JUU4JUE3JTkyJUU4JTg5JUIyIiwiVVNFUi1XSE9MRS1ORVQiOjAsIlVTRVIiOiIyMDAxNjU5MCIsIlVTRVItUk9MRSI6Inp4cHRyeSIsImlhdCI6MTY5MzQ4MDQ3NSwiZXhwIjoxNjk0MDg1Mjc1fQ.MRCliHA0yUHN4ZQP7Nl-qeWKBphQ4QuvKXnMceP8rFk; hwork_refresh_token=dMbmc+cdajswMi/nSxw+Va76lKUhzS6NfZhalqQWEiQi3PHh4R7ellGu4UYetCd9; JSESSIONID=85FD941C469ACAD38E7F3E205E534AA2";

        String[] userCodes = new String[]{
                "00606712",
                "00951572",
                "01019544",
                "01275894",
                "01383016",
                "01427749",
                "01435539",
                "01458325",
                "01494690",
                "01517934",
                "19003223",
                "20015679",
                "20016590",
                "21001568",
                "21036381",
                "21903627",
                "01494690",
                "21022615",
                "21028814",
                "21032851"
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
            expList.add(generateSqlForExpReqApply(userCode1,orgCode,orgPath));
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
        return String.format(" update purchase_plan set department_code_path = '%s' where applicant_code = '%s'  and (department_code_path is null or department_code_path = '');", orgPath, userCode, orgCode);
    }

    public static String generateSqlForReq(String userCode, String orgCode, String orgPath) {
        return String.format(" update purchase_request set department_code_path = '%s' where applicant_code = '%s'  and (department_code_path is null or department_code_path = '');", orgPath, userCode, orgCode);
    }

    public static String generateSqlForOrder(String userCode, String orgCode, String orgPath) {
        return String.format(" update purchase_order set department_code_path = '%s' where applicant_code = '%s'  and (department_code_path is null or department_code_path = '');", orgPath, userCode, orgCode);
    }

    public static String generateSqlForUse(String userCode, String orgCode, String orgPath) {
        return String.format(" update use_order set department_code_path = '%s' where use_user_code = '%s'  and (department_code_path is null or department_code_path = '');", orgPath, userCode, orgCode);
    }

    /**
     * todo
     * @param userCode
     * @param orgCode
     * @param orgPath
     * @return
     */
    public static String generateSqlForExpReq(String userCode, String orgCode, String orgPath) {
        return String.format(" update purchase_order_exception_request set department_code_path = '%s' where applicant_code = '%s'  and (department_code_path is null or department_code_path = '');", orgPath, userCode, orgCode);
    }

    public static String generateSqlForExpReqApply(String userCode, String orgCode, String orgPath) {
        return String.format(" update purchase_order_exception_request set apply_user_department_code = '%s',apply_user_department_code_path = '%s' where apply_user_code = '%s' and (apply_user_department_code_path is null or apply_user_department_code_path = '');", orgCode, orgPath, userCode);
    }
}
