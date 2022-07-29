package org.shikhar;


public interface DefaultGuiEventsHandler {

	public void onAction(Object widget, String actionName);
	
	public void onPerform(Object widget,String actionName, Object item);
		
	public void onDialogClose(Object dlg);
	
	
}
