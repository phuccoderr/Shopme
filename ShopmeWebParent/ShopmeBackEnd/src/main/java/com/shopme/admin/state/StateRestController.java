package com.shopme.admin.state;

import ch.qos.logback.core.model.Model;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import com.shopme.common.entity.StateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StateRestController {
    @Autowired private StateRepository repo;

    @GetMapping("/states/list_by_country/{id}")
    public List<StateDTO> listByCountry(@PathVariable("id") Integer id,
                                        Model model) {

        List<State> listStates = repo.findByCountryOrderByNameAsc(new Country(id));
        List<StateDTO> result = new ArrayList<>();

        for (State state : listStates) {
            result.add(new StateDTO(state.getId(),state.getName()));
        }
        return result;
    }

    @PostMapping("/states/save")
    public String saveStateByCountry(@RequestBody State state) {
        State savedState = repo.save(state);
        return String.valueOf(savedState.getId());
    }

    @DeleteMapping("/states/delete/{id}")
    public void deleteState(@PathVariable("id")Integer id) {
        repo.deleteById(id);
    }
}
