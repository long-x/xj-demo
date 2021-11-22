package com.ecdata.cmp.iaas.utils;

import lombok.extern.slf4j.Slf4j;

import javax.swing.text.html.parser.Parser;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZhaoYX
 * @since 2019/12/4 15:33,
 */
@Slf4j
public class ScriptParser {
    /**
     * 将字符串text中由openToken和closeToken组成的占位符依次替换为args数组中的值
     * @param openToken  定义起始占位符
     * @param closeToken  定义终止占位符
     * @param text  整个带占位符的脚本文件
     * @param args 传入的参数
     * @return
     */
    public static String parse(String openToken, String closeToken, String text, Object... args) {
        //没参数返回原脚本
        if (args == null || args.length <= 0) {
            return text;
        }

        //没脚本返回空字符串
        if (text == null || text.isEmpty()) {
            return "";
        }


        int argsIndex = 0;
        //文本内容转成字符数组
        char[] src = text.toCharArray();
        //距离第一个起始占位符的偏移量
        int offset = 0;
        // search open token  第一个起始占位符位置
        int start = text.indexOf(openToken, offset);
        log.info("start 起始占位符位置"+start);
        if (start == -1) {
            log.info("start 没有在脚本文件中找到起始占位符，返回原脚本");
            return text;
        }

        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        while (start > -1) {//有起始占位符的情况下
            if (start > 0 && src[start - 1] == '\\') { //起始占位符前位是双斜杠
                // this open token is escaped. remove the backslash and continue.
                //越过该占位符，最终解析成原占位符，参数替换脚本的下一个占位符
                //0-5  一直到斜杠，但是不包括，而是把起始符号加入
                builder.append(src, offset, start - offset - 1).append(openToken);
                log.info("start offset  "+offset);
                log.info("start while \\ "+builder.toString());
                offset = start + openToken.length();
                log.info("start offset 前有\\的{位置 "+offset);
            } else {
                // found open token. let's search close token.
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }

                builder.append(src, offset, start - offset);
                log.info("start else builder "+builder.toString());
                offset = start + openToken.length();
                log.info("start else offset  "+offset);
                //终止占位符的位置
                int end = text.indexOf(closeToken, offset);
                log.info("end 对应结束符位置 "+end);
                while (end > -1) {
                    if (end > offset && src[end - 1] == '\\') { //同样前面有双斜杠
                        // this close token is escaped. remove the backslash and continue.
                        expression.append(src, offset, end - offset - 1).append(closeToken);
                        log.info("end \\ expression  "+expression);
                        offset = end + closeToken.length();
                        log.info("end while offset  "+offset);
                        end = text.indexOf(closeToken, offset);
                        log.info("end end  "+end);
                    } else {
                        expression.append(src, offset, end - offset);
                        log.info("end else expression "+expression);
                        offset = end + closeToken.length();
                        log.info("end else offset "+offset);
                        break;
                    }
                }
                if (end == -1) {
                    // close token was not found.
                    builder.append(src, start, src.length - start);
                    offset = src.length;
                    log.info("close not found.builder "+builder);
                    log.info("close not found.offset "+offset);
                } else {
                    ///////////////////////////////////////仅仅修改了该else分支下的个别行代码////////////////////////
                    //参数放入或补全脚本
                    String value = (argsIndex <= args.length - 1) ?
                            (args[argsIndex] == null ? "" : args[argsIndex].toString()) : expression.toString();
                    builder.append(value);
                    log.info("value "+value);
                    log.info("value "+builder);
                    offset = end + closeToken.length();
                    log.info("value "+offset);
                    argsIndex++;
                    ////////////////////////////////////////////////////////////////////////////////////////////////
                }
            }
            start = text.indexOf(openToken, offset);
            log.info("restart "+start);
        }
        if (offset < src.length) {
            log.info("没占位符了 "+offset);
            builder.append(src, offset, src.length - offset);
            log.info("没占位符了 "+builder);
        }
        return builder.toString();
    }


    public static String parse0(String text, Object... args) {
        return ScriptParser.parse("${", "}", text, args);
    }

    public static String parse1(String text, Object... args) {
        return ScriptParser.parse("{", "}", text, args);
    }

    public static String sql(String text, Object... args) {
        return ScriptParser.parse("#{", "}", text, args);
    }

    public static String trans(String text,Map<String,Object> map){
        int argsIndex = 0;
        String openToken="#{";
        String closeToken="}";
        char[] src = text.toCharArray();
        int offset = 0;
        int start = text.indexOf(openToken, offset);

        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        while (start > -1) {//有起始占位符的情况下
            if (start > 0 && src[start - 1] == '\\') { //起始占位符前位是双斜杠
                // this open token is escaped. remove the backslash and continue.
                //越过该占位符，最终解析成原占位符，参数替换脚本的下一个占位符
                //0-5  一直到斜杠，但是不包括，而是把起始符号加入
                builder.append(src, offset, start - offset - 1).append(openToken);
                log.info("start offset  "+offset);
                log.info("start while \\ "+builder.toString());
                offset = start + openToken.length();
                log.info("start offset 前有\\的{位置 "+offset);
            } else {
                // found open token. let's search close token.
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }

                builder.append(src, offset, start - offset);
                log.info("start else builder "+builder.toString());
                offset = start + openToken.length();
                log.info("start else offset  "+offset);
                //终止占位符的位置
                int end = text.indexOf(closeToken, offset);
                log.info("end 对应结束符位置 "+end);
                while (end > -1) {
                    if (end > offset) { //同样前面有双斜杠 // && src[end - 1] == '\\'
                        expression.append(src, offset, end - offset);
                        log.info("end else expression "+expression);
                        offset = end + closeToken.length();
                        log.info("end else offset "+offset);
                        break;
                        // this close token is escaped. remove the backslash and continue.
//                        expression.append(src, offset, end - offset - 1).append(closeToken);
//                        log.info("end \\ expression  "+expression);
//                        offset = end + closeToken.length();
//                        log.info("end while offset  "+offset);
//                        end = text.indexOf(closeToken, offset);
//                        log.info("end end  "+end);
                    } else {
//                        expression.append(src, offset, end - offset);
//                        log.info("end else expression "+expression);
//                        offset = end + closeToken.length();
//                        log.info("end else offset "+offset);
//                        break;
                    }
                }
                if (end == -1) {
                    // close token was not found.
                    builder.append(src, start, src.length - start);
                    offset = src.length;
                    log.info("close not found.builder "+builder);
                    log.info("close not found.offset "+offset);
                } else {
                   //参数放入或补全脚本
                    String value =  expression.toString();
                    Object trueValue = map.get(value);
                    builder.append(trueValue);
                    log.info("value value "+ trueValue);
                    log.info("value builder "+builder);
                    offset = end + closeToken.length();
                    log.info("value offset "+offset);
                    argsIndex++;
                }
            }
            start = text.indexOf(openToken, offset);
            log.info("restart "+start);
        }
        return builder.toString();
    }
    /**
     * 使用示例
     * @param args
     */
    public static void main(String... args) {
//        List<Map<String,Object>> list = new ArrayList<>();

//        list.add(id);
//        Map<String,Object> kind = new LinkedHashMap<>();

//        list.add(kind);
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("id","28599706619822088");
        map.put("kind","zzzzz");
        String text = "select * from iaas_component where id = #{id} and kind=#{kind}";
        System.out.println(ScriptParser.trans(text,map));
        //{}被转义，不会被替换
//        System.out.println(ScriptParser.parse("{", "}", "我的名字是\\{},结果是{}，可信度是%{}", "雷锋", true, 100));
//        System.out.println(ScriptParser.parse("{", "}","我的名字是{\\},结果是{}，可信度是%{}", "雷锋", true, 100));
//        System.out.println(ScriptParser.parse0("我的名字是${},结果是${}，可信度是%${}", "雷锋", true, 100));
//        System.out.println(ScriptParser.parse1("我的名字是{},结果是{}，可信度是%{}", "雷锋", true, 100));
       //    输出结果如下：
//    我的名字是{},结果是true，可信度是%100
//    我的名字是雷锋,结果是true，可信度是%100
//    我的名字是雷锋,结果是true，可信度是%100




    }
}
