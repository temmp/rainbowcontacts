package ccdr.rainbow.Tool;

import android.util.Log;

/**
 * �������Թ�����
 * 
 * @author Administrator BZee
 * 
 */
public class BzeeLog {
	public static String tag = "������Ϣ��";

	static int stepnum = 1;

	/**
	 * ��ÿ�δ�ӡ�����ı���ͷ������Ĭ��Ϊ��������Ϣ������
	 * 
	 * @param newTag
	 *            Ҫ�滻�ı���ͷ
	 */
	public final static void setTag(String newTag) {
		tag = newTag;
	}

	/**
	 * ��õ�ǰ�ı���ͷ��Ĭ��Ϊ��������Ϣ�������������������
	 * 
	 * @return ��ǰ�ı���ͷ��Ĭ��Ϊ��������Ϣ������
	 */
	public final static String getTag() {
		return tag;
	}

	/**
	 * ������ͷ����Ϊ��������Ϣ����
	 */
	public final static void resetTag() {
		tag = "������Ϣ��";
	}

	/**
	 * ��ӡ����������Ϣ����title�����ݣ�����content�����ݣ���
	 * 
	 * @param title
	 * @param content
	 */
	public final static void show(String title, int content) {
		Log.v(tag + title, Integer.toString(content));
	}

	/**
	 * ��ӡ����������Ϣ����title�����ݣ�����content�����ݣ���
	 * 
	 * @param title
	 * @param content
	 */
	public final static void show(String title, float content) {
		Log.v(tag + title, Float.toString(content));
	}

	/**
	 * ��ӡ����������Ϣ����title�����ݣ�����content�����ݣ���
	 * 
	 * @param title
	 * @param content
	 */
	public final static void show(String title, long content) {
		Log.v(tag + title, Long.toString(content));
	}

	/**
	 * ��ӡ����������Ϣ����title�����ݣ�����content�����ݣ���
	 * 
	 * @param title
	 * @param content
	 */
	public final static void show(String title, char content) {
		Log.v(tag + title, Character.toString(content));
	}

	/**
	 * ��ӡ����������Ϣ����title�����ݣ�����content�����ݣ���
	 * 
	 * @param title
	 * @param content
	 */
	public final static void show(String title, boolean content) {
		Log.v(tag + title, Boolean.toString(content));
	}

	/**
	 * ��ӡ����������Ϣ����title�����ݣ�����content�����ݣ���
	 * 
	 * @param title
	 * @param content
	 */
	public final static void show(String title, String content) {
		Log.v(tag + title, content);
	}

	/**
	 * ��ӡ����������Ϣ����title�����ݣ�����content�����ݣ���
	 * 
	 * @param title
	 * @param content
	 */
	public final static void show(String title, short content) {
		Log.v(tag + title, Short.toString(content));
	}

	/**
	 * ��ӡ����������Ϣ����title�����ݣ�����content�����ݣ���
	 * 
	 * @param title
	 * @param content
	 */
	public final static void show(String title, byte content) {
		Log.v(tag + title, Byte.toString(content));
	}

	/**
	 * ��ӡ����������Ϣ����title�����ݣ�����content�����ݣ���
	 * 
	 * @param title
	 * @param content
	 */
	public final static void show(String title, double content) {
		Log.v(tag + title, Double.toString(content));
	}

	/**
	 * ��������1
	 */
	public final static void increaseStep() {
		stepnum++;
	}

	/**
	 * ������һ
	 */
	public final static void resetStep() {
		stepnum = 1;
	}

	/**
	 * ����ǰ��������ΪnewStep
	 * 
	 * @param newStep
	 *            Ҫ���õĲ���
	 */
	public final static void setStep(int newStep) {
		stepnum = newStep;
	}

	/**
	 * ȡ�õ�ǰ����
	 * 
	 * @return ��ǰ����
	 */
	public final static int getStep() {
		return stepnum;
	}

