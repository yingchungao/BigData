package domain;

import java.util.HashMap;

public class HashMapTest {
    public static void main(String[] args) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("1", "2");
        hashMap.put("1", "4");

        System.out.println("刘明".hashCode());
    }
}
