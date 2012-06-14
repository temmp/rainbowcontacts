package ccdr.rainbow.Tool;

import com.ccdr.rainbowcontacts.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MenuActivity extends Activity{
	private static final int ABOUT = 1;  
	private static final int BACK = 2;
    private static final int EXIT = 3;   
    private static final int ABOUT_DIALOG = 11;
    public static final int EXIT_APPLICATION=101;
    @Override  
    public boolean onCreateOptionsMenu(Menu menu) {   
        // TODO Auto-generated method stub   
        // �������ʹ��xml�ļ�Ҳ����ʹ�ô��뷽ʽ�����뷽ʽ�Ƚ����һЩ~~~   
        // MenuInflater inflater = new MenuInflater(getApplicationContext());   
        // inflater.inflate(R.menu.options_menu, menu);   
        menu.add(0, ABOUT, 1, R.string.about).setIcon(android.R.drawable.ic_menu_info_details);   
//        menu.add(0, BACK, 2, "��ҳ").setIcon(android.R.drawable.ic_menu_set_as);
//        menu.add(0, EXIT, 3, "�˳�").setIcon(android.R.drawable.ic_lock_power_off);   
  
//        setMenuBackgroud();   
        return true;   
    }   
    /**  
    * �������˵�  
    *   
    * @param item  
    * @return  
    * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)  
    */  
   @Override  
   public boolean onOptionsItemSelected(MenuItem item) {   
       // TODO Auto-generated method stub   
       int id = item.getItemId();   
       switch (id) {   
       case ABOUT: //���ʹ��xml��ʽ���������ʹ��R.id.about   
    	   showDialog(ABOUT_DIALOG);
           break;   
  /*     case BACK:
    	   Intent intent=new Intent();
			intent.setClass(this, NewMain.class);
			intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);*/
/*       case EXIT: //ͬ��   
    	  Intent startMain =new Intent();
    	  startMain.setClass(this, Start.class);
startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
//startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
startActivity(startMain);
System.exit(0);*/
       }   
       return super.onOptionsItemSelected(item);   
   }   
/*   *//**  
    * ���ñ���  
    *   
    * @author mys at 2011-3-1 ����01:04:29  
    *//*  
   private void setMenuBackgroud() {   
       // TODO Auto-generated method stub   
       getLayoutInflater().setFactory(new Factory() {   
 
           @Override  
           public View onCreateView(String name, Context context,   
                   AttributeSet attrs) {   
               // TODO Auto-generated method stub   
               if (name   
                       .equals("com.android.internal.view.menu.IconMenuItemView")) {   
                   LayoutInflater f = getLayoutInflater();   
                   try {   
                       final View view = f.createView(name, null, attrs); // ���Դ��������Լ�����   
                       new Handler().post(new Runnable() {   
 
                           @Override  
                           public void run() {   
                               // TODO Auto-generated method stub   
                               view   
                                       .setBackgroundResource(R.drawable.icon); // ���ñ���Ϊ�����Զ����ͼƬ   
                           }   
                       });   
                       return view;   
 
                   } catch (InflateException e) {   
                       // TODO Auto-generated catch block   
                       e.printStackTrace();   
                   } catch (ClassNotFoundException e) {   
                       // TODO Auto-generated catch block   
                       e.printStackTrace();   
                   }   
               }   
               return null;   
           }   
       });   
   }   
*/
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		Dialog dialog = null;
		LayoutInflater inflater = LayoutInflater.from(this);
        final View textEntryView = inflater.inflate(
                R.layout.about, null);
        TextView text_message = (TextView)textEntryView.findViewById(R.id.about_text);
		System.out.println(id);
		switch(id){
			case ABOUT_DIALOG:
				Builder b =new AlertDialog.Builder(this);
				b.setIcon(android.R.drawable.ic_dialog_info);
				b.setTitle(R.string.about);
				text_message.setText(getString(R.string.about_message));
				b.setView(textEntryView);
				
				b.setPositiveButton(R.string.ok,new OnClickListener() {
				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
				dialog =b.create();
				break;
				default:
					break;
		}
		return dialog;
	}

}
