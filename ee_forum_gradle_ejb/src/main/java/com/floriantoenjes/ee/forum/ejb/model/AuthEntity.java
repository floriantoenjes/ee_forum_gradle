package com.floriantoenjes.ee.forum.ejb.model;

public interface AuthEntity {
    boolean isUserAuthorized(User user);
}
