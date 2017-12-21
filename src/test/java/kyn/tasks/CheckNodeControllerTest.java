package kyn.tasks;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import kyn.tasks.controller.CheckNodeController;
import kyn.tasks.repository.TorNodesRepository;
import kyn.tasks.service.CheckNodeService;
import kyn.tasks.util.Constants;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CheckNodeControllerTest
{
	@Autowired
	CheckNodeService srv;

	@Autowired
	CheckNodeController checkCtrl;

	@MockBean
	TorNodesRepository repo;

	@Before
	public void init()
	{
		when(repo.getNodesCache()).thenReturn(Constants.TOR_EXIT_IPS);
	}

	@Test
	public void testCheckNodeController()
	{

		ResponseEntity<String> respOk = checkCtrl.check("0.247.72.201");
		ResponseEntity<String> respNotFound = checkCtrl.check("0.247.72.208");

		assertEquals(respOk.getStatusCode(), HttpStatus.OK);
		assertEquals(respOk.getBody(), "IP 0.247.72.201 is present");

		assertEquals(respNotFound.getStatusCode(), HttpStatus.NOT_FOUND);
		assertEquals(respNotFound.getBody(), "");
	}

}
