package tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultOperator 
{
	 /** 
     * ��ȡ���ֵ,����ִ�н�� 
     *  
     * @param �ַ���
     * @return �ַ���
     */  
	public int searchMAX(String s,String target)
	{
		Pattern p = Pattern.compile(target+"\\s\\d+");               //����������ʽ����ƥ��
		Matcher m = p.matcher(s);
		int max = 0;
	
		while(m.find())
		{
			String[] targets = m.group().split("\n");	             //��ƥ����ת��Ϊ�ַ���
			for(String t : targets)
			{
				String[] result = t.split(" ");                      //�ֽ��ַ�����ȡ��ֵ
				if (Integer.parseInt(result[1]) > max)
				{
					max = Integer.parseInt(result[1]);
				}    			
			}
		}		
    	
		return max;
			
	}

}
