package org.oh.pdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 페이지
 * 
 * @author skoh
 */
public class Page {
	/**
	 * 크기
	 * 
	 * <pre>
	 * - 기본값 : A4(new int[] { 595, 842 })
	 * </pre>
	 * 
	 * @see com.itextpdf.text.PageSize
	 */
	protected static int[] size = new int[] { 595, 842 };

	/**
	 * 번호
	 * 
	 * <pre>
	 * - 기본값 : 1
	 * </pre>
	 */
	protected int no = 1;

	/**
	 * 이미지들
	 */
	protected List<Image> images = new ArrayList<Image>();

	/**
	 * 도형들
	 */
	protected List<Shape> shapes = new ArrayList<Shape>();

	/**
	 * 텍스트들
	 */
	protected List<Text> texts = new ArrayList<Text>();

	public static int[] getSize() {
		return size;
	}

	public static void setSize(int[] size) {
		Page.size = size;
	}

	public static int getWidth() {
		return Page.size[0];
	}

	public static int getHeight() {
		return Page.size[1];
	}

	public Page() {
	}

	public Page(int no) {
		this();

		setNo(no);
	}

	public Page(int no, List<Text> texts) {
		this();

		setNo(no);
		setTexts(texts);
	}

	public Page(int no, List<Image> images, List<Text> texts) {
		this();

		setNo(no);
		setImages(images);
		setTexts(texts);
	}

	public Page(int no, List<Image> images, List<Shape> shapes, List<Text> texts) {
		this();

		setNo(no);
		setImages(images);
		setShapes(shapes);
		setTexts(texts);
	}

	public Image getImage(String name) {
		return (Image) Base.getBase(name, images);
	}

	public void setImage(String name, Image image) {
		for (int i = 0; i < texts.size(); i++) {
			Image image1 = images.get(i);
			if (name.equals(image1.getName()))
				images.set(i, image);
		}

		throw new PdfException("Page001", name + " 이미지가 존재하지 않습니다.");
	}

	public void setImage(String name, String filePath) {
		Image image = (Image) Base.getBase(name, images);
		if (image != null)
			image.setFilePath(filePath);
	}

	public void addImage(Image image) {
		images.add(image);
	}

	public int getImageSize() {
		return (images == null) ? 0 : images.size();
	}

	public Shape getShape(String name) {
		return (Shape) Base.getBase(name, shapes);
	}

	public void setShape(String name, Shape shape) {
		for (int i = 0; i < shapes.size(); i++) {
			Shape shape1 = shapes.get(i);
			if (name.equals(shape1.getName()))
				shapes.set(i, shape);
		}

		throw new PdfException("Page002", name + " 도형이 존재하지 않습니다.");
	}

	public void setShape(String name, Shape.TYPE type) {
		Shape shape = (Shape) Base.getBase(name, shapes);
		if (shape != null)
			shape.setType(type);
	}

	public void addShape(Shape shape) {
		shapes.add(shape);
	}

	public int getShapeSize() {
		return (shapes == null) ? 0 : shapes.size();
	}

	public Text getText(String name) {
		return (Text) Base.getBase(name, texts);
	}

	public void setText(String name, Text text) throws PdfException {
		for (int i = 0; i < texts.size(); i++) {
			Text text1 = texts.get(i);
			if (name.equals(text1.getName())) {
				texts.set(i, text);
				return;
			}
		}

		throw new PdfException("Page003", name + " 텍스트가 존재하지 않습니다.");
	}

	public void setText(String name, String value) {
		Text text = (Text) Base.getBase(name, texts);
		if (text != null)
			text.setValue(value);
	}

	public void addText(Text text) {
		texts.add(text);
	}

	public int getTextSize() {
		return (texts == null) ? 0 : texts.size();
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public List<Shape> getShapes() {
		return shapes;
	}

	public void setShapes(List<Shape> shapes) {
		this.shapes = shapes;
	}

	public List<Text> getTexts() {
		return texts;
	}

	public void setTexts(List<Text> texts) {
		this.texts = texts;
	}

	@Override
	public String toString() {
		return "Page [size=" + Arrays.toString(size) + ", no=" + no + ", images=" + images + ", shapes=" + shapes
				+ ", texts=" + texts + "]";
	}
}
