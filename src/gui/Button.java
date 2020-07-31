package gui;

/**
 * This class represents a button that knows its position on screen and has a name that can match it to a method
 * 
 * @author sailymahe
 */
public class Button {
	int x; //left and top respectively of button on screen
	int y;
	
	int width;
	int height;
	
	String buttonName;
	
	/**
	 * Constructs button object putting supplied parameters into fields
	 * 
	 * @param x		left of button
	 * @param y		top of button
	 * @param width		width of button
	 * @param height		height of button
	 * @param buttonName	buttons name to be used to match it to a method
	 */
	public Button(int x, int y, int width, int height, String buttonName) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.buttonName = buttonName;
	}
	
	/**
	 * This method evaluates whether mouse is over this buttons image on screen
	 * 
	 * @param mouseX x of mouse
	 * @param mouseY y of mouse
	 * @return true if mouse is over button on screen
	 */
	public boolean isOver(int mouseX, int mouseY) {
		if(mouseX>x && mouseX<x+width && mouseY>y && mouseY<y+height) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * @return name of this button
	 */
	public String getName() {
		return buttonName;
	}

	/**
	 * @return left/x of this button
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return top/y of this button
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return width of this button
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return height of this button
	 */
	public int getHeight() {
		return height;
	}
}
