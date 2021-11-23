package fr.polytech.authentification.authentifDemo.Model;

import java.util.Date;

public class Token {

    String key;
    Long time;

    public Token() {
        this.key = generate_key(25);
        this.time = new Date().getTime();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String generate_key(int n) {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder s = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index = (int)(str.length() * Math.random());
            s.append(str.charAt(index));
        }
        return s.toString();
    }

    public Boolean hasExpired() {
        Date T1 = new Date();
        return (T1.getTime() - this.time) > 300000;
    }


}
