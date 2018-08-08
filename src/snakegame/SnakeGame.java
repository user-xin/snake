package snakegame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SnakeGame extends JPanel {
	private static final long serialVersionUID = 1L;
	// ��������ʾ��ͼ�Ŀ�͸�
	protected static final int HEIGHT = 30;
	protected static final int WIDTH = 30;
	// ��������ʾÿ������Ŀ�͸�
	protected static final int CELL_H = 20;
	protected static final int CELL_W = 20;
	// ��������ʾ�ߵķ���
	protected static final int UP_DIRECTION = 1;
	protected static final int DOWN_DIRECTION = -1;
	protected static final int LEFT_DIRECTION = 2;
	protected static final int RIGHT_DIRECTION = -2;
	// ��ǰ����Ĭ��Ϊ�Ϸ�
	private int currentdirection = UP_DIRECTION;
	// char�������������ͼ
	protected char[][] map = new char[HEIGHT][WIDTH];
	// ���������������ߵ�������Ϣ
	private LinkedList<Point> snake = new LinkedList<Point>();
	// ʳ���������Point��������
	private Point food = new Point();
	// �����ж���Ϸ�Ƿ����
	protected static boolean GameOver = false;
	protected static boolean IsAuto = true;
	private static int Difficult_Degree = 1;
	private Scanner in;

	/* ͼ��ˢ���� */
	// ��дPaint����
	@Override
	public void paint(Graphics g) {
		// ����ͼ
		for (int i = 0; i < HEIGHT; i++)
			for (int j = 0; j < WIDTH; j++) {
				if (map[i][j] == '*') {
					g.setColor(Color.GRAY);
				} else {
					g.setColor(Color.WHITE);
				}
				g.fill3DRect(j * CELL_W, i * CELL_H, CELL_W, CELL_H, true);
			}

		// ����
		// ��λ��ͷ
		int W = snake.getFirst().x;
		int H = snake.getFirst().y;
		g.setColor(Color.BLUE);
		g.fill3DRect(W * CELL_W, H * CELL_H, 20, 20, true);
		// ��λ����
		for (int t = 1; t < this.snake.size(); t++) {
			W = snake.get(t).x;
			H = snake.get(t).y;
			g.setColor(Color.GREEN);
			g.fill3DRect(W * CELL_W, H * CELL_H, 20, 20, true);

		}

		// ��ʳ��
		map[food.y][food.x] = '@';
		g.setColor(Color.YELLOW);
		g.fill3DRect(food.x * CELL_W, food.y * CELL_H, 20, 20, true);

		// ����
		if (SnakeGame.GameOver) {
			g.setColor(Color.ORANGE);
			g.setFont(new Font("����", Font.BOLD, 30));
			g.drawString("GAMW OVER!", CELL_W * (WIDTH / 2 - 5), CELL_H * (HEIGHT / 2));
		}
	}

	/* ��ʼ����ͼ���ߡ�ʳ�ﺯ���� */
	// ��ʼ��Map
	public void intiMap() {

		for (int i = 0; i < HEIGHT; i++)
			for (int j = 0; j < WIDTH; j++) {
				if (i == 0 || (i == HEIGHT - 1)) {
					map[i][j] = '*';
				} else
					this.map[i][j] = ' ';
			}
	}

	// ��ʼ��snake
	public void intiSnake() {
		int x = WIDTH / 2;
		int y = HEIGHT / 2;
		snake.addFirst(new Point(x - 1, y));
		snake.addFirst(new Point(x, y));
		snake.addFirst(new Point(x + 1, y));
	}

	// ��ʼ��ʳ��
	public void intiFood() {
		while (true) {
			Random random = new Random();
			int x = random.nextInt(WIDTH - 1);
			int y = random.nextInt(HEIGHT - 1);
			if (map[y][x] != '*' && map[y][x] != '#' && map[y][x] != '$') {
				food.x = x;
				food.y = y;
				break;
			}
		}
	}

	/* ���ƶ������� */
	// run���������Լ���
	public void runAuto() throws InterruptedException {
		while (true) {
			move();
			IsOver();
			reFresh();
			repaint();

			if (GameOver) {
				repaint();
			}
			Thread.sleep(1000 / Difficult_Degree);
		}
	}

	// ���ƶ�����
	public void move() {
		// ȡ��ͷ
		Point snakehead = snake.getFirst();
		// ���ݷ����ƶ�
		switch (currentdirection) {
		// �����ƶ�
		case UP_DIRECTION:
			// ������ʱ��ִ�����ͷ���
			if (!GameOver) {
				snake.addFirst(new Point(snakehead.x, snakehead.y - 1));
			}

			break;
		// �����ƶ�
		case DOWN_DIRECTION:
			// ������ʱ��ִ�����ͷ���
			if (!GameOver) {
				snake.addFirst(new Point(snakehead.x, snakehead.y + 1));
			}

			break;
		// �����ƶ�
		case LEFT_DIRECTION:
			if (!GameOver) {
				if (snakehead.x == 0) {
					snake.addFirst(new Point(snakehead.x - 1 + WIDTH, snakehead.y));
				} else {
					snake.addFirst(new Point(snakehead.x - 1, snakehead.y));
				}
			}

			break;
		// �����ƶ�
		case RIGHT_DIRECTION:
			if (!GameOver) {
				snake.addFirst(new Point((snakehead.x + 1) % WIDTH, snakehead.y));
			}

			break;
		default:
			break;
		}
		if (eatFood()) {
			// ��ˢ�£���ֹ����ʳ�ﳤ������
			repaint();
			// �ؽ�ʳ��
			intiFood();
		} else {
			// ��Ϸ������ʱ��ִ��ɾ��β�ڵ�
			if (!GameOver) {
				// ɾ����β
				snake.removeLast();
			}
		}
	}

	// �ı��ߵķ���
	public void changeDirection(int newDirection) {
		if (newDirection + currentdirection != 0) {
			currentdirection = newDirection;
		}
	}

	// �߳�ʳ����
	public boolean eatFood() {
		// ȡ��ͷ
		Point snakehead = snake.getFirst();
		if (snakehead.equals(food)) {
			return true;
		}
		return false;
	}

	/* ˢ���� */
	public void reFresh() {
		// ˢ�µ�ͼ
		intiMap();
		// ˢ����
		showSnake();
		// ˢ��ʳ��
		showFood();
	}

	// �ڵ�ͼ����ʾ��
	public boolean showSnake() {
		{
			// ��λ��ͷ
			int W = snake.getFirst().x;
			int H = snake.getFirst().y;
			map[H][W] = '$';
			// ��λ����
			for (int i = 1; i < this.snake.size(); i++) {
				W = snake.get(i).x;
				H = snake.get(i).y;
				map[H][W] = '#';
			}
			return true;
		}
	}

	// �ڵ�ͼ����ʾʳ��
	public boolean showFood() {
		{
			map[food.y][food.x] = '@';
			return true;
		}
	}

	/* ������Ϸ�Ѷ��� */
	public void set_Difficuty() {
		System.out.println("��ѡ���Ѷ�ϵ����1��10��");
		in = new Scanner(System.in);
		Difficult_Degree = in.nextInt();
	}

	/* ������Ϸ�� */
	public void IsOver() {
		// ȡ��ͷ
		Point snakehead = snake.getFirst();
		// ײǽ��
		if (map[snakehead.y][snakehead.x] == '*') {
			GameOver = true;
		}
		// ҧ���Լ���
		if (map[snakehead.y][snakehead.x] == '#') {
			GameOver = true;
		}
	}

	public static void main(String[] args) throws InterruptedException {

		// ͼ�ν���
		SnakeGame sg1 = new SnakeGame();
		sg1.set_Difficuty();
		JFrame frame1 = new JFrame("̰����");
		frame1.setSize(CELL_W * WIDTH + 20, CELL_H * HEIGHT + 45);
		frame1.setVisible(true);
		frame1.add(sg1);
		sg1.intiMap();
		sg1.intiSnake();
		sg1.intiFood();
		sg1.repaint();
		// Ϊ������Ӽ�����
		frame1.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					IsAuto = false;
					sg1.changeDirection(UP_DIRECTION);
					break;
				case KeyEvent.VK_DOWN:
					IsAuto = false;
					sg1.changeDirection(DOWN_DIRECTION);
					break;
				case KeyEvent.VK_LEFT:
					IsAuto = false;
					sg1.changeDirection(LEFT_DIRECTION);
					break;
				case KeyEvent.VK_RIGHT:
					IsAuto = false;
					sg1.changeDirection(RIGHT_DIRECTION);
					break;
				default:
					break;
				}
				sg1.move();
				sg1.IsOver();
				sg1.reFresh();
				sg1.repaint();

				if (SnakeGame.GameOver) {
					sg1.repaint();
				}
				IsAuto = true;
			}

		});
		// ���IsAuto Ϊ�棬���Լ���
		if (IsAuto)
			sg1.runAuto();
	}
}
