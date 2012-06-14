package ccdr.rainbow.Tool;

/* import相关class */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import ccdr.rainbow.Exception.NullKeyWordException;
import ccdr.rainbow.Exception.NullSuffixException;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

/**
 * 文件搜索类，使用之前确认程序获得权限
 * 
 * @author Administrator
 * 
 */
public class FileSearch extends Activity { /* 声明对象变量 */
	public static final String ROOTPATH = "/";
	public static final String SDROOTPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	/* 搜索根目录文件的method */
	/*
	 * private String searchRootFile(String keyword) { String result=""; File[]
	 * files=new File("/").listFiles(); for( File f : files ) {
	 * if(f.getName().indexOf(keyword)>=0) { result+=f.getPath()+"\n"; } }
	 * if(result.equals("")) result="找不到文件!!"; return result; }
	 */

	// 比较核心的为前两个函数
	/**
	 * 搜索特定目录下的所有包含“keyword”的文件，返回文件路径列表
	 * 
	 * @param path
	 *            需要搜索的目录
	 * @param keyword
	 *            关键词不可以为空
	 * @return 返回文件路径的字符串列表
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
		// 该目录不存在,会throw FileNotFoundException
		if (!dir.exists())
			throw new FileNotFoundException();
		File[] files = dir.listFiles();
		if (files == null)
			return null;
		for (File f : files) {
			// 为目录
			if (f.isDirectory()) {
				Log.d("sch", f.getName());
				ArrayList<String> tmp = searchFiles(f.getPath(), keyword);
				if (tmp != null) {
					if (resultpaths == null)
						resultpaths = new ArrayList<String>();
					resultpaths.addAll(tmp);
				}
				// 为普通的文件
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
	 * 搜索特定目录下的所有包含“keyword”的文件，和后缀为suffix的文件，返回文件路径列表
	 * 
	 * @param path
	 *            需要搜索的目录
	 * @param keyword
	 *            关键词可以为空，前提是suffix不为空
	 * @param suffix
	 *            不能为空
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
		// 该目录不存在,会throw FileNotFoundException
		if (!dir.exists())
			throw new FileNotFoundException();
		File[] files = dir.listFiles();
		if (files == null)
			return null;
		for (File f : files) {
			// 为目录
			if (f.isDirectory()) {
				Log.d("sch", f.getName());
				ArrayList<String> tmp = searchFiles(f.getPath(), keyword,
						suffix);
				if (tmp != null) {
					if (resultpaths == null)
						resultpaths = new ArrayList<String>();
					resultpaths.addAll(tmp);
				}
				// 为普通的文件
			} else {
				// 不需要keyword
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
	 * 搜索SD卡根目录下所有包含关键词的文件
	 * 
	 * @param keyword
	 *            关键词，不能为空
	 * @return 返回搜索文件结果的路径
	 * @throws NullKeyWordException
	 * @throws FileNotFoundException
	 */
	public ArrayList<String> searchSDFiles(String keyword)
			throws FileNotFoundException, NullKeyWordException {
		ArrayList<String> resultpaths = searchFiles(ROOTPATH, keyword);
		return resultpaths;
	}

	/**
	 * 搜索SD卡根目录下所有包含关键词和特定后缀的文件
	 * 
	 * @param keyword
	 *            关键词可以为空，前提是suffix不为空
	 * @param suffix
	 *            不能为空
	 * @return
	 * @throws NullSuffixException
	 * @throws FileNotFoundException
	 */
	public ArrayList<String> searchSDFilesBySuf(String keyword, String suffix)
			throws FileNotFoundException, NullSuffixException {
		ArrayList<String> resultpaths = searchFiles(ROOTPATH, keyword, suffix);
		return resultpaths;
	}

	// 因为权限问题，搜索sd卡外的目录会有莫名的错误，也没必要获得系统的文件内容，因此废弃两个函数
	/**
	 * 搜索根目录下所有包含关键词的文件
	 * 
	 * @param keyword
	 *            关键词，不能为空
	 * @return 返回搜索文件结果的路径
	 * @throws NullKeyWordException
	 * @throws FileNotFoundException
	 */
	/*
	 * public ArrayList<String> searchAllFiles(String keyword) throws
	 * FileNotFoundException, NullKeyWordException{ ArrayList<String>
	 * resultpaths = searchFiles( ROOTPATH, keyword); return resultpaths; }
	 */

	/**
	 * 搜索目录下所有包含关键词和特定后缀的文件
	 * 
	 * @param keyword
	 *            关键词可以为空，前提是suffix不为空
	 * @param suffix
	 *            不能为空
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