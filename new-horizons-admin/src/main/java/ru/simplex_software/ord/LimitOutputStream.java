package ru.simplex_software.ord;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class LimitOutputStream extends FilterOutputStream{

    private long limit;

    public LimitOutputStream(OutputStream out,long limit){
        super(out);
        this.limit = limit;
    }

    public void write(byte[]b) throws IOException{
        long left = Math.min(b.length,limit);
        if (left<=0){
            throw new SizeLimitException();
        }

        limit-=left;
        out.write(b, 0, (int)left);
    }

    public void write(int b) throws IOException{
        if (limit<=0){
            throw new SizeLimitException();
        }
        limit--;
        out.write(b);
    }

    public void write(byte[]b,int off, int len) throws IOException{
        long left = Math.min(len,limit);
        if (left<=0){
            throw new SizeLimitException();
        }
        limit-=left;
        out.write(b,off,(int)left);
    }
}