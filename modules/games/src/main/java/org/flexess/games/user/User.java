package org.flexess.games.user;

/**
 * User object used for authenticated player info.
 */
public class User {

    private String userid;

    private String name;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " (userid: " + userid + ")";
    }
}
