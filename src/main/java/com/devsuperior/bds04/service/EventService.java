package com.devsuperior.bds04.service;

import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.entities.Event;
import com.devsuperior.bds04.repositories.CityRepository;
import com.devsuperior.bds04.repositories.EventRepository;
import com.devsuperior.bds04.service.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CityRepository cityRepository;

    @Transactional(readOnly = true)
    public Page<EventDTO> findAllEvents(Pageable p){
        return eventRepository.findAll(p).map(EventDTO::new);
    }

    @Transactional
    public EventDTO insertEvent(EventDTO eventDTO){
        var city = cityRepository.getReferenceById(eventDTO.getCityId());
        var entity  = new Event(null,eventDTO.getName(),eventDTO.getDate(),eventDTO.getUrl(),city);
        eventRepository.save(entity);
        return new EventDTO(entity);
    }

    @Transactional
    public EventDTO updateEvent(Long id,EventDTO eventDTO){
        if(!eventRepository.existsById(id)){
            throw new NotFoundException("Event not found");
        }
        var entity = eventRepository.getReferenceById(id);
        var city = cityRepository.getReferenceById(eventDTO.getCityId());
        entity.setName(eventDTO.getName());
        entity.setDate(eventDTO.getDate());
        entity.setUrl(eventDTO.getUrl());
        entity.setCity(city);
        return new EventDTO(entity);
    }
    public void deleteEvent(Long id) {
        if(!eventRepository.existsById(id)) {
            throw new NotFoundException("Event not found");
        }
        eventRepository.deleteById(id);
    }
}
