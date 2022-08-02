package com.endava.petclinic.controllers;

import com.endava.petclinic.model.Owner;
import com.github.javafaker.Faker;

public class OwnerController {

    public static Owner generateNewRandomOwner() {
        Faker faker = new Faker();

        Owner owner = new Owner();
        owner.setFirstName( faker.name().firstName());
        owner.setLastName( faker.name().lastName());
        owner.setAddress(faker.address().streetAddress());
        owner.setCity(faker.address().city());
        owner.setTelephone(faker.number().digits(10));

        return owner;
    }
}

