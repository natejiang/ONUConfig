package actions;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tools.TelnetOperator;

import com.opensymphony.xwork2.Action;




public class ZXONUListAction 
{
	private String makername;       //��������
	private ArrayList<HashMap<String, String>> onuArr = new ArrayList<HashMap<String,String>>();	
	public String execute() throws Exception
	{	
		FileInputStream fr = new FileInputStream("d:/zte_server_list.txt");    //��ȡOLT�б�
    	InputStreamReader reader = new InputStreamReader(fr);
    	BufferedReader br = new BufferedReader(reader);
    	String buffer;
    	while ((buffer = br.readLine()) != null)
    	{
    		/**
    		 * ���ȶ�ȡserver_info.txt����ȡOLT��ַ��OLT�е�VLAN�б�
    		 */      		
    		String[] serverArr = buffer.split(":");   		 
    		String[] vlanArr = serverArr[1].split(",");
    		String ip = serverArr[0];
   		   
    		TelnetOperator to = new TelnetOperator();
    		to.initParam("d:/zte_info.txt");
    	    to.setIp(ip);
    		to.login();    		
    		String resultStr = to.sendCommand("show gpon onu uncfg","#");
   
    		Pattern p1 = Pattern.compile("\\d/\\d/\\d");                        //������ʽƥ��interface
        	Matcher m1 = p1.matcher(resultStr);
        	Pattern p2 = Pattern.compile("ZTE\\w{9}");                          //������ʽƥ��ONT���к�
        	Matcher m2 = p2.matcher(resultStr);
        	while(m1.find() && m2.find())
        	{
        		/**
        		 * ��ȡvlan����Լ����ʽ��server_info.txt�ļ���vlan����˳��Ϊ��λ����˳��
        		 */
        		String[] cardidArr = m1.group().split("/");
        		int cardid = Integer.parseInt(cardidArr[1]);
        		String vlan = vlanArr[cardid-2];
        	      		
        		HashMap<String, String> onuHas = new HashMap<String, String>();
        		onuHas.put("onuinterface",m1.group());
        		onuHas.put("sn", m2.group());
        		onuHas.put("ip", ip);
        		onuHas.put("vlan", vlan);
        		onuArr.add(onuHas);
        	}
        	to.distinct();
            		
    	}
    	if (onuArr.isEmpty())
    	{
    		HashMap<String, String> onuHas = new HashMap<String, String>();
    		onuHas.put("onuinterface","null");
    		onuHas.put("sn", "null");
    		onuHas.put("ip","null");
    		onuHas.put("vlan","null");
    		onuArr.add(onuHas);
    	}
    	br.close();		
		return Action.SUCCESS;
			
	}
	
	/**
	 * Ϊfield�ṩgetter,setter������struts�Զ����� ��Ӧ��
	 */	
	public String getMakername() {
		return makername;
	}

	public void setMakername(String makername) {
		this.makername = makername;
	}

	public ArrayList<HashMap<String, String>> getOnuArr() {
		return onuArr;
	}

	public void setOnuArr(ArrayList<HashMap<String, String>> onuArr) {
		this.onuArr = onuArr;
	}


}
