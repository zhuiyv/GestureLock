package com.android.view;

public class MyCycle {
	private float r;         // 半径长度
	private int x;		//简略坐标
	private int y;		//简略坐标
	private int perSize;	//坐标系数
	private boolean onTouch; // false=未选中
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
	
	
	//获取实际x坐标
	public int getOx() {
		return perSize * (x * 2 + 1);
	}
	//获取实际y坐标
	public int getOy() {
		return perSize * (y * 2 + 1);
	}
	
	// 第y行 第x列 的编号
		public int getNum() {
			return (3 * this.y + this.x);
		}
}
