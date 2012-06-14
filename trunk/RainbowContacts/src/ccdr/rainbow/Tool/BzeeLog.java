package ccdr.rainbow.Tool;

import android.util.Log;

/**
 * 超级调试工具类
 * 
 * @author Administrator BZee
 * 
 */
public class BzeeLog {
	public static String tag = "调试信息：";

	static int stepnum = 1;

	/**
	 * 将每次打印出来的标题头换掉（默认为“调试信息：”）
	 * 
	 * @param newTag
	 *            要替换的标题头
	 */
	public final static void setTag(String newTag) {
		tag = newTag;
	}

	/**
	 * 获得当前的标题头（默认为“调试信息：”），详见其他方法
	 * 
	 * @return 当前的标题头（默认为“调试信息：”）
	 */
	public final static String getTag() {
		return tag;
	}

	/**
	 * 将标题头重置为“调试信息：”
	 */
	public final static void resetTag() {
		tag = "调试信息：";
	}

	/**
	 * 打印出“调试信息：（title的内容），（content的内容）”
	 * 
	 * @param title
	 * @param content
	 */
	public final static void show(String title, int content) {
		Log.v(tag + title, Integer.toString(content));
	}

	/**
	 * 打印出“调试信息：（title的内容），（content的内容）”
	 * 
	 * @param title
	 * @param content
	 */
	public final static void show(String title, float content) {
		Log.v(tag + title, Float.toString(content));
	}

	/**
	 * 打印出“调试信息：（title的内容），（content的内容）”
	 * 
	 * @param title
	 * @param content
	 */
	public final static void show(String title, long content) {
		Log.v(tag + title, Long.toString(content));
	}

	/**
	 * 打印出“调试信息：（title的内容），（content的内容）”
	 * 
	 * @param title
	 * @param content
	 */
	public final static void show(String title, char content) {
		Log.v(tag + title, Character.toString(content));
	}

	/**
	 * 打印出“调试信息：（title的内容），（content的内容）”
	 * 
	 * @param title
	 * @param content
	 */
	public final static void show(String title, boolean content) {
		Log.v(tag + title, Boolean.toString(content));
	}

	/**
	 * 打印出“调试信息：（title的内容），（content的内容）”
	 * 
	 * @param title
	 * @param content
	 */
	public final static void show(String title, String content) {
		Log.v(tag + title, content);
	}

	/**
	 * 打印出“调试信息：（title的内容），（content的内容）”
	 * 
	 * @param title
	 * @param content
	 */
	public final static void show(String title, short content) {
		Log.v(tag + title, Short.toString(content));
	}

	/**
	 * 打印出“调试信息：（title的内容），（content的内容）”
	 * 
	 * @param title
	 * @param content
	 */
	public final static void show(String title, byte content) {
		Log.v(tag + title, Byte.toString(content));
	}

	/**
	 * 打印出“调试信息：（title的内容），（content的内容）”
	 * 
	 * @param title
	 * @param content
	 */
	public final static void show(String title, double content) {
		Log.v(tag + title, Double.toString(content));
	}

	/**
	 * 步数增加1
	 */
	public final static void increaseStep() {
		stepnum++;
	}

	/**
	 * 步数归一
	 */
	public final static void resetStep() {
		stepnum = 1;
	}

	/**
	 * 将当前步数设置为newStep
	 * 
	 * @param newStep
	 *            要设置的步数
	 */
	public final static void setStep(int newStep) {
		stepnum = newStep;
	}

	/**
	 * 取得当前步数
	 * 
	 * @return 当前步数
	 */
	public final static int getStep() {
		return stepnum;
	}

	/**
	 * 定制化步数显示，打印出“调试信息：（step步数，比如1,2,3等）”
	 * 
	 * @param step
	 */
	public final static void step(int step) {
		Log.v(tag, Integer.toString(step));
		stepnum = step;
	}

	/**
	 * 步数归一，打印出“调试信息：1”
	 */
	public final static void stepBegin() {
		resetStep();
		step(stepnum);
	}

	/**
	 * 根据上一步的步数，打印下一步。详见step相关方法
	 */
	public final static void stepOn() {
		increaseStep();
		step(stepnum);
	}

	/**
	 * 打印显示现在走到步数1
	 */
	public final static void step1() {
		Log.v(tag, "1");
	}

	/**
	 * 打印显示现在走到步数2
	 */
	public final static void step2() {
		Log.v(tag, "2");
	}

	/**
	 * 打印显示现在走到步数3
	 */
	public final static void step3() {
		Log.v(tag, "3");
	}

	/**
	 * 打印显示现在走到步数4
	 */
	public final static void step4() {
		Log.v(tag, "4");
	}

	/**
	 * 打印显示现在走到步数5
	 */
	public final static void step5() {
		Log.v(tag, "5");
	}

	/**
	 * 打印显示现在走到步数6
	 */
	public final static void step6() {
		Log.v(tag, "6");
	}

	/**
	 * 打印显示现在走到步数7
	 */
	public final static void step7() {
		Log.v(tag, "7");
	}

	/**
	 * 打印显示现在走到步数8
	 */
	public final static void step8() {
		Log.v(tag, "8");
	}

	/**
	 * 打印显示现在走到步数9
	 */
	public final static void step9() {
		Log.v(tag, "9");
	}

	/**
	 * 打印出“调试信息：(mark的内容)，(mark的内容)”
	 * 
	 * @param mark
	 */
	public final static void mark(String mark) {
		Log.v(tag + mark, mark);
	}

	/**
	 * 打印出“调试信息：(mark的内容)，(mark的内容)”
	 * 
	 * @param mark
	 */
	public final static void mark(int mark) {
		Log.v(tag + mark, Integer.toString(mark));
	}

	/**
	 * 打印出“调试信息：(mark的内容)，(mark的内容)”
	 * 
	 * @param mark
	 */
	public final static void mark(float mark) {
		Log.v(tag + mark, Float.toString(mark));
	}

	/**
	 * 打印出“调试信息：(mark的内容)，(mark的内容)”
	 * 
	 * @param mark
	 */
	public final static void mark(long mark) {
		Log.v(tag + mark, Long.toString(mark));
	}

	/**
	 * 打印出“调试信息：(mark的内容)，(mark的内容)”
	 * 
	 * @param mark
	 */
	public final static void mark(char mark) {
		Log.v(tag + mark, Character.toString(mark));
	}

	/**
	 * 打印出“调试信息：(mark的内容)，(mark的内容)”
	 * 
	 * @param mark
	 */
	public final static void mark(boolean mark) {
		Log.v(tag + mark, Boolean.toString(mark));
	}

	/**
	 * 打印出“调试信息：(mark的内容)，(mark的内容)”
	 * 
	 * @param mark
	 */
	public final static void mark(short mark) {
		Log.v(tag + mark, Short.toString(mark));
	}

	/**
	 * 打印出“调试信息：(mark的内容)，(mark的内容)”
	 * 
	 * @param mark
	 */
	public final static void mark(byte mark) {
		Log.v(tag + mark, Byte.toString(mark));
	}

	/**
	 * 打印出“调试信息：(mark的内容)，(mark的内容)”
	 * 
	 * @param mark
	 */
	public final static void mark(double mark) {
		Log.v(tag + mark, Double.toString(mark));
	}
}
