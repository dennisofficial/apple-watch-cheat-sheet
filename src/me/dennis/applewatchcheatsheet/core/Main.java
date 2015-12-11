package me.dennis.applewatchcheatsheet.core;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame f = new JFrame("Apple Watch Cheat Sheet");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(new JavaPanel());
		f.setVisible(true);
		f.setSize(312 + 6, 390 + 28);
		f.setResizable(false);
		f.setLocationRelativeTo(null);
	}
	
}
