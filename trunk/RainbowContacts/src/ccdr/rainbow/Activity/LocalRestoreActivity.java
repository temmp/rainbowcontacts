package ccdr.rainbow.Activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.ccdr.rainbowcontacts.R;
import ccdr.rainbow.Constants.Constants_File;
import ccdr.rainbow.Exception.NullSuffixException;
import ccdr.rainbow.Tool.ChangeSkinUtil;
import ccdr.rainbow.Tool.FileSearch;
import ccdr.rainbow.Tool.PathSelectAdapter;
import ccdr.rainbow.Tool.PathSelectItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LocalRestoreActivity extends Activity {
	private int Setting = 4321;
	private TextView text_path;
	private static final int ABOUT_DIALOG = 11;
	String[] paths = null;// �����õ���VCF�ļ�·��
	String result = null;// ���ڴ������ѡ��Ľ��
	public static final String ROOTPATH = "/data/data/pip.UIofPIP/files/";
	public static final String SDROOTPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	
	////////////////////////////////
	private ArrayList<PathSelectItem> m_ListItem=null;
	private PathSelectAdapter m_ListAdapter=null;
//	private ListView m_ListView;
	private LinearLayout skin_linearLayout ,skin_linearLayout1,skin_linearLayout2;
	private TextView up_text,down_text,down_text1;
	private TextView importlocal_text1,importlocal_text2,importlocal_text3;
	private ImageButton button1, button2,button3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.localrestore);
		importlocal_text1 = (TextView)findViewById(R.id.importlocal_text1);
		importlocal_text2 = (TextView)findViewById(R.id.importlocal_text2);
		importlocal_text3 = (TextView)findViewById(R.id.importlocal_text3);
		 text_path = (TextView)findViewById(R.id.waiting_text4);
			skin_linearLayout = (LinearLayout)findViewById(R.id.ltest);
			skin_linearLayout1 = (LinearLayout)findViewById(R.id.ltest1);
			skin_linearLayout2 = (LinearLayout)findViewById(R.id.ltest2);
			up_text = (TextView)findViewById(R.id.up_text);
			down_text = (TextView)findViewById(R.id.down_text);
			down_text1 = (TextView)findViewById(R.id.down_text1);
			SharedPreferences sp = getSharedPreferences(ChangeSkinUtil.changeSkin,ChangeSkinUtil.popedom);
			ChangeSkinUtil changeSkinUtil = new ChangeSkinUtil(sp);
			System.out.println(changeSkinUtil.up_view+"--magazineActivityTitle----"+R.drawable.uplay_new);
			skin_linearLayout.setBackgroundResource(changeSkinUtil.up_view);
			skin_linearLayout1.setBackgroundResource(changeSkinUtil.mid_view);
			skin_linearLayout2.setBackgroundResource(changeSkinUtil.down_view);
			System.out.println(changeSkinUtil.up_text+"--magazineActivityTitle----"+R.color.title);
			up_text.setTextColor(this.getResources().getColor(changeSkinUtil.up_text));
			down_text.setTextColor(this.getResources().getColor(changeSkinUtil.down_text));
			down_text1.setTextColor(this.getResources().getColor(changeSkinUtil.down_text1));
			text_path.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
			importlocal_text1.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
			importlocal_text2.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
			importlocal_text3.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
			button1 = (ImageButton)findViewById(R.id.local_ok);
			button2 = (ImageButton)findViewById(R.id.setting_button);
			button3 = (ImageButton)findViewById(R.id.local_back);
			button1.setImageResource(changeSkinUtil.import_local_ok);
			button2.setImageResource(changeSkinUtil.import_setting_button);
			button3.setImageResource(changeSkinUtil.import_local_back);
		//////////////////////////
