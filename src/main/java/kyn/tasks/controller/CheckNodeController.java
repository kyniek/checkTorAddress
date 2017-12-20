package kyn.tasks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import kyn.tasks.service.CheckTorService;


@RestController
public class CheckNodeController {

	
	@Value("${tor.list}")
	String torList;
	
	@Autowired
	CheckTorService serv;
	/**
	 * Wyrażenie regularne - ":.+" - jest potrzebne by część parametru znajdującego się po "." nie była obcinana 
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
			return new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
		}
	}
}
