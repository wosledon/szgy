package com.hnpmxx.ev26.internals;

import com.hnpmxx.ev26.Ev26Bodies;
import com.hnpmxx.ev26.exceptions.Ev26ErrorCode;
import com.hnpmxx.ev26.exceptions.Ev26Exception;
import com.hnpmxx.ev26.extensions.AssemblyExtensions;
import com.hnpmxx.ev26.interfaces.IEv26MsgIdFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Ev26MsgIdFactory implements IEv26MsgIdFactory {

    public Map<Byte, Object> map;

    public Ev26MsgIdFactory() throws Ev26Exception, InstantiationException, IllegalAccessException {
        map = new HashMap<>();

        initMap();
    }

    private void initMap() throws InstantiationException, IllegalAccessException, Ev26Exception {
        List<Class> types = AssemblyExtensions.getAllClassByPath("com.hnpmxx.ev26").stream().filter(x -> x.getSuperclass() != null && x.getSuperclass().getTypeName().equals(Ev26Bodies.class.getTypeName())).collect(Collectors.toList());
        for (Class type : types) {
//            System.out.println(type.getSuperclass());
            Object instance = type.newInstance();
            byte msgId = 0;
            try {
                Field[] fields = type.getDeclaredFields();
                Field field = Arrays.stream(fields).filter(x -> x.getName().equals("msgId")).findFirst().get();
                msgId = (byte) field.get(instance);
            } catch (Exception e) {
                continue;
            }

            if (map.containsKey(msgId)) {
                throw new Ev26Exception(Ev26ErrorCode.initException,
                        String.format("%s %d, 已存在相同的msgId", type.getName(), msgId));
            } else {
                map.put(msgId, instance);
            }
        }
    }

    @Override
    public void register(Class c) {

    }

    @Override
    public Object getValue(byte msgId) {
        return map.getOrDefault(msgId, null);
    }

    @Override
    public IEv26MsgIdFactory setMap() {
        return null;
    }

    public static void main(String[] args) throws Ev26Exception, InstantiationException, IllegalAccessException {
        Ev26MsgIdFactory factory = new Ev26MsgIdFactory();

        System.out.println(1);
    }
}
