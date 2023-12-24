package com.shopme.admin.report;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class ReportRestControllerTests {

    @Autowired private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user1",password = "pass1",authorities = {"Admin"})
    public void testGetReportDataLast7Days() throws Exception {
        String requestUrl = "/report/sales_by_date/last_7_days";

        mockMvc.perform(get(requestUrl)).andExpect(status().isOk()).andDo(print());

    }

    @Test
    @WithMockUser(username = "user1",password = "pass1",authorities = {"Admin"})
    public void testGetReportDataLast6Months() throws Exception {
        String requestUrl = "/report/sales_by_date/last_6_months";

        mockMvc.perform(get(requestUrl)).andExpect(status().isOk()).andDo(print());

    }

    @Test
    @WithMockUser(username = "user1",password = "pass1",authorities = {"Admin"})
    public void testGetReportDataLastDataRange() throws Exception {
        String startDate = "2023-12-1";
        String endDate = "2023-12-19";
        String requestUrl = "/report/sales_by_date/" + startDate + "/" + endDate;

        mockMvc.perform(get(requestUrl)).andExpect(status().isOk()).andDo(print());

    }

    @Test
    @WithMockUser(username = "user1",password = "pass1",authorities = {"Admin"})
    public void testGetReportDataByCategory() throws Exception {
        String startDate = "2023-12-1";
        String endDate = "2023-12-19";
        String requestUrl = "/report/category/last_7_days";

        mockMvc.perform(get(requestUrl)).andExpect(status().isOk()).andDo(print());

    }

    @Test
    @WithMockUser(username = "user1",password = "pass1",authorities = {"Admin"})
    public void testGetReportDataByProduct() throws Exception {
        String startDate = "2023-12-1";
        String endDate = "2023-12-19";
        String requestUrl = "/report/product/last_7_days";

        mockMvc.perform(get(requestUrl)).andExpect(status().isOk()).andDo(print());

    }
}
