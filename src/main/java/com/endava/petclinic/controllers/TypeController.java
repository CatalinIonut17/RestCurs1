package com.endava.petclinic.controllers;

import com.endava.petclinic.model.Type;
import com.github.javafaker.Faker;

import java.util.Locale;

public class TypeController {

    public static Type generateRandomType(){
        Faker faker = new Faker();

        Type type = new Type();

        type.setName(faker.animal().name().toLowerCase(Locale.ROOT));
        return type;
    }
}
