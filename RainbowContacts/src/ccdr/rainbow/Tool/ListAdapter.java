package ccdr.rainbow.Tool;


import java.util.ArrayList;
import java.util.regex.Pattern;

import ccdr.rainbow.Activity.selectContactsActivity;

import com.ccdr.rainbowcontacts.R;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;


public class ListAdapter extends BaseAdapter {
//	public static final String URL_PREFIX = "http://www.google.com/";
	protected VcardOperator myOperator;
	private LayoutInflater layoutInflater;
	private OnClickListener onClickListener;
	private ArrayList<Contacts> stringArr;
	private ImageView contact_photo = null;
	public static  ArrayList<Boolean> checkedItem=new ArrayList<Boolean>();

	public ListAdapter(Context context, ArrayList<Contacts> arr, ArrayList<Boolean> checkedItem,OnClickListener listener) {
		layoutInflater = LayoutInflater.from(context);
		this.onClickListener = listener;
		stringArr = arr;
		ListAdapter.checkedItem = checkedItem;
		
	}

	@Override
	public int getCount() {
		return stringArr == null ? 0 : stringArr.size();
		
	}

	@Override
	public Object getItem(int position) {
		if (stringArr != null) {
			return stringArr.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.firstCharHintTextView = (TextView) convertView
					.findViewById(R.id.text_first_char_hint);
//			holder.orderTextView = (TextView) convertView.findViewById(R.id.list_order_number);
			holder.nameTextView = (CheckedTextView) convertView.findViewById(R.id.text);
//			holder.urlTextView = (TextView) convertView.findViewById(R.id.text_website_url);
			holder.imgView = (ImageView) convertView.findViewById(R.id.list_item_img_view);
			holder.nameTextView.setTextColor(selectContactsActivity.colorlist);
			convertView.setTag(holder);
//			checkedItem.add(true);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		holder.orderTextView.setText(String.valueOf(position + 1) + ".");
		
//		holder.nameTextView.setGravity(Gravity.CENTER_VERTICAL);
//		holder.urlTextView.setText(URL_PREFIX + stringArr.get(position));
//		holder.urlTextView.setTextColor(0xFFFFFF00);
		if(stringArr.get(position).getPhoto()!=null){
			holder.imgView.setImageBitmap(stringArr.get(position).getPhoto());
		}else{
			holder.imgView.setImageResource(R.drawable.person);
		}
		try{
			if(stringArr.get(position).getName().length()>12){
			holder.nameTextView.setText(stringArr.get(position).getName());
		}else{
			holder.nameTextView.setText(stringArr.get(position).getName());
		}
		}catch(Exception e){
			holder.nameTextView.setText(R.string.name_unknow);
		}
		
		
		holder.imgView.setOnClickListener(onClickListener);
		holder.imgView.setTag(position);
		final int p=position;
		int idx = position - 1;
		
	
		
		String previewChar = idx >= 0 ? stringArr.get(position-1).getPy() : " ";
		String currentChar = stringArr.get(position).getPy();
	
		
		if (!currentChar.equals(previewChar)) {
			holder.firstCharHintTextView.setVisibility(View.VISIBLE);
			holder.firstCharHintTextView.setText(currentChar);
//			checkedItem[position]=true;	
		} else {
			holder.firstCharHintTextView.setVisibility(View.GONE);
			System.out.println();
//			checkedItem[position]=true;	
		
	        
		}
//		checkedItem[position]=true;	
//		checkedItem.get(position);
/*		if(checkedItem[position]){
			holder.nameTextView.setChecked(true);
		}else{
			holder.nameTextView.setChecked(false);
		}*/
//		 holder.nameTextView.setChecked(true);
		if(checkedItem.get(position)==true||checkedItem.get(position)==false){
		   if(checkedItem.get(position)==true){
			   holder.nameTextView.setChecked(true);
		 }
           else{
        	   holder.nameTextView.setChecked(false);
        	  }
		}
		return convertView;
	}
 

	public final class ViewHolder {
		
		public TextView firstCharHintTextView;
//		public TextView orderTextView;
		public CheckedTextView nameTextView;
//		public TextView urlTextView;
		public ImageView imgView;
	}
	

	//获得汉语拼音首字母
    private String getAlpha(String str) {  
        if (str == null) {  
            return "1";  
        }  
  
        if (str.trim().length() == 0) {  
            return "1";  
        }  
  
        char c = str.trim().substring(0, 1).charAt(0);  
        // 正则表达式，判断首字母是否是英文字母  
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");  
        if (pattern.matcher(c + "").matches()) {  
            return (c + "").toUpperCase();  
        } else {  
            return "1";  
        }  
    }
}