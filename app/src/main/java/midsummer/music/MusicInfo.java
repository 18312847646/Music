package midsummer.music;

/**
 * 项目名称：Mu
 * 类描述：
 * 创建人：77.
 * 创建时间：2015/8/26 0026 20:55
 * 修改人：77.
 * 修改时间：2015/8/26 0026 20:55
 * 修改备注：
 */
public class MusicInfo
{
	private String musicPath;
	private String musicName;
	private int duration;
	
	public int getDuration()
	{
		return duration;
	}
	
	public void setDuration(int pDuration)
	{
		duration = pDuration;
	}
	
	public String getMusicName()
	{
		return musicName;
	}
	
	public void setMusicName(String pMusicName)
	{
		musicName = pMusicName;
	}
	
	public String getMusicPath()
	{
		return musicPath;
	}
	
	public void setMusicPath(String pMusicPath)
	{
		musicPath = pMusicPath;
	}
}
