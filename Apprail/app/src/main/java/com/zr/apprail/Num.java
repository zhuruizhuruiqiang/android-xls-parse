package com.zr.apprail;

public class Num {
	public static String getString(int num)
	{
		StringBuilder sb=new StringBuilder();
		String aS = String.valueOf(num);

		char[] asC = aS.toCharArray();
		for(int i=0;i<asC.length;i++)
		{
			switch(asC[i])
			{
				case '0':
					 sb.append(new String("零"));
					break;
				case '1':
					sb.append(new String("一"));
					break;
				case '2':
					sb.append(new String("二"));
					break;
				case '3':
					sb.append(new String("三"));
					break;
				case '4':
					sb.append(new String("四"));
					break;
				case '5':
					sb.append(new String("五"));
					break;
				case '6':
					sb.append(new String("六"));
					break;
				case '7':
					sb.append(new String("七"));
					break;
				case '8':
					sb.append(new String("八"));
					break;
				case '9':
					sb.append(new String("九"));
					break;
				default:
					break; 

			}
		}
		return sb.toString();
	}
}
