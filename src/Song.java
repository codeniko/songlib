import java.util.Vector;

public class Song 
{
	/**
	 * @author Nikolay Feldman
	 */
	
	private String name, artist, album, year;
	private static Vector<String> list;
	
	public Song(String name, String artist, String album, String year)
	{
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
	public void setData(String name, String artist, String album, String year)
	{
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
	public String[] getData()
	{
		String[] data = new String[4];
		data[0] = name;
		data[1] = artist;
		data[2] = album;
		data[3] = year;
		return data;
	}
	
	public static void setList(Vector<String> l)
	{
		list = l;
	}
	
	public static Vector<String> getList()
	{
		return list;
	}
}
