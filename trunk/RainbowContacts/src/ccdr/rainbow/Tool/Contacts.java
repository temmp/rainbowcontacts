package ccdr.rainbow.Tool;

import android.graphics.Bitmap;

public class Contacts {
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPy() {
		return py;
	}
	public void setPy(String py) {
		this.py = py;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	private String name;
	private String py;
	private int position;
	private int id;
	private Bitmap  photo;
	public Bitmap  getPhoto() {
		return photo;
	}
	public void setPhoto(Bitmap  photo) {
		this.photo = photo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
