package ccdr.rainbow.Tool;
import java.util.ArrayList;
import java.util.List;

import com.ccdr.rainbowcontacts.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;



public class PathSelectAdapter extends ArrayAdapter<PathSelectItem> {
	
	private boolean checked_state[]=null;
	private static final int RESOURCE_LIST_XML=R.layout.pathselectitem;
	private static final int RESOURCE_ITEM_FILENAME=R.id.filename;
	private static final int RESOURCE_ITEM_FILEPATH=R.id.filepath;
	private static final int RESOURCE_ITEM_RADIOBUTTON=R.id.checkedbutton;
	
	public void setState(int position,boolean state){checked_state[position]=state;}
//	private static final int TEXT_SIZE_BIG=30;
//	private static final int TEXT_SIZE_MIDDLE=20;
//	private static final int TEXT_SIZE_SMALL=10;
	
	private LayoutInflater m_Inflater;
	private ArrayList<PathSelectItem> m_ArrayList;

	public PathSelectAdapter(Context context,List<PathSelectItem> list,ListView listview)
	{
		super(context,0,list);
		m_Inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_ArrayList=(ArrayList<PathSelectItem>) list;
		checked_state=new boolean[getCount()];
		for(int i=0;i<getCount();++i){checked_state[i]=false;}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView=m_Inflater.inflate(RESOURCE_LIST_XML, null);
		
		TextView text_filename=(TextView)convertView.findViewById(RESOURCE_ITEM_FILENAME);
		TextView text_filepath=(TextView)convertView.findViewById(RESOURCE_ITEM_FILEPATH);
		
		RadioButton radio_button=(RadioButton)convertView.findViewById(RESOURCE_ITEM_RADIOBUTTON);
		boolean haschecked=checked_state[position];
		radio_button.setChecked(haschecked);
		
		PathSelectItem item=getItem(position);
		
//		text_filename.setTextSize(TEXT_SIZE_BIG);
//		text_filepath.setTextSize(TEXT_SIZE_SMALL);
		
		text_filename.setText(item.getFileName());
		text_filepath.setText(item.getFilePath());
		
		return convertView;
	}
}
