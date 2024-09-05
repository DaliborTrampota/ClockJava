package dalibortrampota.github.com;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.text.DateFormatSymbols;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Main extends JPanel implements ActionListener {

    static int CLOCK_COUNT = 5;

    Timer timer = new Timer(10, this);
    double scaleDelta = 0.003;

    int sizeX = 600;
    int sizeY = 600;
    int r1, r2;

    double scale = 0.75;
    long frame = 0;

    
    static class Label {
        String text;
        double angle;

        public Label(String text, double angle) {
            this.text = text;
            this.angle = angle;
        }

        public int getX(int radius) {
            double rad = Math.toRadians(angle + 180.0);
            return (int)(Math.sin(-rad) * (double)radius);
        }

        public int getY(int radius) {
            double rad = Math.toRadians(angle + 180.0);
            return (int)(Math.cos(-rad) * (double)radius);
        }
    }

    Label[] labels = new Main.Label[12];



    public static void main(String[] args) {
        JFrame jf = new JFrame("Canvas");
        Main m = new Main();

        jf.setSize(m.sizeX + 50, m.sizeY + 50);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // jf.set
        jf.add(m);

        jf.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension dim = e.getComponent().getSize();
                m.sizeX = (int)dim.getWidth();
                m.sizeY = (int)dim.getHeight();
            }
            @Override
            public void componentMoved(ComponentEvent e) {
            //   System.out.println("Moved to " + e.getComponent().getLocation());
            }
          });

        m.timer.start();

        for(int i = 1; i <= 12; i++) {
            m.labels[i - 1] = new Main.Label(String.format("%d", i), (double)i / 12.0 * 360.0);
        } 

        System.out.println(m.labels.length);
    }

    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);



        g.fillRect(0, 0, sizeX, sizeY);
        g.setFont(new Font("ArialBlack", Font.BOLD, 20));
        g.setColor(Color.BLACK);


        int centerX = sizeX / 2;
        int centerY = sizeY / 2;

        AffineTransform prev = g2.getTransform();

        double ratio = ((double)r2/r1);
        double tempScale = scale / ratio;
        for(int i = 0; i < CLOCK_COUNT; i++) {
            drawClock(g, g2, centerX, centerY, Math.min(sizeX, sizeY) / 2 - 50, tempScale);
            g2.setTransform(prev);
            tempScale *= ratio;
        } 


        // drawClock(g, g2, centerX, centerY, Math.min(sizeX, sizeY) / 2 - 50, tempScale);
        // g2.setTransform(prev);


        // drawClock(g, g2, centerX, centerY, Math.min(sizeX, sizeY) / 2 - 50, scale);
        // g2.setTransform(prev);

        // //System.out.println(String.format("r1: %d r2: %d, r: %s, s: %s", r1, r2, (double)r2/r1, scale));
        // tempScale = scale * ((double)r2/r1);
        // drawClock(g, g2, centerX, centerY, r1, tempScale);
        // g2.setTransform(prev);

        // tempScale = tempScale * ((double)r2/r1);
        // drawClock(g, g2, centerX, centerY, r1, tempScale);
        // g2.setTransform(prev);
        
        scale += scaleDelta * Math.exp(scaleDelta * frame);
        frame++;

        if(scale >= 4) {
            scale = 1.;
            frame = 0;
        }
    }

    void drawHand(Graphics g, int centerX, int centerY, int val, int maxVal, int length, int width) {
        double angle = Math.toRadians((double)val / (double)maxVal * 360.0 + 180);

        double X = Math.sin(-angle);
        double Y = Math.cos(-angle);

        ((Graphics2D)g).setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT | BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        g.drawLine(centerX + (int)(X * r2 * 1.04), centerY + (int)(Y * r2 * 1.04), centerX + (int)(X * length), centerY + (int)(Y * length));
    }

    void drawClock(Graphics g, Graphics2D g2, int centerX, int centerY, int r, double scale) {

        AffineTransform tScale = new AffineTransform();
        tScale.translate(centerX - centerX * scale, centerY - centerY * scale);
        tScale.scale(scale, scale);
        g2.setTransform(tScale);

        r1 = r;
        r2 = (int)(r1 * 0.25);

        //System.out.println(String.format("%s %s", getScaledStroke(5, scale), scale));

        
        g2.setStroke(new BasicStroke(getScaledStroke(5, scale), BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        g.drawArc(centerX - r, centerY - r, 2*r1, 2*r1, 0, 360);
        
        g2.setStroke(new BasicStroke(getScaledStroke(3, scale), BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        g.drawArc(centerX - r + (r1 - r2), centerY - r + (r1 - r2), 2*r2, 2*r2, 0, 360);


        Calendar d = Calendar.getInstance();
        g.setColor(Color.RED);

        int hours = d.get(Calendar.HOUR_OF_DAY);
        int minutes = d.get(Calendar.MINUTE);
        int seconds = d.get(Calendar.SECOND);

        
        drawHand(g, centerX, centerY, minutes, 60, r1 - 45, 5);
        drawHand(g, centerX, centerY, hours, 12, r1 - 90, 3);
        g.setColor(Color.BLACK);
        drawHand(g, centerX, centerY, seconds, 60, r1 - 25, 2);

        AffineTransform prev = g2.getTransform();
        for(int i = 0; i < 60; i++) {
            AffineTransform t = new AffineTransform();
            t.translate(centerX, centerY);
            t.scale(scale, scale);
            t.rotate(Math.toRadians(i / 60.0 * 360.0 - 180));
            g2.setTransform(t);
            g2.drawLine(0, r1, 0, r1 - (i % 5 == 0 ? 8 : 5));
        }
        g2.setTransform(prev);

        for(Label l : labels) {
            AffineTransform t = new AffineTransform();
            t.translate(centerX, centerY);
            t.scale(scale, scale);
            t.rotate(Math.toRadians(l.angle - 180));

            g2.setTransform(t);
            g.drawString(l.text, -l.text.length() * 5, r1 - 15);//l.getX(r - 15) + centerX - (l.text.length() * 5), l.getY(r - 15) + centerY + 10);
            
        }
        g2.setTransform(prev);

        String month = new DateFormatSymbols().getMonths()[d.get(Calendar.MONTH)];
        String dateText = String.format("%d. %s", d.get(Calendar.DATE), month);
        String msText = String.format("%d", d.get(Calendar.MILLISECOND), month);
        g2.drawString(dateText, centerX - (int)(dateText.length() / 2) * 10, centerY - r1 / 2);
        g2.drawString(msText, centerX - (int)(msText.length() / 2) * 10, centerY - r1 / 2 + 25);
    }


    int getScaledStroke(int w, double s) {
        return Math.max(1, Math.min((int)(w / s), w));
    }
}