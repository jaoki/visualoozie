package net.junaoki.fflo.persistent;

import java.util.List;



public class UserConfiguration{
	
	private String userId;
	private Menu menu;
	private MainPanel mainPanel;
	private List<Panel> panels;

	public UserConfiguration(){}

	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }
	
	public Menu getMenu() { return menu; }
	public void setMenu(Menu menu) { this.menu = menu; }

	public void setMainPanel(MainPanel mainPanel) { this.mainPanel = mainPanel; }
	public MainPanel getMainPanel() { return mainPanel; }

	public void setPanels(List<Panel> panels) { this.panels = panels; }
	public List<Panel> getPanels() { return panels; }
	
}