//		m_ListView=new ListView(this);
	}

	/************ ������߼����á� *************/
	public void onSetting(View view) {

		result = null;
		ArrayList<String> t = new ArrayList<String>();
		ArrayList<String> sd = new ArrayList<String>();

		try {
			FileSearch fs = new FileSearch();
			t = fs.searchFiles(ROOTPATH, null, ".vcf");// ��data·������������VCFΪ��׺���ļ�
		} catch (FileNotFoundException e) {
			Log.v("FileNotFoundException", "FileNotFoundException");
			t = null;
			e.printStackTrace();
		} catch (NullSuffixException e) {
			Log.v("NullSuffixException", "NullSuffixException");
			t = null;
			e.printStackTrace();
		}

		try {
			FileSearch fs = new FileSearch();
			sd = fs.searchFiles(SDROOTPATH, null, ".vcf");// ��SD������������VCFΪ��׺���ļ�
		} catch (FileNotFoundException e) {
			Log.v("SDFileNotFoundException", "SDFileNotFoundException");
			sd = null;
			e.printStackTrace();
		} catch (NullSuffixException e) {
			Log.v("SDNullSuffixException", "SDNullSuffixException");
			sd = null;
			e.printStackTrace();
		}

		if (t != null || sd != null) {// ��������VCF�ļ�
			ArrayList<String> pathsList = new ArrayList<String>();
			if (t != null) {
				for (int i = 0; i < t.size(); i++) {// �õ�data·��������VCF�ļ�
					pathsList.add(t.get(i));
				}
			}
			if (sd != null) {
				for (int i = 0; i < sd.size(); i++) {// �õ�SD��������VCF�ļ�
					pathsList.add(sd.get(i));
				}
			}
			paths = new String[pathsList.size()];
			for (int i = 0; i < pathsList.size(); i++)
				paths[i] = pathsList.get(i);
			showDialog(Constants_File.Dialog.VCFDIALOG);// ��ʾѡ��VCF�ļ��Ի���
		} else {// ��û��������VCF�ļ�
			showDialog(Constants_File.Dialog.HINTDIALOG);// ��ʾ��ʾ�Ի���
		}
		

		

	}

	/**
	 * �����Ի���
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		LayoutInflater inflater = LayoutInflater.from(this);
        final View textEntryView = inflater.inflate(
                R.layout.about, null);
		switch (id) {
		case Constants_File.Dialog.HINTDIALOG:// ������ʾ�Ի���
			Builder hintbuilder = new AlertDialog.Builder(LocalRestoreActivity.this);
			hintbuilder.setTitle(R.string.novcf1);
			hintbuilder.setMessage(R.string.novcf2);
			hintbuilder.setPositiveButton(R.string.ok, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			// ����һ����ʾ�Ի���
			dialog = hintbuilder.create();
			break;
		case ABOUT_DIALOG:
			Builder b =new AlertDialog.Builder(LocalRestoreActivity.this);
			b.setIcon(android.R.drawable.ic_dialog_info);
			b.setTitle(R.string.about);
			b.setView(textEntryView);
			b.setPositiveButton(R.string.ok,new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			dialog =b.create();
			break;
		case Constants_File.Dialog.VCFDIALOG:// ������ѡ��Ի���
			
			///////////////////////////////////////////////	
			//���һ��ArrayList<PathSelectItem>
			m_ListItem=new ArrayList<PathSelectItem>();
			m_ListItem.clear();
			for(int n=0;n<paths.length;++n)
			{
				String path=paths[n];
				int index=path.lastIndexOf("/");
				String fpath=getString(R.string.import_file_path)+path.substring(0, index+1);
				String fname=getString(R.string.import_file_name)+path.substring(index+1);
				m_ListItem.add(new PathSelectItem(fpath,fname));
			}
			ListView m_ListView=new ListView(this);
	        m_ListAdapter=new PathSelectAdapter(this,m_ListItem,m_ListView);
	        m_ListView.setAdapter(m_ListAdapter);
	        //ΪListView��ӵ����¼�
	        m_ListView.setOnItemClickListener(new OnItemClickListener()
	        {
	        	@Override
	        	public void onItemClick(AdapterView<?> arg0,View arg1,int arg2,long arg3)
	        	{
	        		//�Ȱ�����ѡ���checked��Ϊfalse
	        		for(int i=0;i<arg0.getCount();++i)
	        		{
	        			View v=arg0.getChildAt(i);
	        			if(null!=v)
	        			{
	        			m_ListAdapter.setState(i, false);
	        			RadioButton rbtn=(RadioButton)v.findViewById(R.id.checkedbutton);
	        			rbtn.setChecked(false);
	        			}
	        		}
	        		m_ListAdapter.setState(arg2, true);
	        		RadioButton radiobtn=(RadioButton)arg1.findViewById(R.id.checkedbutton);
	        		radiobtn.setChecked(true);
	        		
	        		result=null;
	        		result=paths[arg2];
	        	}
	        });
	        
	        ///////////////////////////////////
			Builder builder = new android.app.AlertDialog.Builder(LocalRestoreActivity.this);
			// ���öԻ���ı���
			builder.setTitle(R.string.chooseyourfile);
			
			
			builder.setView(m_ListView);
			//////////////////////////////////////////
/*			builder.setSingleChoiceItems(paths, -1,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							result = null;
							result = paths[which];// ѡ��ѡ��
						}
					});*/
			///////////////////////////////////////////
			// ���һ��ȷ����ť
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if(result == null) {
								dialog.dismiss();
							} else {
								Log.v("name", result);
								Intent in = new Intent(LocalRestoreActivity.this,
										importContactsActivity.class);
								in.putExtra("filepath", result);
								startActivity(in);
							}
						}
					});
			// ���һ��ȡ����ť
			builder.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						

							dialog.dismiss();

						}
					});
			// ����һ����ѡ��Ի���
			dialog = builder.create();
			break;
		}
		return dialog;
	}

	/************ ��������ء� *************/
	public void onBack(View view) {
//		Intent i = new Intent(LocalRestoreActivity.this,
//				ContactMove.class);
//		startActivity(i);
		onBackPressed();
	}

	/************ �����ȷ���� *************/
	public void onOK(View view) {
		String LOCALPATH = Constants_File.Path.LOCALPATH;
		String SDLOCALPATH = Constants_File.Path.SDLOCALPATH;
		File f = new File(SDLOCALPATH);
		if (!f.exists()) {
			File ff = new File(LOCALPATH);
			if (!ff.exists()) {
				showDialog(Constants_File.Dialog.HINTDIALOG);// ��ʾ��ʾ�Ի���
			} else {
				Intent i = new Intent(LocalRestoreActivity.this,
						importContactsActivity.class);
				i.putExtra("filepath", LOCALPATH);
				startActivity(i);
			}

		} else {
			Intent i = new Intent(LocalRestoreActivity.this, importContactsActivity.class);
			i.putExtra("filepath", SDLOCALPATH);
			startActivity(i);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String LOCALPATH = Constants_File.Path.LOCALPATH;
		String SDLOCALPATH = Constants_File.Path.SDLOCALPATH;
		File f = new File(SDLOCALPATH);
		if (!f.exists()){
			File ff = new File(LOCALPATH);
			if (!ff.exists()) {	
			}else{
				text_path.setText(LOCALPATH);
			}
		}else{
			text_path.setText(SDLOCALPATH);
		}
	}

}
