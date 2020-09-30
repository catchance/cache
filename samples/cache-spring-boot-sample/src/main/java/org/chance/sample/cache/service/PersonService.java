package org.chance.sample.cache.service;


import org.chance.sample.cache.entity.Person;

public interface PersonService {
    Person save(Person person);

    void remove(Long id);

    void removeAll();

    Person findOne(Person person);
}
