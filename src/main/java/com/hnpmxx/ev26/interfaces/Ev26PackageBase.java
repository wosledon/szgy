package com.hnpmxx.ev26.interfaces;

import com.hnpmxx.ev26.Ev26Bodies;
import com.hnpmxx.ev26.Ev26Header;
import com.hnpmxx.ev26.enums.PackageType;

public abstract class Ev26PackageBase {
    public short begin;
    public Ev26Header ev26Header;
    public Ev26Bodies ev26Bodies;
    public PackageType packageType = PackageType.Type1;
    public short end = (short) ((0x0D << 8) + 0x0A);
}


