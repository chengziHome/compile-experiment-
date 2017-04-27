package me.ichengzi.experiment.compiler;

/**
 * Coding is pretty charming when you love it!
 *
 * 读取暂存源成语字符序列的工具类。
 *
 * @author Chengzi Start
 * @date 2017/4/27
 * @time 16:54
 */
public class CharReader {

    //整个源字符串序列
    protected char[] buf;
    protected int bp=-1;
    protected final int buflen;


    //当前要识别的字符
    protected char ch;

    //用于生成token的一个缓冲区
    protected char[] sbuf = new char[128];
    protected int sp;


    /**
     *
     * @param buf
     * @param buflen
     */
    public CharReader(char[] buf, int buflen) {
        this.buf = buf;
        this.buflen = buflen;
        scanChar();
    }

    /**
     * 向后识别一个字符
     * @return
     */
    protected boolean scanChar(){
        if (bp<buflen){
            ch = buf[++bp];
            return true;
        }else{
            return false;
        }
    }

    /**
     * 将当前字符放置于sbuf中。
     */
    protected void putChar(){
        sbuf[sp++] = ch;
        ch = buf[++bp];
    }


    /**
     * 将sbuf中的字符返回成一个字符串对象
     * @return
     */
    protected String chars(){
        return new String(sbuf,0,sp);

    }





}
