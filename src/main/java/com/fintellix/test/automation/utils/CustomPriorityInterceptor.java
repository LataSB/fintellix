package com.fintellix.test.automation.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

public class CustomPriorityInterceptor implements IMethodInterceptor {

	@Override
	public List<IMethodInstance> intercept(List<IMethodInstance> methods,ITestContext context) 
	{

	    List<IMethodInstance> result = new ArrayList<IMethodInstance>();
	    
	    for (IMethodInstance m : methods)
	    {
	        result.add(m);
	    }
	    
	    Collections.sort(result, new Comparator <IMethodInstance>(){
	    	public int compare(IMethodInstance inst1, IMethodInstance inst2){
	    		
	    		try {               
		            //Get the FIELD object from - Methodobj->method->class->field
		        	//System.out.println(m.getMethod().getRealClass().getName());
		            Field f1 = inst1.getMethod().getRealClass().getField("priority");
		            Field f2 = inst2.getMethod().getRealClass().getField("priority");
		            //Get the object instance of the method object              
		            int priority1=f1.getInt(inst1.getInstance());
		            int priority2=f2.getInt(inst2.getInstance());
		            return priority1 - priority2;
		        } 
		         catch (Exception e) {
		            e.printStackTrace();
		        }       
	    		return 0;
	    	}
	    });
	    //Now iterate through each of these test methods
	  /*  for (IMethodInstance m : methods)
	    {
	        try {               
	            //Get the FIELD object from - Methodobj->method->class->field
	        	//System.out.println(m.getMethod().getRealClass().getName());
	            Field f = m.getMethod().getRealClass().getField("priority");
	            //Get the object instance of the method object              
	            array_index=f.getInt(m.getInstance());
	        } 
	         catch (Exception e) {
	            e.printStackTrace();
	        }           
	        result.set(array_index-1, m);           
	    }*/

	    return result;
	}

	
}
