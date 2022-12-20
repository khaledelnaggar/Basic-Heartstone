package game.view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.heroes.Hero;
@SuppressWarnings("serial")
public class GameView extends JFrame{
	private JPanel Player;
	private JPanel currentPlayer;
	private JPanel opponentPlayer;
	private JPanel upperPlayer;
	private JPanel lowerPlayer;
	private JPanel middleSec;
	private JPanel upperMiddle;
	private JPanel lowerMiddle;
	private JPanel fieldPanel;
	private JPanel actionPanel;
	private JButton HeroUpPresenter=new JButton();
	private JButton HeroDownPresenter=new JButton();
	private JButton endTurn;
	private JButton UseHeroPower;
	private JButton addToField;
	private JButton attackHero;
	private JButton attackMinion;
	private JLabel label;
	private TextArea text1;
	private TextArea text2;
	private Hero h1;
	private Hero h2;
	private JLabel winner;
	//*****************************************************************************************************
	private ArrayList<JButton>cPlayerButtons;
	private ArrayList<JButton>oPlayerButtons;
	private ArrayList<JButton>upperField;
	private ArrayList<JButton>lowerField;
	//*********************************************************************************************************

	
	
public GameView(){
	this.setTitle("HearthStone");
	this.setBounds(250, 20, 1400, 1000);
	this.setVisible(true);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	Player=new JPanel();
	Player.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()*3/4));
	Player.setLayout(new GridLayout(1, 5));
	label=new JLabel();
	label.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()/5));
	label.setBackground(Color.BLACK);
	label.setIcon(new ImageIcon("materials/Logo.png"));
	JPanel x=new JPanel(new GridLayout(1, 3));
	x.setBackground(Color.BLACK);
	JLabel y=new JLabel();
	y.setBackground(Color.BLACK);
	JLabel z=new JLabel();
	z.setBackground(Color.BLACK);
	x.add(y);
	x.add(label);
	x.add(z);
	
	this.add(x,BorderLayout.NORTH);
	//this.add(label,BorderLayout.NORTH);
	this.add(Player,BorderLayout.CENTER);
	this.revalidate();
	this.repaint();
}
//*****************************************************************************************************************************************
public GameView(int i){
	this.setTitle("HearthStone");
	this.setBounds(250, 20, 1400, 1000);
	this.setVisible(false);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.setBackground(Color.BLACK);
	cPlayerButtons=new ArrayList<JButton>();
	oPlayerButtons=new ArrayList<JButton>();
	upperField=new ArrayList<JButton>();
	lowerField=new ArrayList<JButton>();

	middleSec=new JPanel();
	middleSec.setLayout(new BorderLayout());
	actionPanel=new JPanel();
	actionPanel.setLayout(new GridLayout(5,1));
	fieldPanel=new JPanel();
	fieldPanel.setLayout(new GridLayout(2,1));
	upperMiddle=new JPanel();
	upperMiddle.setLayout(new FlowLayout());
	lowerMiddle=new JPanel();
	lowerMiddle.setLayout(new FlowLayout());
	endTurn=new JButton("EndTurn");
	UseHeroPower=new JButton("UseHeroPower");
	addToField=new JButton("Add To Field");
	attackHero=new JButton("AttackHero");
	attackMinion= new JButton("AttackMinion");
	middleSec.add(actionPanel,BorderLayout.EAST);
	middleSec.add(fieldPanel,BorderLayout.CENTER);
	upperMiddle.setPreferredSize(new Dimension(middleSec.getWidth(),middleSec.getHeight()/2));
	upperMiddle.setBackground(Color.darkGray);
	fieldPanel.add(upperMiddle);
	lowerMiddle.setPreferredSize(new Dimension(middleSec.getWidth(),middleSec.getHeight()/2));
	lowerMiddle.setBackground(Color.CYAN);
	fieldPanel.add(lowerMiddle);
	actionPanel.add(endTurn);
	actionPanel.add(UseHeroPower);
	actionPanel.add(addToField);
	actionPanel.add(attackHero);
	actionPanel.add(attackMinion);
	
	
	
	
	
	//**************************************************************************************

	currentPlayer=new JPanel();
	opponentPlayer=new JPanel();
	upperPlayer=new JPanel();
	lowerPlayer=new JPanel();
	upperPlayer.setLayout(new BorderLayout());
	lowerPlayer.setLayout(new BorderLayout());
	text1=new TextArea();
	text2=new TextArea();
	text1.setEditable(false);

	text2.setEditable(false);
	currentPlayer.setLayout(new GridLayout(1,11));
	opponentPlayer.setLayout(new GridLayout(1,11));
	currentPlayer.setPreferredSize(new Dimension(this.getWidth(),this.getHeight()/5));
	opponentPlayer.setPreferredSize(new Dimension(this.getWidth(),this.getHeight()/5));

	currentPlayer.setBackground(Color.BLACK);
	opponentPlayer.setBackground(Color.BLUE);
	text1.setBackground(Color.GREEN);
	text2.setBackground(Color.RED);
	text1.setVisible(true);
	text2.setVisible(true);
	currentPlayer.add(text1);
	opponentPlayer.add(text2);
	upperPlayer.add(text2,BorderLayout.LINE_START);
	lowerPlayer.add(text1,BorderLayout.LINE_START);
	
	upperPlayer.add(opponentPlayer,BorderLayout.CENTER);
	lowerPlayer.add(currentPlayer,BorderLayout.CENTER);
	this.add(middleSec,BorderLayout.CENTER);
	this.add(lowerPlayer,BorderLayout.PAGE_END);
	this.add(upperPlayer,BorderLayout.PAGE_START);


	
	this.revalidate();
	this.repaint();
	
	
}

	public GameView(String x) {
		this.setTitle("HearthStone");
		this.setBounds(600,250,500,300);
		this.setVisible(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		winner = new JLabel("Congratulations "+ x+ " you have won the game!");
		winner.setPreferredSize(new Dimension(this.getWidth(),this.getHeight()));
		/* try {
	            this.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("materials/hearthstone-wallpapers.jpg")))));
	        } 
		 catch (IOException e) {
	            e.printStackTrace();
	        }*/
		 this.add(winner);
	        this.pack();
	        

		
	}
	



public JPanel getCurrentPlayer() {
	return currentPlayer;
}
public JPanel getOpponentPlayer() {
	return opponentPlayer;
}
public JPanel getUpperMiddle() {
	return upperMiddle;
}
public JPanel getLowerMiddle() {
	return lowerMiddle;
}
public JButton getEndTurn() {
	return endTurn;
}
public JButton getUseHeroPower() {
	return UseHeroPower;
}
public TextArea getText1() {
	return text1;
}
public TextArea getText2() {
	return text2;
}
public JPanel getPlayer() {
	return Player;
}

public JLabel getLabel() {
	return label;
}


public JButton getAttackHero() {
	return attackHero;
}
public JButton getAttackMinion() {
	return attackMinion;
}
public ArrayList<JButton> getUpperField() {
	return upperField;
}
public ArrayList<JButton> getLowerField() {
	return lowerField;
}
public ArrayList<JButton> getcPlayerButtons() {
	return cPlayerButtons;
}
public ArrayList<JButton> getoPlayerButtons() {
	return oPlayerButtons;
}
public JButton getAddToField() {
	return addToField;
}
public Hero getCurrHero() {
	return h1;
}
public Hero getOppHero() {
	return h2;
}
public void setCurrHero(Hero h1) {
	this.h1 = h1;
}
public void setOppHero(Hero h2) {
	this.h2 = h2;
}



public JButton getHeroUpPresenter() {
	return HeroUpPresenter;
}

public JButton getHeroDownPresenter() {
	return HeroDownPresenter;
}

public void playSound(String filePath){
	try {
	
		AudioInputStream audioInputStream=AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
		Clip clip=AudioSystem.getClip();
		clip.open(audioInputStream);
		clip.start();
		
	} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

public static void main(String[]args){
	GameView x=new GameView(1);
	x.setVisible(true);
}




}
