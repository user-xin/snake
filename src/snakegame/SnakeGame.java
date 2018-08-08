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
	// 常量，表示地图的宽和高
	protected static final int HEIGHT = 30;
	protected static final int WIDTH = 30;
	// 常量，表示每个方格的宽和高
	protected static final int CELL_H = 20;
	protected static final int CELL_W = 20;
	// 常量，表示蛇的方向
	protected static final int UP_DIRECTION = 1;
	protected static final int DOWN_DIRECTION = -1;
	protected static final int LEFT_DIRECTION = 2;
	protected static final int RIGHT_DIRECTION = -2;
	// 当前方向，默认为上方
	private int currentdirection = UP_DIRECTION;
	// char型数组来储存地图
	protected char[][] map = new char[HEIGHT][WIDTH];
	// 容器，用来储存蛇的坐标信息
	private LinkedList<Point> snake = new LinkedList<Point>();
	// 食物的坐标用Point类来储存
	private Point food = new Point();
	// 用来判断游戏是否结束
	protected static boolean GameOver = false;
	protected static boolean IsAuto = true;
	private static int Difficult_Degree = 1;
	private Scanner in;

	/* 图形刷新区 */
	// 重写Paint方法
	@Override
	public void paint(Graphics g) {
		// 画地图
		for (int i = 0; i < HEIGHT; i++)
			for (int j = 0; j < WIDTH; j++) {
				if (map[i][j] == '*') {
					g.setColor(Color.GRAY);
				} else {
					g.setColor(Color.WHITE);
				}
				g.fill3DRect(j * CELL_W, i * CELL_H, CELL_W, CELL_H, true);
			}

		// 画蛇
		// 定位蛇头
		int W = snake.getFirst().x;
		int H = snake.getFirst().y;
		g.setColor(Color.BLUE);
		g.fill3DRect(W * CELL_W, H * CELL_H, 20, 20, true);
		// 定位蛇身
		for (int t = 1; t < this.snake.size(); t++) {
			W = snake.get(t).x;
			H = snake.get(t).y;
			g.setColor(Color.GREEN);
			g.fill3DRect(W * CELL_W, H * CELL_H, 20, 20, true);

		}

		// 画食物
		map[food.y][food.x] = '@';
		g.setColor(Color.YELLOW);
		g.fill3DRect(food.x * CELL_W, food.y * CELL_H, 20, 20, true);

		// 画字
		if (SnakeGame.GameOver) {
			g.setColor(Color.ORANGE);
			g.setFont(new Font("宋体", Font.BOLD, 30));
			g.drawString("GAMW OVER!", CELL_W * (WIDTH / 2 - 5), CELL_H * (HEIGHT / 2));
		}
	}

	/* 初始化地图、蛇、食物函数区 */
	// 初始化Map
	public void intiMap() {

		for (int i = 0; i < HEIGHT; i++)
			for (int j = 0; j < WIDTH; j++) {
				if (i == 0 || (i == HEIGHT - 1)) {
					map[i][j] = '*';
				} else
					this.map[i][j] = ' ';
			}
	}

	// 初始化snake
	public void intiSnake() {
		int x = WIDTH / 2;
		int y = HEIGHT / 2;
		snake.addFirst(new Point(x - 1, y));
		snake.addFirst(new Point(x, y));
		snake.addFirst(new Point(x + 1, y));
	}

	// 初始化食物
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

	/* 蛇移动函数区 */
	// run函数，蛇自己走
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

	// 蛇移动函数
	public void move() {
		// 取蛇头
		Point snakehead = snake.getFirst();
		// 依据方向，移动
		switch (currentdirection) {
		// 向上移动
		case UP_DIRECTION:
			// 结束的时候不执行添加头结点
			if (!GameOver) {
				snake.addFirst(new Point(snakehead.x, snakehead.y - 1));
			}

			break;
		// 向下移动
		case DOWN_DIRECTION:
			// 结束的时候不执行添加头结点
			if (!GameOver) {
				snake.addFirst(new Point(snakehead.x, snakehead.y + 1));
			}

			break;
		// 向左移动
		case LEFT_DIRECTION:
			if (!GameOver) {
				if (snakehead.x == 0) {
					snake.addFirst(new Point(snakehead.x - 1 + WIDTH, snakehead.y));
				} else {
					snake.addFirst(new Point(snakehead.x - 1, snakehead.y));
				}
			}

			break;
		// 向右移动
		case RIGHT_DIRECTION:
			if (!GameOver) {
				snake.addFirst(new Point((snakehead.x + 1) % WIDTH, snakehead.y));
			}

			break;
		default:
			break;
		}
		if (eatFood()) {
			// 先刷新，防止出现食物长到身上
			repaint();
			// 重建食物
			intiFood();
		} else {
			// 游戏结束的时候不执行删除尾节点
			if (!GameOver) {
				// 删除蛇尾
				snake.removeLast();
			}
		}
	}

	// 改变蛇的方向
	public void changeDirection(int newDirection) {
		if (newDirection + currentdirection != 0) {
			currentdirection = newDirection;
		}
	}

	// 蛇吃食物区
	public boolean eatFood() {
		// 取蛇头
		Point snakehead = snake.getFirst();
		if (snakehead.equals(food)) {
			return true;
		}
		return false;
	}

	/* 刷新区 */
	public void reFresh() {
		// 刷新地图
		intiMap();
		// 刷新蛇
		showSnake();
		// 刷新食物
		showFood();
	}

	// 在地图上显示蛇
	public boolean showSnake() {
		{
			// 定位蛇头
			int W = snake.getFirst().x;
			int H = snake.getFirst().y;
			map[H][W] = '$';
			// 定位蛇身
			for (int i = 1; i < this.snake.size(); i++) {
				W = snake.get(i).x;
				H = snake.get(i).y;
				map[H][W] = '#';
			}
			return true;
		}
	}

	// 在地图上显示食物
	public boolean showFood() {
		{
			map[food.y][food.x] = '@';
			return true;
		}
	}

	/* 设置游戏难度区 */
	public void set_Difficuty() {
		System.out.println("请选择难度系数（1—10）");
		in = new Scanner(System.in);
		Difficult_Degree = in.nextInt();
	}

	/* 结束游戏区 */
	public void IsOver() {
		// 取蛇头
		Point snakehead = snake.getFirst();
		// 撞墙死
		if (map[snakehead.y][snakehead.x] == '*') {
			GameOver = true;
		}
		// 咬到自己死
		if (map[snakehead.y][snakehead.x] == '#') {
			GameOver = true;
		}
	}

	public static void main(String[] args) throws InterruptedException {

		// 图形界面
		SnakeGame sg1 = new SnakeGame();
		sg1.set_Difficuty();
		JFrame frame1 = new JFrame("贪吃蛇");
		frame1.setSize(CELL_W * WIDTH + 20, CELL_H * HEIGHT + 45);
		frame1.setVisible(true);
		frame1.add(sg1);
		sg1.intiMap();
		sg1.intiSnake();
		sg1.intiFood();
		sg1.repaint();
		// 为窗口添加监听器
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
		// 如果IsAuto 为真，则自己走
		if (IsAuto)
			sg1.runAuto();
	}
}
