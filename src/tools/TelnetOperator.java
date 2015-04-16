package tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;

import org.apache.commons.net.telnet.TelnetClient;

public class TelnetOperator 
{
	private String ip;
	private int port;
	private String username;
	private String password;
	private String prompt; 
	private char promptChar;              //结束标志字符
    private TelnetClient telnet;  
    private InputStream in;     
    private PrintStream out;   
    
    /** 
     * @param termtype  协议类型：VT100、VT52、VT220、VTNT、ANSI I 
     * @param prompt    结果结束标识
     */  
    public TelnetOperator(String termtype,String prompt){  
        telnet = new TelnetClient(termtype);  
        setPrompt(prompt);  
    }  
      
    public TelnetOperator(String termtype){  
        telnet = new TelnetClient(termtype);  
    }  
      
    public TelnetOperator(){  
        telnet = new TelnetClient();  
    }  
	public void setIp(String ip) {
		this.ip = ip;
	}
    public void setPrompt(String prompt) {  
        if(prompt!=null){  
            this.prompt = prompt;   
        }  
    }  
    /** 
     * 初始化参数
     */ 
    public void initParam(String paramFile)  throws Exception
   	{
   		Properties props = new Properties();
   		props.load(new FileInputStream(paramFile));   
   		port =Integer.parseInt(props.getProperty("port"));
   		username = props.getProperty("username");
   		password = props.getProperty("password");
   		prompt = props.getProperty("prompt");
   		promptChar = prompt.charAt(prompt.length() - 1); 
   		
   	}
    /** 
     * 登录到目标主机
     * @param ip 
     * @param port 
     * @param username 
     * @param password 
     */     
    public void login(){  
        try 
        {	
        	telnet.connect(ip, port);  
            in = telnet.getInputStream();  
            out = new PrintStream(telnet.getOutputStream());  
            System.out.println(readUntil("Username:")); 
            write(username);  
            System.out.println(readUntil("Password:"));  
            write(password);  
            String resultStr = readUntil(prompt); 
            System.out.println(resultStr);
            if(resultStr!=null&&resultStr.contains("No username or bad password")){  
                throw new RuntimeException("登陆失败");  
            }  
        }catch (RuntimeException e){
        	System.out.println("登陆失败");
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    
    public String readUntil(String pattern) {  
        StringBuffer sb = new StringBuffer();  
        try {  
            	char lastChar = (char)-1;  
            	boolean flag = pattern!=null&&pattern.length()>0;  
            	if(flag)  
            		lastChar = pattern.charAt(pattern.length() - 1);  //字符串最后一个字符
            	char ch;  
            	int code = -1;  
            	while ((code = in.read()) != -1) {  
            		ch = (char)code;  
            		sb.append(ch);  
            		//匹配到结束标识时返回结果   
                    if (flag) 
                    {  
                        if (ch == lastChar && sb.toString().endsWith(pattern)) {  
                            return sb.toString();  
                        }  
                    }
                    else
                    {  
                    	//如果没指定结束标识,匹配到默认结束标识字符时返回结果   
                        if(ch == promptChar)  
                            return sb.toString();  
                    }  
                    if(ch == promptChar)  
                    { 
                    	return sb.toString();  
                    }
                  //登录失败时返回结果    
                    if(sb.toString().contains("Login Failed")){  
                        return sb.toString();  
                    }              
                 }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return sb.toString();  
    }  

    public void write(String value) 
    {  
        try {      	
            out.println(value);  
            out.flush();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    } 
    
    public String sendCommand(String command,String myprompt) 
    {  
    	String rs = null;
        try {  
            write(command); 
            rs = readUntil(myprompt);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        System.out.println(rs);
        return rs;  
    }  
    
    public void distinct(){  
        try {  
            if(telnet!=null&&!telnet.isConnected())  
                telnet.disconnect();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }    
     
}
