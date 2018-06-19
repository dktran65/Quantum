package com.quantum.utils;


import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;
import com.quantum.utils.ConsoleUtils;
import com.quantum.utils.DriverUtils;

import org.testng.Assert;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RestAPIUtils {

	public static void exeRestCmd(String cmd,String subcmd,Map<String, String> params) throws Exception {
		QAFExtendedWebDriver d=DriverUtils.getDriver();
		Map<String, String> actions=new HashMap<String, String>();
		
		actions.put("command", cmd);
		actions.put("subcommand", subcmd);
		
		params.put("deviceId",d.getCapabilities().getCapability("deviceName").toString());
		String svcStr="executions/"+d.getCapabilities().getCapability("executionId").toString();
		String res=exeRestOps("command",svcStr,actions,params);
		ConsoleUtils.logWarningBlocks("Step result:" +res);
		Assert.assertTrue(res.toLowerCase().contains("success"),res);
		
	}

	public static String retrieveDeviceInfo(String deviceId) throws Exception {
//		QAFExtendedWebDriver d=DriverUtils.getDriver();
		Map<String, String> actions=new HashMap<String, String>();
		Map<String, String> params=new HashMap<String, String>();

		String svcStr="handsets/"+deviceId;

		return exeRestOps("info",svcStr,actions,params);

	}
	public static String exeRestOps(String Ops,String serviceStr,Map<String, String> actions,Map<String, String> params) throws Exception {


		String cloudServer =
				new URL(ConfigurationManager.getBundle().getString("remote.server")).getHost();
	    String securityToken = ConfigurationManager.getBundle().getString("perfecto.capabilities.securityToken") ;
	    String user = ConfigurationManager.getBundle().getString("perfecto.capabilities.user") ;
	    String password = ConfigurationManager.getBundle().getString("perfecto.capabilities.password") ;
	    String authStr;
	    String actionStr;
	    String paramStr;

	    
	    if (  null ==securityToken || securityToken.trim().isEmpty())
	    	authStr="&user=" + user
					+ "&password=" + password;
	    else
	    	authStr="&securityToken=" + securityToken;
	    
	    actionStr="";
		for (Map.Entry<String, String> et:actions.entrySet())
		{
			actionStr=actionStr+"&" + et.getKey()+"="+et.getValue();
			
		}
				
		paramStr="";
		for (Map.Entry<String, String> et:params.entrySet())
		{
			paramStr=paramStr+"&param." + et.getKey()+"="+et.getValue();
			
		}
		
		String url = "https://"
				+ cloudServer
				+ "/services/"
				+ serviceStr
				+ "?operation=" +Ops
				+ authStr
				+ actionStr
				+ paramStr;
				//+ "&param.deviceId="+d.getCapabilities().getCapability("deviceName").toString();
		

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		//JSONObject jObj = new JSONObject(response.toString());

//		ConsoleUtils.logWarningBlocks("Step result:" +response.toString());
//		Assert.assertTrue(response.toString().toLowerCase().contains("success"),response.toString());

		return response.toString();
	}
}