package com.floriantoenjes.ee.forum.ejb;

public abstract class EntityBean<T> {

    public abstract T find(Long id);

}
