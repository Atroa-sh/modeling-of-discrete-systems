import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

public class Board extends JComponent implements MouseInputListener, ComponentListener {
    private static final long serialVersionUID = 1L;
    private Point[][] points;
    private int size = 10;
    public int editType = 0;
    public static final int SLOW_DOWN_CHANCE = 30;
    public static final int RUSH = 0;
    public static final int MAX_SPEED = 5;
    public static final int INITIAL_CARS = 2000;
    public static final Random random = new Random();
    private FileSaver fileSaver = new FileSaver("results.csv");
    int velocitySum = 0;
    int overtakeSum = 0;
    int slowdownSum = 0;  //zwolnienia przed kolizj�
    int passes=0;
    GUI gui;

    public Board(int length, int height, GUI gui) {
        this.gui = gui;
        addMouseListener(this);
        addComponentListener(this);
        addMouseMotionListener(this);
        setBackground(Color.WHITE);
        setOpaque(true);
    }

    public void iteration() {
        int iterNum = gui.getIterNum();
        if (iterNum % 10 == 0) {
            fileSaver.saveRecord(iterNum + ";" + velocitySum + ";" + 1.0 * velocitySum / INITIAL_CARS + ";" + overtakeSum + ";" + slowdownSum + ";"+passes+";\n");
        }
        Point nextPoint;
        boolean collision = false;
        for (int x = 0; x < points.length; ++x) {
            for (int y = 0; y < points[x].length; ++y) {
                Point pt = points[x][y];


                if (pt.getState() == CellState.OCCUPIED) {
                    //carsSum++;
                    boolean overtakeAbove = false, overtakeBelow = false;
                    pt.setVelocity(Math.min(pt.getVelocity() + 1, MAX_SPEED));


                    if (pt.getVelocity() == MAX_SPEED && random.nextInt(100) < SLOW_DOWN_CHANCE) {
                        pt.setVelocity(Math.max(pt.getVelocity() - 1, 0));
                    }

                    for (int i = 1; i <= pt.getVelocity(); i++) {
                        nextPoint = points[(x + i) % points.length][y];
                        if (nextPoint.getState() == CellState.OCCUPIED || nextPoint.getState() == CellState.NEXT) {
                            if (random.nextInt(100) < RUSH) {
                                overtakeAbove = true;
                                overtakeBelow = true;
                                Point above = null;
                                Point below = null;
                                for (int j = -MAX_SPEED; j < pt.getVelocity(); j++) {
                                    if(y==0){
                                        above=points[(x + i) % points.length][points[0].length-1];
                                    }else{
                                        above = points[(x + i) % points.length][(y - 1) % points[0].length];

                                    }



                                     below = points[(x + i) % points.length][(y + 1) % points[0].length];

                                    if (above.getState() == CellState.OCCUPIED || above.getState() == CellState.NEXT) {
                                        overtakeAbove = false;
                                    }
                                    if (below.getState() == CellState.OCCUPIED || below.getState() == CellState.NEXT) {
                                        overtakeBelow = false;
                                    }
                                }
                                if (overtakeAbove) {
                                    above.setVelocity(pt.getVelocity());
                                    above.setState(CellState.NEXT);
                                    pt.setState(CellState.FREE);

                                } else if (overtakeBelow) {
                                    below.setState(CellState.NEXT);
                                    below.setState(CellState.NEXT);
                                    pt.setState(CellState.FREE);
                                } else {

                                    collision = true;
                                    pt.setVelocity(i - 1);
                                    slowdownSum++;
                                }
                            } else {
                                collision = true;
                                pt.setVelocity(i - 1);
                                slowdownSum++;
                            }
                            break;
                        }
                    }
                    if (overtakeAbove || overtakeBelow) {
                        overtakeSum++;
                    } else if (pt.getVelocity() != 0) {
                        points[(x + pt.getVelocity()) % points.length][y].setVelocity(pt.getVelocity());
                        pt.setState(CellState.FREE);
                        points[(x + pt.getVelocity()) % points.length][y].setState(CellState.NEXT);
                    }
                    velocitySum += pt.getVelocity();
                    if((x+ pt.getVelocity())%points.length<pt.getVelocity()){
                            passes++;
                    }
                    //
                }


            }
        }

        for (int x = 0; x < points.length; ++x) {
            for (int y = 0; y < points[x].length; ++y) {
                Point pt = points[x][y];
                if (pt.getState() == CellState.NEXT) {
                    pt.setState(CellState.OCCUPIED);
                }
            }
        }
        this.repaint();
    }

    public void clear() {
        for (int x = 0; x < points.length; ++x)
            for (int y = 0; y < points[x].length; ++y) {
                points[x][y].clear();
            }
        this.repaint();
    }

    private void initialize(int length, int height) {
        points = new Point[length][height];

        for (int x = 0; x < points.length; ++x) {
            for (int y = 0; y < points[x].length; ++y) {
                points[x][y] = new Point();
            }
        }
        for (int i = 0; i < INITIAL_CARS; i++) {
            int xpos = random.nextInt(length);
            int ypos = random.nextInt(height);
            while (points[xpos][ypos].getState() != CellState.FREE) {
                xpos = random.nextInt(length);
                ypos = random.nextInt(height);
            }
            points[xpos][ypos].setState(CellState.OCCUPIED);

        }
    }

    protected void paintComponent(Graphics g) {
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
        g.setColor(Color.GRAY);
        drawNetting(g, size);
    }

    private void drawNetting(Graphics g, int gridSpace) {
        Insets insets = getInsets();
        int firstX = insets.left;
        int firstY = insets.top;
        int lastX = this.getWidth() - insets.right;
        int lastY = this.getHeight() - insets.bottom;

        int x = firstX;
        while (x < lastX) {
            g.drawLine(x, firstY, x, lastY);
            x += gridSpace;
        }

        int y = firstY;
        while (y < lastY) {
            g.drawLine(firstX, y, lastX, y);
            y += gridSpace;
        }

        for (x = 0; x < points.length; ++x) {
            for (y = 0; y < points[x].length; ++y) {
                Point pt = points[x][y];
                float a = 1.0F;
                float z = 0.0F;

                if (pt.getState() == CellState.OCCUPIED) {
                    g.setColor(new Color(z, z, z, 0.7f));
                } else {
                    g.setColor(new Color(a, a, a, 0.7f));
                }
                g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));

            }
        }

    }

    public void mouseClicked(MouseEvent e) {
        int x = e.getX() / size;
        int y = e.getY() / size;
        if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
            if (editType == 0) {
                points[x][y].clicked();
            }
            this.repaint();
        }
    }

    public void componentResized(ComponentEvent e) {
        int dlugosc = (this.getWidth() / size) + 1;
        int wysokosc = (this.getHeight() / size) + 1;
        initialize(dlugosc, wysokosc);
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX() / size;
        int y = e.getY() / size;
        if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
            if (editType == 0) {
                points[x][y].clicked();
            }
            this.repaint();
        }
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }
}
