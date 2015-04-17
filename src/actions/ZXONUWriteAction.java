package actions;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import tools.ResultOperator;
import tools.TelnetOperator;

import com.opensymphony.xwork2.ActionSupport;

public class ZXONUWriteAction extends ActionSupport
{
	private static final long serialVersionUID = -7229625851141927418L;
	String onuinterface;
	String sn;
	String ip;
	String vlan;
	
	public String getOnuinterface() {
		return onuinterface;
	}

	public void setOnuinterface(String onuinterface) {
		this.onuinterface = onuinterface;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getVlan() {
		return vlan;
	}

	public void setVlan(String vlan) {
		this.vlan = vlan;
	}

	public String execute() throws Exception
	{
		System.out.println("ip="+ ip + ";sn=" + sn + ";vlan=" + vlan + ";interface=" + onuinterface);
		TelnetOperator to = new TelnetOperator();
		to.initParam("d:/zte_info.txt");
		to.setIp(ip);
		to.login();
		to.sendCommand("configure terminal","#");
		int id;
		String resultStr =  to.sendCommand("show running-config interface gpon-olt_"+ onuinterface,"--More--"); 
		StringBuffer resultStrb = new StringBuffer();
	    resultStrb.append(resultStr);
	    while(resultStr.endsWith("--More--"))     //嵌套递归方法解决分屏问题
	    {        	
	    	resultStr = to.sendCommand(" ", "--More--");
	        resultStrb.append(resultStr);	        	
	    }
	    System.out.println(resultStr);
	    
	    ResultOperator rt = new ResultOperator();
        id = rt.searchMAX(resultStrb.toString(),"onu");   //id属于易变参数，所以要在配置前临时获取，以免发生变化。
        System.out.println("id=" + id);   
    
        FileInputStream fr = new FileInputStream("d:/zte_script.txt");
    	InputStreamReader reader = new InputStreamReader(fr);
    	BufferedReader br = new BufferedReader(reader);
    	String buffer;
    	while ((buffer = br.readLine()) != null)
    	{
    		String bufferAfter = buffer.replaceAll("%interface%", onuinterface).replaceAll("%sn%", sn).replaceAll("%id%", Integer.toString(id+1)).replaceAll("%vlan%", vlan);
    		System.out.println(bufferAfter);
    		to.sendCommand(bufferAfter,"#");
    	}   	
    	br.close(); 
    	to.distinct();
		return SUCCESS;		
	}
}
