package me.ichengzi.experiment.compiler;

import java.io.*;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

/**
 * Coding is pretty charming when you love it!
 *
 * 驱动Shell程序
 *
 * @author Chengzi Start
 * @date 2017/4/27
 * @time 17:38
 */
public class Main {


    public static void main(String[] args) {
        Reader reader = null;
        Writer tokenWriter = null;
        Writer errWriter = null;

        try {
        String fileName = args[0];

            reader = new FileReader(fileName);
            char[] cs = new char[1024];
            int len = reader.read(cs);
            CharReader cReader = new CharReader(cs,len);
            Tokenizer tokenizer = new Tokenizer(cReader);

            boolean success = tokenizer.readAllToken();
            List<Token> tokenList = tokenizer.getTokenList();
            List<Tokenizer.Error> errorList = tokenizer.getErrorList();


            String dydPath = fileName.substring(0,fileName.lastIndexOf("/")) + "/";
            String dydName = fileName.substring(fileName.lastIndexOf("/")+1,fileName.lastIndexOf("."));
            String dydFile = dydPath + dydName + ".dyd";

            tokenWriter = new FileWriter(dydFile);
            for (Token token:tokenList){
                String symbol = token.getSymbol();
                char[] result = new char[20];
                int num = symbol.length();
                for (int i = 0; i < 20; i++) {
                    result[i] = ' ';
                }

                symbol.getChars(0,num,result,16-num);

                int no = token.getTokenKind().no;
                if (no<10){
                    result[18] = String.valueOf(no).toCharArray()[0];
                }else{
                    char[] tmp = String.valueOf(no).toCharArray();
                    result[17] = tmp[0];
                    result[18] = tmp[1];

                }
                result[19] = '\n';
                tokenWriter.write(new String(result));
            }

            //输出错误信息
            if (!success){
                String errFile = dydPath + dydName + ".err";
                errWriter = new FileWriter(errFile);
                for (Tokenizer.Error error:errorList){
                    errWriter.write("line"+error.getLine()+":"+error.getMsg()+"\n");
                }
            }


            //测试语法分析器
            Parser parser = new Parser(tokenList);
            parser.parse();
            System.out.println("size:"+tokenList.size());

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (reader!=null){
                    reader.close();
                }
                if (tokenWriter!=null){
                    tokenWriter.flush();
                    tokenWriter.close();
                }
                if(errWriter!=null){
                    errWriter.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }



}
