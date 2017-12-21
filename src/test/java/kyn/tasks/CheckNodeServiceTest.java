package kyn.tasks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import kyn.tasks.repository.TorNodesRepository;
import kyn.tasks.service.CheckNodeService;
import kyn.tasks.util.Constants;

public class CheckNodeServiceTest
{
	@InjectMocks
	private CheckNodeService srv;
	
	@Mock
	TorNodesRepository repo;
	
	
	@Before
	public void init()
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testCheck()
	{
		when(repo.getNodesCache()).thenReturn(Constants.TOR_EXIT_IPS);
		
		assertTrue(srv.check("0.247.72.201"));		
		assertFalse(srv.check("0.247.72.208"));
	}

}
