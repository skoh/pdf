package org.oh.pdf;

/**
 * 도형
 * 
 * @author skoh
 */
public class Shape extends Base {
	/**
	 * 모양
	 */
	public enum TYPE {
		/**
		 * 선
		 */
		LINE,

		/**
		 * 사각
		 */
		RECTANGLE,

		/**
		 * 원
		 */
		ELLIPSE
	}

	/**
	 * 모양
	 * 
	 * <pre>
	 * - 유효값 : 선({@link TYPE#LINE}), 사각({@link TYPE#RECTANGLE}), 원({@link TYPE#ELLIPSE})
	 * - 기본값 : 선({@link TYPE#LINE})
	 * </pre>
	 */
	protected TYPE type = TYPE.LINE;

	public Shape() {
	}

	public Shape(TYPE type) {
		this();

		switch (type) {
		case LINE:

			break;

		case RECTANGLE:
			color = null;

			break;

		case ELLIPSE:
			color = null;

			break;
		}
	}

	public Shape(String name, int x, int y, int width, int height, int[] color, int rotation, TYPE type) {
		this(type);

		setName(name);

		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);

		setColor(color);
		setRotation(rotation);

		setType(type);
	}

	public Shape(String name, int x, int y, int width, int height, int[] color, int rotation, float border_width,
			int border_legnth, TYPE type) {
		this(type);

		setName(name);

		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);

		setColor(color);
		setRotation(rotation);

		setBorder_width(border_width);
		setBorder_legnth(border_legnth);

		setType(type);
	}

	public Shape(String name, int x, int y, int width, int height, int[] border_color, float border_width,
			int border_legnth, TYPE type) {
		this(type);

		setName(name);

		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);

		setBorder_color(border_color);
		setBorder_width(border_width);
		setBorder_legnth(border_legnth);

		setType(type);
	}

	public Shape(String name, int x, int y, int width, int height, int[] color, int rotation, int[] border_color,
			float border_width, int border_legnth, TYPE type) {
		this(type);

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

		setType(type);
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Shape [" + super.toString() + ", type=" + type + "]";
	}
}
