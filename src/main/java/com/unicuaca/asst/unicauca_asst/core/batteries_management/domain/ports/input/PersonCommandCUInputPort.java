package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.Person;

public interface PersonCommandCUInputPort {

    Person createPerson(Person person);  
}
