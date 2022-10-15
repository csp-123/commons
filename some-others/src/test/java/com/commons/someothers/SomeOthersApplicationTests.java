package com.commons.someothers;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SpringBootTest
class SomeOthersApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Test
    void contextLoads() {
        String a = "csp";
        Map<String, String> b = new HashMap<>();
        b.put("a", "csp");
        redisTemplate.opsForValue().set(a, b);
        Object csp = redisTemplate.opsForValue().get("csp");
        System.out.println(csp);

    }


    @Test
    void test1() throws IOException {

        String script = IOUtils.toString(new FileInputStream("D:\\Practice\\commons\\auth\\src\\main\\resources\\lua\\test1.lua"), StandardCharsets.UTF_8);
        System.out.println(script);

        Object eval = redissonClient.getScript().eval(RScript.Mode.READ_WRITE, script, RScript.ReturnType.STATUS, Collections.singletonList("username"));
        System.out.println(eval.toString());


        List<Object> list = new ArrayList<>();
        list.add(null);

        List<String> list1 = new LinkedList<>();
        list1.add("a");

    }

    @Test
    void test2() throws IOException {
        InputStream in = new FileInputStream("D:\\gov.xls");
        InputStream in2 = new FileInputStream("D:\\column.xls");
        InputStream in3 = new FileInputStream("D:\\column2.xls");

        List<List<String>> dataList = new ArrayList<>();
        try {
            HSSFWorkbook sheets = new HSSFWorkbook(in);
            int numberOfSheets = sheets.getNumberOfSheets();

            for (int sheetIndex = 0; sheetIndex < numberOfSheets; sheetIndex++) {
                HSSFSheet sheet = sheets.getSheetAt(sheetIndex);

                for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                    List<String> data = new ArrayList<>();
                    HSSFRow row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }
                    // 读取每一格内容
                    System.out.println(row.getLastCellNum());
                    for (int index = 0; index < row.getLastCellNum(); index++) {
                        HSSFCell cell = row.getCell(index);
                        if (cell != null) {
                            String stringCellValue = cell.getStringCellValue();
                            if (StringUtils.isEmpty(stringCellValue)) {
                                data.add("");
                            } else {
                                data.add(stringCellValue);
                            }
                        } else {
                            data.add("");
                        }
                    }

                    dataList.add(data);
                }
            }
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        List<List<String>> dataList2 = new ArrayList<>();
        try {
            HSSFWorkbook sheets = new HSSFWorkbook(in2);
            int numberOfSheets = sheets.getNumberOfSheets();

            for (int sheetIndex = 0; sheetIndex < numberOfSheets; sheetIndex++) {
                HSSFSheet sheet = sheets.getSheetAt(sheetIndex);

                for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                    List<String> data = new ArrayList<>();
                    HSSFRow row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }
                    // 读取每一格内容

                    for (int index = 0; index < row.getLastCellNum(); index++) {

                        HSSFCell cell = row.getCell(index);
                        if (cell != null) {
                            String stringCellValue = cell.getStringCellValue();
                            if (StringUtils.isEmpty(stringCellValue)) {
                                data.add("");
                            } else {
                                data.add(stringCellValue);
                            }
                        } else {
                            data.add("");
                        }
                    }

                    dataList2.add(data);
                }
            }
        } finally {
            try {
                if (in2 != null) {
                    in2.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<List<String>> dataList3 = new ArrayList<>();
        try {
            HSSFWorkbook sheets = new HSSFWorkbook(in3);
            int numberOfSheets = sheets.getNumberOfSheets();

            for (int sheetIndex = 0; sheetIndex < numberOfSheets; sheetIndex++) {
                HSSFSheet sheet = sheets.getSheetAt(sheetIndex);

                for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                    List<String> data = new ArrayList<>();
                    HSSFRow row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }
                    // 读取每一格内容

                    for (int index = 0; index < row.getLastCellNum(); index++) {

                        HSSFCell cell = row.getCell(index);
                        if (cell != null) {
                            String stringCellValue = cell.getStringCellValue();
                            if (StringUtils.isEmpty(stringCellValue)) {
                                data.add("");
                            } else {
                                data.add(stringCellValue);
                            }
                        } else {
                            data.add("");
                        }
                    }

                    dataList3.add(data);
                }
            }
        } finally {
            try {
                if (in3 != null) {
                    in3.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // 拼接url
        String prefix = "http://www.qingdao.gov.cn";
        for (List<String> list : dataList) {

            if (list.size() < 5) {
                String[] channelIds = list.get(2).split(",");
                for (int i = 0; i < channelIds.length; i++) {
                    // todo
                    boolean flag = false;
                    for (List<String> list1 : dataList2) {
                        if (list1.get(0).equals(channelIds[i])) {
                            if (list1.size() > 1) {
                                channelIds[i] = list1.get(1);
                                flag = true;
                                break;
                            }

                        }
                    }
                    if (!flag) {
                        for (List<String> list1 : dataList3) {
                            if (list1.get(0).equals(channelIds[i])) {
                                if (list1.size() > 1) {
                                    channelIds[i] = list1.get(1);
                                    flag = true;
                                    break;
                                }

                            }
                        }
                    }

                }

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(prefix);
                for (String channelId : channelIds) {
                    stringBuilder.append("/").append(channelId);
                }
                list.add(stringBuilder.toString());
            }
        }

        // 栏目名称 [1]

        for (List<String> list : dataList) {
            System.out.println(list.get(1));
        }

        System.out.println("====================");

        for (List<String> list : dataList) {
            System.out.println(list.get(4));
        }


    }


    @Test
    void test3() {
        List<String> list = new ArrayList<>();
        list.add("青岛市城阳第七中学_5540,基本概况_5883,领导信息_5886");
        list.add(
                "青岛市城阳区实验幼儿园_5605,基本概况_7035,领导信息_7038");
        list.add(
                "青岛市城阳区桃源居幼儿园_5606,基本概况_7053,领导信息_7056");
        list.add(
                "青岛市城阳区顺德居幼儿园_5607,基本概况_7071,领导信息_7074");
        list.add(
                "青岛市城阳区白沙湾幼儿园_5608,基本概况_7089,领导信息_7092");
        list.add(
                "青岛市城阳区城阳街道中心幼儿园_5609,基本概况_7107,领导信息_7110");
        list.add(
                "青岛市城阳区流亭街道中心幼儿园_5610,基本概况_7125,领导信息_7128");
        list.add(
                "青岛市城阳区夏庄街道中心幼儿园_5611,基本概况_7143,领导信息_7146");
        list.add(
                "青岛市城阳区惜福镇街道中心幼儿园_5612,基本概况_7161,领导信息_7164");
        list.add(
                "青岛市城阳区上马街道中心幼儿园_5613,基本概况_7179,领导信息_7182");
        list.add(
                "青岛市城阳区棘洪滩街道中心幼儿园_8025,基本概况_8028,领导信息_8031");
        list.add(
                "青岛市城阳区红岛街道办事处中心幼儿园_8026,基本概况_8046,领导信息_8049");
        list.add(
                "青岛市城阳区河套街道中心幼儿园_8027,基本概况_8064,领导信息_8067");
        list.add(
                "卫生健康_5516,公立医院_7197,青岛市城阳区人民医院（区属医院）_7201,基本概况_7214,领导信息_7222");
        list.add(
                "青岛城阳水务有限公司_7569,基本概况_7577,领导信息_7585");
        list.add(
                "青岛鑫江供水有限公司_7570,基本概况_7596,领导信息_7599");
        list.add(
                "青岛新奥新城燃气有限公司_7572,基本概况_7634,领导信息_7637");
        list.add(
                "泰能天然气有限公司城阳分公司_7573,基本概况_7653,领导信息_7656");
        list.add(
                "青岛顺安热电有限公司_7574,基本概况_7672,领导信息_7675");
        list.add(
                "青岛金海热电有限公司_7575,基本概况_7691,领导信息_7694");
        list.add(
                "青岛公交集团轨道巴士有限公司_8082,基本概况_8084,领导信息_8087");
        list.add(
                "青岛公交集团城阳巴士有限公司_8083,基本概况_8100,领导信息_8103");
        list.add(
                "青岛城阳开发投资集团有限公司_7765,基本概况_7774,领导信息_7777");
        list.add(
                "青岛动车小镇投资集团有限公司_7766,基本概况_7798,领导信息_7801");
        list.add(
                "青岛城阳市政开发建设投资集团有限公司_7767,基本概况_7822,领导信息_7825");
        list.add(
                "青岛城阳城市发展集团有限公司_7768,基本概况_7846,领导信息_7849");
        list.add(
                "青岛市城阳区阳光城阳控股集团有限公司_7769,基本概况_7870,领导信息_7873");
        list.add(
                "青岛市城阳区阳光创新投资有限公司_7770,基本概况_7894,领导信息_7897");
        list.add(
                "国科健康科技小镇（青岛）有限公司_7773,基本概况_7966,领导信息_7969");
        list.add(
                "青岛红建投资有限公司_8958,基本概况_8959,领导信息_8962");
        list.add(
                "青岛环海新城投资有限公司_9297,基本概况_9298,领导信息_9301");
        list.add(
                "青岛市城阳第十中学_5539,基本概况_5865,领导信息_5868");
        list.add(
                "青岛市城阳区白沙湾第二幼儿园_9760,基本概况_9761,领导信息_9764");
        list.add(
                "青岛市城阳第一高级中学_5528,学校概况_第一高级中学,领导信息_学校概况_第一高级中学");
        list.add(
                "青岛市城阳第二高级中学_5529,学校概况_第二高级中学,领导信息_学校概况_第二高级中学");
        list.add(
                "青岛市城阳第三高级中学_5530,学校概况_第三高级中学,领导信息_学校概况_第三高级中学");
        list.add(
                "青岛市城阳区实验中学（区属学校）_5531,学校概况_实验中学（区属学校）,领导信息_学校概况_实验中学（区属学校）");
        list.add(
                "青岛市城阳区第二实验中学（区属学校）_5532,学校概况_第二实验中学,领导信息_学校概况_第二实验中学");
        list.add(
                "青岛市城阳第四中学_5533,学校概况_第四中学,领导信息_学校概况_第四中学");
        list.add(
                "青岛市城阳第十五中学_5534,学校概况_第十五中学,领导信息_学校概况_第十五中学");
        list.add(
                "青岛市城阳区实验小学（区属学校）_5545,学校概况_实验小学,领导信息_学校概况_实验小学");
        list.add(
                "青岛市城阳第十三中学_5535,学校概况_第十三中学,领导信息_学校概况_第十三中学");
        list.add(
                "青岛市城阳第五中学_5536,学校概况_第五中学,领导信息_学校概况_第五中学");
        list.add(
                "青岛市城阳第九中学_5537,学校概况_第九中学,领导信息_学校概况_第九中学");
        list.add(
                "青岛市城阳第十七中学_5538,学校概况_第十七中学,领导信息_学校概况_第十七中学");
        list.add(
                "青岛市城阳第十中学_5539,学校概况_第十中学,领导信息_学校概况_第十中学");
        list.add(
                "青岛市城阳第七中学_5540,学校概况_第七中学,领导信息_学校概况_第七中学");
        list.add(
                "青岛市城阳第二十中学_5541,学校概况_第二十中学,领导信息_学校概况_第二十中学");
        list.add(
                "青岛市城阳区第二实验小学（区属学校）_5546,学校概况_第二实验小学,领导信息_学校概况_第二实验小学");
        list.add(
                "青岛市城阳第六中学_5542,学校概况_第六中学,领导信息_学校概况_第六中学");
        list.add(
                "青岛市城阳区第三实验小学（区属学校）_5547,学校概况_第三实验小学,领导信息_学校概况_第三实验小学");
        list.add(
                "青岛市城阳第八中学_5543,学校概况_第八中学,领导信息_学校概况_第八中学");
        list.add(
                "青岛市城阳第十一中学_5544,学校概况_第十一中学,领导信息_学校概况_第十一中学");
        list.add(
                "北京师范大学青岛城阳附属学校（区属学校）_5598,学校概况_附属学校,领导信息_学校概况_附属学校");
        list.add(
                "青岛市城阳区天泰城学校（区属学校）_5599,学校概况_天泰城学校,领导信息_学校概况_天泰城学校");
        list.add(
                "青岛市城阳区国城小学（区属学校）_5548,学校概况_国城小学,领导信息_学校概况_国城小学");
        list.add(
                "青岛市城阳区白沙湾学校（区属学校）_5600,学校概况_白沙湾学校,领导信息_学校概况_白沙湾学校");
        list.add(
                "青岛市城阳区白云山学校_5601,学校概况_白云山学校,领导信息_学校概况_白云山学校");
        list.add(
                "特殊教育学校_5526,青岛市城阳区特殊教育中心_5604,学校概况_特殊教育中心,领导信息_学校概况_特殊教育中心");
        list.add(
                "城阳区育才小学_9527,学校概况_育才小学,领导信息_学校概况_育才小学");
        list.add(
                "青岛市城阳区城阳街道古庙小学_5549,学校概况_古庙小学,领导信息_学校概况_古庙小学");
        list.add(
                "城阳区礼德小学_9528,学校概况_礼德小学,领导信息_学校概况_礼德小学");
        list.add(
                "青岛市城阳区红岛街道中心小学_5597,学校概况_红岛街道中心小学,领导信息_学校概况_红岛街道中心小学");
        list.add(
                "青岛出口加工区实验小学_5596,学校概况_青岛出口加工区实验小学,领导信息_学校概况_青岛出口加工区实验小学");
        list.add(
                "青岛市城阳区河套街道汇海小学_5595,学校概况_河套街道汇海小学,领导信息_学校概况_河套街道汇海小学");
        list.add(
                "青岛市城阳区河套街道中心小学_5594,学校概况_河套街道中心小学,领导信息_学校概况_河套街道中心小学");
        list.add(
                "青岛市城阳区上马街道张哥庄小学_5593,学校概况_上马街道张哥庄小学,领导信息_学校概况_上马街道张哥庄小学");
        list.add(
                "青岛市城阳区上马街道桃源小学_5592,学校概况_上马街道桃源小学,领导信息_学校概况_上马街道桃源小学");
        list.add(
                "青岛市城阳区职业教育中心学校_5602,学校概况_职业教育中心学校,领导信息_学校概况_职业教育中心学校");
        list.add(
                "青岛市城阳区上马街道程哥庄小学_5591,学校概况_上马街道程哥庄小学,领导信息_学校概况_上马街道程哥庄小学");
        list.add(
                "青岛市城阳区上马街道上马中心小学_5590,学校概况_上马街道上马中心小学,领导信息_学校概况_上马街道上马中心小学");
        list.add(
                "青岛市城阳区职业中等专业学校_5603,学校概况_职业中等专业学校,领导信息_学校概况_职业中等专业学校");
        list.add(
                "青岛市城阳区棘洪滩街道南万小学_5589,学校概况_棘洪滩街道南万小学,领导信息_学校概况_棘洪滩街道南万小学");
        list.add(
                "青岛市城阳区环城路小学_5550,学校概况_环城路小学,领导信息_学校概况_环城路小学");
        list.add(
                "青岛市城阳区棘洪滩街道中华埠小学_5588,学校概况_棘洪滩街道中华埠小学,领导信息_学校概况_棘洪滩街道中华埠小学");
        list.add(
                "青岛市城阳区城阳街道田村小学_5551,学校概况_田村小学,领导信息_学校概况_田村小学");
        list.add(
                "青岛市城阳区棘洪滩街道棘洪滩小学_5587,学校概况_棘洪滩街道棘洪滩小学,领导信息_学校概况_棘洪滩街道棘洪滩小学");
        list.add(
                "青岛市城阳区城阳街道大北曲小学_5552,学校概况_北曲小学,领导信息_学校概况_北曲小学");
        list.add(
                "青岛市城阳区春阳路小学_5586,学校概况_春阳路小学,领导信息_学校概况_春阳路小学");
        list.add(
                "青岛市城阳区城阳街道后桃林小学_5553,学校概况_桃林小学,领导信息_学校概况_桃林小学");
        list.add(
                "青岛市城阳区城阳街道仲村小学_5554,学校概况_仲村小学,领导信息_学校概况_仲村小学");
        list.add(
                "青岛市城阳区棘洪滩街道锦绣小学_5585,学校概况_棘洪滩街道锦绣小学,领导信息_学校概况_棘洪滩街道锦绣小学");
        list.add(
                "青岛市城阳长城路小学_5555,学校概况_长城路小学,领导信息_学校概况_长城路小学");
        list.add(
                "青岛市城阳区惜福镇街道棉花小学_5584,学校概况_惜福镇街道棉花小学,领导信息_学校概况_惜福镇街道棉花小学");
        list.add(
                "青岛市城阳区正阳路小学_5556,学校概况_正阳路小学,领导信息_学校概况_正阳路小学");
        list.add(
                "青岛市城阳区城阳街道京口小学_5557,学校概况_京口小学,领导信息_学校概况_京口小学");
        list.add(
                "青岛市城阳区惜福镇街道葛家小学_5583,学校概况_惜福镇街道葛家小学,领导信息_学校概况_惜福镇街道葛家小学");
        list.add(
                "青岛市城阳区流亭街道仙家寨小学_5558,学校概况_仙家寨小学,领导信息_学校概况_仙家寨小学");
        list.add(
                "青岛市城阳区流亭街道赵红路小学_5559,学校概况_赵红路小学,领导信息_学校概况_赵红路小学");
        list.add(
                "青岛市城阳区惜福镇街道金村小学_5582,学校概况_惜福镇街道金村小学,领导信息_学校概况_惜福镇街道金村小学");
        list.add(
                "青岛市城阳区流亭街道空港小学_5560,学校概况_空港小学,领导信息_学校概况_空港小学");
        list.add(
                "青岛市城阳区惜福镇街道牟家小学_5581,学校概况_惜福镇街道牟家小学,领导信息_学校概况_惜福镇街道牟家小学");
        list.add(
                "青岛市城阳区流亭街道双埠小学_5561,学校概况_双埠小学,领导信息_学校概况_双埠小学");
        list.add(
                "青岛市城阳区惜福镇街道铁骑山小学_5580,学校概况_惜福镇街道铁骑山小学,领导信息_学校概况_惜福镇街道铁骑山小学");
        list.add(
                "青岛市城阳区流亭街道春雨小学_5562,学校概况_春雨小学,领导信息_学校概况_春雨小学");
        list.add(
                "青岛市城阳区惜福镇街道惜福镇小学_5579,学校概况_惜福镇街道惜福镇小学,领导信息_学校概况_惜福镇街道惜福镇小学");
        list.add(
                "青岛市城阳区流亭街道流亭小学_5563,学校概况_流亭小学,领导信息_学校概况_流亭小学");
        list.add(
                "青岛市城阳区流亭街道天河小学_5564,学校概况_天河小学,领导信息_学校概况_天河小学");
        list.add(
                "青岛市城阳区惜福镇街道中心小学_5578,学校概况_惜福镇街道中心小学,领导信息_学校概况_惜福镇街道中心小学");
        list.add(
                "青岛市城阳区夏庄街道古镇小学_5565,学校概况_古镇小学,领导信息_学校概况_古镇小学");
        list.add(
                "青岛市城阳区夏庄街道丹山小学_5577,学校概况_夏庄街道丹山小学,领导信息_学校概况_夏庄街道丹山小学");
        list.add(
                "青岛市城阳区夏庄街道源头小学_5566,学校概况_源头小学,领导信息_学校概况_源头小学");
        list.add(
                "青岛市城阳区夏庄街道夏庄小学_5567,学校概况_夏庄小学,领导信息_学校概况_夏庄小学");
        list.add(
                "青岛市城阳区夏庄街道沙沟小学_5576,学校概况_夏庄街道沙沟小学,领导信息_学校概况_夏庄街道沙沟小学");
        list.add(
                "青岛市城阳区夏庄街道云头崮小学_5568,学校概况_云头崮小学,领导信息_学校概况_云头崮小学");
        list.add(
                "青岛市城阳区夏庄街道营村小学_5575,学校概况_夏庄街道营村小学,领导信息_学校概况_夏庄街道营村小学");
        list.add(
                "青岛市城阳区夏庄街道曹村小学_5569,学校概况_曹村小学,领导信息_学校概况_曹村小学");
        list.add(
                "青岛市城阳区夏庄街道小水小学_5570,学校概况_小水小学,领导信息_学校概况_小水小学");
        list.add(
                "青岛市城阳区夏庄街道黄埠小学_5574,学校概况_夏庄街道黄埠小学,领导信息_学校概况_夏庄街道黄埠小学");
        list.add(
                "青岛市城阳区夏庄街道三台小学_5571,学校概况_三台小学,领导信息_学校概况_三台小学");
        list.add(
                "青岛市城阳区夏庄街道山色峪学校_5573");
        list.add(
                "青岛市城阳区夏庄街道华阴小学_5572");


        for (String s : list) {
            s = s.split(",")[0];
            System.out.println(s);
        }


    }

}
