package kyn.tasks;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import kyn.tasks.controller.CheckNodeController;
import kyn.tasks.service.CheckNodeService;

@RunWith(SpringRunner.class)
@WebMvcTest(CheckNodeController.class)
public class MVCCheckNodeControllerTest
{
	@Autowired
	private MockMvc mvc;

	@MockBean
	private CheckNodeService serv;

	@Before
	public void init()
	{
		given(this.serv.check("0.247.72.201")).willReturn(true);

		given(this.serv.check("0.247.72.200")).willReturn(false);
	}

	@Test
	public void testCheckOK() throws Exception
	{

		this.mvc.perform(get("/0.247.72.201").accept(MediaType.TEXT_PLAIN)).andExpect(status().isOk())
				.andExpect(content().string("IP 0.247.72.201 is present"));

	}

	@Test
	public void testCheckNotFound() throws Exception
	{

		this.mvc.perform(get("/0.247.72.200").accept(MediaType.TEXT_PLAIN)).andExpect(status().isNotFound())
				.andExpect(content().string(""));
	}
	
	
	@Test
	public void testCheckHeadOk() throws Exception
	{

		this.mvc.perform(head("/0.247.72.201").accept(MediaType.TEXT_PLAIN)).andExpect(status().isOk())
				.andExpect(content().string(""));
	}
	
	@Test
	public void testCheckHeadNotFound() throws Exception
	{

		this.mvc.perform(head("/0.247.72.200").accept(MediaType.TEXT_PLAIN)).andExpect(status().isNotFound())
				.andExpect(content().string(""));
	}	

}
