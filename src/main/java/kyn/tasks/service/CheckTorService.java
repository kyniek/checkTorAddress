package kyn.tasks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kyn.tasks.repository.TorExitNodesRepository;

@Service
public class CheckTorService 
{
	@Autowired
	TorExitNodesRepository repo;
	
	public boolean check(String ip)
	{
		return repo.getNodesCache().contains(ip);
	}

}
