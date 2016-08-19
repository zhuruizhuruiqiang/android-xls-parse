package com.zr.domain;

public enum Role {
    /** 司机 */
	driver 
	{public String getName(){return "司机";}
	 public int getOrdinal(){return 0;}},
    /** 随车机械师 */
	follower 
	{public String getName(){return "随车机械师";}
	 public int getOrdinal(){return 1;}},
    /** 列车调度员 */
	dispatcher 
	{public String getName(){return "列车调度员";}
	 public int getOrdinal(){return 2;}},
    /** 客运人员 */
	transporter 
	{public String getName(){return "客运人员";}
	 public int getOrdinal(){return 3;}};
    
    public abstract String getName();
    public abstract int getOrdinal();
    
    public static Role getRole(String name)
    {
    	if(name.equals("司机"))
    	{
    		return driver;
    	}
    	else if(name.equals("随车机械师")||name.equals("随车机械员"))
    	{
    		return follower;
    	}
    	else if(name.equals("列车调度员"))
    	{
    		return dispatcher;
    	}
    	else if(name.equals("客运人员"))
    	{
    		return transporter;
    	}
		return null;    	
    }
    public Role getRole(int index)
    {
    	if(index==0)
    	{
    		return Role.driver;
    	}
    	else if(index==1)
    	{
    		return Role.follower;
    	}
    	else if(index==2)
    	{
    		return Role.dispatcher;
    	}
    	else
		{
			return Role.transporter;
		}
    }
    
	public int getordinal()
	{
		return ordinal();
	}
}
