package game.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import model.cards.Card;
import model.cards.minions.Icehowl;
import model.cards.minions.Minion;
import model.cards.spells.AOESpell;
import model.cards.spells.FieldSpell;
import model.cards.spells.HeroTargetSpell;
import model.cards.spells.LeechingSpell;
import model.cards.spells.MinionTargetSpell;
import model.cards.spells.Spell;
import model.heroes.Hero;
import model.heroes.Hunter;
import model.heroes.Mage;
import model.heroes.Paladin;
import model.heroes.Priest;
import model.heroes.Warlock;
import engine.Game;
import engine.GameListener;
import exceptions.CannotAttackException;
import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughManaException;
import exceptions.NotSummonedException;
import exceptions.NotYourTurnException;
import exceptions.TauntBypassException;
import game.view.GameView;

public class Controller implements ActionListener,GameListener{
private Game model;
private GameView view;
private JPanel viewbottom;
private JPanel tmp1;
private JPanel tmp2;
private GameView body;
private Hero h1=null;
private Hero h2=null;
private Hero a=new Hunter();
private Hero b= new Mage();
private Hero c=new Paladin();
private Hero d=new Priest();
private Hero e=new Warlock();
private JButton selectedCard;
private JButton select;
private JButton SelectedButton;
private ArrayList<Hero>x=new ArrayList<Hero>();
private GameView gameOver;
private Stack<JButton> store;

public Controller() throws IOException, CloneNotSupportedException{
	//playSoundLoop ("materials/MainTitle.wav");
	gameOver=new GameView("");
	selectedCard=new JButton();
	SelectedButton=new JButton();
	store=new Stack<JButton>();
	x.add(a);
	x.add(b);
	x.add(c);
	x.add(d);
	x.add(e);
	view=new GameView();
	body=new GameView(0);
	changeFontSize(body.getText1());
	changeFontSize(body.getText2());

	body.getUseHeroPower().addActionListener(this);
	body.getEndTurn().addActionListener(this);
	body.getAddToField().addActionListener(this);
	body.getAttackHero().addActionListener(this);
	body.getAttackMinion().addActionListener(this);
	select=new JButton();
	select.setBorder(null);
	//select.setPressedIcon(new ImageIcon("materials/hiclip2.png"));
	select.setFocusPainted(false);
	select.setActionCommand("Select");
	select.setPreferredSize(new Dimension(view.getWidth(), view.getHeight()/7));
	viewbottom=new JPanel(new GridLayout(1, 3));
	viewbottom.setBackground(Color.BLACK);
	tmp1=new JPanel();
	tmp1.setBackground(Color.BLACK);
	tmp2=new JPanel();
	tmp2.setBackground(Color.BLACK);
	view.add(viewbottom,BorderLayout.SOUTH);
	viewbottom.add(tmp1);
	viewbottom.add(select);
	viewbottom.add(tmp2);
	select.addActionListener(this);
	select.setIcon(new ImageIcon("materials/giphy.gif"));
	select.setBackground(Color.BLACK);
	select.setBorderPainted(false);
	
	
	for(int i=0;i<x.size();i++){
		JButton y =new JButton(x.get(i).getName());
		y.setBackground(Color.BLACK);
		y.setBorderPainted(false);
		y.setFocusPainted(false);
		switch(x.get(i).getName()){
		case("Jaina Proudmoore"): y.setIcon(new ImageIcon("materials/jaina proudmoore.png"));break;
		case("Rexxar"):y.setIcon(new ImageIcon("materials/Rexxar.png"));break;
		case("Gul'dan"):y.setIcon(new ImageIcon("materials/Gul'dan.png"));break;
		case("Uther Lightbringer"):y.setIcon(new ImageIcon("materials/uther-lightbringer.png"));break;
		case("Anduin Wrynn"):y.setIcon(new ImageIcon("materials/anduin wrynn.png"));break;
		}
		y.addActionListener(this);
		view.getPlayer().add(y);
	}
	//view.getLabel().setText("Welcome to HearthStone Where Cards Means More!!....."+"\nFirst Player Choose Your Hero!");
	view.revalidate();
	view.repaint();


	
}
public static String toString(Hero x){
	return "Name: "+x.getName()+"\nHP: "+x.getCurrentHP()+"\nManaCrystals : "+x.getCurrentManaCrystals()+"/"+x.getTotalManaCrystals()+"\nCards in Deck: "+x.getDeck().size();
}
public static String toString(Card x){
	String s="";
	if(x instanceof Minion){
	 s="    Minion"+"\nName: "+x.getName()+"\nMana Cost: "+x.getManaCost()+"\nRarity: "+x.getRarity()+"\n Attack"+((Minion)x).getAttack()+"\nCurrent HP: "+((Minion)x).getCurrentHP()+"\nTaunt: "+((Minion)x).isTaunt()+"\nDivine: "+((Minion)x).isDivine()+"\nSleeping: "+((Minion)x).isSleeping();
	}
	else if(x instanceof Spell){
		s="   Spell"+"\nName: "+x.getName()+"\nMana Cost: "+x.getManaCost()+"\nRarity: "+x.getRarity();
	}
	return s;
}
//**************************************************************************************************************
@Override
public void actionPerformed(ActionEvent e){
		JButton b=(JButton)(e.getSource());
		playSound("materials/click.wav");
	if(view.isVisible()){
	if(!b.getActionCommand().equals("Select")){
		SelectedButton=b;
		switch(SelectedButton.getActionCommand()){
		case("Jaina Proudmoore"):playSound("materials/Jaina Proudmoore (Mage) greeting[2].wav");break;
		case("Gul'dan"):playSound("materials/Gul'dan (Warlock) greetings.wav");break;
		case("Uther Lightbringer"):playSound("materials/Uther Lightbringer (Paladin) greetings.wav");break;
		case("Anduin Wrynn"):playSound("materials/Anduin Wrynn (Priest) greetings.wav");break;
		case("Rexxar"):playSound("materials/Rexxar (Hunter) greetings.wav");
		
		}
	
		return;
	}
	else if(b.getActionCommand().equals("Select")&& SelectedButton.getActionCommand().equals(""))
		return;
	else if(h1==null&&h2==null){
	if(b.getActionCommand().equals("Select")){
			for(int i=0;i<x.size();i++){
				if(x.get(i).getName().equals(SelectedButton.getActionCommand())){
					h1=x.get(i);
					
					
					SelectedButton=null;
					//view.getLabel().setText("You Chose "+x.get(i).getName()+" Nice Choice!.............."+" You There Second Player Choose Your Hero!!");
					return;
				}}}}

else if(h1!=null&&h2==null){
	if(b.getActionCommand().equals("Select")){
		//********************************************************************************
			for(int i=0;i<x.size();i++){
				if(x.get(i).getName().equals(SelectedButton.getActionCommand())){
					if(h1==x.get(i)){
						try{
						if(x.get(i) instanceof Mage)
							h2=new Mage();
						if(x.get(i) instanceof Hunter)
							h2= new Hunter();
						if(x.get(i) instanceof Warlock)
							h2=new Warlock();
						if(x.get(i) instanceof Priest)
						h2=new Priest();
						if(x.get(i)instanceof Paladin)
							h2=new Paladin();
						}
						catch(IOException | CloneNotSupportedException e1){
							e1.printStackTrace();
							
						}
					}
					else{
					h2=x.get(i); 
					SelectedButton=null;
					}
					//view.getLabel().setText("You Chose "+x.get(i).getName()+" NOOOOOOIIIIICEEEEEEE! Let The Game BEGIIIIINNNNNNNN!!");

					if(h1!=null&&h2!=null){
						try {
							model=new Game(h1, h2);
							model.setListener(this);
							body.setCurrHero(model.getCurrentHero());
							body.setOppHero(model.getOpponent());
							this.onFieldUpdate();
							view.setVisible(false);
							body.setVisible(true);
							return;
					
						} catch (FullHandException | CloneNotSupportedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}}}}}}
	}
	else if(body.isVisible()){
		 store.push(b);
		 System.out.println(store.peek().getActionCommand());
 
		if(b.getActionCommand().equals("EndTurn")){
			try {
				model.endTurn();
				this.onFieldUpdate();
				body.revalidate();
				body.repaint();
				return;
			} catch (FullHandException | CloneNotSupportedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		//*********************************************************************************************************
		//another button
		//*********************************************************************************************************
		else if(b.getActionCommand().equals("UseHeroPower")){
	
				store.pop();
				this.useHeroPowerButton();
	}
		//*********************************************************************************************************
		//another button
		//*********************************************************************************************************
		else if(b.getActionCommand().equals("Add To Field")){
			store.pop();
				if(body.getCurrHero()==model.getCurrentHero()&&body.getcPlayerButtons().contains(store.peek())){
					int index=body.getcPlayerButtons().indexOf(store.peek());
					if(model.getCurrentHero().getHand().get(index) instanceof Minion){
					try {model.getCurrentHero().playMinion((Minion)model.getCurrentHero().getHand().get(index));
						this.onFieldUpdate();
						body.revalidate();
						body.repaint();
						return;
					} catch (NotYourTurnException | NotEnoughManaException
							| FullFieldException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					catch(Throwable e1){
						e1.printStackTrace();
					}
				
					}
					else if(model.getCurrentHero().getHand().get(index) instanceof AOESpell||model.getCurrentHero().getHand().get(index) instanceof FieldSpell){
						this.castSpell((Spell)model.getCurrentHero().getHand().get(index));
					}		
			}
					else if(body.getOppHero()==model.getCurrentHero()&&body.getoPlayerButtons().contains(store.peek())){
					int index=body.getoPlayerButtons().indexOf(store.peek());
					if(model.getCurrentHero().getHand().get(index) instanceof Minion){
					try {
						model.getCurrentHero().playMinion((Minion)model.getCurrentHero().getHand().get(index));
						this.onFieldUpdate();
						body.revalidate();
						body.repaint();
						store.clear();
						return;
					} catch (NotYourTurnException | NotEnoughManaException
							| FullFieldException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					catch(Throwable e1){
						e1.printStackTrace();
					}
					}
					else if(model.getCurrentHero().getHand().get(index) instanceof AOESpell||model.getCurrentHero().getHand().get(index) instanceof FieldSpell){
						this.castSpell((Spell)model.getCurrentHero().getHand().get(index));
					}		
					
					else
						return;
				}	
					else
						this.addSpell();
				}
		else if(b.getActionCommand().equals("AttackMinion")){
				store.pop();
				if(body.getCurrHero()==model.getCurrentHero()){
					SelectedButton=store.peek();
					boolean friends=false;
					int index1;
					int index2;
					if(body.getUpperField().contains(store.pop())&&body.getUpperField().contains(store.peek())){
						friends=true;
							}
						else if(body.getLowerField().contains(store.peek())){
							selectedCard=store.pop();
							try {
								index2=body.getLowerField().indexOf(selectedCard);
								if(friends){
								index1=body.getLowerField().indexOf(SelectedButton);
								model.getCurrentHero().attackWithMinion(model.getCurrentHero().getField().get(index2), model.getCurrentHero().getField().get(index1));
								}
								else
								{index1=body.getUpperField().indexOf(SelectedButton);
								model.getCurrentHero().attackWithMinion(model.getCurrentHero().getField().get(index2), model.getOpponent().getField().get(index1));
								}
								this.onFieldUpdate();
								body.revalidate();
								body.repaint();
								return;
							} 
							catch (CannotAttackException | NotYourTurnException
									| TauntBypassException | InvalidTargetException
									| NotSummonedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							
							
						}
							catch(Throwable e1){
								e1.printStackTrace();
							}
					}
					}
					
				else if(body.getOppHero()==model.getCurrentHero()){
					SelectedButton=store.peek();
					boolean friends=false;
					int index1;
					int index2;
					if(body.getLowerField().contains(store.pop())&&body.getLowerField().contains(store.peek()))
						friends=true;
						
					else if (body.getUpperField().contains(store.peek())){
						selectedCard=store.pop();
						try {
							index2=body.getUpperField().indexOf(selectedCard);
							if(friends){
								index1=body.getUpperField().indexOf(SelectedButton);
								model.getCurrentHero().attackWithMinion(model.getCurrentHero().getField().get(index2), model.getCurrentHero().getField().get(index1));
							}
							else{
							index1=body.getLowerField().indexOf(SelectedButton);
							model.getCurrentHero().attackWithMinion(model.getCurrentHero().getField().get(index2), model.getOpponent().getField().get(index1));
							}
							this.onFieldUpdate();
							body.revalidate();
							body.repaint();
							return;
						
						} catch (CannotAttackException | NotYourTurnException
								| TauntBypassException | InvalidTargetException
								| NotSummonedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						catch(Throwable e1){
							e1.printStackTrace();
						}
						}
				}
				else 
					return;	
		}
	else if(b.getActionCommand().equals("AttackHero")){
		store.pop();
		if(((JButton)store.peek()).getActionCommand().equals(model.getOpponent().getName())||((JButton)store.peek()).getActionCommand().equals(model.getCurrentHero().getName()))
			store.pop();
		if(body.getCurrHero()==model.getCurrentHero()){
			if(body.getLowerField().contains(store.peek())){
				int index1=body.getLowerField().indexOf(store.peek());
				
					try {
						model.getCurrentHero().attackWithMinion(model.getCurrentHero().getField().get(index1), model.getOpponent());
						this.onFieldUpdate();
						body.revalidate();
						body.repaint();
						return;
					} catch (CannotAttackException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NotYourTurnException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (TauntBypassException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NotSummonedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InvalidTargetException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			
		
			
				
				
			}
			else return;
			
		}
		else if(body.getOppHero()==model.getCurrentHero()){
			
			if(body.getUpperField().contains(store.peek())){
				int index1=body.getUpperField().indexOf(store.peek());
				
					try {
						model.getCurrentHero().attackWithMinion(model.getCurrentHero().getField().get(index1), model.getOpponent());
						this.onFieldUpdate();
						body.revalidate();
						body.repaint();
						return;
					} catch (CannotAttackException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NotYourTurnException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (TauntBypassException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NotSummonedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InvalidTargetException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				
				
			}
			else return;
		}
	}
	}
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
public void playSoundLoop(String filePath){
	try {

		AudioInputStream audioInputStream=AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
		Clip clip=AudioSystem.getClip();
	
		clip.open(audioInputStream);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		clip.start();
		
	} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}


@Override
public void onGameOver() {
body.setVisible(false);
gameOver.setVisible(true);
	
}

public static void main(String[]args) throws IOException, CloneNotSupportedException{
	Controller z=new Controller();


}
public static void changeFontSize(TextArea z){
	Font f=z.getFont();
	Font f2=new Font(f.getFontName(),f.getStyle(),f.getSize()+3);
	z.setFont(f2);
	return;
}
public void onFieldUpdate(){
body.getLowerField().clear();
body.getLowerMiddle().removeAll();
body.getUpperField().clear();
body.getUpperMiddle().removeAll();
body.getcPlayerButtons().clear();
body.getCurrentPlayer().removeAll();
body.getoPlayerButtons().clear();
body.getOpponentPlayer().removeAll();
if(body.getCurrHero()==model.getCurrentHero()){
	for (int i = 0; i < model.getCurrentHero().getField().size(); i++) {
		JButton y=new JButton("<html>"+ toString(model.getCurrentHero().getField().get(i)).replaceAll("\n", "<br>")+ "<html>");
		y.addActionListener(this);
		body.getLowerMiddle().add(y);
		body.getLowerField().add(y);
		
	}
	body.getHeroDownPresenter().setText(model.getCurrentHero().getName());
	body.getHeroDownPresenter().setActionCommand(model.getCurrentHero().getName());
	body.getHeroDownPresenter().setVisible(true);
	body.getHeroDownPresenter().addActionListener(this);
	body.getCurrentPlayer().add(body.getHeroDownPresenter());
	body.getHeroUpPresenter().setText(model.getOpponent().getName());
	body.getHeroUpPresenter().setActionCommand(model.getOpponent().getName());
	body.getHeroUpPresenter().setVisible(true);
	body.getHeroUpPresenter().addActionListener(this);
	body.getOpponentPlayer().add(body.getHeroUpPresenter());

	for (int i = 0; i < model.getCurrentHero().getHand().size(); i++) {
		JButton y=new JButton("<html>"+ toString(model.getCurrentHero().getHand().get(i)).replaceAll("\n", "<br>")+ "<html>");
		y.addActionListener(this);
		body.getCurrentPlayer().add(y);
		body.getcPlayerButtons().add(y);
		
	}
	for (int i = 0;i<model.getOpponent().getField().size(); i++) {
		JButton y=new JButton("<html>"+ toString(model.getOpponent().getField().get(i)).replaceAll("\n", "<br>")+ "<html>");
		y.addActionListener(this);
		body.getUpperMiddle().add(y);
		body.getUpperField().add(y);
		
	}
	for (int i = 0; i < model.getOpponent().getHand().size(); i++) {
		JButton y=new JButton("<html>"+ toString(model.getOpponent().getHand().get(i)).replaceAll("\n", "<br>")+ "<html>");
		y.addActionListener(this);
		body.getOpponentPlayer().add(y);
		body.getoPlayerButtons().add(y);
		
	}
	
}
else{
	for (int i = 0; i < model.getOpponent().getField().size(); i++) {
		JButton y=new JButton("<html>"+ toString(model.getOpponent().getField().get(i)).replaceAll("\n", "<br>")+ "<html>");
		y.addActionListener(this);
		body.getLowerMiddle().add(y);
		body.getLowerField().add(y);
		
	}
	body.getHeroDownPresenter().setText(model.getOpponent().getName());
	body.getHeroDownPresenter().setActionCommand(model.getOpponent().getName());
	body.getHeroDownPresenter().setVisible(true);
	body.getHeroDownPresenter().addActionListener(this);
	body.getCurrentPlayer().add(body.getHeroDownPresenter());
	body.getHeroUpPresenter().setText(model.getCurrentHero().getName());
	body.getHeroUpPresenter().setActionCommand(model.getCurrentHero().getName());
	body.getHeroUpPresenter().setVisible(true);
	body.getHeroUpPresenter().addActionListener(this);
	body.getOpponentPlayer().add(body.getHeroUpPresenter());
	for (int i = 0; i < model.getOpponent().getHand().size(); i++) {
		JButton y=new JButton("<html>"+ toString(model.getOpponent().getHand().get(i)).replaceAll("\n", "<br>")+ "<html>");
		y.addActionListener(this);
		body.getCurrentPlayer().add(y);
		body.getcPlayerButtons().add(y);
		
	}
	for (int i = 0;i<model.getCurrentHero().getField().size(); i++) {
		JButton y=new JButton("<html>"+ toString(model.getCurrentHero().getField().get(i)).replaceAll("\n", "<br>")+ "<html>");
		y.addActionListener(this);
		body.getUpperMiddle().add(y);
		body.getUpperField().add(y);
		
	}
	for (int i = 0; i < model.getCurrentHero().getHand().size(); i++) {
		JButton y=new JButton("<html>"+ toString(model.getCurrentHero().getHand().get(i)).replaceAll("\n", "<br>")+ "<html>");
		y.addActionListener(this);
		body.getOpponentPlayer().add(y);
		body.getoPlayerButtons().add(y);
		
	}
	
}
//******************************************************
body.getText2().setText(toString(body.getOppHero()));
body.getText1().setText(toString(body.getCurrHero()));
//************************************************************
if(body.getOppHero()==model.getOpponent()){
	//********************************************************
	body.getText2().setText(toString(body.getOppHero())+"\nCards in Hand: "+model.getOpponent().getHand().size());
	//*******************************************************
	body.getOpponentPlayer().removeAll();
	body.getHeroUpPresenter().setText(model.getOpponent().getName());
	body.getHeroUpPresenter().setActionCommand(model.getOpponent().getName());
	body.getHeroUpPresenter().setVisible(true);
	body.getHeroUpPresenter().addActionListener(this);
	body.getOpponentPlayer().add(body.getHeroUpPresenter());
	for(int i=0;i<model.getOpponent().getHand().size();i++){
		JButton y=new JButton("****");
		body.getOpponentPlayer().add(y);
	}
}
else{
	//***********************************************************************************
	body.getText1().setText(toString(body.getCurrHero())+"\nCards in Hand: "+model.getOpponent().getHand().size());
	//***********************************************************************************
	body.getCurrentPlayer().removeAll();
	body.getHeroDownPresenter().setText(model.getOpponent().getName());
	body.getHeroDownPresenter().setActionCommand(model.getOpponent().getName());
	body.getHeroDownPresenter().setVisible(true);
	body.getHeroDownPresenter().addActionListener(this);
	body.getCurrentPlayer().add(body.getHeroDownPresenter());
	for(int i=0;i<model.getOpponent().getHand().size();i++){
		JButton y=new JButton("****");
		body.getCurrentPlayer().add(y);
	}

}
	body.revalidate();
	body.repaint();
}
public void useHeroPowerButton() {
	JButton tmp=store.peek();
	if (body.getCurrHero() == model.getCurrentHero()) {
		try {
			
			//**********************************************************************************************
			if(model.getCurrentHero() instanceof Mage){
				if(tmp.getActionCommand().equals(model.getOpponent().getName()))
					((Mage)model.getCurrentHero()).useHeroPower(model.getOpponent());
				//else if(tmp.getActionCommand().equals(model.getCurrentHero().getName()))
				//	((Mage)model.getCurrentHero()).useHeroPower(model.getCurrentHero());
				else if(body.getLowerField().contains(tmp)){
				int index1=body.getLowerField().indexOf(tmp);
				if(model.getCurrentHero().getField().get(index1) instanceof Minion)
				((Mage)model.getCurrentHero()).useHeroPower(model.getCurrentHero().getField().get(index1));
			}
				else if(body.getUpperField().contains(tmp)){
					int index1=body.getUpperField().indexOf(tmp);
					if(model.getOpponent().getField().get(index1) instanceof Minion)
					((Mage)model.getCurrentHero()).useHeroPower(model.getOpponent().getField().get(index1));
				}
			}
			else if(model.getCurrentHero() instanceof Priest){
				if(tmp.getActionCommand().equals(model.getOpponent().getName()))
					((Priest)model.getCurrentHero()).useHeroPower(model.getOpponent());
				else if(tmp.getActionCommand().equals(model.getCurrentHero().getName()))
				((Priest)model.getCurrentHero()).useHeroPower(model.getCurrentHero());
				else if(body.getLowerField().contains(tmp)){
				int index1=body.getLowerField().indexOf(tmp);
				if(model.getCurrentHero().getField().get(index1) instanceof Minion)
				((Priest)model.getCurrentHero()).useHeroPower(model.getCurrentHero().getField().get(index1));
			}
				else if(body.getUpperField().contains(tmp)){
					int index1=body.getUpperField().indexOf(tmp);
					if(model.getOpponent().getField().get(index1) instanceof Minion)
					((Priest)model.getCurrentHero()).useHeroPower(model.getOpponent().getField().get(index1));
				}
				
			}
			else
			model.getCurrentHero().useHeroPower();
			//******************************************************************************************************
			this.onFieldUpdate();
			body.revalidate();
			body.repaint();
		} catch (NotEnoughManaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeroPowerAlreadyUsedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotYourTurnException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FullHandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FullFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} else if (body.getOppHero() == model.getCurrentHero()) {
		try {
			//**********************************************************************************************
			if(model.getCurrentHero() instanceof Mage){
				if(tmp.getActionCommand().equals(model.getOpponent().getName()))
					((Mage)model.getCurrentHero()).useHeroPower(model.getOpponent());
				//else if(tmp.getActionCommand().equals(model.getCurrentHero().getName()))
				//	((Mage)model.getCurrentHero()).useHeroPower(model.getCurrentHero());
				else if(body.getLowerField().contains(tmp)){
				int index1=body.getLowerField().indexOf(tmp);
				if(model.getOpponent().getField().get(index1) instanceof Minion)
				((Mage)model.getCurrentHero()).useHeroPower(model.getOpponent().getField().get(index1));
				}
				
				else if(body.getUpperField().contains(tmp)){
					int index1=body.getUpperField().indexOf(tmp);
					if(model.getCurrentHero().getField().get(index1) instanceof Minion)
					((Mage)model.getCurrentHero()).useHeroPower(model.getCurrentHero().getField().get(index1));
				}
			}
			else if(model.getCurrentHero() instanceof Priest){
				if(tmp.getActionCommand().equals(model.getOpponent().getName()))
					((Priest)model.getCurrentHero()).useHeroPower(model.getOpponent());
				else if(tmp.getActionCommand().equals(model.getCurrentHero().getName()))
				((Priest)model.getCurrentHero()).useHeroPower(model.getCurrentHero());
				else if(body.getLowerField().contains(tmp)){
				int index1=body.getLowerField().indexOf(tmp);
				if(model.getOpponent().getField().get(index1) instanceof Minion)
				((Priest)model.getCurrentHero()).useHeroPower(model.getOpponent().getField().get(index1));
			}
				else if(body.getUpperField().contains(tmp)){
					int index1=body.getUpperField().indexOf(tmp);
					if(model.getCurrentHero().getField().get(index1) instanceof Minion)
					((Priest)model.getCurrentHero()).useHeroPower(model.getCurrentHero().getField().get(index1));
				}
				
			}
			else
			model.getCurrentHero().useHeroPower();
			//******************************************************************************************************
			this.onFieldUpdate();
			
			body.revalidate();
			body.repaint();
		} catch (NotEnoughManaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HeroPowerAlreadyUsedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotYourTurnException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FullHandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FullFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
public void castSpell(Spell s){
	if(s instanceof AOESpell){
		try {
			model.getCurrentHero().castSpell((AOESpell)s, model.getOpponent().getField());
		} catch (NotYourTurnException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotEnoughManaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	else if(s instanceof FieldSpell){
		try {
			model.getCurrentHero().castSpell((FieldSpell)s);
		} catch (NotYourTurnException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotEnoughManaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	this.onFieldUpdate();
	
}
public void castSpell(Spell s,Minion m){
	if(s instanceof MinionTargetSpell){
		try {
			model.getCurrentHero().castSpell((MinionTargetSpell)s, m);
		} catch (NotYourTurnException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotEnoughManaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	else if(s instanceof LeechingSpell){
		try {
			model.getCurrentHero().castSpell((LeechingSpell)s, m);
		} catch (NotYourTurnException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotEnoughManaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	this.onFieldUpdate();
	
}
public void castSpell(Spell s,Hero h){
	if(s instanceof HeroTargetSpell){
		try {
			model.getCurrentHero().castSpell((HeroTargetSpell)s, h);
		} catch (NotYourTurnException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotEnoughManaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	this.onFieldUpdate();
}
public void addSpell(){
	Hero tmpHero=model.getOpponent();
	Minion tmpMinion=new Icehowl();
		if(body.getCurrHero()==model.getCurrentHero()){
			if(body.getHeroUpPresenter().equals(store.peek())&&store.peek().getActionCommand().equals(model.getOpponent().getName())){
			tmpHero=model.getOpponent();
			store.pop();
			if(body.getcPlayerButtons().contains(store.peek())){
				int index=body.getcPlayerButtons().indexOf(store.peek());
				if(model.getCurrentHero().getHand().get(index) instanceof HeroTargetSpell){
					this.castSpell((Spell)model.getCurrentHero().getHand().get(index),tmpHero);
				}
				else return;
			}
			}
			else if(body.getHeroDownPresenter().equals(store.peek())&&store.peek().getActionCommand().equals(model.getCurrentHero().getName())){
				tmpHero=model.getCurrentHero();
				store.pop();
				if(body.getcPlayerButtons().contains(store.peek())){
					int index=body.getcPlayerButtons().indexOf(store.peek());
					if(model.getCurrentHero().getHand().get(index) instanceof HeroTargetSpell){
						this.castSpell((Spell)model.getCurrentHero().getHand().get(index),tmpHero);
					}
					else return;
				}
				}
			else if(body.getUpperField().contains(store.peek())){
				int index1=body.getUpperField().indexOf(store.peek());
				tmpMinion=model.getOpponent().getField().get(index1);
				store.pop();
				if(body.getcPlayerButtons().contains(store.peek())){
					int index2=body.getcPlayerButtons().indexOf(store.peek());
					if(model.getCurrentHero().getHand().get(index2) instanceof MinionTargetSpell){
						this.castSpell((Spell)model.getCurrentHero().getHand().get(index2),tmpMinion);
					}
					else return;
				}
			}
			else if(body.getLowerField().contains(store.peek())){
				int index1=body.getLowerField().indexOf(store.peek());
				tmpMinion=model.getCurrentHero().getField().get(index1);
				store.pop();
				if(body.getcPlayerButtons().contains(store.peek())){
					int index2=body.getcPlayerButtons().indexOf(store.peek());
					if(model.getCurrentHero().getHand().get(index2) instanceof MinionTargetSpell){
						this.castSpell((Spell)model.getCurrentHero().getHand().get(index2),tmpMinion);
					}
					else return;
				}
			}
		}
//*******************************************************************************************************************************************************
		else if(body.getOppHero()==model.getCurrentHero()){
			if(body.getHeroDownPresenter().equals(store.peek())&&store.peek().getActionCommand().equals(model.getOpponent().getName())){
			tmpHero=model.getOpponent();
			store.pop();
			if(body.getoPlayerButtons().contains(store.peek())){
				int index=body.getoPlayerButtons().indexOf(store.peek());
				if(model.getCurrentHero().getHand().get(index) instanceof HeroTargetSpell){
					this.castSpell((Spell)model.getCurrentHero().getHand().get(index),tmpHero);
					this.onFieldUpdate();
				}
				else return;
			}
			}
			else if(body.getHeroUpPresenter().equals(store.peek())&&store.peek().getActionCommand().equals(model.getCurrentHero().getName())){
				tmpHero=model.getCurrentHero();
				store.pop();
				if(body.getoPlayerButtons().contains(store.peek())){
					int index=body.getoPlayerButtons().indexOf(store.peek());
					if(model.getCurrentHero().getHand().get(index) instanceof HeroTargetSpell){
						this.castSpell((Spell)model.getCurrentHero().getHand().get(index),tmpHero);
						this.onFieldUpdate();
					}
					else return;
				}
				}
			else if(body.getUpperField().contains(store.peek())){
				int index1=body.getUpperField().indexOf(store.peek());
				tmpMinion=model.getCurrentHero().getField().get(index1);
				store.pop();
				if(body.getoPlayerButtons().contains(store.peek())){
					int index2=body.getoPlayerButtons().indexOf(store.peek());
					if(model.getCurrentHero().getHand().get(index2) instanceof MinionTargetSpell){
						this.castSpell((Spell)model.getCurrentHero().getHand().get(index2),tmpMinion);
						this.onFieldUpdate();
					}
					else return;
				}
			}
			else if(body.getLowerField().contains(store.peek())){
				int index1=body.getLowerField().indexOf(store.peek());
				tmpMinion=model.getOpponent().getField().get(index1);
				store.pop();
				if(body.getoPlayerButtons().contains(store.peek())){
					int index2=body.getoPlayerButtons().indexOf(store.peek());
					if(model.getCurrentHero().getHand().get(index2) instanceof MinionTargetSpell){
						this.castSpell((Spell)model.getCurrentHero().getHand().get(index2),tmpMinion);
						this.onFieldUpdate();
					}
					else return;
				}
			}
		}
		this.onFieldUpdate();		
		
	}

	

	
}


