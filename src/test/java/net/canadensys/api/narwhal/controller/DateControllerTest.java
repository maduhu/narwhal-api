package net.canadensys.api.narwhal.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:test-dispatcher-servlet.xml")
public class DateControllerTest {
	@Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    @Test
    public void testDateJSON() throws Exception {
    	//test GET
        this.mockMvc.perform(get("/dates.json").param("data","2012/12/10"))
        	.andExpect(status().isOk())
        	.andExpect(content().encoding("UTF-8"))
        	.andExpect(content().contentType("application/json")) //this is a bug in Spring 3.2, charset should be avoided  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        	.andExpect(jsonPath("$.data.results.[0].year").value(2012));
        
        //test POST
        this.mockMvc.perform(post("/dates.json").param("data","2012/12/10"))
        	.andExpect(status().isOk())
        	.andExpect(content().encoding("UTF-8"))
        	.andExpect(content().contentType("application/json")) //this is a bug in Spring 3.2, charset should be avoided  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        	.andExpect(jsonPath("$.data.results.[0].day").value(10));
        
        //test with error
        this.mockMvc.perform(get("/dates.json").param("data","12,25"))
        	.andExpect(status().isOk())
        	.andExpect(jsonPath("$.data.results[0].error").exists());
    }
    
    @Test
    public void testDateJSONP() throws Exception {
    	this.mockMvc.perform(get("/dates.json").param("data","2012/12/10").param("callback", "callme"))
	    	.andExpect(status().isOk())
	    	.andExpect(content().encoding("UTF-8"))
	    	.andExpect(content().contentType("application/x-javascript")); //this is a bug in Spring 3.2, charset should be avoided  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }
    
    @Test
    public void testDateXML() throws Exception {
    	//test GET
        this.mockMvc.perform(get("/dates.xml").param("data","2012/12/10"))
        	.andExpect(status().isOk())
        	.andExpect(content().encoding("UTF-8"))
        	.andExpect(content().contentType("application/xml")) //this is a bug in Spring 3.2, charset should be avoided  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        	.andExpect(xpath("/results/result/year").number(2012d));
    }
}
