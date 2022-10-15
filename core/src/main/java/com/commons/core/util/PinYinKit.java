package com.commons.core.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Title:  汉字转拼音工具类
 * Description:
 * Project: commons
 * Author: csp
 * Create Time:2022/10/13 11:15
 */
public class PinYinKit {


    /**
     * 汉字转拼音首字母小写
     * @param chinese 汉字
     * @return 转换后拼音字符串
     */
    public static String chineseToPinYinFirstLowerCase(String chinese) throws BadHanyuPinyinOutputFormatCombination {
        StringBuilder stringBuilder = new StringBuilder();

        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        // 小写
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        // 不带音标和*号
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        for (char c : chinese.toCharArray()) {

            char firstChar = PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat)[0].charAt(0);
            stringBuilder.append(firstChar);
        }
        return stringBuilder.toString();
    }

}
