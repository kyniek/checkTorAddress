package kyn.tasks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kyn.tasks.repository.TorNodesRepository;

@Service
public class CheckNodeService 
{
	@Autowired
	TorNodesRepository repo;
	
	public boolean check(String ip)
	{
		return repo.getNodesCache().contains(ip);
	}

}
