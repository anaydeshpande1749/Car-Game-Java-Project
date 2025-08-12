import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

public class Cargame extends JPanel implements Runnable {
     int carX, carY;
     int obstacleX, obstacleY;
     boolean gameOver;
     int score;

     Image carImage;
     Image obstacleImage;
     Image backgroundImage;

     final int CAR_WIDTH = 30;
     final int CAR_HEIGHT = 60;
     final int OBSTACLE_WIDTH = 30;
     final int OBSTACLE_HEIGHT = 60;

    public Cargame() { 
        carX = 100;
        carY = 390;
        obstacleX = 200;
        obstacleY = 0;
        score = 0;
        gameOver = false;

        carImage = new ImageIcon(getClass().getClassLoader().getResource("images/car.png")).getImage();
        obstacleImage = new ImageIcon(getClass().getClassLoader().getResource("images/bot.png")).getImage();
        backgroundImage = new ImageIcon(getClass().getClassLoader().getResource("images/road.jpg")).getImage();
       obstacleImage = new ImageIcon(getClass().getClassLoader().getResource("images/bot2.png")).getImage();

        addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (!gameOver) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        carX -= 30;
                        if (carX < 0) carX = 0;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        carX += 30;
                        if (carX + CAR_WIDTH > getWidth()) carX = getWidth() - CAR_WIDTH;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_UP) {
                        carY -= 30;
                        if (carY< 0) carY = 0;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        carY += 30;
                        if (carY + CAR_HEIGHT > getHeight()) carY = getHeight() - CAR_HEIGHT;
                    }
                } else {
                    if (e.getKeyCode() == KeyEvent.VK_R) {
                        resetGame();
                    }
                }
                repaint();
            }

            public void keyReleased(KeyEvent e) {}
            public void keyTyped(KeyEvent e) {}
        });

        setFocusable(true);
        new Thread(this).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        g.drawImage(carImage, carX, carY, CAR_WIDTH, CAR_HEIGHT, this);
        g.drawImage(obstacleImage, obstacleX, obstacleY, OBSTACLE_WIDTH, OBSTACLE_HEIGHT, this);
       
        g.drawImage(obstacleImage, obstacleX, obstacleY, OBSTACLE_WIDTH, OBSTACLE_HEIGHT, this);

        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);

        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over! Press 'R' to Restart.", 100, 250);
            g.setFont(new Font("serif", Font.BOLD, 5));
        }
    }

    @Override
    public void run() {
        while (true) {
            if (!gameOver) {
                obstacleY += 5;
                if (obstacleY > getHeight()) {
                    obstacleY = 0;
                    obstacleX = (int) ( 2*Math.random() * (getWidth() - OBSTACLE_WIDTH));
                    score += 10;
                }

                checkCollision();
                repaint();
            }
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkCollision() {
        Rectangle carBounds = new Rectangle(carX, carY, CAR_WIDTH, CAR_HEIGHT);
        Rectangle obstacleBounds = new Rectangle(obstacleX, obstacleY, OBSTACLE_WIDTH, OBSTACLE_HEIGHT);

        if (carBounds.intersects(obstacleBounds)) {
            gameOver = true;
        }
    }

    private void resetGame() {
        carX = 250;
        carY = 390;
        obstacleX = 250;
        obstacleY = 0;
        score = 0;
        gameOver = false;
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Car Racing Game");
        Cargame game = new Cargame();
        frame.add(game);
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
