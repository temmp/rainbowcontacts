package ccdr.rainbow.Tool;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

public class UnitSize {
	public static final float devicenamesize = 20;
	public static final float devicebondstatesize = 15;
	public static final float connectiontips = (float) 21.681415929;
	public static final float contactnamesize = 20;
	public static final float[] devicenamemargins ={100,10,0,0};
	public static final float[] devicemarginsmobile ={50,15,0,0};
	public static final float[] devicemarginslaptop ={45, 15, 0, 0};
	public static final float[] devicebondmargins ={100, 35, 0, 0};
	public static final float[] contactnamepadding={25, 2, 0, 0};
	
	public static int dip2px(Context context, float dipValue){   
        final float scale = context.getResources().getDisplayMetrics().density;   
        return (int)(dipValue * scale + 0.5f);   
    }   
       
	public static int px2dip(Context context, float pxValue){   
	        final float scale = context.getResources().getDisplayMetrics().density;   
	        return (int)(pxValue / scale + 0.5f);   
	    } 
	public static void mysetMargins(Context contex,RelativeLayout.LayoutParams rl,float ar1,float ar2,float ar3,float ar4){
		rl.setMargins(UnitSize.dip2px(contex,ar1),
				UnitSize.dip2px(contex,ar2), 
				UnitSize.dip2px(contex,ar3),
				UnitSize.dip2px(contex,ar4));
	}
	
	public static void mysetMargins(Context contex,RelativeLayout.LayoutParams rl,float[]ar){
		rl.setMargins(UnitSize.dip2px(contex,ar[0]),
				UnitSize.dip2px(contex,ar[1]), 
				UnitSize.dip2px(contex,ar[2]),
				UnitSize.dip2px(contex,ar[3]));
	}
	public static void mysetPadding(Context contex,View view,float ar1,float ar2,float ar3,float ar4){
		view.setPadding(UnitSize.dip2px(contex,ar1),
				UnitSize.dip2px(contex,ar2), 
				UnitSize.dip2px(contex,ar3),
				UnitSize.dip2px(contex,ar4));
	}
	
	public static void mysetPadding(Context contex,View view,float[]ar){
		view.setPadding(UnitSize.dip2px(contex,ar[0]),
				UnitSize.dip2px(contex,ar[1]), 
				UnitSize.dip2px(contex,ar[2]),
				UnitSize.dip2px(contex,ar[3]));
	}
}
