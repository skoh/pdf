package org.oh.pdf;

import java.util.Arrays;
import java.util.List;

import com.itextpdf.text.Element;

/**
 * 기본
 * 
 * @author skoh
 */
public abstract class Base {
	/**
	 * 정렬 안함
	 */
	public static final int ALIGN_NONE = -1;

	/**
	 * 가로정렬 왼쪽
	 */
	public static final int ALIGN_LEFT = Element.ALIGN_LEFT;
	/**
	 * 가로정렬 가운데
	 */
	public static final int ALIGN_CENTER = Element.ALIGN_CENTER;
	/**
	 * 가로정렬 오른쪽
	 */
	public static final int ALIGN_RIGHT = Element.ALIGN_RIGHT;

	/**
	 * 검은색
	 */
	public static final int[] COLOR_BLACK = new int[] { 0, 0, 0 };
	public static final int[] COLOR_LIGHT_GRAY = new int[] { 192, 192, 192 };
	public static final int[] COLOR_GRAY = new int[] { 128, 128, 128 };
	public static final int[] COLOR_DARK_GRAY = new int[] { 64, 64, 64 };
	public static final int[] COLOR_RED = new int[] { 255, 0, 0 };
	public static final int[] COLOR_PINK = new int[] { 255, 175, 175 };
	public static final int[] COLOR_ORANGE = new int[] { 255, 200, 0 };
	public static final int[] COLOR_YELLOW = new int[] { 255, 255, 0 };
	public static final int[] COLOR_GREEN = new int[] { 0, 255, 0 };
	public static final int[] COLOR_MAGENTA = new int[] { 255, 0, 255 };
	public static final int[] COLOR_CYAN = new int[] { 0, 255, 255 };
	public static final int[] COLOR_BLUE = new int[] { 0, 0, 255 };
	/**
	 * 흰색
	 */
	public static final int[] COLOR_WHITE = new int[] { 255, 255, 255 };

	/**
	 * 이름
	 */
	protected String name = null;

	/**
	 * x 좌표
	 * 
	 * <pre>
	 * - 기본값 : 0
	 * </pre>
	 */
	protected int x = 0;
	/**
	 * y 좌표
	 * 
	 * <pre>
	 * - 기본값 : 0
	 * </pre>
	 */
	protected int y = 0;
	/**
	 * 넓이
	 * 
	 * <pre>
	 * - 기본값 : 0
	 * </pre>
	 */
	protected int width = 0;
	/**
	 * 높이
	 * 
	 * <pre>
	 * - 기본값 : 0
	 * </pre>
	 */
	protected int height = 0;

	/**
	 * 가로 정렬
	 * 
	 * <pre>
	 * - 유효값 : {@link #ALIGN_NONE}, {@link #ALIGN_LEFT}, {@link #ALIGN_CENTER}, {@link #ALIGN_RIGHT}
	 * - 기본값 : {@link #ALIGN_LEFT}
	 * </pre>
	 */
	protected int hAlign = ALIGN_LEFT;

	/**
	 * 색상(RGB)
	 * 
	 * <pre>
	 * - 기본값 : 검은색({@link #COLOR_BLACK})
	 * - null : 투명
	 * </pre>
	 */
	protected int[] color = COLOR_BLACK;
	/**
	 * 회전각도(시계반대방향) : 중심축 기준
	 * 
	 * <pre>
	 * - 유효값 : 0 ~ 360 도
	 * - 기본값 : 0
	 * </pre>
	 */
	protected int rotation = 0;

	/**
	 * 외곽선 색상(RGB)
	 * 
	 * <pre>
	 * - 기본값 : 검은색({@link #COLOR_BLACK})
	 * - null : 투명
	 * </pre>
	 */
	protected int[] border_color = COLOR_BLACK;
	/**
	 * 외곽선 굵기(단위 : 픽셀)
	 * 
	 * <pre>
	 * - 기본값 : 0
	 * </pre>
	 */
	protected float border_width = 0;
	/**
	 * 외곽선 길이(단위 : 픽셀)
	 * 
	 * <pre>
	 * - 유효값 : 양수(0:실선, 1~:점선)
	 * - 기본값 : 0(실선)
	 * </pre>
	 */
	protected int border_legnth = 0;

	public static Base getBase(String name, List<? extends Base> bases) {
		for (Base base : bases) {
			if (name.equals(base.getName()))
				return base;
		}
		return null;
	}

	public Base() {
	}

	public Base(String name, int x, int y, int width, int height, int[] color) {
		this();

		setName(name);

		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);

		setColor(color);
	}

	public Base(String name, int x, int y, int width, int height, int[] color, int rotation, int[] border_color,
			float border_width, int border_legnth) {
		this();

		setName(name);

		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);

		setColor(color);
		setRotation(rotation);

		setBorder_color(border_color);
		setBorder_width(border_width);
		setBorder_legnth(border_legnth);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return Page.getHeight() - (y + height);
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int gethAlign() {
		return hAlign;
	}

	public void sethAlign(int hAlign) {
		this.hAlign = hAlign;
	}

	public int[] getColor() {
		return color;
	}

	public void setColor(int[] color) {
		this.color = color;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public int[] getBorder_color() {
		return border_color;
	}

	public void setBorder_color(int[] border_color) {
		this.border_color = border_color;
	}

	public float getBorder_width() {
		return border_width;
	}

	public void setBorder_width(float border_width) {
		this.border_width = border_width;
	}

	public int getBorder_legnth() {
		return border_legnth;
	}

	public void setBorder_legnth(int border_legnth) {
		this.border_legnth = border_legnth;
	}

	@Override
	public String toString() {
		return "Base [name=" + name + ", x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", hAlign="
				+ hAlign + ", color=" + Arrays.toString(color) + ", rotation=" + rotation + ", border_color="
				+ Arrays.toString(border_color) + ", border_width=" + border_width + ", border_legnth=" + border_legnth
				+ "]";
	}
}
