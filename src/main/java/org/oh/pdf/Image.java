package org.oh.pdf;

/**
 * 이미지
 * 
 * @author skoh
 */
public class Image extends Base {
	/**
	 * 파일경로
	 * 
	 * <pre>
	 * - 유효값 : .jpg, .png, .gif 파일
	 * </pre>
	 */
	protected String filePath = null;

	/**
	 * 확대/축소 비율(단위 : %)
	 * 
	 * <pre>
	 * - 유효값 : 0 ~
	 * - 기본값 : 100
	 * </pre>
	 */
	protected int scale_percent = 100;

	/**
	 * <pre>
	 * - 기본값
	 *   hAlign : {@link #ALIGN_NONE}
	 * </pre>
	 */
	public Image() {
		hAlign = ALIGN_NONE;
	}

	/**
	 * 배경이미지로 사용
	 */
	public Image(String name, String filePath) {
		this();

		setName(name);
		setFilePath(filePath);
		setWidth(Page.getWidth());
		setHeight(Page.getHeight());
	}

	/**
	 * 배경이미지 외에는 반드시 이미지 크기를 입력 (좌표 계산시 필요)
	 */
	public Image(String name, String filePath, int width, int height) {
		this();

		setName(name);
		setFilePath(filePath);
		setWidth(width);
		setHeight(height);
	}

	/**
	 * 좌표값으로 위치 지정
	 */
	public Image(String name, String filePath, int x, int y, int width, int height) {
		this();

		setName(name);
		setFilePath(filePath);
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
	}

	public Image(String name, String filePath, int x, int y, int width, int height, int rotation, int scale_percent) {
		this();

		setName(name);
		setFilePath(filePath);
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		setRotation(rotation);
		setScale_percent(scale_percent);
	}

	/**
	 * 가로 정렬로 위치 지정
	 */
	public Image(String name, String filePath, int width, int height, int hAlign) {
		this();

		setName(name);
		setFilePath(filePath);
		setWidth(width);
		setHeight(height);
		sethAlign(hAlign);
	}

	public Image(String name, String filePath, int width, int height, int hAlign, int rotation, int scale_percent) {
		this();

		setName(name);
		setFilePath(filePath);
		setWidth(width);
		setHeight(height);
		sethAlign(hAlign);
		setRotation(rotation);
		setScale_percent(scale_percent);
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getScale_percent() {
		return scale_percent;
	}

	public void setScale_percent(int scale_percent) {
		this.scale_percent = scale_percent;
	}

	@Override
	public String toString() {
		return "Image [filePath=" + filePath + ", scale_percent=" + scale_percent + "]";
	}
}
