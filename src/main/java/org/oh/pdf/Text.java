package org.oh.pdf;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

/**
 * 텍스트
 * 
 * @author skoh
 */
public class Text extends Base {
	/**
	 * 폰트 디렉토리
	 * 
	 * <pre>
	 * - 기본값 : resources/fonts
	 * </pre>
	 */
	protected static String fontsPath = "resources/fonts";

	/**
	 * 세로정렬 위쪽
	 */
	public static final int ALIGN_TOP = Element.ALIGN_TOP;
	/**
	 * 세로정렬 중간
	 */
	public static final int ALIGN_MIDDLE = Element.ALIGN_MIDDLE;
	/**
	 * 세로정렬 아래쪽
	 */
	public static final int ALIGN_BOTTOM = Element.ALIGN_BOTTOM;

	/**
	 * 고딕
	 */
	public static final String FONT_NAME_HYGOTHIC_MEDIUM = "HYGoThic-Medium";
	/**
	 * 명조
	 */
	public static final String FONT_NAME_HYSMYEONGJO_MEDIUM = "HYSMyeongJo-Medium";
	/**
	 * 명조 표준
	 */
	public static final String FONT_NAME_HYSMYEONGJOSTD_MEDIUM = "HYSMyeongJoStd-Medium";

	/**
	 * 가로모드 폰트 인코딩
	 */
	public static final String FONT_ENCODING_UNIKS_UCS2_H = "UniKS-UCS2-H";
	/**
	 * 세로모드 폰트 인코딩
	 */
	public static final String FONT_ENCODING_UNIKS_UCS2_V = "UniKS-UCS2-V";

	/**
	 * 가로모드 폰트파일 인코딩
	 */
	public static final String FONT_ENCODING_IDENTITY_H = "Identity-H";
	/**
	 * 세로모드 폰트파일 인코딩
	 */
	public static final String FONT_ENCODING_IDENTITY_V = "Identity-V";

	/**
	 * 폰트 내장
	 */
	public static final boolean FONT_EMBEDDED = BaseFont.EMBEDDED;
	/**
	 * 폰트 비내장
	 */
	public static final boolean FONT_NOT_EMBEDDED = BaseFont.NOT_EMBEDDED;

	/**
	 * 보통
	 */
	public static final int FONT_STYLE_NORMAL = Font.NORMAL;
	/**
	 * 굵게
	 */
	public static final int FONT_STYLE_BOLD = Font.BOLD;
	/**
	 * 기울임꼴
	 */
	public static final int FONT_STYLE_ITALIC = Font.ITALIC;
	/**
	 * 밑줄
	 */
	public static final int FONT_STYLE_UNDERLINE = Font.UNDERLINE;
	/**
	 * 
	 */
	public static final int FONT_STYLE_STRIKETHRU = Font.STRIKETHRU;

	/**
	 * 값
	 */
	protected String value = "";

	/**
	 * 세로 정렬
	 * 
	 * <pre>
	 * - 유효값 : {@link #ALIGN_TOP}, {@link #ALIGN_MIDDLE}, {@link #ALIGN_BOTTOM}
	 * - 기본값 : {@link #ALIGN_TOP}
	 * </pre>
	 */
	protected int vAlign = ALIGN_TOP;

	/**
	 * 폰트명
	 * 
	 * <pre>
	 * - 기본 뷰어(Apple iBooks 등)에서 한글이 깨지는 현상이 있음
	 * - 유효값 : {@link #FONT_NAME_HYGOTHIC_MEDIUM}, {@link #FONT_NAME_HYSMYEONGJO_MEDIUM}, {@link #FONT_NAME_HYSMYEONGJOSTD_MEDIUM}
	 * 
	 * 폰트파일
	 * - 유효값 : .ttf, .ttc, .otf, .afm, .pfm 파일 (C:/windows/fonts 폴더의 모든 폰트 사용 가능) -> 단, 1단계는 제외
	 * - 기본값 : {@link #FONT_NAME_HYGOTHIC_MEDIUM}
	 * - 사용예
	 *   C:/windows/fonts/gulim.ttc,0  굴림
	 *   C:/windows/fonts/gulim.ttc,1  굴림체
	 *   C:/windows/fonts/gulim.ttc,2  돋움
	 *   C:/windows/fonts/gulim.ttc,3  돋움체
	 *   C:/windows/fonts/batang.ttc,0 바탕
	 *   C:/windows/fonts/batang.ttc,1 바탕체
	 *   C:/windows/fonts/batang.ttc,2 궁서
	 *   C:/windows/fonts/batang.ttc,3 궁서체
	 *   C:/windows/fonts/malgun.ttf   맑은고딕
	 *   C:/windows/fonts/malgunbd.ttf 맑은고딕 굵게
	 * </pre>
	 */
	protected String font_name = FONT_NAME_HYGOTHIC_MEDIUM;
	/**
	 * 폰트 인코딩
	 * 
	 * <pre>
	 * - 유효값 : {@link #FONT_ENCODING_UNIKS_UCS2_H}, {@link #FONT_ENCODING_UNIKS_UCS2_V}
	 * 
	 * 폰트파일 인코딩
	 * - 유효값 : {@link #FONT_ENCODING_IDENTITY_H}, {@link #FONT_ENCODING_IDENTITY_V} -> 단, 1단계는 제외
	 * - 기본값 : {@link #FONT_ENCODING_UNIKS_UCS2_H}
	 * </pre>
	 */
	protected String font_encoding = FONT_ENCODING_UNIKS_UCS2_H;
	/**
	 * 폰트 내장 여부
	 * 
	 * <pre>
	 * - 유효값 : {@link #FONT_EMBEDDED}, {@link #FONT_NOT_EMBEDDED}
	 * - 기본값 : {@link #FONT_NOT_EMBEDDED}
	 * </pre>
	 */
	protected boolean font_embedded = FONT_NOT_EMBEDDED;
	/**
	 * 폰트 크기
	 * 
	 * <pre>
	 * - 기본값 : 12
	 * </pre>
	 */
	protected int font_size = Font.DEFAULTSIZE;
	/**
	 * 폰트 스타일
	 * 
	 * <pre>
	 * - 유효값 : {@link #FONT_STYLE_NORMAL}, {@link #FONT_STYLE_BOLD}, {@link #FONT_STYLE_ITALIC}, {@link #FONT_STYLE_UNDERLINE}, {@link #FONT_STYLE_STRIKETHRU}
	 * - 기본값 : {@link #FONT_STYLE_NORMAL}
	 * - 사용예 : {@link #FONT_STYLE_BOLD} | {@link #FONT_STYLE_ITALIC}
	 * </pre>
	 */
	protected int font_style = FONT_STYLE_NORMAL;

