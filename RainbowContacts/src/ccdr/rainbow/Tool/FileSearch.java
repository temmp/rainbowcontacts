package ccdr.rainbow.Tool;

/* import���class */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import ccdr.rainbow.Exception.NullKeyWordException;
import ccdr.rainbow.Exception.NullSuffixException;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

/**
 * �ļ������࣬ʹ��֮ǰȷ�ϳ�����Ȩ��
 * 
 * @author Administrator
 * 
 */
public class FileSearch extends Activity { /* ����������� */
	public static final String ROOTPATH = "/";
	public static final String SDROOTPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	/* ������Ŀ¼�ļ���method */
	/*
	 * private String searchRootFile(String keyword) { String result=""; File[]
	 * files=new File("/").listFiles(); for( File f : files ) {
	 * if(f.getName().indexOf(keyword)>=0) { result+=f.getPath()+"\n"; } }
	 * if(result.equals("")) result="�Ҳ����ļ�!!"; return result; }
	 */

	// �ȽϺ��ĵ�Ϊǰ��������
	/**
	 * �����ض�Ŀ¼�µ����а�����keyword�����ļ��������ļ�·���б�
	 * 
	 * @param path
	 *            ��Ҫ������Ŀ¼
	 * @param keyword
	 *            �ؼ��ʲ�����Ϊ��
	 * @return �����ļ�·�����ַ����б�
	 * @throws NullKeyWordException
	 */
	public ArrayList<String> searchFiles(String path, String keyword)
			throws FileNotFoundException, NullKeyWordException {
		ArrayList<String> resultpaths = null;
		if (keyword.equals("")) {
			throw new NullKeyWordException();
		}
		String result;
		File dir = new File(path);
		// ��Ŀ¼������,��throw FileNotFoundException
		if (!dir.exists())
			throw new FileNotFoundException();
		File[] files = dir.listFiles();
		if (files == null)
			return null;
		for (File f : files) {
			// ΪĿ¼
			if (f.isDirectory()) {
				Log.d("sch", f.getName());
				ArrayList<String> tmp = searchFiles(f.getPath(), keyword);
				if (tmp != null) {
					if (resultpaths == null)
						resultpaths = new ArrayList<String>();
					resultpaths.addAll(tmp);
				}
				// Ϊ��ͨ���ļ�
			} else {
				Log.d("sch", f.getName());
				if (f.getName().indexOf(keyword) >= 0) {
					result = f.getPath();
					if (resultpaths == null)
						resultpaths = new ArrayList<String>();
					resultpaths.add(result);
				}
			}
		}
		return resultpaths;
	}

	/**
	 * �����ض�Ŀ¼�µ����а�����keyword�����ļ����ͺ�׺Ϊsuffix���ļ��������ļ�·���б�
	 * 
	 * @param path
	 *            ��Ҫ������Ŀ¼
	 * @param keyword
	 *            �ؼ��ʿ���Ϊ�գ�ǰ����suffix��Ϊ��
	 * @param suffix
	 *            ����Ϊ��
	 * @return
	 * @throws NullSuffixException
	 * @throws FileNotFoundException
	 */
	public ArrayList<String> searchFiles(String path, String keyword,
			String suffix) throws NullSuffixException, FileNotFoundException {
		ArrayList<String> resultpaths = null;
		if (suffix.equals("")) {
			throw new NullSuffixException();
		}
		String result;
		File dir = new File(path);
		// ��Ŀ¼������,��throw FileNotFoundException
		if (!dir.exists())
			throw new FileNotFoundException();
		File[] files = dir.listFiles();
		if (files == null)
			return null;
		for (File f : files) {
			// ΪĿ¼
			if (f.isDirectory()) {
				Log.d("sch", f.getName());
				ArrayList<String> tmp = searchFiles(f.getPath(), keyword,
						suffix);
				if (tmp != null) {
					if (resultpaths == null)
						resultpaths = new ArrayList<String>();
					resultpaths.addAll(tmp);
				}
				// Ϊ��ͨ���ļ�
			} else {
				// ����Ҫkeyword
				Log.d("sch", f.getName());
				if (keyword == "" || keyword == null) {
					if (f.getName().endsWith(suffix)) {
						result = f.getPath();
						if (resultpaths == null)
							resultpaths = new ArrayList<String>();
						resultpaths.add(result);
					}
				} else {
					if (f.getName().endsWith(suffix)
							&& f.getName().indexOf(keyword) >= 0) {
						result = f.getPath();
						resultpaths.add(result);
					}
				}
			}
		}
		return resultpaths;
	}

	/**
	 * ����SD����Ŀ¼�����а����ؼ��ʵ��ļ�
	 * 
	 * @param keyword
	 *            �ؼ��ʣ�����Ϊ��
	 * @return ���������ļ������·��
	 * @throws NullKeyWordException
	 * @throws FileNotFoundException
	 */
	public ArrayList<String> searchSDFiles(String keyword)
			throws FileNotFoundException, NullKeyWordException {
		ArrayList<String> resultpaths = searchFiles(ROOTPATH, keyword);
		return resultpaths;
	}

	/**
	 * ����SD����Ŀ¼�����а����ؼ��ʺ��ض���׺���ļ�
	 * 
	 * @param keyword
	 *            �ؼ��ʿ���Ϊ�գ�ǰ����suffix��Ϊ��
	 * @param suffix
	 *            ����Ϊ��
	 * @return
	 * @throws NullSuffixException
	 * @throws FileNotFoundException
	 */
	public ArrayList<String> searchSDFilesBySuf(String keyword, String suffix)
			throws FileNotFoundException, NullSuffixException {
		ArrayList<String> resultpaths = searchFiles(ROOTPATH, keyword, suffix);
		return resultpaths;
	}

	// ��ΪȨ�����⣬����sd�����Ŀ¼����Ī���Ĵ���Ҳû��Ҫ���ϵͳ���ļ����ݣ���˷�����������
	/**
	 * ������Ŀ¼�����а����ؼ��ʵ��ļ�
	 * 
	 * @param keyword
	 *            �ؼ��ʣ�����Ϊ��
	 * @return ���������ļ������·��
	 * @throws NullKeyWordException
	 * @throws FileNotFoundException
	 */
	/*
	 * public ArrayList<String> searchAllFiles(String keyword) throws
	 * FileNotFoundException, NullKeyWordException{ ArrayList<String>
	 * resultpaths = searchFiles( ROOTPATH, keyword); return resultpaths; }
	 */

	/**
	 * ����Ŀ¼�����а����ؼ��ʺ��ض���׺���ļ�
	 * 
	 * @param keyword
	 *            �ؼ��ʿ���Ϊ�գ�ǰ����suffix��Ϊ��
	 * @param suffix
	 *            ����Ϊ��
	 * @return
	 * @throws NullSuffixException
	 * @throws FileNotFoundException
	 */
	/*
	 * public ArrayList<String> searchAllFilesBySuf(String keyword ,String
	 * suffix) throws FileNotFoundException, NullSuffixException{
	 * ArrayList<String> resultpaths = searchFiles( ROOTPATH, keyword, suffix);
	 * return resultpaths; }
	 */

}