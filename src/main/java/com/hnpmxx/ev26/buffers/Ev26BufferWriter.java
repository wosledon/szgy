package com.hnpmxx.ev26.buffers;

import java.util.Arrays;

public class Ev26BufferWriter {
    @SuppressWarnings("FieldMayBeFinal")
    private byte[] buffer;
    // 编码之前写入位置
    public int beforeCodingWrittenPosition;
    public int writtenCount;

    public Ev26BufferWriter(byte[] buffer){
        this.buffer = buffer;
        writtenCount = 0;
        beforeCodingWrittenPosition = 0;
    }

    public byte[] free(){
        return buffer;
    }

    public byte[] written(){
        return Arrays.copyOfRange(buffer, 0, writtenCount);
    }

    public void advance(int count){
        writtenCount += count;
    }

    public byte[] getBuffer(){
        return this.buffer;
    }
}
