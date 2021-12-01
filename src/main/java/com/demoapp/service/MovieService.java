/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demoapp.service;

import com.demoapp.model.Movie;
import com.demoapp.model.Producer;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demoapp.repository.MovieRepository;
import com.demoapp.repository.ProducerRepository;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author oem
 */
@Service
public class MovieService {
    @Autowired
    MovieRepository movieRepository;
    
    @Autowired
    ProducerRepository producerRepository;
    
    @Autowired
    EntityManager entityManager;
    
    @Autowired
    ProducerService producerService;
    
    public List<Movie> listAll() {
        return movieRepository.findAll();
    }
    
    public Movie findById(Integer id) {
        return movieRepository.findById(id);
    }
    
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }
    
    public void delete(Movie movie) {
        movieRepository.delete(movie);
    }
    
    public void importCsv(MultipartFile file) throws Exception {
        List<String> linhas = new ArrayList<>();
        try {
            if (file == null || file.isEmpty()) {
                linhas = loadContentFromFile();
            } else {
                linhas = getFileContent(file.getInputStream());
            }
            importMovies(linhas);
        } catch (IOException ex) {
            throw new Exception("Falha ao tentar importar arquivo CSV.");
        }
    }
    
    private List<String> loadContentFromFile() throws Exception {
        List<String> linhas = new ArrayList<>();
        try {
            linhas = getFileContent(new FileInputStream("/home/paulofachi/movielist.csv"));
        } catch (IOException ex) {
            throw new Exception("Falha ao tentar importar arquivo CSV.");
        }
        return linhas;
    }
    
    private List<String> getFileContent(InputStream inputStream) {
        List<String> linhas = new ArrayList<>();
        Scanner reader = new Scanner(inputStream);
        while (reader.hasNext()) {
            linhas.add(reader.nextLine());
        }
        return linhas;
    }
    
    public void importMovies(List<String> linhasConteudo) throws Exception {
        clearDataBase();
        generateMovies(linhasConteudo);
    }
    
    private void clearDataBase() {
        movieRepository.deleteAll();
        producerRepository.deleteAll();
    }
    
    public void generateMovies(List<String> linhas) throws Exception {
        for (int i = 1; i < linhas.size(); i++) {
            String linha = linhas.get(i);
            if (linha != null) {
                String[] colunas = linha.split(";");
                if (colunas != null && colunas.length > 0) {
                    Movie newMovie = getNewMovie(colunas);
                    movieRepository.save(newMovie);
                }
            }
        }
    }
    
    public Movie getNewMovie(String[] colunas) {
        Movie movie = new Movie();
        for (int i = 0; i < colunas.length; i++) {
            switch (i) {
                case 0:
                    movie.setYear(Integer.parseInt(colunas[i]));
                    break;
                case 1:
                    movie.setTitle(colunas[i]);
                    break;
                case 2:
                    movie.setStudios(colunas[i]);
                    break;
                case 3:
                    movie.setProducers(getProducersList(colunas[i]));
                    break;
                case 4:
                    movie.setWinner(colunas[i] != null && colunas[i].equals("yes"));
                    break;
                default:
                    break;
            }
        }
        return movie;
    }
    
    public List<Producer> getProducersList(String producers) {
        List<Producer> producersList = new ArrayList<>();
        String[] prodList = producers.split(",");
        for (String prod : prodList) {
            if (prod.toLowerCase().contains(" and ")) {
                String[] subProdList = prod.toLowerCase().split(" and ");
                for (String subProducer : subProdList) {
                    if (subProducer != null && !subProducer.isEmpty()) {
                        producersList.add(loadProducer(subProducer));
                    }
                }
            } else {
                if (prod != null && !prod.isEmpty()) {
                    producersList.add(loadProducer(prod));
                }
            }
        }
        return producersList;
    }
    
    public Producer loadProducer(String name) {
        Query query = entityManager.createQuery("select p from Producer p where p.name = ?1");
        query.setParameter(1, name.trim());
        Producer p = null;
        try {
            p = (Producer) query.getSingleResult();
        } catch (Exception e) { }
        if (p == null) {
            p = new Producer();
            p.setName(name.trim());
            p = producerService.save(p);
        }
        return p;
    }
    
    //
    public Map getProducersIntervals() {
        Map rtno = new HashMap<>();
        List<Map> producersWinners = getProducersWinners();
        rtno.put("min", getProducerLowerInterval(producersWinners));
        rtno.put("max", getProducerGreatestInterval(producersWinners));
        return rtno;
    }
    
    public List<Map> getProducersWinners() {
        List<Map> lista = new ArrayList<>();
        StringBuffer sql = new StringBuffer();
        sql.append("select p.id, p.name, m.year as first_year, next_win.year as second_year, (next_win.year - m.year) as intervalo ");
        sql.append("from movie m ");
        sql.append("inner join movie_producer mp on mp.id_movie = m.id ");
        sql.append("inner join producer p on p.id = mp.id_producer ");
        sql.append("inner join ( ");
        sql.append("    select m2.year,mp2.id_producer ");
        sql.append("    from movie m2 ");
        sql.append("    inner join movie_producer mp2 on mp2.id_movie = m2.id ");
        sql.append("    where m2.winner = true ");
        sql.append("    order by m2.year ");
        sql.append(") next_win on next_win.id_producer = p.id and next_win.year > m.year ");
        sql.append("where m.winner = true ");
        sql.append("    and ( ");
        sql.append("        select count(*)  ");
        sql.append("        from movie inner_movie  ");
        sql.append("        inner join movie_producer inner_mp on inner_mp.id_movie = inner_movie.id and inner_mp.id_producer = p.id ");
        sql.append("        where inner_movie.winner = true  ");
        sql.append("    ) > 1 ");
        sql.append("    and not exists ( ");
        sql.append("        select m3.id ");
        sql.append("        from movie m3 ");
        sql.append("        inner join movie_producer mp3 on mp3.id_movie = m3.id and mp3.id_producer = p.id ");
        sql.append("        where m3.winner = true ");
        sql.append("            and m3.year between m.year+1 and next_win.year-1 ");
        sql.append("    ) ");
        sql.append("order by p.id, m.year ");
        
        Query query = entityManager.createNativeQuery(sql.toString());
        final List<Object[]> result = query.getResultList();
        if (result != null) {
            for (Object[] obj : result) {
                Map<String, Object> mapRtno = new HashMap<>();
                mapRtno.put("producer", obj[1].toString());
                mapRtno.put("interval", Integer.parseInt(obj[4].toString()));
                mapRtno.put("previousWin", Integer.parseInt(obj[2].toString()));
                mapRtno.put("followingWin", Integer.parseInt(obj[3].toString()));
                lista.add(mapRtno);
            }
        }
        return lista;
    }
    
    public List<Map> getProducerLowerInterval(List<Map> producersWinners) {
        List<Map> returnList = new ArrayList<>();
        if (producersWinners != null && !producersWinners.isEmpty()) {
            returnList = filterList(producersWinners, "MIN");
        }
        return returnList;
    }
    
    public List<Map> getProducerGreatestInterval(List<Map> producersWinners) {
        List<Map> returnList = new ArrayList<>();
        if (producersWinners != null && !producersWinners.isEmpty()) {
            returnList = filterList(producersWinners, "MAX");
        }
        return returnList;
    }
    
    public List<Map> filterList(List<Map> producersWinners, String intervalType) {
        List<Map> returnList = new ArrayList<>();
        Integer parameterInterval = Integer.parseInt(producersWinners.get(0).get("interval").toString());
        for (Map resultProducer : producersWinners) {
            Integer producerInterval = Integer.parseInt(resultProducer.get("interval").toString());
            if (compareInterval(producerInterval, parameterInterval, intervalType)) {
                returnList.clear();
                parameterInterval = producerInterval;
                returnList.add(resultProducer);
            } else if (Objects.equals(producerInterval, parameterInterval)) {
                returnList.add(resultProducer);
            }
        }
        return returnList;
    }
    
    public boolean compareInterval(Integer producerInterval, Integer paramInterval, String intervalType) {
        return intervalType.equals("MAX") ? producerInterval > paramInterval : producerInterval < paramInterval;
    }
    
}
