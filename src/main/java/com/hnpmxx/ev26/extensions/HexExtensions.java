package com.hnpmxx.ev26.extensions;

public class HexExtensions {
    /**
     * 将int转为高字节在前，低字节在后的byte数组（大端）
     *
     * @param n int
     * @return byte[]
     */
    public static byte[] intToByteBig(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * 将int转为低字节在前，高字节在后的byte数组（小端）
     *
     * @param n int
     * @return byte[]
     */
    public static byte[] intToByteLittle(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * byte数组到int的转换(小端)
     *
     * @param bytes
     * @return
     */
    public static int bytes2IntLittle(byte[] bytes) {
        int int1 = bytes[0] & 0xff;
        int int2 = (bytes[1] & 0xff) << 8;
        int int3 = (bytes[2] & 0xff) << 16;
        int int4 = (bytes[3] & 0xff) << 24;

        return int1 | int2 | int3 | int4;
    }

    /**
     * byte数组到int的转换(大端)
     *
     * @param bytes
     * @return
     */
    public static int bytes2IntBig(byte[] bytes) {
        int int1 = bytes[3] & 0xff;
        int int2 = (bytes[2] & 0xff) << 8;
        int int3 = (bytes[1] & 0xff) << 16;
        int int4 = (bytes[0] & 0xff) << 24;

        return int1 | int2 | int3 | int4;
    }

    /**
     * 将short转为高字节在前，低字节在后的byte数组（大端）
     *
     * @param n short
     * @return byte[]
     */
    public static byte[] shortToByteBig(short n) {
        byte[] b = new byte[2];
        b[1] = (byte) (n & 0xff);
        b[0] = (byte) (n >> 8 & 0xff);
        return b;
    }

    /**
     * 将short转为低字节在前，高字节在后的byte数组(小端)
     *
     * @param n short
     * @return byte[]
     */
    public static byte[] shortToByteLittle(short n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        return b;
    }

    /**
     * 读取小端byte数组为short
     *
     * @param b
     * @return
     */
    public static short byteToShortLittle(byte[] b) {
        return (short) (((b[1] << 8) | b[0] & 0xff));
    }

    /**
     * 读取大端byte数组为short
     *
     * @param b
     * @return
     */
    public static short byteToShortBig(byte[] b) {
        return (short) (((b[0] << 8) | b[1] & 0xff));
    }

    /**
     * long类型转byte[] (大端)
     *
     * @param n
     * @return
     */
    public static byte[] longToBytesBig(long n) {
        byte[] b = new byte[8];
        b[7] = (byte) (n & 0xff);
        b[6] = (byte) (n >> 8 & 0xff);
        b[5] = (byte) (n >> 16 & 0xff);
        b[4] = (byte) (n >> 24 & 0xff);
        b[3] = (byte) (n >> 32 & 0xff);
        b[2] = (byte) (n >> 40 & 0xff);
        b[1] = (byte) (n >> 48 & 0xff);
        b[0] = (byte) (n >> 56 & 0xff);
        return b;
    }

    /**
     * long类型转byte[] (小端)
     *
     * @param n
     * @return
     */
    public static byte[] longToBytesLittle(long n) {
        byte[] b = new byte[8];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        b[4] = (byte) (n >> 32 & 0xff);
        b[5] = (byte) (n >> 40 & 0xff);
        b[6] = (byte) (n >> 48 & 0xff);
        b[7] = (byte) (n >> 56 & 0xff);
        return b;
    }

    /**
     * byte[]转long类型(小端)
     *
     * @param array
     * @return
     */
    public static long bytesToLongLittle(byte[] array) {
        return ((((long) array[0] & 0xff) << 0)
                | (((long) array[1] & 0xff) << 8)
                | (((long) array[2] & 0xff) << 16)
                | (((long) array[3] & 0xff) << 24)
                | (((long) array[4] & 0xff) << 32)
                | (((long) array[5] & 0xff) << 40)
                | (((long) array[6] & 0xff) << 48)
                | (((long) array[7] & 0xff) << 56));
    }

    /**
     * byte[]转long类型(大端)
     *
     * @param array
     * @return
     */
    public static long bytesToLongBig(byte[] array) {
        return ((((long) array[0] & 0xff) << 56)
                | (((long) array[1] & 0xff) << 48)
                | (((long) array[2] & 0xff) << 40)
                | (((long) array[3] & 0xff) << 32)
                | (((long) array[4] & 0xff) << 24)
                | (((long) array[5] & 0xff) << 16)
                | (((long) array[6] & 0xff) << 8)
                | (((long) array[7] & 0xff) << 0));
    }

    public static String toHexString(byte value, String format) {
        if (format.equals("X2")) {
            return String.format("%2s", Integer.toHexString(value & 0xFF).toUpperCase()).replace(' ', '0');
        } else {
            return String.format("%2s", Integer.toHexString(value & 0xFF).toLowerCase()).replace(' ', '0');
        }
    }

    public static byte[] toHexBytes(String hexString) {
        hexString = hexString.replace(" ", "");

        byte[] buf = new byte[hexString.length() / 2];
        char[] span = hexString.toCharArray();

        for (int i = 0; i < hexString.length(); i++) {
            if (i % 2 == 0) {
                buf[i / 2] = (byte) Integer.parseInt(String.copyValueOf(BufferExtensions.Slice(span, i, 2)), 16);
            }
        }

        return buf;
    }

    public static String toHexString(byte[] buffer) {
        StringBuilder sb = new StringBuilder();
        for (byte b : buffer
        ) {
            sb.append(HexExtensions.toHexString(b, "X2"));
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        byte[] buf = HexExtensions.toHexBytes("8D");
        int value = (byte) Integer.parseInt("8D", 16);

        byte v = Integer.valueOf("0D", 16).byteValue();


        System.out.println(buf);
    }
}
