import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class DrawGraph {

    static final int WIDTH = 800;
    static final int HEIGHT = 800;
    static final int NODE_R = 25;

    public static void main(String[] args) throws Exception {

        String input = args.length > 0 ? String.join(" ", args) : "ANT CUN BOG AMA DC TOL SAN";
        String[] vertices = input.trim().split("\\s+");
        int n = vertices.length;

        int[][] adj = new int[n][n];
        for (int i = 0; i < n; i++) {
            adj[i][(2 * i + 1) % n] = 1;
            adj[i][(2 * i + 2) % n] = 1;
        }

        System.out.println("Adjacency Matrix:");
        System.out.print("       ");
        for (String v : vertices) System.out.printf("%-6s", v);
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.printf("%-6s ", vertices[i]);
            for (int j = 0; j < n; j++)
                System.out.printf("%-6d", adj[i][j]);
            System.out.println();
        }

        int cx = WIDTH / 2, cy = HEIGHT / 2;
        int[] x = new int[n], y = new int[n];
        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n - Math.PI / 2;
            x[i] = (int) (cx + 300 * Math.cos(angle));
            y[i] = (int) (cy + 300 * Math.sin(angle));
        }

        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setStroke(new BasicStroke(2));
        for (int i = 0; i < n; i++) {
            int right = (2 * i + 1) % n;
            int left  = (2 * i + 2) % n;
            drawArrow(g, x[i], y[i], x[right], y[right], Color.RED,   i == right);
            drawArrow(g, x[i], y[i], x[left],  y[left],  Color.GREEN, i == left);
        }

        for (int i = 0; i < n; i++) {
            g.setColor(Color.CYAN);
            g.fillOval(x[i] - NODE_R, y[i] - NODE_R, 2 * NODE_R, 2 * NODE_R);
            g.setColor(Color.BLACK);
            g.drawOval(x[i] - NODE_R, y[i] - NODE_R, 2 * NODE_R, 2 * NODE_R);
            g.setFont(new Font("Arial", Font.BOLD, 11));
            FontMetrics fm = g.getFontMetrics();
            g.drawString(vertices[i], x[i] - fm.stringWidth(vertices[i]) / 2, y[i] + 5);
        }

        g.dispose();

        ImageIO.write(img, "PNG", new File("graph.png"));
        System.out.println("Saved graph.png");

        JFrame frame = new JFrame("Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JLabel(new ImageIcon(img)));
        frame.pack();
        frame.setVisible(true);
    }

    static void drawArrow(Graphics2D g, int x1, int y1, int x2, int y2, Color c, boolean loop) {
        g.setColor(c);

        if (loop) {
            g.drawOval(x1 - NODE_R, y1 - 3 * NODE_R, 2 * NODE_R, 2 * NODE_R);
            return;
        }

        double dx = x2 - x1, dy = y2 - y1;
        double dist = Math.sqrt(dx * dx + dy * dy);
        double ux = dx / dist, uy = dy / dist;
        int sx = (int) (x1 + ux * NODE_R);
        int sy = (int) (y1 + uy * NODE_R);
        int ex = (int) (x2 - ux * NODE_R);
        int ey = (int) (y2 - uy * NODE_R);

        double mx = (sx + ex) / 2.0 - uy * 20;
        double my = (sy + ey) / 2.0 + ux * 20;
        g.draw(new QuadCurve2D.Double(sx, sy, mx, my, ex, ey));

        double angle = Math.atan2(ey - my, ex - mx);
        int ax1 = (int) (ex - 12 * Math.cos(angle - Math.toRadians(30)));
        int ay1 = (int) (ey - 12 * Math.sin(angle - Math.toRadians(30)));
        int ax2 = (int) (ex - 12 * Math.cos(angle + Math.toRadians(30)));
        int ay2 = (int) (ey - 12 * Math.sin(angle + Math.toRadians(30)));
        g.fillPolygon(new int[]{ex, ax1, ax2}, new int[]{ey, ay1, ay2}, 3);
    }
}