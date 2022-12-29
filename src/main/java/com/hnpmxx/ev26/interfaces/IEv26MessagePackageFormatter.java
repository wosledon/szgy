package com.hnpmxx.ev26.interfaces;

import com.hnpmxx.ev26.exceptions.Ev26Exception;
import com.hnpmxx.ev26.formatters.Ev26MessagePackReader;
import com.hnpmxx.ev26.formatters.Ev26MessagePackWriter;

public interface IEv26MessagePackageFormatter<T> extends IEv26Formatter {
    /**
     * 序列化
     *
     * @param writer 写入器
     * @param value  数据包
     */
    void serialize(Ev26MessagePackWriter writer, T value) throws Ev26Exception;

    /**
     * 反序列化
     *
     * @param reader         阅读器
     * @param isNeedStartEnd 是否需要收尾标识
     * @return 数据包
     */
    T deserialize(Ev26MessagePackReader reader, boolean isNeedStartEnd) throws Exception;
}
