package com.hnpmxx.ev26.formatters;

import com.hnpmxx.ev26.Ev26Package;
import com.hnpmxx.ev26.buffers.Ev26BufferWriter;
import com.hnpmxx.ev26.enums.PackageType;
import com.hnpmxx.ev26.exceptions.Ev26ErrorCode;
import com.hnpmxx.ev26.exceptions.Ev26Exception;
import com.hnpmxx.ev26.extensions.CrcExtensions;
import com.hnpmxx.ev26.extensions.HexExtensions;
import com.hnpmxx.ev26.extensions.StringExtensions;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Ev26MessagePackWriter {
    @SuppressWarnings("FieldMayBeFinal")
    private Ev26BufferWriter writer;

    public PackageType packageType;

    public Ev26MessagePackWriter(byte[] buffer, PackageType packageType) {
        writer = new Ev26BufferWriter(buffer);
        this.packageType = packageType;
    }

    public Ev26MessagePackWriter(byte[] buffer) {
        writer = new Ev26BufferWriter(buffer);
        this.packageType = PackageType.Type1;
    }

    /**
     * 设置包类型
     *
     * @param packageType 包类型
     */
    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    /**
     * 编码后内存块
     *
     * @return byte[]
     */
    public byte[] flushAndGetEncodingArray() {
        byte[] buff = writer.written();
        return Arrays.copyOfRange(buff, writer.beforeCodingWrittenPosition, buff.length);
    }

    /**
     * 获取实际写入的内存块
     *
     * @return byte[]
     */
    public byte[] flushAndGetReal() {
        return writer.written();
    }

    public void writeStart() {
        this.writeInt16(Ev26Package.getBeginFlag(PackageType.Type1));
    }

    public void writeStart(PackageType packageType) {
        this.writeInt16(Ev26Package.getBeginFlag(packageType));
    }

    public void writeEnd() {
        this.writeInt16(Ev26Package.endFlag);
    }

    /**
     * 写入空表示, 0x00
     *
     * @return position
     */
    public int nil() {
        int position = writer.writtenCount;
        byte[] span = writer.getBuffer();
        span[position] = 0x00;
        writer.advance(1);

        return position;
    }

    /**
     * 跳过多少字节数
     *
     * @param count shu
     * @return position
     */
    public int skip(int count) {
        int position = writer.writtenCount;
        byte[] span = writer.getBuffer();
        for (int i = 0; i < count; i++) {
            span[position + i] = 0x00;
        }
        writer.advance(count);

        return position;
    }

    /**
     * 跳过多少字节
     *
     * @param count     count
     * @param fullValue 用什么字节填充
     * @return position
     */
    public int skip(int count, byte fullValue) {
        int position = writer.writtenCount;
        byte[] span = writer.getBuffer();
        for (int i = 0; i < count; i++) {
            span[position + i] = fullValue;
        }

        writer.advance(count);

        return position;
    }

    public void writeChar(char value) {
        byte[] span = writer.getBuffer();
        span[writer.writtenCount] = (byte) value;
        writer.advance(1);
    }

    public void writeByte(byte value) {
        byte[] span = writer.getBuffer();
        span[writer.writtenCount] = value;
        writer.advance(1);
    }

    private void writeBytes(byte[] value) {
        for (int i = 0; i < value.length; i++) {
            writeByte(value[i]);
        }
    }

    public void writeInt16(short value) {
        byte[] bytes = HexExtensions.shortToByteBig(value);
        writeBytes(bytes);
    }

    public void writeInt32(int value) {
        byte[] bytes = HexExtensions.intToByteBig(value);
        writeBytes(bytes);
    }

    public void writeInt64(long value) {
        byte[] bytes = HexExtensions.longToBytesBig(value);
        writeBytes(bytes);
    }

    public void writeInt24(int value) {
        byte[] bytes = new byte[]{(byte) (value >> 16), (byte) (value >> 8), (byte) (value)};
        writeBytes(bytes);
    }

    /**
     * 根据内存定位, 反写2个字节的数据
     *
     * @param value
     * @param position
     */
    public void writeInt16Return(short value, int position) {
        byte[] span = writer.getBuffer();
        byte[] bytes = HexExtensions.shortToByteBig(value);
        for (int i = position, j = 0; i < position + 2; i++, j++) {
            span[i] = bytes[j];
        }
    }

    /**
     * 根据内存定位, 反写4个字节的数据
     *
     * @param value
     * @param position
     */
    public void writeInt32Return(int value, int position) {
        byte[] span = writer.getBuffer();
        byte[] bytes = HexExtensions.intToByteBig(value);
        for (int i = position, j = 0; i < position + 4; i++, j++) {
            span[i] = bytes[j];
        }
    }

    /**
     * 根据内存定位, 反写8个字节的数据
     *
     * @param value
     * @param position
     */
    public void writeInt64Return(long value, int position) {
        byte[] span = writer.getBuffer();
        byte[] bytes = HexExtensions.longToBytesBig(value);
        for (int i = position, j = 0; i < position + 8; i++, j++) {
            span[i] = bytes[j];
        }
    }

    /**
     * 根据内存定位, 反写8个字节的数据
     *
     * @param value
     * @param position
     */
    public void writeByteReturn(byte value, int position) {
        byte[] span = writer.getBuffer();
        span[position] = value;
    }

    private String fillZero(int len) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < len; i++) {
            s.append("0");
        }
        return s.toString();
    }

    /**
     * 反写bcd码
     *
     * @param value
     * @param len
     * @param position
     */
    public void writeBcdReturn(String value, int len, int position) {
        String bcdText = value;
        int startIndex = 0;
        int noOfZero = len - bcdText.length();
        if (noOfZero > 0) {
            bcdText = fillZero(noOfZero) + bcdText;
        }

        int byteIndex = 0;
        int count = len / 2;
        byte[] bcdSpan = string2BCD(bcdText);
        while (startIndex < bcdSpan.length && byteIndex < count) {
            writer.getBuffer()[position + (byteIndex++)] = bcdSpan[startIndex++];
        }
    }

    /**
     * 将十进制字符串转换为 BCD编码
     *
     * @param str
     * @return
     */
    private byte[] string2BCD(String str) {
        byte[] s = str.getBytes();
        byte[] b = new byte[s.length / 2];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte) (s[2 * i] << 4 | (s[2 * i + 1] & 0xf));
        }
        return b;
    }

    /**
     * 将BCD编码的byte数组转换为String
     *
     * @param bcd
     * @return
     */
    private String bcd2String(byte[] bcd) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bcd.length; i++) {
            sb.append(bcd[i] >> 4 & 0xf)
                    .append(bcd[i] & 0xf);
        }
        return sb.toString();
    }

    /**
     * 根据内存定位, 反写一组数据
     *
     * @param value
     * @param position
     */
    public void writeArrayReturn(byte[] value, int position) {
        byte[] span = writer.getBuffer();
        for (int i = 0; i < value.length; i++) {
            span[position + i] = value[i];
        }
    }

    /**
     * 整型数据转为BCD BYTE 为了兼容int类型，不使用byte做参数 支持0xFF一个字节的转换
     *
     * @param value
     * @return
     */
    public byte int2Bcd(int value) {
        byte result = 0;
        if (value <= 0xFF) {
            int high = value / 10;
            int low = value % 10;
            result = (byte) (high << 4 | low);
        }

        return result;
    }

    /**
     * 整型数据转为BCD BYTE[]
     *
     * @param value 整数值
     * @param list  bytes
     * @param count 字节数=整数值
     */
    public void int2Bcd(int value, byte[] list, int count) {
        int level = count - 1;
        int high = value / 100;
        int low = value % 100;
        if (high > 0) {
            IntToBcd(high, list, --count);
        }
        byte res = (byte) (((low / 10) << 4) + (low % 10));
        list[level] = res;
    }

    /**
     * 整型数据转为BCD BYTE[]
     *
     * @param value     整数值
     * @param list      bytes
     * @param byteCount 字节数=整数值
     */
    public void IntToBcd(long value, byte[] list, int byteCount) {
        int level = byteCount - 1;
        if (level < 0) return;
        long high = value / 100;
        long low = value % 100;
        if (high > 0) {
            IntToBcd(high, list, --byteCount);
        }
        byte res = (byte) (((low / 10) << 4) + (low % 10));
        list[level] = res;
    }

    public void writeBcd(long value, int byteCount) {
        byte[] span = new byte[byteCount];
        IntToBcd(value, span, byteCount);
        writeBytes(span);
    }

    public void writeBcd(int value, int byteCount) {
        byte[] span = new byte[byteCount];
        IntToBcd(value, span, byteCount);
        writeBytes(span);
    }

    /**
     * 将指定内存块进行或运算并入一个字节
     *
     * @param start
     * @param end
     */
    public void writeXor(int start, int end) throws Ev26Exception {
        if (start > end) {
            throw new Ev26Exception(Ev26ErrorCode.outOfRangeException, String.format("%d>%d", start, end));
        }

        byte[] xorSpan = Arrays.copyOfRange(writer.written(), start, end);

        byte result = xorSpan[0];
        for (int i = start + 1; i < end; i++) {
            result = (byte) (result ^ xorSpan[i]);
        }

        byte[] span = writer.getBuffer();
        span[writer.writtenCount] = result;
        writer.advance(1);
    }

    public void writeCrcForHeader() throws Ev26Exception {
        if (writer.writtenCount < 1) {
            throw new Ev26Exception(Ev26ErrorCode.outOfRangeException,
                    String.format("Written<start:%d>1", writer.writtenCount));
        }

        byte[] xorSpan = Arrays.copyOfRange(writer.written(), 1, writer.writtenCount);
        short crc = CrcExtensions.GetCrc16ForHeader(xorSpan);

        writeByte((byte) (crc >> 8));
        writeByte((byte) crc);
    }

    public void writeCrcForPackage() throws Ev26Exception {
        if (writer.writtenCount < 1) {
            throw new Ev26Exception(Ev26ErrorCode.outOfRangeException,
                    String.format("Written<start:%d>1", writer.writtenCount));
        }

        byte[] xorSpan = Arrays.copyOfRange(writer.written(), 0, writer.writtenCount);
        short crc = CrcExtensions.GetCrc16ForPackage(xorSpan);

        writeByte((byte) (crc >> 8));
        writeByte((byte) crc);
    }

    /**
     * 写入Crc
     *
     * @throws Ev26Exception
     */
    public void writeCrc() throws Ev26Exception {
        if (writer.writtenCount < 1) {
            throw new Ev26Exception(Ev26ErrorCode.outOfRangeException,
                    String.format("Written<start:%d>1", writer.writtenCount));
        }

        byte[] xorSpan = Arrays.copyOfRange(writer.written(), 0, writer.writtenCount);
        short crc = CrcExtensions.GetCrc16(xorSpan);

        writeByte((byte) (crc >> 8));
        writeByte((byte) crc);
    }

    /**
     * 写入Bcd编码数据
     *
     * @param value
     * @param len
     */
    public void writeBcd(String value, int len) {
        String bcdText = value;
        int startIndex = 0;
        int noOfZero = len - bcdText.length();
        if (noOfZero > 0) {
            bcdText = fillZero(noOfZero) + bcdText;
        }
        int byteIndex = 0;
        int count = len / 2;
        byte[] bcdSpan = string2BCD(bcdText);

//        while (startIndex < bcdSpan.length && byteIndex < count) {
//            writer.getBuffer()[(writer.writtenCount + byteIndex++)] = bcdSpan[startIndex++];
//            startIndex += 1;
//        }
//        writer.advance(byteIndex);
        writeBytes(bcdSpan);
    }

    //TODO

    /**
     * Hex
     *
     * @param value
     * @param len
     */
    @Deprecated
    public void writeHex(String value, int len) {

    }

    public void writeUniCode(String value) throws Ev26Exception {
        try {
            byte[] bytes = value.getBytes("unicode");
            writeBytes(bytes);
        } catch (Exception e) {
            throw new Ev26Exception(Ev26ErrorCode.writerException, "Unicode编码错误", e);
        }
    }

    public void writeAscii(String value) throws Ev26Exception {
        try {
            byte[] bytes = value.getBytes("ascii");
            writeBytes(bytes);
        } catch (Exception e) {
            throw new Ev26Exception(Ev26ErrorCode.writerException, "Ascii编码错误", e);
        }
    }

    public void writeAscii(String value, int len) throws Ev26Exception {
        try {
            byte[] temp = new byte[len];
            byte[] bytes = value.getBytes("ascii");
            System.arraycopy(bytes, 0, temp, 0, bytes.length);
            writeBytes(temp);
        } catch (Exception e) {
            throw new Ev26Exception(Ev26ErrorCode.writerException, "Ascii编码错误", e);
        }
    }

    public void writeBigNumber(String value, int len) {
        byte[] span = writer.getBuffer();
        long number = StringExtensions.isNullOrEmpty(value) ? 0 : (long) Double.parseDouble(value);
        for (int i = len - 1; i >= 0; i--) {
            span[i] = (byte) (number & 0xFF); //取低8位
            number = number >> 8;
        }
        writer.advance(len);
    }

    /**
     * 写入6字节时间(可空)
     *
     * @param value
     */
    public void writeDateTime6(LocalDateTime value) {
        byte[] span = new byte[6];

        if (value == null) {
            span[0] = 0;
            span[1] = 0;
            span[2] = 0;
            span[3] = 0;
            span[4] = 0;
            span[5] = 0;
        } else {
            byte yy = String.valueOf(value.getYear()).substring(2).getBytes()[0];
            span[0] = yy;
            span[1] = (byte) value.getMonthValue();
            span[2] = (byte) value.getDayOfMonth();
            span[3] = (byte) value.getHour();
            span[4] = (byte) value.getMinute();
            span[5] = (byte) value.getSecond();
        }

        writeBytes(span);
    }

    /**
     * 获取当前内存块写入位置
     *
     * @return
     */
    public int getCurrentPosition() {
        return writer.writtenCount;
    }

    /**
     * 编码
     */
    public void writeEncode() {
        byte[] tmpSpan = writer.written();

        writer.beforeCodingWrittenPosition = writer.writtenCount;
        byte[] span = writer.getBuffer();
        int tempOffset = 0;
        int index = writer.writtenCount;
//        span[index + tempOffset++] = tmpSpan[0];
        for (int i = 0; i < tmpSpan.length; i++) {
            span[index + tempOffset++] = tmpSpan[i];
        }

        writer.advance(tempOffset);
    }

    public static void main(String[] args) {
        Ev26MessagePackWriter wr = new Ev26MessagePackWriter(new byte[10]);
        wr.writeChar('1');
        wr.skip(1, (byte) 0x99);
        wr.writeChar('2');
        byte[] bu = wr.writer.getBuffer();
        System.out.println(bu);
    }
}
