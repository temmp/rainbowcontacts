package ccdr.rainbow.Tool;

import com.ccdr.rainbowcontacts.R;

import android.app.Activity;
import android.content.SharedPreferences;


public class ChangeSkinUtil {

	/**
	 * 各界面需要换肤的控件 定义为int类型
	 */
	public int 	   up_view,mid_view,down_view,up_text,down_text,down_text1,numbercount_text,//通用
					talkbutton,talkbutton1,talkbutton2,//NewMain
	onekeysharecontact_tip_BackButton,onekeysharecontact_tip_OkButton,onekeysharecontact_text_color,//ExplainActivity
	search_button,search_button1,radarback,uplayText,bondstatetv_id,onlyoneuncheckedclick_id,onlyonecheckedclick_id,//searchDevices
	connecting_id,//PBAPFetch
	returnbtn,//importing
	temp_cs_send,temp_cs_receiver,//Temp_CS		
	onekeysharecontact_tip_NextButton,//OneKeyShareContact_tips
	btserver_backbutton,//BtServerFetch
	import_contactsbutton,export_contactsbutton,//ContactMove
	import_local_ok,import_setting_button,import_local_back,//importcontacttolocal
	export_contact_all,export_contact_deselect,export_contact_back,export_contact_ok,itemchecked,itemnotcheckedclick,itemcheckedclick,imageView1,choosecontact_back,darkgray;//choosecontact
	public int    list1,list2,blue_pull_down1,blue_pull_down2,wifi_pull_down1,wifi_pull_down2;
	/**
	 * 各颜色皮肤 定义为String类型
	 */
	public static String able="";
	public static String grassland = "1", star = "2", 
						 changeSkin = "changeSkin";

	/**
	 * 设置访问权限
	 */
	public static int popedom = Activity.MODE_WORLD_READABLE
			+ Activity.MODE_WORLD_WRITEABLE;

