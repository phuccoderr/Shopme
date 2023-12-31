package com.shopme.review;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.common.entity.Review;
import com.shopme.review.vote.ReviewVoteRepository;
import com.shopme.review.vote.VoteResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
public class ReviewVoteRestControllerTests {
    @Autowired private ReviewVoteRepository repo;
    @Autowired private ReviewRepository reviewRepo;
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper mapper;

    @Test
    public void testVoteNotLogin() throws Exception {
        String requestURL = "/vote_review/1/up";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(requestURL).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        VoteResult voteResult = mapper.readValue(json, VoteResult.class);
        AssertionErrors.assertFalse("error",voteResult.isSuccessful());
        Assertions.assertThat(voteResult.getMessage()).contains("Bạn cần phải đăng nhập");
    }

    @Test
    @WithMockUser(username = "phuctapcode1604@gmail.com",password = "123")
    public void testVoteNonExistReview() throws Exception {
        String requestURL = "/vote_review/123/up";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(requestURL).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        VoteResult voteResult = mapper.readValue(json, VoteResult.class);
        AssertionErrors.assertFalse("error",voteResult.isSuccessful());
        Assertions.assertThat(voteResult.getMessage()).contains("no longer exists");
    }

    @Test
    @WithMockUser(username = "gigabyelevi@gmail.com",password = "123")
    public void testVoteUP() throws Exception {
        Integer reviewId = 6;
        String requestURL = "/vote_review/"+ reviewId +"/up";

        Review review = reviewRepo.findById(reviewId).get();
        int voteCountBefore = review.getVotes();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(requestURL).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        VoteResult voteResult = mapper.readValue(json, VoteResult.class);
        org.junit.jupiter.api.Assertions.assertTrue(voteResult.isSuccessful());
        Assertions.assertThat(voteResult.getMessage()).contains("successfully voted up");

        int voteCountAfter = voteResult.getVoteCount();

        org.junit.jupiter.api.Assertions.assertEquals(voteCountBefore + 1,voteCountAfter);
    }

}
