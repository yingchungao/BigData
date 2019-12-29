package com.geek.dmp.paartner.analysis;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Hecming {
    public static void main(String[] args) {

        AtomicInteger i = new AtomicInteger(1);
        System.out.println(i.getAndIncrement());
    }
}
