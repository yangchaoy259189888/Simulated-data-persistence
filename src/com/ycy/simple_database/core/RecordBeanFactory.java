package com.ycy.simple_database.core;

import com.mec.util.PackageScanner;
import com.ycy.simple_database.annotation.Table;

import java.util.HashMap;
import java.util.Map;

public class RecordBeanFactory {
//    一个recordMap对应一个RecordDefinition
    private static final Map<String, RecordDefinition<?>> recordMap = new HashMap<>();

    public static void scanRecord(String packageName) {
        new PackageScanner() {
            @Override
            public void dealClass(Class<?> klass) {
                if (!klass.isAnnotationPresent(Table.class)) {
                    return;
                }
                RecordDefinition<?> record = new RecordDefinition<>();
                try {
                    record.setKlass(klass);
                    recordMap.put(klass.getName(), record);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.packageScan(packageName);
    }

    public static RecordDefinition<?> getRecord(Class<?> klass) {
        return getRecord(klass.getName());
    }

    private static RecordDefinition<?> getRecord(String className) {
        return recordMap.get(className);
    }
}