	public static String getFontsPath() {
		return fontsPath;
	}

	public static void setFontsPath(String fontsPath) {
		Text.fontsPath = fontsPath;
	}

	/**
	 * <pre>
	 * - 기본값
	 *   width : 150
	 *   height : 20
	 * </pre>
	 */
	public Text() {
		width = 150;
		height = 20;
	}

	/**
	 * -> 1단계
	 */
	public Text(String name, int x, int y, int width, int height, int hAlign, int font_size) {
		this();

		setName(name);

		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		sethAlign(hAlign);

		setFont_size(font_size);
	}

	/**
	 * -> 1단계
	 */
	public Text(String name, int x, int y, int width, int height, int hAlign, int[] color, int font_size,
			int[] border_color, float border_width) {
		this();

		setName(name);

		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		sethAlign(hAlign);

		setColor(color);
		setFont_size(font_size);

		setBorder_color(border_color);
		setBorder_width(border_width);
	}

	public Text(String name, String value, int x, int y, int width, int height, int font_size) {
		this();

		setName(name);
		setValue(value);

		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);

		setFont_size(font_size);
	}

	public Text(String name, String value, int x, int y, int width, int height, int hAlign, int vAlign, int font_size) {
		this();

		setName(name);
		setValue(value);

		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		sethAlign(hAlign);
		setvAlign(vAlign);

		setFont_size(font_size);
	}

	public Text(String name, String value, int x, int y, int width, int height, String font_name, String font_encoding,
			int font_size) {
		this();

		setName(name);
		setValue(value);

		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);

		setFont_name(font_name);
		setFont_encoding(font_encoding);
		setFont_size(font_size);
	}

	public Text(String name, String value, int x, int y, int width, int height, int hAlign, int vAlign,
			String font_name, String font_encoding, int font_size) {
		this();

		setName(name);
		setValue(value);

		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		sethAlign(hAlign);
		setvAlign(vAlign);

		setFont_name(font_name);
		setFont_encoding(font_encoding);
		setFont_size(font_size);
	}

	public Text(String name, String value, int x, int y, int width, int height, int hAlign, int vAlign, int[] color,
			int font_size, int font_style) {
		this();

		setName(name);
		setValue(value);

		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		sethAlign(hAlign);
		setvAlign(vAlign);

		setColor(color);
		setFont_size(font_size);
		setFont_style(font_style);
	}

	public Text(String name, String value, int x, int y, int width, int height, int hAlign, int vAlign, int rotation,
			int[] color, String font_name, String font_encoding, boolean font_embedded, int font_size, int font_style) {
		this();

		setName(name);
		setValue(value);

		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		sethAlign(hAlign);
		setvAlign(vAlign);
		setRotation(rotation);

		setColor(color);
		setFont_name(font_name);
		setFont_encoding(font_encoding);
		setFont_embedded(font_embedded);
		setFont_size(font_size);
		setFont_style(font_style);
	}

	public Text(String name, String value, int x, int y, int width, int height, int hAlign, int vAlign, int rotation,
			int[] color, String font_name, String font_encoding, boolean font_embedded, int font_size, int font_style,
			int[] border_color, float border_width) {
		this();

		setName(name);
		setValue(value);

		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		sethAlign(hAlign);
		setvAlign(vAlign);
		setRotation(rotation);

		setColor(color);
		setFont_name(font_name);
		setFont_encoding(font_encoding);
		setFont_embedded(font_embedded);
		setFont_size(font_size);
		setFont_style(font_style);

		setBorder_color(border_color);
		setBorder_width(border_width);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getvAlign() {
		return vAlign;
	}

	public void setvAlign(int vAlign) {
		this.vAlign = vAlign;
	}

	public String getFont_name() {
		return font_name;
	}

	public void setFont_name(String font_name) {
		this.font_name = font_name;
	}

	public String getFont_encoding() {
		return font_encoding;
	}

	public void setFont_encoding(String font_encoding) {
		this.font_encoding = font_encoding;
	}

	public boolean isFont_embedded() {
		return font_embedded;
	}

	public void setFont_embedded(boolean font_embedded) {
		this.font_embedded = font_embedded;
	}

	public int getFont_size() {
		return font_size;
	}

	public void setFont_size(int font_size) {
		this.font_size = font_size;
	}

	public int getFont_style() {
		return font_style;
	}

	public void setFont_style(int font_style) {
		this.font_style = font_style;
	}

	@Override
	public String toString() {
		return "Text [" + super.toString() + ", value=" + value + ", vAlign=" + vAlign + ", font_name=" + font_name
				+ ", font_encoding=" + font_encoding + ", font_embedded=" + font_embedded + ", font_size=" + font_size
				+ ", font_style=" + font_style + "]";
	}
}
