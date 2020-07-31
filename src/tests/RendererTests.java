package tests;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;

import gui.Button;
import renderer.Renderer;
import world.Game;
import world.entities.item.Lamp;
import world.entities.mobs.Player;

public class RendererTests {

	@Test
	public void test1(){
		Graphics2D g = new Graphics2D() {

			@Override
			public void draw(java.awt.Shape s) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void drawString(String str, int x, int y) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void drawString(String str, float x, float y) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void drawString(AttributedCharacterIterator iterator, int x, int y) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void drawString(AttributedCharacterIterator iterator, float x, float y) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void drawGlyphVector(GlyphVector g, float x, float y) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void fill(java.awt.Shape s) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean hit(java.awt.Rectangle rect, java.awt.Shape s, boolean onStroke) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public GraphicsConfiguration getDeviceConfiguration() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setComposite(Composite comp) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setPaint(java.awt.Paint paint) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setStroke(Stroke s) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setRenderingHint(Key hintKey, Object hintValue) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Object getRenderingHint(Key hintKey) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setRenderingHints(Map<?, ?> hints) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void addRenderingHints(Map<?, ?> hints) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public RenderingHints getRenderingHints() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void translate(int x, int y) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void translate(double tx, double ty) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void rotate(double theta) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void rotate(double theta, double x, double y) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void scale(double sx, double sy) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void shear(double shx, double shy) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void transform(AffineTransform Tx) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setTransform(AffineTransform Tx) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public AffineTransform getTransform() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public java.awt.Paint getPaint() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Composite getComposite() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setBackground(java.awt.Color color) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public java.awt.Color getBackground() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Stroke getStroke() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void clip(java.awt.Shape s) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public FontRenderContext getFontRenderContext() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Graphics create() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public java.awt.Color getColor() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setColor(java.awt.Color c) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setPaintMode() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setXORMode(java.awt.Color c1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Font getFont() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setFont(Font font) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public FontMetrics getFontMetrics(Font f) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public java.awt.Rectangle getClipBounds() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void clipRect(int x, int y, int width, int height) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setClip(int x, int y, int width, int height) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public java.awt.Shape getClip() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setClip(java.awt.Shape clip) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void copyArea(int x, int y, int width, int height, int dx, int dy) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void drawLine(int x1, int y1, int x2, int y2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void fillRect(int x, int y, int width, int height) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void clearRect(int x, int y, int width, int height) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void drawOval(int x, int y, int width, int height) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void fillOval(int x, int y, int width, int height) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean drawImage(Image img, int x, int y, java.awt.Color bgcolor, ImageObserver observer) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean drawImage(Image img, int x, int y, int width, int height, java.awt.Color bgcolor,
					ImageObserver observer) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2,
					ImageObserver observer) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2,
					java.awt.Color bgcolor, ImageObserver observer) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}

		};

		Set<Button> inGameMenuButtons = new HashSet<Button>();

		inGameMenuButtons.add(new Button(0, 0, 0, 0, "newGame"));
		inGameMenuButtons.add(new Button(0, 0, 0, 0, "loadGame"));
		inGameMenuButtons.add(new Button(0, 0, 0, 0, "rButton"));
		inGameMenuButtons.add(new Button(0, 0, 0, 0, "fButton"));
		inGameMenuButtons.add(new Button(0, 0, 0, 0, "spaceButton"));
		inGameMenuButtons.add(new Button(0, 0, 0, 0, "noName"));

		Renderer r = new Renderer(inGameMenuButtons, new Button(0, 0, 0, 0, "menu"), new Button(0, 0, 0, 0, "mapEditor"), new Button(0, 0, 0, 0, "saveGame"), new Button(0, 0, 0, 0, "X"));

		r.renderMenu(g);

		for(int i = 0; i < 7; i++) {
			r.addMessage("Message");
		}

		r.getInventoryIndex(0, 0);
		r.getInventoryIndex(40, 640);

		Game game = new Game();
		
		game.getPlayer().setTile(game.getCurrentRoom().getTile(5, 5));

		r.getTileFromMouse(0,0, game.getPlayer(), game.getCurrentRoom());
		r.render(g, game.getCurrentRoom().getTiles(), game.getPlayer());
		r.switchView();
		r.render(g, game.getCurrentRoom().getTiles(), game.getPlayer());
		
		game.getCurrentRoom().setDark(true);
		r.render(g, game.getCurrentRoom().getTiles(), game.getPlayer());
		game.getPlayer().addInventory(new Lamp());
		game.getPlayer().setHeldItem(0);
		r.render(g, game.getCurrentRoom().getTiles(), game.getPlayer());
		
		r.changeDropMessage("drop");
		r.changeInteractMessage("interact");
		r.changeUseMessage("use");
		
	}
}
