package org.redeagle.utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.redeagle.BotConfiguration;
import org.redeagle.debugger.Log;


public class PNGDrawer {
	
	private JPanel holder;
	private Graphics2D canvas;
	private BufferedImage images;

	
	public void init(int x, int y) {
		holder = new JPanel();
		holder.setSize(x, y);
		images = new BufferedImage(holder.getWidth(), holder.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		canvas = images.createGraphics();
		canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}
	
	public void draw(String path,int x, int y) {
		try {
			canvas.drawImage(ImageIO.read(new File(BotConfiguration.PATH + "asset/" + path)), x, y, null);
		} catch(Exception e) {
			Log.e("Drawing image ");
		}
	}
	
	public void drawFromURL(String url, int x, int y) {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url) .openConnection();
			connection.setRequestProperty(
			    "User-Agent",
			    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
			canvas.drawImage(ImageIO.read(connection.getInputStream()), x, y, null);
			connection.disconnect();
		} catch(IOException e) {
			Log.e(e.getMessage());
		}
	}
	
	public void drawFromURL(String url, int x, int y, int sizeX,int sizeY) {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url) .openConnection();
			connection.setRequestProperty(
			    "User-Agent",
			    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
			canvas.drawImage(ImageIO.read(connection.getInputStream()), x, y, sizeX, sizeY, null);
			connection.disconnect();
		} catch(IOException e) {
			Log.e(e.getMessage());
		}
	}

	public void drawText(String text, Color color, int x, int y, int rectX, int rectY, String font, int type, int size) {
		Rectangle bounds = new Rectangle(x,y,rectX,rectY);
		drawString(text, new Font(font,type, size), color, bounds, TextAlignment.TOP_LEFT,TextFormat.FIRST_LINE_VISIBLE);	
	}
	
	public void drawText(String text, Color color, int x, int y, int rectX, int rectY, String font, int type, int size, TextAlignment align) {
		Rectangle bounds = new Rectangle(x,y,rectX,rectY);
		drawString(text, new Font(font,type, size), color, bounds, align,TextFormat.FIRST_LINE_VISIBLE);	
	}
	
	private  Rectangle drawString(String text, Font font, Color color, Rectangle bounds, TextAlignment align, int format) {
		if (text == null)
			throw new NullPointerException("The text cannot be null.");
		if (font == null)
		    throw new NullPointerException("The font cannot be null.");
		if (color == null)
		    throw new NullPointerException("The text color cannot be null.");
		if (bounds == null)
		    throw new NullPointerException("The text bounds cannot be null.");
		if (align == null)
		    throw new NullPointerException("The text alignment cannot be null."); 
		Graphics2D g2D = (Graphics2D)canvas;

		AttributedString attributedString = new AttributedString(text);
		attributedString.addAttribute(TextAttribute.FOREGROUND, color);
		attributedString.addAttribute(TextAttribute.FONT, font); 
		
		AttributedCharacterIterator attributedCharIterator = attributedString.getIterator();

		FontRenderContext fontContext = new FontRenderContext(null, !TextFormat.isEnabled(format, TextFormat.NO_ANTI_ALIASING), false);
		LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(attributedCharIterator, fontContext);
		
		Point targetLocation = new Point(bounds.x, bounds.y);
		int nextOffset = 0;

		if (align.isMiddle() || align.isBottom())
		{
		    if (align.isMiddle())
		        targetLocation.y = bounds.y + (bounds.height / 2);
		    if (align.isBottom())
		        targetLocation.y = bounds.y + bounds.height;

		    while (lineMeasurer.getPosition() < text.length())
		    {
		        nextOffset = lineMeasurer.nextOffset(bounds.width);
		        nextOffset = nextTextIndex(nextOffset, lineMeasurer.getPosition(), text);
		        
		        TextLayout textLayout = lineMeasurer.nextLayout(bounds.width, nextOffset, false);
		        
		        if (align.isMiddle())
		            targetLocation.y -= (textLayout.getAscent() + textLayout.getLeading() + textLayout.getDescent()) / 2;
		        if (align.isBottom())
		            targetLocation.y -= (textLayout.getAscent() + textLayout.getLeading() + textLayout.getDescent());
		    }

		    if (TextFormat.isEnabled(format, TextFormat.FIRST_LINE_VISIBLE))
		        targetLocation.y = Math.max(0, targetLocation.y);

		    lineMeasurer.setPosition(0);
		}
		if (align.isRight() || align.isCenter())
		    targetLocation.x = bounds.x + bounds.width;
		Rectangle consumedBounds = new Rectangle(targetLocation.x, targetLocation.y, 0, 0);
		while (lineMeasurer.getPosition() < text.length())
		{
		    nextOffset = lineMeasurer.nextOffset(bounds.width);
		    nextOffset = nextTextIndex(nextOffset, lineMeasurer.getPosition(), text);

		    TextLayout textLayout = lineMeasurer.nextLayout(bounds.width, nextOffset, false);
		    Rectangle2D textBounds = textLayout.getBounds();

		    targetLocation.y += textLayout.getAscent();
		    consumedBounds.width = Math.max(consumedBounds.width, (int)textBounds.getWidth());
		    switch (align)
		    {
		        case TOP_LEFT:
		        case MIDDLE_LEFT:
		        case BOTTOM_LEFT:
		            textLayout.draw(g2D, targetLocation.x, targetLocation.y);
		            break;

		        case TOP:
		        case MIDDLE:
		        case BOTTOM:
		            targetLocation.x = bounds.x + (bounds.width / 2) - (int)(textBounds.getWidth() / 2);
		            consumedBounds.x = Math.min(consumedBounds.x, targetLocation.x);
		            textLayout.draw(g2D, targetLocation.x, targetLocation.y);
		            break;

		        case TOP_RIGHT:
		        case MIDDLE_RIGHT:
		        case BOTTOM_RIGHT:
		            targetLocation.x = bounds.x + bounds.width - (int)textBounds.getWidth();
		            textLayout.draw(g2D, targetLocation.x, targetLocation.y);
		            consumedBounds.x = Math.min(consumedBounds.x, targetLocation.x);
		            break;
		    } 
		    targetLocation.y += textLayout.getLeading() + textLayout.getDescent();
		}
		consumedBounds.height = targetLocation.y - consumedBounds.y;
		
	    return consumedBounds;  
	}
	
	private int nextTextIndex(int nextOffset, int measurerPosition, String text) {
	    for (int i = measurerPosition + 1; i < nextOffset; ++i)
	    {
	        if (text.charAt(i) == '\n')
	            return i;
	    }

	    return nextOffset;
	}
	
	
	public void commit() {
		holder.paintAll(canvas);
	}
	
	public void save(String tag, String uID) {
		try {
			ImageIO.write(images, "png", new File(BotConfiguration.PATH + "asset/generated/" + tag + "_" + uID + ".png"));
		} catch(IOException e) {
			Log.e("Saving image... ");
		}
	}
}





