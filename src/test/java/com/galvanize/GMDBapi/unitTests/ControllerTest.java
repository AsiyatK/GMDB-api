package com.galvanize.GMDBapi.unitTests;

import com.galvanize.GMDBapi.model.Movie;
import com.galvanize.GMDBapi.repository.MovieRepository;
import com.galvanize.GMDBapi.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    MovieService mockMovieService;

    List<Movie> movies;
    Movie movie;

    @BeforeEach
    public void setup(){
        movie = new Movie();
        movie.setTitle("The Avengers");
        movie.setDirector("Joss Whedon");
        movie.setActors("Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth");
        movie.setRelease("2012");
        movie.setDescription("Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity.");
        //movie.setRating(null);
    }

    @Test
    public void getAllMovies_returnsMovieList() throws Exception {
        movies = new ArrayList<Movie>();
        movies.add(movie);

        when(mockMovieService.getAllMovies()).thenReturn(movies);

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists()) //$ represents json to be return
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[0].title").value("The Avengers"));
    }

    @Test
    public void getMovieByTitle_returnsMovie() throws Exception {
        when(mockMovieService.findByTitle("The Avengers")).thenReturn(movie);

        mockMvc.perform(get("/api/movies/The Avengers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Avengers"))
                .andExpect(jsonPath("$.director").value("Joss Whedon"))
                .andExpect(jsonPath("$.actors").value("Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth"))
                .andExpect(jsonPath("$.release").value("2012"))
                .andExpect(jsonPath("$.description").value("Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity."))
                ;
    }
}
