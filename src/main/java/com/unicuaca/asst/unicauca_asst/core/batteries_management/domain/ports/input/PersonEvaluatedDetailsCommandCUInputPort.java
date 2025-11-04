package com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.ports.input;

import com.unicuaca.asst.unicauca_asst.core.batteries_management.domain.models.PersonEvaluatedDetails;

public interface PersonEvaluatedDetailsCommandCUInputPort {

    PersonEvaluatedDetails createPersonEvaluatedDetails(PersonEvaluatedDetails personEvaluatedDetails);

}
