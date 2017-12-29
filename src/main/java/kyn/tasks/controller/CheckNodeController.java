package kyn.tasks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import kyn.tasks.service.CheckNodeService;


@RestController
public class CheckNodeController {

	
	@Value("${tor.list}")
	private String torList;
	
	@Autowired
	private CheckNodeService serv;
	/**
	 * Regular expression - ":.+" - is needed because part of parameter after "." is cut by default - we want to avoid this 
	 * @param ip
	 * @return
	 */
	@RequestMapping(value = "/{ip:.+}", method = RequestMethod.GET)
	public ResponseEntity<String> check(@PathVariable("ip") String ip) 
	{
		boolean isPresent = serv.check(ip);

		if(isPresent)
		{
			return new ResponseEntity<String>("IP "+ ip + " is present", HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<String> defMsg()
	{
		return new ResponseEntity<String>("Instructions : after hostname enter coma separated IP : A.B.C.D", HttpStatus.OK);
	}
}
