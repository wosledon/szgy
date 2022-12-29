package com.hnpmxx.ev26.formatters;

import com.hnpmxx.ev26.enums.PackageType;
import com.hnpmxx.ev26.exceptions.Ev26ErrorCode;
import com.hnpmxx.ev26.exceptions.Ev26Exception;
import com.hnpmxx.ev26.extensions.BufferExtensions;
import com.hnpmxx.ev26.extensions.CrcExtensions;
import com.hnpmxx.ev26.extensions.HexExtensions;
import com.hnpmxx.ev26.extensions.StringExtensions;
import com.hnpmxx.ev26.models.TimeZoneLanguageModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Ev26MessagePackReader {
    public byte[] reader;

    public byte[] srcBuffer;

    public int readerCount = 0;

    public PackageType packageType;

    private boolean _checkCrcVerify;

    private final byte[] decode7878 = {0x78, 0x78};
    private final byte[] decode7979 = {0x79, 0x79};

    private boolean isNeedStartEnd;

    public Ev26MessagePackReader(byte[] srcBuffer,
                                 PackageType type,
                                 boolean isNeedStartEnd) {
        this.srcBuffer = srcBuffer;
        readerCount = 0;
        packageType = type;
        reader = srcBuffer;
        _checkCrcVerify = false;
        this.isNeedStartEnd = isNeedStartEnd;
    }

    public Ev26MessagePackReader(byte[] srcBuffer,
                                 PackageType type) {
        this.srcBuffer = srcBuffer;
        readerCount = 0;
        packageType = type;
        reader = srcBuffer;
        _checkCrcVerify = false;
        this.isNeedStartEnd = true;
    }

    public void decode(byte[] allocateBuffer) {
        byte[] crcCode = BufferExtensions.Slice(srcBuffer, srcBuffer.length - (isNeedStartEnd ? 4 : 2), 2);
        _checkCrcVerify = CrcExtensions.AuthCrc(srcBuffer, CrcExtensions.toCrc(crcCode), isNeedStartEnd);
    }

    public short readStart() {
        return readInt16();
    }

    public short readEnd() {
        return readInt16();
    }

    public void Decode(byte[] allocateBuffer) {
        int start = srcBuffer.length - (isNeedStartEnd ? 4 : 2);
        byte[] crcCode = BufferExtensions.Slice(srcBuffer, start, 2);
        _checkCrcVerify = CrcExtensions.AuthCrc(srcBuffer, CrcExtensions.toCrc(crcCode), isNeedStartEnd);
    }

    public boolean getCheckCrcVerify() {
        return _checkCrcVerify;
    }

    public short readInt16() {
        return HexExtensions.byteToShortBig(getSpan(2));
    }

    public int readInt24() {
        byte[] spans = getSpan(3);
        return (spans[0] << 16) + (spans[1] << 8) + spans[2];
    }

    public int readInt32() {
        return HexExtensions.bytes2IntBig(getSpan(4));
    }

    public long readInt64() {
        return HexExtensions.bytesToLongBig(getSpan(8));
    }

    public byte readByte() {
        return getSpan(1)[0];
    }

    public char readChar() {
        return (char) getSpan(1)[0];
    }

    public byte readVirtualByte() {
        return getVirtualSpan(1)[0];
    }

    public byte[] readVirtualArray(int count) {
        return getVirtualSpan(count);
    }

    public short readVirtualInt16() {
        return HexExtensions.byteToShortBig(getVirtualSpan(2));
    }

    public int readVirtualInt24() {
        byte[] spans = getVirtualSpan(3);
        return (spans[0] << 16) + (spans[1] << 8) + spans[2];
    }

    public int readVirtualInt32() {
        return HexExtensions.byteToShortBig(getVirtualSpan(4));
    }

    public long readVirtualInt64() {
        return HexExtensions.byteToShortBig(getVirtualSpan(8));
    }

    public String readBigNumber(int len) {
        long result = 0;
        byte[] span = getSpan(len);
        for (int i = 0; i < len; i++) {
            long currentData = (long) span[i] << (8 * (len - i - 1));
            result += currentData;
        }
        return String.format("%d", result);
    }

    public byte[] readArray(int len) {
        return getSpan(len);
    }

    public byte[] readArray(int start, int end) {
        return Arrays.copyOfRange(reader, start, end);
    }

    public String readAscii(int len) throws Ev26Exception {
        try {
            byte[] span = getSpan(len);
//        return System.Text.Encoding.Unicode.GetString(span);
            return new String(span, "ascii");
        } catch (Exception e) {
            throw new Ev26Exception(Ev26ErrorCode.readerException, "读取Ascii错误", e);
        }
    }

    public String readUnicode(int len) throws Ev26Exception {
        try {
            byte[] span = getSpan(len);
//        return System.Text.Encoding.Unicode.GetString(span);
            return new String(span, "unicode");
        } catch (Exception e) {
            throw new Ev26Exception(Ev26ErrorCode.readerException, "读取Unicode错误", e);
        }
    }

    @Deprecated
    public String readHex(int len) {
        return String.format("");
    }

    public String readBcd(int len, boolean trim) {
        int count = len / 2;
        byte[] span = getSpan(count);
        StringBuilder bcdSb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            bcdSb.append(HexExtensions.toHexString(span[i], "X2"));
        }

        if (trim) {
            return StringExtensions.trimStart(bcdSb.toString(), '0');
        } else {
            return bcdSb.toString();
        }
    }

    public String readBcd(int len) {
        return readBcd(len, true);
    }

    public LocalDateTime readDateTime6() throws Ev26Exception {
        try {
            byte[] span = getSpan(6);
            LocalDateTime result = LocalDateTime.parse(String.format("%d-%d-%d %d:%d:%d",
                    span[0], span[1], span[2], span[3], span[4], span[5]), DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss"));
            return result;
        } catch (Exception e) {
            throw new Ev26Exception(Ev26ErrorCode.readerException, e);
        }
    }

    public int bcd2Int(byte value) {
        int high = value >> 4;
        int low = value & 0xF;
        int number = 10 * high + low;
        return number;
    }

    public TimeZoneLanguageModel readTimeZoneLanguage() {
        byte[] span = getSpan(2);

        return new TimeZoneLanguageModel().Deserialize(span);
    }

    public byte[] readContent() {
        return Arrays.copyOfRange(reader, readerCount, reader.length);
    }

    public byte[] readCount(int count) {
        return Arrays.copyOfRange(reader, readerCount, count + readerCount);
    }

    /**
     * 读取剩余数据体内容长度
     *
     * @return
     */
    public int readCurrentRemainContentLength() {
        return reader.length - readerCount;
    }

    public void skip(int count) {
        readerCount += count;
    }

    public void skip() {
        skip(1);
    }

    private byte[] getSpan(int count) {
        readerCount += count;
        return Arrays.copyOfRange(reader, readerCount - count, reader.length);
    }

    private byte[] getVirtualSpan(int count) {
        return Arrays.copyOfRange(reader, readerCount, readerCount + count);
    }
}
