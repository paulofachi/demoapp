/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demoapp.service;

import com.demoapp.model.Producer;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demoapp.repository.ProducerRepository;
import javax.persistence.EntityManager;

@Service
public class ProducerService {
    @Autowired
    ProducerRepository producerRepository;
    
    @Autowired
    EntityManager entityManager;
    
    public List<Producer> listAll() {
        return producerRepository.findAll();
    }
    
    public Producer findById(Integer id) {
        return producerRepository.findById(id);
    }
    
    public Producer save(Producer producer) {
        return producerRepository.save(producer);
    }
    
    public void delete(Producer producer) {
        producerRepository.delete(producer);
    }
    
}
