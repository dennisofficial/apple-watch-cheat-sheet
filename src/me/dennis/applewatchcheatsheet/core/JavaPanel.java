package me.dennis.applewatchcheatsheet.core;

import static java.awt.event.KeyEvent.KEY_LAST;
import static java.awt.event.KeyEvent.VK_0;
import static java.awt.event.KeyEvent.VK_1;
import static java.awt.event.KeyEvent.VK_2;
import static java.awt.event.KeyEvent.VK_3;
import static java.awt.event.KeyEvent.VK_4;
import static java.awt.event.KeyEvent.VK_5;
import static java.awt.event.KeyEvent.VK_6;
import static java.awt.event.KeyEvent.VK_7;
import static java.awt.event.KeyEvent.VK_8;
import static java.awt.event.KeyEvent.VK_9;
import static java.awt.event.KeyEvent.VK_BACK_QUOTE;
import static java.awt.event.KeyEvent.VK_BACK_SLASH;
import static java.awt.event.KeyEvent.VK_BACK_SPACE;
import static java.awt.event.KeyEvent.VK_CLOSE_BRACKET;
import static java.awt.event.KeyEvent.VK_COMMA;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_EQUALS;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.event.KeyEvent.VK_MINUS;
import static java.awt.event.KeyEvent.VK_OPEN_BRACKET;
import static java.awt.event.KeyEvent.VK_PERIOD;
import static java.awt.event.KeyEvent.VK_QUOTE;
import static java.awt.event.KeyEvent.VK_SEMICOLON;
import static java.awt.event.KeyEvent.VK_SHIFT;
import static java.awt.event.KeyEvent.VK_SLASH;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.getKeyText;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class JavaPanel extends JPanel implements ActionListener, Runnable {
	
	private List<String> lines = new ArrayList<String>();
	private String output = "";
	private boolean print = false;
	private HashMap<Integer, String[]> chars = new HashMap<Integer, String[]>();
	
	public JavaPanel() {
		setFocusable(true);
		requestFocus();
		
		addKeyListener(new Keyboard());
		
		Keyboard.setupKeys();
		
		new Timer(1000/60, this).start();

		chars.put(VK_SPACE, new String[] {" ", " "});
		chars.put(VK_COMMA, new String[] {",", "<"});
		chars.put(VK_PERIOD, new String[] {".", ">"});
		chars.put(VK_SLASH, new String[] {"/", "?"});
		chars.put(VK_SEMICOLON, new String[] {";", ":"});
		chars.put(VK_QUOTE, new String[] {"'", "\""});
		chars.put(VK_OPEN_BRACKET, new String[] {"[", "{"});
		chars.put(VK_CLOSE_BRACKET, new String[] {"]", "}"});
		chars.put(VK_BACK_SLASH, new String[] {"\\", "|"});
		chars.put(VK_MINUS, new String[]{"-", "_"});
		chars.put(VK_EQUALS, new String[]{"=", "+"});
		chars.put(VK_BACK_QUOTE, new String[]{"`", "~"});
		chars.put(VK_1, new String[]{"1", "!"});
		chars.put(VK_2, new String[]{"2", "@"});
		chars.put(VK_3, new String[]{"3", "#"});
		chars.put(VK_4, new String[]{"4", "$"});
		chars.put(VK_5, new String[]{"5", "%"});
		chars.put(VK_6, new String[]{"6", "^"});
		chars.put(VK_7, new String[]{"7", "&"});
		chars.put(VK_8, new String[]{"8", "*"});
		chars.put(VK_9, new String[]{"9", "("});
		chars.put(VK_0, new String[]{"0", ")"});
		
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(1000);
			BufferedReader reader = new BufferedReader(new FileReader(new File("questions.txt")));
			String line;
			while ((line = reader.readLine()) != null) {
				String question = line.split(";")[0];
				String answer = line.split(";")[1];
				lines.clear();
				lines.add("Q: " + question);
				lines.add("");
				lines.add("A: " + answer);
				saveImage();
			}
			reader.close();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		for (int i = 0; i < KEY_LAST; i++) {
			if (!(i >= VK_0 && i <= VK_9)) {
				if (Keyboard.isDirect(VK_SHIFT)) {
					if (Keyboard.isPressed(i)) {
						if (getKeyText(i).length() == 1) {
							output += getKeyText(i).toUpperCase();
						}
					}
				}
				else {
					if (Keyboard.isPressed(i)) {
						if (getKeyText(i).length() == 1) {
							output += getKeyText(i).toLowerCase();
						}
					}
				}
			}
		}
		otherKeyboardCheck();
		Keyboard.reset();
	}

	public void otherKeyboardCheck() {
		if (Keyboard.isPressed(VK_BACK_SPACE)) {
			if (output.length() > 0) {
				output = output.substring(0, output.length() - 1);
			}
			else {
				if (!lines.isEmpty()) {
					output = lines.get(lines.size() - 1);
					lines.remove(lines.size() - 1);
				}
			}
		}
		else if (Keyboard.isPressed(VK_ENTER)) {
			lines.add(output);
			output = "";
		}
		else if (Keyboard.isPressed(VK_ESCAPE)) {
			saveImage();
		}
		// Special characters
		for (Entry<Integer, String[]> entry : chars.entrySet()) {
			if (Keyboard.isPressed(entry.getKey())) {
				if (Keyboard.isDirect(VK_SHIFT)) {
					output += entry.getValue()[1];
				}
				else {
					output += entry.getValue()[0];
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Font font = null;
		try {
			font = Font.createFont(Font.PLAIN, new File("fonts/main.ttf"));
		}
		catch (FontFormatException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		font = font.deriveFont(30F);
		g2.setFont(font);
		FontMetrics fm = g2.getFontMetrics(font);
		
		g2.setColor(new Color(0x0));
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		List<String> linesWOutput = new ArrayList<String>();
		linesWOutput.addAll(lines);
		if (print) {
			linesWOutput.add(output);
		}
		else {
			linesWOutput.add(output + "|");
		}
		List<String> display = wrapList(linesWOutput, fm);
		linesWOutput = null;
		System.gc();
		
		g2.setColor(new Color(0x4F4F4F));
		int i;
		for (i = 0; i < display.size(); i++) {
			g2.drawString(display.get(i), 5, font.getSize() + 6 + (i * font.getSize()));
		}
		
		repaint();
	}
	
	private void saveImage() {
		BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		print = true;
		paint(image.getGraphics());
		print = false;
		try {
			String path;
			File folder = new File("images/");
			if (!folder.exists()) {
				folder.mkdirs();
			}
			while (true) {
				path = "images/" + new SecureRandom().nextInt() + ".jpg";
				if (!new File(path).exists()) {
					break;
				}
			}
			ImageIO.write(image, "JPEG", new File(path));
		}
		catch (IOException e) {
			System.err.println("Could not save image...");
		}
	}

	private List<String> wrapList(List<String> log, FontMetrics fm) {
		List<String> output = new ArrayList<String>();
		for (int i = 0; i < log.size(); i++) {
			String[] words = log.get(i).split(" ");
			String line = "";
			for (int j = 0; j < words.length; j++) {
				String word = words[j];
				int margin = 20;
				if (fm.stringWidth(word) + margin < getWidth()) {
					if (fm.stringWidth(line + word) + margin < getWidth()) {
						line += word + " ";
					}
					else {
						output.add(line);
						line = "";
						j--;
					}
				}
				else {
					for (int h = 0; h < word.length(); h++) {
						if (fm.stringWidth(line + word.charAt(h)) + margin < getWidth()) {
							line += word.charAt(h);
						}
						else {
							output.add(line);
							line = "";
						}
					}
				}
			}
			output.add(line);
		}
		return output;
	}
	
}
