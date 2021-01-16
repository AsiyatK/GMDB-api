package com.galvanize.GMDBapi.unitTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galvanize.GMDBapi.model.Movie;
import com.galvanize.GMDBapi.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    MovieService mockMovieService;

    @Autowired
    private ObjectMapper mapper;

    List<Movie> movies;
    Movie movie;
    List<String> reviews;

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
        movies = new ArrayList<>();
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
                .andExpect(jsonPath("$.description").value("Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity."));
    }

    @Test
    public void getMovieByTitle_returnsFriendlyMessage_ifMovieDoesntExist() throws Exception {

        when(mockMovieService.findByTitle("Sunshine Central")).thenReturn(null);

        mockMvc.perform(get("/api/movies/Sunshine Central"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("Movie with this title does not exist"));
    }

    @Test
    public void rateMovieByTitle_returnsMovieObjectwithRating() throws  Exception{
        //Acceptance Criteria - 3a
        movie.setRating("5 (Your Rating: 5)");
        when(mockMovieService.updateMovieRatings(any())).thenReturn(movie);

        mockMvc.perform(put("/api/movies/rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(movie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value("5 (Your Rating: 5)"));

        //Acceptance Criteria - 3b
        movie.setRating("4 (Your Rating: 3)");
        when(mockMovieService.updateMovieRatings(any())).thenReturn(movie);

        mockMvc.perform(put("/api/movies/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(movie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value("4 (Your Rating: 3)"));
    }

    @Test
    public void submitTextReview_returnsMovieObjectWithReview() throws Exception {
        reviews = new ArrayList<>();
        reviews.add("Really Cool movie!!");
        reviews.add("liked!!");
        movie.setReviews(reviews);
        movie.setRating("5 (Your Rating: 5)");

        when(mockMovieService.updateMovieReviews(any())).thenReturn(movie);

        mockMvc.perform(put("/api/movies/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(movie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviews.length()").value(2))
                .andExpect(jsonPath("$.reviews.[1]").value("liked!!"))
                .andExpect(jsonPath("$.rating").value("5 (Your Rating: 5)"));
    }

    @Test
    public void submitTextReviewWithoutRating_returnsFriendlyMessage() throws Exception {
        reviews = new ArrayList<>();
        reviews.add("Really Cool movie!!");
        reviews.add("liked!!");
        movie.setReviews(reviews);


        when(mockMovieService.updateMovieReviews(any())).thenReturn(movie);

        mockMvc.perform(put("/api/movies/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(movie)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("Please Submit the Ratings along with the Review"))
                ;
    }

}
