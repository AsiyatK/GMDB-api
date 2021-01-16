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
}
