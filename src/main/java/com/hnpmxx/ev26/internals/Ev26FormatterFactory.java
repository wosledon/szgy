package com.hnpmxx.ev26.internals;

import com.hnpmxx.ev26.MessageBody.Ev26_0x01;
import com.hnpmxx.ev26.extensions.AssemblyExtensions;
import com.hnpmxx.ev26.interfaces.IEv26FormatterFactory;
import com.hnpmxx.ev26.interfaces.IEv26MessagePackageFormatter;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ev26FormatterFactory implements IEv26FormatterFactory {
    public Map<String, Object> formatterDict;

    public Ev26FormatterFactory() {
        formatterDict = new HashMap<>();
        init();
    }

    private void init() {
        List<Class> types = AssemblyExtensions.getAllClassByPath("com.hnpmxx.ev26");

        for (Class type : types) {
            Class[] implTypes = type.getInterfaces();

            if (implTypes != null && implTypes.length > 1) {
                Class firstType = Arrays.stream(implTypes).filter(t -> t.getTypeName().equals(IEv26MessagePackageFormatter.class.getTypeName())).findFirst().get();

                Type genericImplType = type.getGenericSuperclass();
                if (genericImplType != null) {
                    if (!formatterDict.containsKey(genericImplType.getTypeName())) {
                        try {
                            formatterDict.put(genericImplType.getTypeName(), type.newInstance());
                        } catch (InstantiationException e) {
                            throw new RuntimeException(e);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            }
        }
    }

    public Object getValue(byte msgId) {
        return this.formatterDict.getOrDefault(msgId, null);
    }

    @Override
    public IEv26FormatterFactory setMap() {
        return null;
    }

    @Override
    public void register(Class c) {

    }


    public static void main(String[] args) {
        Ev26FormatterFactory factory = new Ev26FormatterFactory();
        for (String t : factory.formatterDict.keySet()
        ) {
            System.out.println(t);
        }

        Ev26_0x01 ev260x01 = new Ev26_0x01();
    }
}
