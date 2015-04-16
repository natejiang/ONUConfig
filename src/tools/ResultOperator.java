package tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultOperator 
{
	 /** 
     * 获取最大值,返回执行结果 
     *  
     * @param 字符串
     * @return 字符串
     */  
	public int searchMAX(String s,String target)
	{
		Pattern p = Pattern.compile(target+"\\s\\d+");               //利用正则表达式进行匹配
		Matcher m = p.matcher(s);
		int max = 0;
	
		while(m.find())
		{
			String[] targets = m.group().split("\n");	             //将匹配结果转化为字符串
			for(String t : targets)
			{
				String[] result = t.split(" ");                      //分解字符串获取数值
				if (Integer.parseInt(result[1]) > max)
				{
					max = Integer.parseInt(result[1]);
				}    			
			}
		}		
    	
		return max;
			
	}

}