	/**
	 * ���ƻ�������ʾ����ӡ����������Ϣ����step����������1,2,3�ȣ���
	 * 
	 * @param step
	 */
	public final static void step(int step) {
		Log.v(tag, Integer.toString(step));
		stepnum = step;
	}

	/**
	 * ������һ����ӡ����������Ϣ��1��
	 */
	public final static void stepBegin() {
		resetStep();
		step(stepnum);
	}

	/**
	 * ������һ���Ĳ�������ӡ��һ�������step��ط���
	 */
	public final static void stepOn() {
		increaseStep();
		step(stepnum);
	}

	/**
	 * ��ӡ��ʾ�����ߵ�����1
	 */
	public final static void step1() {
		Log.v(tag, "1");
	}

	/**
	 * ��ӡ��ʾ�����ߵ�����2
	 */
	public final static void step2() {
		Log.v(tag, "2");
	}

	/**
	 * ��ӡ��ʾ�����ߵ�����3
	 */
	public final static void step3() {
		Log.v(tag, "3");
	}

	/**
	 * ��ӡ��ʾ�����ߵ�����4
	 */
	public final static void step4() {
		Log.v(tag, "4");
	}

	/**
	 * ��ӡ��ʾ�����ߵ�����5
	 */
	public final static void step5() {
		Log.v(tag, "5");
	}

	/**
	 * ��ӡ��ʾ�����ߵ�����6
	 */
	public final static void step6() {
		Log.v(tag, "6");
	}

	/**
	 * ��ӡ��ʾ�����ߵ�����7
	 */
	public final static void step7() {
		Log.v(tag, "7");
	}

	/**
	 * ��ӡ��ʾ�����ߵ�����8
	 */
	public final static void step8() {
		Log.v(tag, "8");
	}

	/**
	 * ��ӡ��ʾ�����ߵ�����9
	 */
	public final static void step9() {
		Log.v(tag, "9");
	}

	/**
	 * ��ӡ����������Ϣ��(mark������)��(mark������)��
	 * 
	 * @param mark
	 */
	public final static void mark(String mark) {
		Log.v(tag + mark, mark);
	}

	/**
	 * ��ӡ����������Ϣ��(mark������)��(mark������)��
	 * 
	 * @param mark
	 */
	public final static void mark(int mark) {
		Log.v(tag + mark, Integer.toString(mark));
	}

	/**
	 * ��ӡ����������Ϣ��(mark������)��(mark������)��
	 * 
	 * @param mark
	 */
	public final static void mark(float mark) {
		Log.v(tag + mark, Float.toString(mark));
	}

	/**
	 * ��ӡ����������Ϣ��(mark������)��(mark������)��
	 * 
	 * @param mark
	 */
	public final static void mark(long mark) {
		Log.v(tag + mark, Long.toString(mark));
	}

	/**
	 * ��ӡ����������Ϣ��(mark������)��(mark������)��
	 * 
	 * @param mark
	 */
	public final static void mark(char mark) {
		Log.v(tag + mark, Character.toString(mark));
	}

	/**
	 * ��ӡ����������Ϣ��(mark������)��(mark������)��
	 * 
	 * @param mark
	 */
	public final static void mark(boolean mark) {
		Log.v(tag + mark, Boolean.toString(mark));
	}

	/**
	 * ��ӡ����������Ϣ��(mark������)��(mark������)��
	 * 
	 * @param mark
	 */
	public final static void mark(short mark) {
		Log.v(tag + mark, Short.toString(mark));
	}

	/**
	 * ��ӡ����������Ϣ��(mark������)��(mark������)��
	 * 
	 * @param mark
	 */
	public final static void mark(byte mark) {
		Log.v(tag + mark, Byte.toString(mark));
	}

	/**
	 * ��ӡ����������Ϣ��(mark������)��(mark������)��
	 * 
	 * @param mark
	 */
	public final static void mark(double mark) {
		Log.v(tag + mark, Double.toString(mark));
	}
}
