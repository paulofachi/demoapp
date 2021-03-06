/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demoapp.resource;

import com.demoapp.model.Movie;
import com.demoapp.service.MovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author oem
 */
@RestController
@RequestMapping(value="/api/movie")
@Api(value="API REST Movie")
@CrossOrigin(origins="*")
public class MovieResource {
    
    @Autowired
    MovieService movieService;
    
    @GetMapping("/list")
    @ApiOperation(value="Retorna a listagem completa de Filmes.")
    public List<Movie> movieList() {
        return movieService.listAll();
    }
    
    @GetMapping("/{id}")
    @ApiOperation(value="Consulta um Filme por id.")
    public Movie findById(@PathVariable(value="id") Integer id) {
        return movieService.findById(id);
    }
    
    @PostMapping("/insert")
    @ApiOperation(value="Cria um novo Filme.")
    public Movie insert(@RequestBody Movie movie) {
        return movieService.save(movie);
    }
    
    @PutMapping("/update")
    @ApiOperation(value="Atualiza o Filme.")
    public Movie update(@RequestBody Movie movie) {
        return movieService.save(movie);
    }
    
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value="Deleta o Filme.")
    public void delete(@PathVariable(value="id") Integer id) {
        Movie movie = this.findById(id);
        if (movie != null) {
            movieService.delete(movie);
        }
    }
    
    @PostMapping("/import")
    @ApiOperation(value="Importa arquivo csv com a listagem de Filmes")
    public String importCsvFile(@RequestParam("file") MultipartFile file ) throws Exception {
        try {
            movieService.importCsv(file);
        } catch (Exception e) {
            throw new Exception("Erro ao importar CSV.", e);
        }
        return "Arquivo importado com sucesso.";
    }
    
    @GetMapping("/getProducersIntervals")
    @ApiOperation(value="Retorna o Produtor com maior intervalo de premia????o.")
    public Map getProducersIntervals() {
        return movieService.getProducersIntervals();
    }
    
}