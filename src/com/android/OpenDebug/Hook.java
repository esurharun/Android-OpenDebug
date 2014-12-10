package com.android.OpenDebug;

import java.lang.reflect.Method;

import android.content.Context;
import android.util.Log;
import com.saurik.substrate.*;

public class Hook {
	private static String _TAG = "OpenDebug";
	   static void initialize() {
		   
		   /*
		    * start(final String processClass,
389                                   final String niceName,
390                                   int uid, int gid, int[] gids,
391                                   int debugFlags, int mountExternal,
392                                   int targetSdkVersion,
393                                   String seInfo,
394                                   String[] zygoteArgs) {
		    */

            MS.hookClassLoad("android.os.Process", new MS.ClassLoadHook() {
                public void classLoaded(Class<?> _class) {
                    hookMethod(_class, "startViaZygote", 
                    		new Class<?>[] {String.class, String.class, int.class, 
                    			int.class, int[].class, int.class, int.class,
                            	int.class, String.class, String[].class});
                }
            });
	   }

	protected static void hookMethod(Class<?> _class, String methodName,
									Class<?>[] params) {
        Method method;
        try {
            method = _class.getMethod(methodName, params);
        } catch (NoSuchMethodException e) {
            method = null;
            Log.i(_TAG, "NoSuchMethod: " + methodName + 
            		" with " + params.length + " args");
        }
        
        
        
 
        if (method != null) {
        
        	
//        	final Method pMethod = method;
        	Log.i(_TAG, "Hooking " + methodName + 
        			"() with " + params.length + " args");
            MS.hookMethod(_class, method,
    			new MS.MethodAlteration<Context, Object>() {
                public Object invoked(final Context hooked,
                		final Object... args) throws Throwable {
                			args[5] = ((Integer) args[5] | 0x1);
                			
//                			Object[] modArgs = new Object[] {
//                					args[0],args[0],args[1],args[2],args[3],args[4],args[5],args[6],args[7]
//                			};
                		
//                			Log.i(_TAG, "Method: "+pMethod.toGenericString());
//                			for (int i=0;i<args.length;i++) {
//                				Log.i(_TAG,"Args ("+i+")"+" "+args[i].getClass().getName()+" : "+args[i]);
//                			}
                			
                			return invoke(hooked, args);
                }
        	});
        }
	}
}