	/**
	 * 根据从SharedPreferences取出的颜色控制图片来源
	 * 
	 * @param spskin
	 */
	public ChangeSkinUtil(SharedPreferences spskin) {
		System.out.println("change");
		System.out.println("----"+spskin.getString("skindata", ""));
		/**
		 * 程序第一次运行时，默认为褐色皮肤
		 */
/*		if (spskin.getString("skindata", "").equals(null)) {
			System.out.println("原来的");
			this.magazineActivityTitle = R.drawable.uplay_new;
			
			
			
			SharedPreferences.Editor edit = spskin.edit();
			edit.putString("skindata", grassland);
			edit.commit();

		}*/

		/**
		 * 取出的字符串为hese时，使用褐色皮肤
		 */
		if (spskin.getString("skindata", "").equals(star)) {
			System.out.println("新的");
			this.up_view = R.drawable.uplay_new_star;
			this.mid_view = R.drawable.midlay_new_star;
			this.down_view = R.drawable.downlay_new_star;
			this.talkbutton = R.drawable.contactonekeyget_star;
			this.talkbutton1 = R.drawable.onekeysharecontact_star;
			this.talkbutton2 = R.drawable.contactmove_star;
			this.up_text = R.color.title_star;
			this.down_text = R.color.down_star;
			this.down_text1 = R.color.down_star;
			this.onekeysharecontact_tip_BackButton = R.drawable.back_2_star;
			this.onekeysharecontact_tip_OkButton = R.drawable.ok_star;
			this.onekeysharecontact_text_color = R.color.little_star;
			this.search_button = R.drawable.back_star;
			this.search_button1 = R.drawable.enterbottombutton_star;
			this.radarback = R.drawable.radarbackground_star;
			this.uplayText = R.color.little_star;
			this.bondstatetv_id = R.color.gray_star;
			this.onlyonecheckedclick_id = R.drawable.onlyonecheckedclick_star;
			this.onlyoneuncheckedclick_id = R.drawable.onlyoneuncheckedclick_star;
			this.connecting_id = R.color.little_star;
			this.returnbtn = R.drawable.norbtn9_star;
			this.temp_cs_send = R.drawable.onekeysendcontact_star;
			this.temp_cs_receiver = R.drawable.onekeyreceivecontact_star;
			this.onekeysharecontact_tip_NextButton = R.drawable.next_star;
			this.btserver_backbutton = R.drawable.back_3_star;
			this.numbercount_text = R.color.number_star;
			this.import_contactsbutton = R.drawable.importcontact_local_star;
			this.export_contactsbutton = R.drawable.exportcontact_local_star;
			this.import_local_ok = R.drawable.ok_3_star;
			this.import_setting_button = R.drawable.highsetting_star;
			this.import_local_back = R.drawable.import_local_back_star;
			this.export_contact_all = R.drawable.selectall_star;
			this.export_contact_deselect = R.drawable.cancelselect_star;
			this.export_contact_back = R.drawable.back_star;
			this.export_contact_ok = R.drawable.export_star;
			this.itemchecked = R.drawable.itemchecked_star;
			this.itemnotcheckedclick = R.drawable.itemnotcheckedclick_star;
			this.itemcheckedclick = R.drawable.itemcheckedclick_star;
			this.imageView1 = R.drawable.line_star;
			this.choosecontact_back = R.drawable.choosecontact_back_star;
			this.darkgray = R.color.darkgray_star;
			this.list1 = R.drawable.blue_cs_star;
			this.list2 = R.drawable.wifi_cs_star;
			this.blue_pull_down1 = R.drawable.blue_pull_down1_star;
			this.blue_pull_down2 = R.drawable.blue_pull_down2_star;
			this.wifi_pull_down1 = R.drawable.wifi_pull_down1_star;
			this.wifi_pull_down2 = R.drawable.wifi_pull_down2_star;
		}else{
			System.out.println("原来的");
			this.up_view = R.drawable.uplay_new;
			this.mid_view = R.drawable.midlay_new;
			this.down_view = R.drawable.downlay_new;
			this.talkbutton = R.drawable.contactonekeyget;
			this.talkbutton1 = R.drawable.onekeysharecontact;
			this.talkbutton2 = R.drawable.contactmove;
			this.up_text = R.color.title;
			this.down_text = R.color.down;
			this.down_text1 = R.color.down;
			this.onekeysharecontact_tip_BackButton = R.drawable.back_2;
			this.onekeysharecontact_tip_OkButton = R.drawable.ok;
			this.onekeysharecontact_text_color = R.color.little;
			this.search_button = R.drawable.back;
			this.search_button1 = R.drawable.enterbottombutton;
			this.radarback = R.drawable.radarbackground;
			this.uplayText = R.color.little;
			this.bondstatetv_id = R.color.gray;
			this.onlyonecheckedclick_id = R.drawable.onlyonecheckedclick;
			this.onlyoneuncheckedclick_id = R.drawable.onlyoneuncheckedclick;
			this.connecting_id = R.color.gray;
			this.returnbtn = R.drawable.norbtn9;
			this.temp_cs_send = R.drawable.onekeysendcontact;
			this.temp_cs_receiver = R.drawable.onekeyreceivecontact;
			this.onekeysharecontact_tip_NextButton = R.drawable.next;
			this.btserver_backbutton = R.drawable.back_3;
			this.numbercount_text = R.color.little;
			this.import_contactsbutton = R.drawable.importcontact_local;
			this.export_contactsbutton = R.drawable.exportcontact_local;
			this.import_local_ok = R.drawable.ok_3;
			this.import_setting_button = R.drawable.highsetting;
			this.import_local_back = R.drawable.import_local_back;
			this.export_contact_all = R.drawable.selectall;
			this.export_contact_deselect = R.drawable.cancelselect;
			this.export_contact_back = R.drawable.back;
			this.export_contact_ok = R.drawable.export;
			this.itemchecked = R.drawable.itemchecked;
			this.itemnotcheckedclick = R.drawable.itemnotcheckedclick;
			this.itemcheckedclick = R.drawable.itemcheckedclick;
			this.imageView1 = R.drawable.line;
			this.choosecontact_back = R.drawable.choosecontact_back;
			this.darkgray = R.color.darkgray;
			this.list1 = R.drawable.blue_cs;
			this.list2 = R.drawable.wifi_cs;
			this.blue_pull_down1 = R.drawable.blue_pull_down1;
			this.blue_pull_down2 = R.drawable.blue_pull_down2;
			this.wifi_pull_down1 = R.drawable.wifi_pull_down1;
			this.wifi_pull_down2 = R.drawable.wifi_pull_down2;
		
			SharedPreferences.Editor edit = spskin.edit();
			edit.putString("skindata", grassland);
			edit.commit();
		}

	/*	*//**
		 * 取出的字符串为fense时，使用粉色皮肤
		 *//*
		if (spskin.getString("skindata", "").equals(fense)) {

			this.magazineActivityTitle = R.drawable.title_bg_1_fen;
			this.magazineActivityBottom = R.drawable.bg_title_sort2_fen;			

		}

		*//**
		 * 取出的字符串为lanse时，使用蓝色皮肤
		 *//*
		if (spskin.getString("skindata", "").equals(lanse)) {

			this.magazineActivityTitle = R.drawable.title_bg_1_lan;
			this.magazineActivityBottom = R.drawable.bg_title_sort2_lan;

		}

		*//**
		 * 取出的字符串为heise时，使用黑色皮肤
		 *//*
		if (spskin.getString("skindata", "").equals(heise)) {

			this.magazineActivityTitle = R.drawable.title_bg_1_hei;
			this.magazineActivityBottom = R.drawable.bg_title_sort2_hei;

		}
		
		*//**
		 * 取出的字符串为katong时，使用卡通皮肤
		 *//*
		if (spskin.getString("skindata", "").equals(katong)) {

			this.magazineActivityTitle = R.drawable.title_bg_1_cartoon;
			this.magazineActivityBottom = R.drawable.bg_title_sort2_cartoon;

		}
		
		*//**
		 * 取出的字符串为shangwu时，使用商务皮肤
		 *//*
		if (spskin.getString("skindata", "").equals(shangwu)) {

			this.magazineActivityTitle = R.drawable.title_bg_1_china;
			this.magazineActivityBottom = R.drawable.bg_title_sort2_china;

		}
		
		*//**
		 * 取出的字符串为fengjing时，使用风景皮肤
		 *//*
		if (spskin.getString("skindata", "").equals(fengjing)) {

			this.magazineActivityTitle = R.drawable.title_bg_1_fengjing;
			this.magazineActivityBottom = R.drawable.bg_title_sort2_fengjing;

		}
		
		*//**
		 * 取出的字符串为tuya时，使用涂鸦皮肤
		 *//*
		if (spskin.getString("skindata", "").equals(tuya)) {

			this.magazineActivityTitle = R.drawable.title_bg_1_tuya;
			this.magazineActivityBottom = R.drawable.bg_title_sort2_tuya;

		}*/
		
	}

	/**
	 * 将String类型的颜色存入SharedPreferences中
	 * 
	 * @param sp
	 * @param skinCode
	 */
	public static void setSkin(SharedPreferences sp, String skinCode) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("skindata", skinCode);
		editor.commit();
	}

}