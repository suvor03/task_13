package ru.vsu.cs.suvorov_d_a;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.Serial;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Title extends JPanel implements KeyListener {

    @Serial
    private static final long serialVersionUID = 1L;
    private final BufferedImage instructions;
    private final Main window;

    public Title(Main window){
        instructions = ImageLoader.loadImage("/arrow.png");
        Timer timer = new Timer(1000 / 60, e -> repaint());

        timer.start();
        this.window = window;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);

        g.drawImage(instructions, Main.WIDTH/2 - instructions.getWidth()/2,
                30 - instructions.getHeight()/2 + 150, null);
        g.setColor(Color.WHITE);
        g.drawString("Press ENTER to play!", 150, Main.HEIGHT / 2 + 100);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar() == KeyEvent.VK_ENTER) {
            window.startTetris();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}