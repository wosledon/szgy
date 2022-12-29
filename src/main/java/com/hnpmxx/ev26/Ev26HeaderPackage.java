package com.hnpmxx.ev26;

import com.hnpmxx.ev26.enums.PackageType;
import com.hnpmxx.ev26.formatters.Ev26MessagePackReader;

public class Ev26HeaderPackage {
    public short begin = 0;
    public Ev26Header ev26Header;
    public byte[] bodies;
    public short crc;
    public short end;
    public PackageType packageType;

    public Ev26HeaderPackage(Ev26MessagePackReader reader) {

    }
}
