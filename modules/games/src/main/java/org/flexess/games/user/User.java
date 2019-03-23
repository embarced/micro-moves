package org.flexess.games.user;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * User object used for authenticated player info.
 */
public class User {

    private String userid;

    private String name;

    private String roles;

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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean hasRole(String role) {
        if (roles != null && !roles.equals("")) {
            String[] allRoles = roles.split(",");
            Set<String> roleSet = new HashSet<String>(Arrays.asList(allRoles));
            return roleSet.contains(role);
        } else {
            return false;
        }

    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(name);

        s.append(" (userid: ");
        s.append(userid);
        s.append(")");

        return s.toString();
    }
}
