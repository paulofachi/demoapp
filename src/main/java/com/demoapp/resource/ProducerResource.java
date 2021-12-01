/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demoapp.resource;

import com.demoapp.model.Producer;
import com.demoapp.service.ProducerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/producer")
@Api(value="API REST Producer")
@CrossOrigin(origins="*")
public class ProducerResource {
    
    @Autowired
    ProducerService producerService;
    
    @GetMapping("/list")
    @ApiOperation(value="Retorna a listagem completa de Produtores.")
    public List<Producer> producerList() {
        return producerService.listAll();
    }
    
    @GetMapping("/{id}")
    @ApiOperation(value="Consulta um Produtor por id.")
    public Producer findById(@PathVariable(value="id") Integer id) {
        return producerService.findById(id);
    }
    
    @PostMapping("/insert")
    @ApiOperation(value="Cria um novo Produtor.")
    public Producer insert(@RequestBody Producer producer) {
        return producerService.save(producer);
    }
    
    @PutMapping("/update")
    @ApiOperation(value="Atualiza o Produtor.")
    public Producer update(@RequestBody Producer producer) {
        return producerService.save(producer);
    }
    
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value="Deleta o Produtor.")
    public void delete(@PathVariable(value="id") Integer id) {
        Producer producer = this.findById(id);
        if (producer != null) {
            producerService.delete(producer);
        }
    }
       
}