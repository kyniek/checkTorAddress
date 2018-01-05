package kyn.tasks.repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import kyn.tasks.repository.util.SelfSignedSSLconnectionUtil;

@Repository
public class TorNodesRepository
{

	private static List<String> nodesCache;

	@Value("${tor.list}")
	private String torList;
	
	@Value("${tor.update.interval}")
	private long updateInterval;
	
	private LocalDateTime lastChecked;

	
	
	
	
	public TorNodesRepository()
	{
		nodesCache = new ArrayList<String>();
	}

	public List<String> getNodesCache()
	{		
		if(lastChecked == null || lastChecked.until(LocalDateTime.now(), ChronoUnit.MINUTES) > updateInterval)
		{
			updateData();
		}
		return nodesCache;
	}

	
	private void updateData()
	{
		SelfSignedSSLconnectionUtil util = new SelfSignedSSLconnectionUtil();
		util.setupDefaultSSLSocketFactory();
		//get records and process them with method addIP defined localy
		List<String> tmpList = util.getRecords(torList, this::addIP);		
		
		//Convert linkedList to arrayList for better performance
		nodesCache = Collections.unmodifiableList(
				tmpList.stream()
				.collect(Collectors.toCollection(ArrayList::new))
		);
		
		lastChecked = LocalDateTime.now();
	}

	private void addIP(List<String> tmpList, String inputLine)
	{
		if (inputLine.startsWith("ExitAddress"))
		{
			tmpList.add(inputLine.split(" ")[1]);
		}
	}

}
