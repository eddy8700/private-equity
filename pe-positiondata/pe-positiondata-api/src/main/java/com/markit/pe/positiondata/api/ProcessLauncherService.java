package com.markit.pe.positiondata.api;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Map;

import com.markit.pe.positiondata.domain.PEClient;

public interface ProcessLauncherService {
	
	
	public Map<String, Object> launchEDMProcess(File fileToPass,final PEClient client) throws MalformedURLException;

}
