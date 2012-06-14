package ccdr.rainbow.Tool;

public class PathSelectItem {
	private String m_FilePath;
	private String m_FileName;
	
	public PathSelectItem(String path,String name){m_FilePath=path;m_FileName=name;}
	
	public void setFilePath(String path){m_FilePath=path;}
	public void setFileName(String name){m_FileName=name;}
	
	public String getFilePath(){return m_FilePath;}
	public String getFileName(){return m_FileName;}
}
