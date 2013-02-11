package net.junaoki.fflo.persistent;

public class Panel {
	private String listId;
	private int width;
	private int height;

	public Panel(){}

	public String getListId() { return listId; }
	public void setListId(String listId) { this.listId = listId; }

	public int getWidth() { return width; }
	public void setWidth(int width) { this.width = width; }
	public void setWidth(double width) { this.width = (int)width; }

	public int getHeight() { return height; }
	public void setHeight(int height) { this.height = height; }
	public void setHeight(double height) { this.height = (int)height; }

}
