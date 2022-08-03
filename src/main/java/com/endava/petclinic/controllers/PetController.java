package com.endava.petclinic.controllers;

import com.endava.petclinic.model.Owner;
import com.endava.petclinic.model.Pet;
import com.endava.petclinic.model.Type;
import com.github.javafaker.Faker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class PetController {
    public static Pet generateNewRandomPet() {
        Faker faker = new Faker();

        Pet pet = new Pet();

        ///Ce am facut eu

        /*String dateString ="04-01-2020";
        String dateString1 ="30-07-2022";
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyyy");
        Date date = null;
        Date date1 = null;
        try {
            date = format.parse(dateString);
            date1= format.parse(dateString1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Date between = faker.date().between(date, date1);
        LocalDate localDate = between.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        pet.setName(faker.animal().name().toLowerCase(Locale.ROOT));
        pet.setBirthDate(String.valueOf(localDate.format(dateTimeFormatter2)));
        //pet.setOwner(OwnerController.generateNewRandomOwner());
        pet.setType(TypeController.generateRandomType());

       *//* Owner owner= new Owner();
        pet.setOwner(owner);
        Type type = new Type();
        pet.setType(type);*/

        //Ce am facut la curs

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");


        pet.setName(faker.dog().name());
        pet.setOwner(OwnerController.generateNewRandomOwner());
        pet.setType(new Type(faker.animal().name()));
        pet.setBirthDate(formatter.format(faker.date().birthday(1,10)));

        return pet;
    }

}
