package com.android.view;

public class MyCycle {
	private float r;         // �뾶����
	private int x;		//��������
	private int y;		//��������
	private int perSize;	//����ϵ��
	private boolean onTouch; // false=δѡ��
	public float getR() {
		return r;
	}
	public void setR(float r) {
		this.r = r;
	}
	public boolean isOnTouch() {
		return onTouch;
	}
	public void setOnTouch(boolean onTouch) {
		this.onTouch = onTouch;
	}
	public boolean isPointIn(int x, int y) {
		double distance = Math.sqrt((x - this.getOx()) * (x - this.getOx()) + (y - this.getOy()) * (y - this.getOy()));
		return distance < r;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public int getPerSize() {
		return perSize;
	}
	public void setPerSize(int perSize) {
		this.perSize = perSize;
	}
	
	
	//��ȡʵ��x����
	public int getOx() {
		return perSize * (x * 2 + 1);
	}
	//��ȡʵ��y����
	public int getOy() {
		return perSize * (y * 2 + 1);
	}
	
	// ��y�� ��x�� �ı��
		public int getNum() {
			return (3 * this.y + this.x);
		}
}
