//package com.ht.common.util;
//
//
//import net.sourceforge.pinyin4j.PinyinHelper;
//import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
//import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
//import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
//import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.hhly.base.util.StringUtil;
//
//
///**
// * 获取汉字串拼音首字母，英文字符不变 
// * 
// */
//
//
//
//public  class LetterUtil{
//	
//	 private static Logger logger = LoggerFactory.getLogger(LetterUtil.class);
//	 
//   
//    /** 
//     * 取得给定汉字的首字母,即声母 
//     *  
//     * @param chinese 给定的汉字 
//     * @return 给定汉字的声母 
//     */  
//    public static  String getFirstLetter(String chinese) {  
//        if (StringUtil.isBlank(chinese) || chinese.equals("#")) {  
//            return "#";  
//        }  
//        
//    	StringBuffer pybf = new StringBuffer(); 
//    	chinese =  chinese.substring(0,1);//这里只读取第一个字符
//        char[] arr = chinese.toCharArray();   
//        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();   
//        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);   
//        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);   
//        for (int i = 0; i < arr.length; i++) {   
//            if (arr[i] > 128) {   
//                    try {   
//                            String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);   
//                            if (temp != null) {   
//                                    pybf.append(temp[0].charAt(0));   
//                            }   
//                    } catch (BadHanyuPinyinOutputFormatCombination e) {   
//                            logger.error("格式化异常", e);
//                            return "#";
//                    }   
//            } else {   
//                    pybf.append(arr[i]);   
//            }   
//        }   
//        
//        String rs =  pybf.toString().replaceAll("\\W", "").trim();
//        if(StringUtil.isNotBlank(rs))
//        	return rs.toUpperCase();
//        else
//        	return "#";
//    }  
//  
//}
