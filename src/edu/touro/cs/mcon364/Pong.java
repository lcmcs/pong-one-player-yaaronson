package edu.touro.cs.mcon364;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.*;
import java.util.PriorityQueue;
import java.util.Properties;

public class Pong extends JFrame {
    public static void main(String[] args) {
        new Pong();
    }

    Properties props;
    PriorityQueue<Score> savingInfo = new PriorityQueue<Score>();
    Timer ballTimer;
    int score = 0;
    JLabel scoreCount;
    JLabel Top = new JLabel(" hello ");
    JButton newGame;
    public Pong(){
        add(new GamePanel(), BorderLayout.CENTER);
        add(Top, BorderLayout.SOUTH);
        JPanel first = new JPanel();
        first.setLayout(new FlowLayout());
        scoreCount = new JLabel("Score: ");
        newGame = new JButton("New Game");
        newGame.addActionListener(e -> {
            new Pong();
        });
        first.add(scoreCount);
        first.add(newGame);
        //first.add(Top);
        add(first, BorderLayout.NORTH);
        loadProperties();
        displayScores();
        this.setResizable(false);
        setSize(500,500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void loadProperties() { //1
        props = new Properties();
        try {
            props.load(new FileInputStream("pong.ini"));
            for (String s: props.stringPropertyNames()) {
                int x = Integer.parseInt(props.getProperty(s));
                savingInfo.add(new Score(x,s));
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
        public void saveProperties(){ //2
        props.clear();  //clears properties
        for (int i =0; i < 3; i++){
            Score top = savingInfo.poll(); //takes out top 3 times
            if (top == null){
                continue;
            }
            props.setProperty(top.getInitials(), String.valueOf(top.getScore()));
        }
            try {
                props.store( new FileOutputStream("pong.ini"), "");
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    public void gameOver(){
        int highScore = savingInfo.peek().getScore();
        if (score >= highScore){
             String name = JOptionPane.showInputDialog(null, "Enter Your Initials: ");
             savingInfo.add(new Score(highScore, name));
        }
        saveProperties();

    }

    public void displayScores() {
        String display = " ";
        for (Score s: savingInfo) {
            display += " Initials: " + s.getInitials() + " Score: " + s.getScore();
        }
        Top.setText(display);
    }

    private class GamePanel extends JPanel {
        private Point ball = new Point(100,100);
        private Point panel = new Point(20,300);
        private Point delta = new Point(+3,-3);
        private boolean alreadyScored = false;

        GamePanel() {
            this.setBackground(Color.black);
            this.setBorder(BorderFactory.createLineBorder(Color.white, 5));
            //this.add(Top);

            ballTimer = new Timer(5,
                    e -> {
                        ball.translate(delta.x, delta.y);

                        if (ball.y < 10 || ball.y > 400 - 20){
                            delta.y = -delta.y;
                        }
                        if (ball.x < 10 || ball.x > 500 - 20){
                            delta.x = -delta.x;
                        }
                        if (ball.distance(panel) <= 30.0 && !alreadyScored) {
                            alreadyScored = true;
                            score++;
                            scoreCount.setText("score is  " + score);
                        }
                        if (ball.distance(panel) > 30.0){
                                alreadyScored = false;
                        }
                        if (score >= savingInfo.peek().getScore()){
                            gameOver();
                            ballTimer.stop();
                        }

                        repaint();
                    });
            ballTimer.start();

            addMouseWheelListener(e -> {
                panel.y += e.getPreciseWheelRotation() * 15;
                repaint();
                    }
            );
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(Color.white);
            g.fillRect(panel.x,panel.y,10,80);
            g.fillOval(ball.x,ball.y,20,20);


        }
    }
}
