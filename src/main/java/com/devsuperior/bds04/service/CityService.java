package com.devsuperior.bds04.service;

import com.devsuperior.bds04.dto.CityDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.repositories.CityRepository;
import com.devsuperior.bds04.service.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    @Transactional(readOnly = true)
    public List<CityDTO> findAllCities(){
        return cityRepository.findAll(Sort.by("name"))
                .stream()
                .map(CityDTO::new).toList();
    }

    @Transactional
    public CityDTO insertCity(CityDTO cityDTO){
        var entity  = new City(null,cityDTO.getName());
        cityRepository.save(entity);
        return new CityDTO(entity);
    }

    public void deleteCity(Long id) {
        if(!cityRepository.existsById(id)) {
            throw new NotFoundException("City not found");
        }
        cityRepository.deleteById(id);
    }
}
