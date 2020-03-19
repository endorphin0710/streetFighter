package com.july.streetfighter;

public class User {

    private String id;
    /**
     * A : 정상
     */
    private String status;
    /**
     * 0 : 일반
     * 1 : 사장님
     */
    private int auth;

    public User() {
    }

    public User(String id, String status, int auth) {
        this.id = id;
        this.status = status;
        this.auth = auth;
    }

    ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

}
