package com.galvanize.GMDBapi.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galvanize.GMDBapi.model.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void getAllHeroesTest() throws Exception{
        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(7))
                .andExpect(jsonPath("$.[0].title").value("The Avengers"))
                .andExpect(jsonPath("$.[1].title").value("Superman Returns"))
                .andExpect(jsonPath("$.[2].title").value("Steel"));
    }

    @Test
    public void getMovieByTitleTest() throws Exception{
        mockMvc.perform(get("/api/movies/The Avengers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Avengers"))
                .andExpect(jsonPath("$.director").value("Joss Whedon"))
                .andExpect(jsonPath("$.actors").value("Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth"))
                .andExpect(jsonPath("$.release").value("2012"))
                .andExpect(jsonPath("$.description").value("Earths mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity."));

    }

    @Test
    public void getMovieByIncorrectTitleTest() throws Exception{
        mockMvc.perform(get("/api/movies/Avengers"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("Movie with this title does not exist"));

    }

    @Test
    public void rateMovieByTitleTest() throws Exception {
        Movie avengerReview1 = new Movie();
        avengerReview1.setTitle("The Avengers");
        avengerReview1.setRating("5");

        mockMvc.perform(put("/api/movies/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(avengerReview1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value("5"));

        Movie avengerReview2 = new Movie();
        avengerReview2.setTitle("The Avengers");
        avengerReview2.setRating("3");

        mockMvc.perform(put("/api/movies/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(avengerReview2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value("4"));

    }


    @Test
    public void submitReviewAndRatingTest() throws Exception{
        List<String> reviews = new ArrayList<>();

        Movie superManReview1 = new Movie();
        reviews.add("liked!!");
        superManReview1.setTitle("Superman Returns");
        superManReview1.setReviews(reviews);
        superManReview1.setRating("5");

        mockMvc.perform(put("/api/movies/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(superManReview1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviews.length()").value(1))
                .andExpect(jsonPath("$.reviews.[0]").value("liked!!"))
                .andExpect(jsonPath("$.rating").value("5"));

        reviews.clear();

        Movie superManReview2 = new Movie();
        reviews.add("Really Cool movie!!");
        superManReview2.setTitle("Superman Returns");
        superManReview2.setReviews(reviews);
        superManReview2.setRating("3");

        mockMvc.perform(put("/api/movies/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(superManReview2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviews.length()").value(2))
                .andExpect(jsonPath("$.reviews.[0]").value("liked!!"))
                .andExpect(jsonPath("$.reviews.[1]").value("Really Cool movie!!"))
                .andExpect(jsonPath("$.rating").value("4"));

    }

}
