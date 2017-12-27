package org.oh.pdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.ListOfArrayDataSource;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

import org.apache.commons.io.IOUtils;
import org.oh.pdf.Shape.TYPE;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * PDF 제작 도구
 * 
 * <pre>
 * <strong>- jdk1.6 이상 호환</strong> (필요시 jdk1.5 적용 가능) 
 * 
 * <strong>- 단계별 제작 방식</strong>
 * 1 단계. 페이지별로 이미지, 도형, 필드변수 등을 입력하여 PDF파일을 만든 후 텍스트(개인화 정보)를 매핑하는 방식
 * 2 단계. 페이지별로 이미지, 도형, 텍스트(개인화 정보) 등을 입력하여 PDF 파일을 만드는 방식
 * 3 단계. 개인화 정보를 입력한 HTML 파일을 PDF로 변환하는 방식
 * 4 단계. XML(디자인) 파일에 개인화 정보를 입력하여 PDF로 변환하는 방식
 * 1+2 단계. 먼저 배경이 되는 PDF 파일을 바닥에 깔고 그 위에 이미지, 도형, 텍스트(개인화 정보) 등을 입히는 방식
 * 2+3 단계. 먼저 이미지, 도형, 텍스트 등을 입력한 후 그 위에 HTML 파일(개인화 정보)을 입히는 방식
 * 
 * <strong>- 문서</strong>
 *   <a href="http://121.78.144.107:8010/pdf">PDF 저작기</a>
 * 
 * <strong>- 단계별 샘플 코드</strong>
 * {@code
 * // <테스트시 고려사항>
 * // 1. 다양한 폰트, 스타일, PDF뷰어 적용
 * // 2. 다양한 모바일 단말기(안드로이드, IOS)의 기본 PDF뷰어로 확인
 * Page page = null;
 * Image image = null;
 * Shape shape = null;
 * Text text = null;
 * 
 * List<Page> pages = new ArrayList<Page>();
 * List<Image> images = new ArrayList<Image>();
 * List<Shape> shapes = new ArrayList<Shape>();
 * List<Text> texts = new ArrayList<Text>();
 * 
 * Pdf pdf = new Pdf("results/pdf/temp_", pages, "results/pdf/temp_.pdf");
 * Pdf newPdf = new Pdf("newPdf");
 * 
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * // 간단한 예제
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * 
 * // 페이지 크기 (A4는 생략 가능)
 * // Page.setSize(new int[] { 595, 842 });
 * Pdf postopia = new Pdf("results/pdf/postopia");
 * 
 * // 1 페이지
 * Page page1 = new Page(1);
 * postopia.addPage(page1);
 * 
 * // 이미지
 * page1.addImage(new Image("image1", "resources/img/postopia.jpg"));
 * 
 * // 도형
 * page1.addShape(new Shape("shape1", 200, 100, 200, 100, Base.COLOR_RED, 0, 3, 3, TYPE.LINE));
 * 
 * // 텍스트 (리눅스에서 폰트는 따로 복사해서 경로 지정)
 * // page1.addText(new Text("text1", "홍길동", 165, 316, 400, 20, Text.ALIGN_LEFT, Text.ALIGN_TOP, 0, Base.COLOR_BLACK,
 * // "C:/windows/fonts/gulim.ttc,0", Text.FONT_ENCODING_IDENTITY_H, Text.FONT_NOT_EMBEDDED, 15,
 * // Text.FONT_STYLE_NORMAL, Base.COLOR_RED, 0.5f)); // 좌표 지정용
 * page1.addText(new Text("text1", "홍길동", 165, 316, 400, 20, "C:/windows/fonts/gulim.ttc,0",
 * 		Text.FONT_ENCODING_IDENTITY_H, 15));
 * 
 * // PDF 생성
 * createPdf2(postopia, "1");
 * 
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * // 1단계 테스트
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * 
 * // 1. PDF 파일 생성
 * // 이미지
 * images = new ArrayList<Image>();
 * image = new Image("image1", "resources/img/page1.jpg", 595, 842);
 * images.add(image);
 * 
 * image = new Image("image11", "resources/img/page1.jpg", 100, 100, 100, 100, 90, 20);
 * images.add(image);
 * 
 * // 도형
 * shapes = new ArrayList<Shape>();
 * shape = new Shape("shape1", 100, 300, 200, 100, Base.COLOR_GREEN, 0, Base.COLOR_RED, 3, 3, TYPE.ELLIPSE);
 * shapes.add(shape);
 * 
 * // 텍스트
 * texts = new ArrayList<Text>();
 * text = new Text("text1", 370, 613, 150, 20, Text.ALIGN_LEFT, 20);
 * texts.add(text);
 * 
 * // 1 페이지
 * page = new Page(1, images, shapes, texts);
 * pages.add(page);
 * 
 * // 이미지
 * images = new ArrayList<Image>();
 * image = new Image("image2", "resources/img/page2.jpg", 595, 842);
 * images.add(image);
 * 
 * // 텍스트
 * texts = new ArrayList<Text>();
 * text = new Text("text2", 325, 470, 150, 20, Text.ALIGN_CENTER, Base.COLOR_BLUE, 20, Base.COLOR_BLACK, 0.5f);
 * texts.add(text);
 * 
 * // 2 페이지
 * page = new Page(2, images, texts);
 * pages.add(page);
 * 
 * createPdfImage(pdf);
 * 
 * pdf.setFilePath("results/pdf/form1.pdf");
 * createPdfForm(pdf, true);
 * 
 * // 2. 개인화 정보 매핑
 * Map<String, String> textMap = new HashMap<String, String>();
 * 
 * textMap.put("text1", "홍길동");
 * textMap.put("text2", "秦王殘劍");
 * 
 * System.out.println("1단계: " + pdf);
 * System.out.println("1단계: " + textMap);
 * createPdf(textMap, "results/pdf/form1.pdf", "results/pdf/result1.pdf");
 * createPdf(textMap, "results/pdf/form1.pdf", "results/pdf/result1_1.pdf", "1");
 * encryptPdf("results/pdf/result1.pdf", "results/pdf/result1_2.pdf", "1");
 * 
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * // 2단계 테스트
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * 
 * pdf.setPdfFilePath("resources/pdf/다이렉트e패키지북1.pdf");
 * 
 * // 1 페이지
 * page = pdf.getPageByNo(1);
 * 
 * // 이미지
 * page.addImage(new Image("image1", "resources/img/page1.jpg", 100, 100, 100, 100, 90, 20));
 * page.addImage(new Image("image2", "resources/img/logo.jpg", 102, 100, Image.ALIGN_RIGHT, 0, 200));
 * 
 * // 도형
 * shape = new Shape("shape1", 100, 300, 200, 100, Base.COLOR_RED, 0, 3, 3, TYPE.LINE);
 * page.addShape(shape);
 * shape = new Shape("shape2", 100, 300, 200, 100, Base.COLOR_RED, 3, 3, TYPE.RECTANGLE);
 * page.addShape(shape);
 * shape = new Shape("shape3", 100, 300, 200, 100, Base.COLOR_RED, 3, 3, TYPE.ELLIPSE);
 * page.addShape(shape);
 * shape = new Shape("shape4", 100, 450, 200, 100, Base.COLOR_BLUE, 0, TYPE.RECTANGLE);
 * page.addShape(shape);
 * shape = new Shape("shape5", 100, 450, 200, 100, Base.COLOR_GREEN, 0, TYPE.ELLIPSE);
 * page.addShape(shape);
 * shape = new Shape("shape6", 100, 600, 200, 100, Base.COLOR_BLUE, 0, Base.COLOR_RED, 3, 3, TYPE.RECTANGLE);
 * page.addShape(shape);
 * shape = new Shape("shape7", 100, 600, 200, 100, Base.COLOR_GREEN, 0, Base.COLOR_RED, 3, 3, TYPE.ELLIPSE);
 * page.addShape(shape);
 * 
 * // 텍스트
 * text = new Text("text1", "홍길동2", 370, 613, 150, 150, Text.ALIGN_CENTER, Text.ALIGN_TOP, 20);
 * page.setText("text1", text);
 * 
 * text = new Text("text3", "손오공2", 325, 470, 150, 150, "C:/windows/fonts/gulim.ttc,0",
 * 		Text.FONT_ENCODING_IDENTITY_H, 20);
 * page.addText(text);
 * 
 * // 2 페이지
 * page = pdf.getPageByNo(2);
 * 
 * // 텍스트
 * text = new Text("text2", "秦王殘劍2", 370, 613, 150, 150, Text.ALIGN_LEFT, Text.ALIGN_TOP, 20);
 * page.setText("text2", text);
 * 
 * text = new Text("text4", "秦王殘劍2", 325, 470, 150, 150, Text.ALIGN_RIGHT, Text.ALIGN_MIDDLE, 0, Base.COLOR_BLUE,
 * // Text.FONT_NAME_HYGOTHIC_MEDIUM, Text.FONT_ENCODING_UNIKS_UCS2_H,
 * 		"C:/windows/fonts/gulim.ttc,0", Text.FONT_ENCODING_IDENTITY_H, // 굴림
 * 		// "C:/windows/fonts/malgun.ttf", Text.FONT_ENCODING_IDENTITY_H, // 맑은고딕
 * 		Text.FONT_EMBEDDED, 30, Text.FONT_STYLE_BOLD | Text.FONT_STYLE_ITALIC, Base.COLOR_BLUE, 0.5f);
 * page.addText(text);
 * 
 * pdf.setFilePath("results/pdf/result2.pdf");
 * System.out.println("2단계: " + pdf);
 * createPdf2(pdf);
 * 
 * pdf.setFilePath("results/pdf/result2_1");
 * createPdf2(pdf, "1");
 * 
 * encryptPdf("results/pdf/result2.pdf", "results/pdf/result2_2.pdf", "1");
 * 
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * // 3단계 테스트
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * 
 * newPdf = new Pdf("results/pdf/test01", new String[] { "resources/xml/test01.html" });
 * convertHtmlToPdf(newPdf, "1");
 * 
 * // CSS
 * newPdf = new Pdf("results/pdf/style_inline", new String[] { "resources/xml/style_inline.html" });
 * convertHtmlToPdf(newPdf);
 * 
 * newPdf = new Pdf("results/pdf/style_outline", new String[] { "resources/xml/style_outline.html" });
 * convertHtmlToPdf(newPdf);
 * 
 * newPdf = new Pdf("results/pdf/style_link", new String[] { "resources/xml/style_link.html" });
 * convertHtmlToPdf(newPdf);
 * 
 * newPdf = new Pdf("results/pdf/style_no", new String[] { "resources/xml/style_no.html" });
 * // newPdf.setCssFilePaths(new String[] { Pdf.DEFAULT_CSS });
 * newPdf.setCssFilePaths("resources/xml/style.css");
 * convertHtmlToPdf(newPdf);
 * 
 * newPdf = new Pdf("results/pdf/walden2", new String[] { "resources/xml/walden2.html" },
 * 		new String[] { "resources/xml/walden.css" });
 * convertHtmlToPdf(newPdf);
 * 
 * // 폰트
 * Map<String, String> inputFontMap = new HashMap<String, String>();
 * inputFontMap.put("MS Mincho", "resources/fonts/cfmingeb.ttf");
 * inputFontMap.put("Serif", "resources/fonts/PT_Serif-Web-Regular.ttf");
 * newPdf = new Pdf("results/pdf/hero", new String[] { "resources/xml/hero.html" }, inputFontMap);
 * convertHtmlToPdf(newPdf);
 * 
 * // 샘플 01
 * newPdf = new Pdf("results/pdf/신청안내페이지", new String[] { "resources/xml/신청안내페이지.html" });
 * convertHtmlToPdf(newPdf);
 * 
 * // 샘플 02
 * newPdf = new Pdf("results/pdf/layout_school", new String[] { "resources/xml/layout_school.html" });
 * convertHtmlToPdf(newPdf);
 * 
 * pdf.setFilePath("results/pdf/layout_school");
 * pdf.setHtmlFilePaths("resources/xml/layout_school.html");
 * System.out.println("3단계: " + pdf);
 * convertHtmlToPdf(pdf);
 * 
 * // HTML String
 * String html = IOUtils.toString(new FileInputStream("resources/xml/layout_school.html"), "UTF-8");
 * newPdf = new Pdf("results/pdf/layout_school-1", IOUtils.toInputStream(html));
 * convertHtmlToPdf(newPdf);
 * 
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * // 4단계 테스트
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * 
 * Map<String, Object> parameters = new HashMap<String, Object>();
 * 
 * // 1. Parameter > String 추가
 * parameters.put("Parameter1", "파라미터1");
 * 
 * // 2. Main DataSource(1개) > ResultSet 추가
 * // 주의) ResultSet 은 반복 사용 불가
 * // PreparedStatement pstmt = null;
 * // ResultSet rs = null;
 * // Class.forName("oracle.jdbc.OracleDriver");
 * // Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "hr", "hr");
 * // try {
 * // pstmt = conn.prepareStatement("SELECT username FROM userinfo");
 * // rs = pstmt.executeQuery();
 * // parameters.put(MAIN_DATA_SOURCE, rs);
 * // // } catch (Exception e) {
 * // // conn.close();
 * // // }
 * 
 * // // 3. Sub DataSource(n개) > ResultSet 추가
 * // // Class.forName("oracle.jdbc.OracleDriver");
 * // // conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "hr", "hr");
 * // // try {
 * // pstmt = conn.prepareStatement("SELECT userid, point FROM userinfo WHERE point = 2000");
 * // rs = pstmt.executeQuery();
 * // parameters.put("DataSource1", rs);
 * // } catch (Exception e) {
 * // conn.close();
 * // e.printStackTrace();
 * // }
 * 
 * // 2-1. Main DataSource(1개) > List 추가
 * List<Object[]> list = new ArrayList<Object[]>();
 * for (int i = 1; i <= 20; i++)
 * 	list.add(new Object[] { "회원" + i });
 * parameters.put(MAIN_DATA_SOURCE, list);
 * addParameterColumnNames(MAIN_DATA_SOURCE, new String[] { "USERNAME" }, parameters);
 * 
 * // 3-1. Sub DataSource(n개) > List 추가
 * list = new ArrayList<Object[]>();
 * list.add(new Object[] { "회원3", 1000 });
 * list.add(new Object[] { "회원4", 2000 });
 * parameters.put("DataSource1", list);
 * addParameterColumnNames("DataSource1", new String[] { "USERID", "POINT" }, parameters);
 * 
 * list = new ArrayList<Object[]>();
 * list.add(new Object[] { "Member5", 3000 });
 * list.add(new Object[] { "회원6", 4000 });
 * parameters.put("DataSource2", list);
 * addParameterColumnNames("DataSource2", new String[] { "USERID", "POINT" }, parameters);
 * 
 * // 4. PDF 변환(1개)
 * System.out.println("4단계: " + parameters);
 * convertXmlToPdf("resources/xml/Blank_A4.jrxml", parameters, "results/pdf/Blank_A4.pdf");
 * 
 * // 4-1. PDF 변환(n개)
 * List<Map<String, Object>> parametersList = new ArrayList<Map<String, Object>>();
 * parametersList.add(parameters);
 * parametersList.add(parameters);
 * convertXmlToPdf(new String[] { "resources/xml/Blank_A4.jrxml", "resources/xml/Blank_A4.jrxml" },
 * 		parametersList, "results/pdf/Blank_A4_1.pdf", "1");
 * 
 * // 4-2. PDF 변환(String 입력)
 * String xml = IOUtils.toString(new FileInputStream("resources/xml/Blank_A4.jrxml"), "UTF-8");
 * InputStream xmlStream = IOUtils.toInputStream(xml);
 * convertXmlToPdfStream(xmlStream, parameters, "results/pdf/Blank_A4_2.pdf", "1");
 * }
 * </pre>
 * 
 * @author skoh
 * 
 * @see <a href="http://itextpdf.com/api">itextpdf api</a>
 * @see <a href="http://itextpdf.com/examples">itextpdf examples</a>
 * @see <a href="http://tutorials.jenkov.com/java-itext">itextpdf tutorials</a>
 * @see <a href="http://jasperreports.sourceforge.net/api">jasperreports api</a>
 * @see <a href="http://community.jaspersoft.com/wiki/jasperreports-library-samples">jasperreports sample</a>
 * @see <a href="http://community.jaspersoft.com/wiki/jasperreports-library-tutorial">jasperreports tutorials</a>
 * @see <a href="http://community.jaspersoft.com/documentation/tibco-jaspersoft-studio-user-guide/v60/getting-started-jaspersoft-studio">jaspersoft studio user guide</a>
 */
public abstract class PdfUtils {
	public static final String MAIN_DATA_SOURCE = JRParameter.REPORT_DATA_SOURCE;
	public static final String COLUMN_NAMES = "_COLUMN_NAMES_";

	private static final JREmptyDataSource JR_EMPTY_DATASOURCE = new JREmptyDataSource();

	protected static XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(Text.getFontsPath());
	static {
		fontProvider.registerDirectories();
	}

	/**
	 * PDF 파일을 암호화한다.
	 * 
	 * @param inputPdfFilePath 입력 PDF 파일경로
	 * @param outputPdfFilePath 출력 PDF 파일경로
	 * @param password 암호화 패스워드(AES_128)
	 * 
	 * @throws PdfException
	 */
	public static void encryptPdf(String inputPdfFilePath, String outputPdfFilePath, String password)
			throws PdfException {
		if (inputPdfFilePath == null || outputPdfFilePath == null || password == null)
			return;

		try {
			PdfReader reader = new PdfReader(inputPdfFilePath);

//			PdfEncryptor.encrypt(reader, new FileOutputStream(outputPdfFilePath), password.getBytes(),
//					password.getBytes(), PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING, true); // STANDARD_128
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputPdfFilePath));
			stamper.setEncryption(password.getBytes(), password.getBytes(), PdfWriter.ALLOW_COPY
					| PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);

			stamper.close();
			reader.close();
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF001", "PDF 파일 암호화 중 오류 발생 [inputPdfFilePath:" + inputPdfFilePath
					+ ", outputPdfFilePath:" + outputPdfFilePath + ", password:" + password + "]", e);
		}
	}

	/**
	 * PDF Writer를 통해 암호화한다.
	 * 
	 * @param password 암호화 패스워드(AES_128)
	 * @param writer PdfWriter
	 * 
	 * @throws PdfException
	 */
	public static void encryptPdf(String password, PdfWriter writer) throws PdfException {
		if (password == null || writer == null)
			return;

		try {
			writer.setEncryption(password.getBytes(), password.getBytes(), PdfWriter.ALLOW_COPY
					| PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF002", "PDF 파일 암호화 중 오류 발생 [password:" + password + "]", e);
		}
	}

	/**
	 * 이미지 정보를 PDF 문서를 추가한다.
	 * 
	 * @param images 이미지 정보들
	 * @param document PdfDocument
	 * 
	 * @throws PdfException
	 */
	public static void addPdfImages(List<Image> images, Document document) throws PdfException {
		if (images == null || document == null)
			return;

		try {
			for (Image image : images) {
				com.itextpdf.text.Image itextImage = com.itextpdf.text.Image.getInstance(image.getFilePath());

				if (image.gethAlign() == Image.ALIGN_NONE)
					itextImage.setAbsolutePosition(image.getX(), image.getY());
				else
					itextImage.setAlignment(image.gethAlign());

				if (image.getRotation() > 0)
					itextImage.setRotationDegrees(image.getRotation());

				if (image.getScale_percent() != 100)
					itextImage.scalePercent(image.getScale_percent());

				document.add(itextImage);
			}
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF004", "PDF 이미지 추가 중 오류 발생 [images:" + images + "]", e);
		}
	}

	/**
	 * 도형 정보를 PDF 문서를 추가한다.
	 * 
	 * @param shapes 도형 정보들
	 * @param writer PdfWriter
	 * 
	 * @throws PdfException
	 */
	public static void addPdfShapes(List<Shape> shapes, PdfWriter writer) throws PdfException {
		if (shapes == null || writer == null)
			return;

		try {
			int[] color = null;
			BaseColor baseColor = null;
			PdfContentByte pcb = writer.getDirectContent();

			for (Shape shape : shapes) {
				pcb.saveState();

				switch (shape.getType()) {
				case LINE:
					if (shape.getBorder_width() > 0) {
						color = shape.getColor();
						baseColor = new BaseColor(color[0], color[1], color[2]);
						pcb.setColorStroke(baseColor);

						pcb.setLineWidth(shape.getBorder_width());
						if (shape.getBorder_legnth() > 0)
							pcb.setLineDash(shape.getBorder_legnth(), 0);
					}

					pcb.moveTo(shape.getX(), shape.getY() + shape.getHeight());
					pcb.lineTo(shape.getX() + shape.getWidth(), shape.getY());

					break;

				case RECTANGLE:
					Rectangle rect = new Rectangle(shape.getX(), shape.getY() + shape.getHeight(), shape.getX()
							+ shape.getWidth(), shape.getY());
					rect.setBorder(Rectangle.BOX);

					if (shape.getColor() != null) {
						color = shape.getColor();
						baseColor = new BaseColor(color[0], color[1], color[2]);
						rect.setBackgroundColor(baseColor);
					}

					if (shape.getBorder_width() > 0) {
						color = shape.getBorder_color();
						baseColor = new BaseColor(color[0], color[1], color[2]);
						rect.setBorderColor(baseColor);

						rect.setBorderWidth(shape.getBorder_width());
						if (shape.getBorder_legnth() > 0)
							pcb.setLineDash(shape.getBorder_legnth(), 0);
					}

					pcb.rectangle(rect);

					break;

				case ELLIPSE:
					if (shape.getColor() != null) {
						color = shape.getColor();
						baseColor = new BaseColor(color[0], color[1], color[2]);
						pcb.setColorFill(baseColor);
					}

					if (shape.getBorder_width() > 0) {
						color = shape.getBorder_color();
						baseColor = new BaseColor(color[0], color[1], color[2]);
						pcb.setColorStroke(baseColor);

						pcb.setLineWidth(shape.getBorder_width());
						if (shape.getBorder_legnth() > 0)
							pcb.setLineDash(shape.getBorder_legnth(), 0);
					}

					pcb.ellipse(shape.getX(), shape.getY() + shape.getHeight(), shape.getX() + shape.getWidth(),
							shape.getY());

					if (shape.getColor() != null) {
						if (shape.getBorder_width() > 0)
							pcb.fillStroke();
						else
							pcb.eoFill();
					}

					break;
				}

				pcb.stroke();
				pcb.restoreState();
			}
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF005", "PDF 도형 추가 중 오류 발생 [shapes:" + shapes + "]", e);
		}
	}

	/**
	 * 텍스트 정보를 PDF 문서를 추가한다.
	 * 
	 * @param texts 텍스트 정보들
	 * @param writer PdfWriter
	 * 
	 * @throws PdfException
	 */
	public static void addPdfTexts(List<Text> texts, PdfWriter writer) throws PdfException {
		if (texts == null || writer == null)
			return;

		try {
			PdfContentByte pcb = writer.getDirectContent();

			for (Text text : texts) {
				BaseFont baseFont = BaseFont.createFont(text.getFont_name(), text.getFont_encoding(),
						text.isFont_embedded());

				int[] color = text.getColor();
				BaseColor baseColor = new BaseColor(color[0], color[1], color[2]);

				Font font = new Font(baseFont, text.getFont_size(), text.getFont_style(), baseColor);
				Paragraph paragraph = new Paragraph(text.getValue(), font);
				paragraph.setAlignment(text.gethAlign());
				paragraph.setLeading(text.getFont_size());

				Rectangle rect = new Rectangle(text.getX(), text.getY(), text.getX() + text.getWidth(), text.getY()
						+ text.getHeight());

				if (text.getBorder_width() > 0) {
					rect.setBorder(Rectangle.BOX);

					color = text.getBorder_color();
					baseColor = new BaseColor(color[0], color[1], color[2]);
					rect.setBorderColor(baseColor);

					rect.setBorderWidth(text.getBorder_width());

					pcb.rectangle(rect);
				}

				boolean simulate = (text.getvAlign() == Text.ALIGN_TOP) ? false : true;
				float y = drawColumnText(paragraph, rect, pcb, simulate);

				if (text.getvAlign() == Text.ALIGN_BOTTOM) {
					rect.setTop(text.getY() + text.getHeight() - (y - text.getY()));
					drawColumnText(paragraph, rect, pcb, false);
				} else if (text.getvAlign() == Text.ALIGN_MIDDLE) {
					rect.setTop(text.getY() + text.getHeight() - ((y - text.getY()) / 2));
					drawColumnText(paragraph, rect, pcb, false);
				}
			}
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF006", "PDF 텍스트 추가 중 오류 발생 [texts:" + texts + "]", e);
		}
	}

	/**
	 * Y 좌표를 구한다.
	 *
	 * @param paragraph 문장
	 * @param rect 좌표 정보
	 * @param pcb PdfContentByte
	 * @param simulate 그리기여부
	 * 
	 * @return Y 좌표
	 * 
	 * @throws PdfException
	 */
	protected static float drawColumnText(Paragraph paragraph, Rectangle rect, PdfContentByte pcb, boolean simulate)
			throws PdfException {
		if (pcb == null || rect == null || paragraph == null)
			return 0;

		try {
			ColumnText ct = new ColumnText(pcb);
			ct.setSimpleColumn(paragraph, rect.getLeft(), rect.getBottom(), rect.getRight(), rect.getTop(), paragraph
					.getFont().getSize(), paragraph.getAlignment());
			ct.go(simulate);

			return ct.getYLine();
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF007", "PDF 텍스트 그리기 중 오류 발생", e);
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 1단계
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 이미지 정보를 가지고 페이지 단위로 PDF 파일을 생성한다.
	 * 
	 * @param pdf 입력 PDF 정보(이미지 정보)
	 * 
	 * @throws PdfException
	 */
	public static void createPdfImage(Pdf pdf) throws PdfException {
		if (pdf == null)
			return;

		try {
			writePdfImage(pdf, new FileOutputStream(pdf.getFilePath()));
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF101", "PDF 파일 생성 중 오류 발생 [pdf:" + pdf + "]", e);
		}
	}

	/**
	 * 이미지 정보를 가지고 페이지 단위로 출력 스트림에 쓴다.
	 * 
	 * @param pdf 입력 PDF 정보(이미지 정보)
	 * @param out 출력 스트림
	 * 
	 * @return OutputStream
	 * 
	 * @throws PdfException
	 */
	public static OutputStream writePdfImage(Pdf pdf, OutputStream out) throws PdfException {
		if (pdf == null || out == null)
			return null;

		try {
			Document document = new Document(new Rectangle(Page.getWidth(), Page.getHeight()));
			PdfWriter writer = PdfWriter.getInstance(document, out);
//			writer.setFullCompression(); // Apple iBooks 뷰어에서 검게 보임

			document.open();

			for (Page page : pdf.getPages()) {
				document.newPage();

				addPdfImages(page.getImages(), document);

				addPdfShapes(page.getShapes(), writer);
			}

			document.close();

			return out;
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF102", "PDF 스트림 생성 중 오류 발생 [pdf:" + pdf + "]", e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	public static void createPdfForm(Pdf pdf) throws PdfException {
		createPdfForm(pdf, false);
	}

	/**
	 * PDF 파일에 페이지 단위로 필드 변수를 매핑하여 PDF양식 파일을 생성한다.
	 * 
	 * @param pdf 입력 PDF 정보(텍스트 정보)
	 * 
	 * @throws PdfException
	 */
	public static void createPdfForm(Pdf pdf, boolean inputFileDelete) throws PdfException {
		if (pdf == null)
			return;

		try {
			writePdfStream(pdf, new FileOutputStream(pdf.getFilePath()));

			if (inputFileDelete)
				new File(pdf.getPdfFilePath()).delete();
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF103", "PDF 파일 생성 중 오류 발생 [pdf:" + pdf + "]", e);
		}
	}

	/**
	 * PDF 파일에 페이지 단위로 필드 변수를 매핑하여 출력 스트림에 쓴다.
	 * 
	 * @param pdf 입력 PDF 정보(텍스트 정보)
	 * @param out 출력 스트림
	 * 
	 * @return OutputStream
	 * 
	 * @throws PdfException
	 */
	public static OutputStream writePdfStream(Pdf pdf, OutputStream out) throws PdfException {
		if (pdf == null || out == null)
			return null;

		try {
			PdfReader reader = new PdfReader(pdf.getPdfFilePath());
			PdfStamper stamper = new PdfStamper(reader, out);
//			stamper.setFullCompression();

			for (int i = 0; i < pdf.getPageSize(); i++) {
				for (Text text : pdf.getPage(i).getTexts()) {
					TextField textField = new TextField(stamper.getWriter(), new Rectangle(text.getX(), text.getY(),
							text.getX() + text.getWidth(), text.getY() + text.getHeight()), text.getName());

					int[] color = text.getColor();
					BaseColor baseColor = new BaseColor(color[0], color[1], color[2]);
					textField.setTextColor(baseColor);

					textField.setAlignment(text.gethAlign());
					textField.setFontSize(text.getFont_size());

					BaseFont bf = BaseFont.createFont(text.getFont_name(), text.getFont_encoding(),
							text.isFont_embedded());
					textField.setFont(bf);

					if (text.getBorder_width() > 0) {
						color = text.getBorder_color();
						baseColor = new BaseColor(color[0], color[1], color[2]);
						textField.setBorderColor(baseColor);

						textField.setBorderWidth(text.getBorder_width());
					}

					PdfFormField formField = textField.getTextField();
					formField.setFieldName(text.getName());
					formField.setFieldFlags(PdfFormField.FF_READ_ONLY);

					stamper.addAnnotation(formField, i + 1);
				}
			}

			stamper.close();
			reader.close();

			return out;
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF104", "PDF 스트림 생성 중 오류 발생 [pdf:" + pdf + "]", e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	public static void createPdf(Pdf pdf) throws PdfException {
		createPdf(pdf, null);
	}

	public static void createPdf(Pdf pdf, String password) throws PdfException {
		Map<String, String> textMap = convertTextMap(pdf);

		createPdf(textMap, pdf.getPdfFilePath(), pdf.getFilePath(), password);
	}

	public static void createPdf(Map<String, String> textMap, String inputPdfFilePath, String outputPdfFilePath)
			throws PdfException {
		createPdf(textMap, inputPdfFilePath, outputPdfFilePath, null);
	}

	/**
	 * PDF양식 파일에 개인화 정보를 매핑하여 PDF 파일을 생성한다.
	 * 
	 * @param textMap 입력 텍스트 정보들
	 * @param inputPdfFilePath 입력 PDF 파일경로
	 * @param outputPdfFilePath 출력 PDF 파일경로
	 * @param password 암호화 패스워드
	 * 
	 * @throws PdfException
	 */
	public static void createPdf(Map<String, String> textMap, String inputPdfFilePath, String outputPdfFilePath,
			String password) throws PdfException {
		if (outputPdfFilePath == null)
			return;

		try {
			writePdfStream(textMap, inputPdfFilePath, password, new FileOutputStream(outputPdfFilePath));
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF105", "PDF 파일 생성 중 오류 발생 [textMap:" + textMap + ", inputPdfFilePath:"
					+ inputPdfFilePath + ", outputPdfFilePath:" + outputPdfFilePath + ", password:" + password + "]", e);
		}
	}

	/**
	 * PDF양식 파일에 개인화 정보를 매핑하여 출력 스트림에 쓴다.
	 * 
	 * @param textMap 입력 텍스트 정보들
	 * @param inputPdfFilePath 입력 PDF 파일경로
	 * @param password 암호화 패스워드
	 * @param out 출력 스트림
	 * 
	 * @return OutputStream
	 * 
	 * @throws PdfException
	 */
	public static OutputStream writePdfStream(Map<String, String> textMap, String inputPdfFilePath, String password,
			OutputStream out) throws PdfException {
		if (inputPdfFilePath == null || out == null)
			return null;

		try {
			PdfReader reader = new PdfReader(inputPdfFilePath);
			PdfStamper stamper = new PdfStamper(reader, out);
//			stamper.setFullCompression();
//			stamper.setFormFlattening(true); // 디지털 서명 지워짐

			encryptPdf(password, stamper.getWriter());

			buildPdfStamper(textMap, stamper);

			stamper.close();
			reader.close();

			return out;
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF106", "PDF 파일 생성 중 오류 발생 [textMap:" + textMap + ", inputPdfFilePath:"
					+ inputPdfFilePath + ", password:" + password + "]", e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	public static void buildPdfStamper(Pdf pdf, PdfStamper stamper) throws PdfException {
		Map<String, String> textMap = convertTextMap(pdf);

		buildPdfStamper(textMap, stamper);
	}

	/**
	 * PDF Stamper에 개인화 정보를 매핑한다.
	 * 
	 * @param textMap 입력 텍스트 정보들
	 * @param stamper PdfStamper
	 * 
	 * @throws PdfException
	 */
	public static void buildPdfStamper(Map<String, String> textMap, PdfStamper stamper) throws PdfException {
		if (textMap == null || stamper == null)
			return;

		try {
			AcroFields acroFields = stamper.getAcroFields();
			for (Map.Entry<String, String> text : textMap.entrySet()) {
				acroFields.setField(text.getKey(), text.getValue());
			}
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF107", "PDF Stamper 갱신 중 오류 발생 [textMap:" + textMap + "]", e);
		}
	}

	/**
	 * 개인화 정보에서 텍스트 정보를 추출한다.
	 * 
	 * @param pdf 입력 PDF 정보(텍스트 정보)
	 * 
	 * @return 텍스트 정보들
	 */
	public static Map<String, String> convertTextMap(Pdf pdf) {
		if (pdf == null)
			return null;

		Map<String, String> textMap = new LinkedHashMap<String, String>();
		for (Page page : pdf.getPages()) {
			for (Text text : page.getTexts()) {
				textMap.put(text.getName(), text.getValue());
			}
		}

		return textMap;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 2단계
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void createPdf2(Pdf pdf) throws PdfException {
		createPdf2(pdf, null);
	}

	/**
	 * 개인화 정보를 가지고 페이지 단위로 PDF 파일을 생성한다.
	 * 
	 * @param pdf 입력 PDF 정보(PDF/이미지/텍스트 정보)
	 * @param password 암호화 패스워드
	 * 
	 * @throws PdfException
	 */
	public static void createPdf2(Pdf pdf, String password) throws PdfException {
		if (pdf == null)
			return;

		try {
			createPdfStream2(pdf, password, new FileOutputStream(pdf.getFilePath()));
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF201", "PDF 파일 생성 중 오류 발생 [pdf:" + pdf + ", password:" + password + "]", e);
		}
	}

	/**
	 * 개인화 정보를 가지고 페이지 단위로 출력 스트림에 쓴다.
	 * 
	 * @param pdf 입력 PDF 정보(PDF/이미지/텍스트 정보)
	 * @param password 암호화 패스워드
	 * @param out 출력 스트림
	 * 
	 * @return OutputStream
	 * 
	 * @throws PdfException
	 */
	public static OutputStream createPdfStream2(Pdf pdf, String password, OutputStream out) throws PdfException {
		if (out == null)
			return null;

		try {
			Document document = new Document(new Rectangle(Page.getWidth(), Page.getHeight()));
			PdfWriter writer = PdfWriter.getInstance(document, out);
//			writer.setFullCompression();

			encryptPdf(password, writer);

			document.open();

			buildPdfDocument2(pdf, document, writer);

			document.close();

			return out;
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF202", "PDF 파일 생성 중 오류 발생 [pdf:" + pdf + ", password:" + password + "]", e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	/**
	 * PDF 문서에 PDF 정보를 입력한다.
	 * 
	 * @param pdf 입력 PDF 정보(PDF/이미지/텍스트 정보)
	 * @param document PdfDocument
	 * @param writer PdfWriter
	 * 
	 * @throws PdfException
	 */
	public static void buildPdfDocument2(Pdf pdf, Document document, PdfWriter writer) throws PdfException {
		if (pdf == null || document == null || writer == null)
			return;

		try {
			PdfReader reader = null;
			int size = pdf.getPageSize();
			if (pdf.getPdfFilePath() != null) {
				reader = new PdfReader(pdf.getPdfFilePath());
				if (reader.getNumberOfPages() > pdf.getPageSize())
					size = reader.getNumberOfPages();
			}

			PdfContentByte pcb = writer.getDirectContentUnder();
			for (int i = 0; i < size; i++) {
				document.newPage();

				if (reader != null)
					pcb.addTemplate(writer.getImportedPage(reader, i + 1), 0, 0);

				if (i < pdf.getPageSize())
					buildPdfPage2(pdf.getPage(i), document, writer);
			}
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF203", "PDF 문서 갱신 중 오류 발생 [pdf:" + pdf + "]", e);
		}
	}

	public static void buildPdfPage2(Page page, Document document, PdfWriter writer) throws PdfException {
		if (page == null || writer == null)
			return;

		try {
			addPdfImages(page.getImages(), document);

			addPdfShapes(page.getShapes(), writer);

			addPdfTexts(page.getTexts(), writer);
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF204", "PDF 페이지 갱신 중 오류 발생 [page:" + page + "]", e);
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 3단계
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void convertHtmlToPdf(Pdf pdf) throws PdfException {
		convertHtmlToPdf(pdf, null);
	}

	/**
	 * HTML/CSS/폰트 파일을 PDF 파일로 변환한다.
	 * 
	 * @param pdf 입력 PDF 정보(HTML/CSS/Font 정보)
	 * @param password 암호화 패스워드
	 * 
	 * @throws PdfException
	 */
	public static void convertHtmlToPdf(Pdf pdf, String password) throws PdfException {
		if (pdf == null)
			return;

		try {
			writePdfStream3(pdf, password, new FileOutputStream(pdf.getFilePath()));
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF301", "PDF 파일 생성 중 오류 발생 [pdf:" + pdf + ", password:" + password + "]", e);
		}
	}

	/**
	 * HTML/CSS/폰트 파일을 파싱하여 출력 스트림에 쓴다.
	 * 
	 * @param pdf 입력 PDF 정보(HTML/CSS/Font 정보)
	 * @param password 암호화 패스워드
	 * @param out 출력 스트림
	 * 
	 * @return OutputStream
	 * 
	 * @throws PdfException
	 */
	public static OutputStream writePdfStream3(Pdf pdf, String password, OutputStream out) throws PdfException {
		if (out == null)
			return null;

		try {
			Document document = new Document(new Rectangle(Page.getWidth(), Page.getHeight()));
			PdfWriter writer = PdfWriter.getInstance(document, out);
//			writer.setFullCompression();

			encryptPdf(password, writer);

			document.open();

			buildPdfDocument3(pdf, document, writer);

			document.close();

			return out;
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF302", "PDF 파일 생성 중 오류 발생 [pdf:" + pdf + ", password:" + password + "]", e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	/**
	 * PDF 문서에 PDF 정보를 입력한다.
	 * 
	 * @param pdf 입력 PDF 정보(HTML/CSS/Font 정보)
	 * @param document PdfDocument
	 * @param writer PdfWriter
	 * 
	 * @throws PdfException
	 */
	public static void buildPdfDocument3(Pdf pdf, Document document, PdfWriter writer) throws PdfException {
		if (pdf == null || document == null || writer == null)
			return;

		try {
			CSSResolver cssResolver = new StyleAttrCSSResolver();
			for (String cssFile : pdf.getCssFilePaths()) {
				if (cssFile.equalsIgnoreCase(Pdf.DEFAULT_CSS))
					cssResolver.addCss(XMLWorkerHelper.getInstance().getDefaultCSS());
				else
					cssResolver.addCss(XMLWorkerHelper.getCSS(new FileInputStream(cssFile)));
			}

			for (Map.Entry<String, String> font : pdf.getFontMap().entrySet()) {
				fontProvider.register(font.getValue(), font.getKey());
			}
			CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
			HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
			htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

			PdfWriterPipeline pdfWriter = new PdfWriterPipeline(document, writer);
			HtmlPipeline html = new HtmlPipeline(htmlContext, pdfWriter);
			CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

			XMLWorker worker = new XMLWorker(css, true);
			XMLParser p = new XMLParser(worker);

			if (pdf.getHtmlFilePaths().length > 0) {
				for (int i = 0; i < pdf.getHtmlFilePaths().length; i++) {
					document.newPage();

					// 페이지
					if (pdf.getPageSize() > i)
						buildPdfPage2(pdf.getPage(i), document, writer);

					// HTML
					String htmlFile = pdf.getHtmlFilePaths()[i];
					p.parse(new FileInputStream(htmlFile), Charset.forName("UTF-8"));
				}
			} else if (pdf.getHtmlInputStream() != null) {
				for (Page page : pdf.getPages()) {
					document.newPage();

					// 페이지
					buildPdfPage2(page, document, writer);
				}

				// HTML
				p.parse(pdf.getHtmlInputStream(), Charset.forName("UTF-8"));
			}
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF303", "PDF 문서 갱신 중 오류 발생 [pdf:" + pdf + "]", e);
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 4단계
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void convertXmlToPdf(String[] inputXmlFilePaths, List<Map<String, Object>> parametersList,
			String outputPdfFilePath) throws PdfException {
		convertXmlToPdf(inputXmlFilePaths, parametersList, outputPdfFilePath, null);
	}

	public static void convertXmlToPdf(String[] inputXmlFilePaths, List<Map<String, Object>> parametersList,
			String outputPdfFilePath, String password) throws PdfException {
		try {
			List<InputStream> inputStreams = new ArrayList<InputStream>();
			for (String inputXmlFilePath : inputXmlFilePaths)
				inputStreams.add(new FileInputStream(inputXmlFilePath));

			convertXmlToPdfStream(inputStreams, parametersList, outputPdfFilePath, password);
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF401", "PDF 파일 변환 중 오류 발생 [inputXmlFilePaths:"
					+ Arrays.toString(inputXmlFilePaths) + ", outputPdfFilePath:" + outputPdfFilePath
					+ ", parametersList:" + parametersList + ", password:" + password + "]", e);
		}
	}

	public static void convertXmlToPdf(String inputXmlFilePath, Map<String, Object> parameters, String outputPdfFilePath)
			throws PdfException {
		convertXmlToPdf(inputXmlFilePath, parameters, outputPdfFilePath, null);
	}

	/**
	 * XML 파일에 파라미터들을 매핑하여 PDF 파일로 변환한다.
	 * 
	 * @param inputXmlFilePath XML 파일
	 * @param parameters 입력 파라미터들
	 * @param outputPdfFilePath 출력 PDF 파일경로
	 * @param password 암호화 패스워드
	 * 
	 * @throws PdfException
	 */
	public static void convertXmlToPdf(String inputXmlFilePath, Map<String, Object> parameters,
			String outputPdfFilePath, String password) throws PdfException {
		try {
			List<InputStream> inputStreams = new ArrayList<InputStream>();
			inputStreams.add(new FileInputStream(inputXmlFilePath));

			List<Map<String, Object>> parametersList = new ArrayList<Map<String, Object>>();
			parametersList.add(parameters);

			convertXmlToPdfStream(inputStreams, parametersList, outputPdfFilePath, password);
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF402", "PDF 파일 변환 중 오류 발생 [inputXmlFilePath:" + inputXmlFilePath
					+ ", outputPdfFilePath:" + outputPdfFilePath + ", parameters:" + parameters + ", password:"
					+ password + "]", e);
		}
	}

	public static void convertXmlToPdfStream(List<InputStream> inputStreams, List<Map<String, Object>> parametersList,
			String outputPdfFilePath) throws PdfException {
		convertXmlToPdfStream(inputStreams, parametersList, outputPdfFilePath, null);
	}

	public static void convertXmlToPdfStream(List<InputStream> inputStreams, List<Map<String, Object>> parametersList,
			String outputPdfFilePath, String password) throws PdfException {
		try {
			parametersList = convertJasperParameters(parametersList);

			buildPdfDocument4(inputStreams, parametersList, outputPdfFilePath, password);
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF403", "PDF 파일 변환 중 오류 발생 [inputStreams:" + inputStreams + ", outputPdfFilePath:"
					+ outputPdfFilePath + ", parametersList:" + parametersList + ", password:" + password + "]", e);
		}
	}

	public static void convertXmlToPdfStream(InputStream inputStream, Map<String, Object> parameters,
			String outputPdfFilePath) throws PdfException {
		convertXmlToPdfStream(inputStream, parameters, outputPdfFilePath, null);
	}

	/**
	 * XML 스트림에 파라미터들을 매핑하여 PDF 파일로 변환한다.
	 * 
	 * @param inputStream 입력 XML 스트림
	 * @param parameters 입력 파라미터들
	 * @param outputPdfFilePath 출력 PDF 파일경로
	 * @param password 암호화 패스워드
	 * 
	 * @throws PdfException
	 */
	public static void convertXmlToPdfStream(InputStream inputStream, Map<String, Object> parameters,
			String outputPdfFilePath, String password) throws PdfException {
		try {
			parameters = convertJasperParameters(parameters);

			List<InputStream> inputStreams = new ArrayList<InputStream>();
			inputStreams.add(inputStream);

			List<Map<String, Object>> parametersList = new ArrayList<Map<String, Object>>();
			parametersList.add(parameters);

			buildPdfDocument4(inputStreams, parametersList, outputPdfFilePath, password);
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF404", "PDF 파일 변환 중 오류 발생 [inputStream:" + inputStream + ", outputPdfFilePath:"
					+ outputPdfFilePath + ", parameters:" + parameters + ", password:" + password + "]", e);
		}
	}

	public static void convertXmlToPdfList(List<JasperReport> jasperReports, List<Map<String, Object>> parametersList,
			String outputPdfFilePath, String password) throws PdfException {
		try {
			parametersList = convertJasperParameters(parametersList);

			List<JasperPrint> jasperPrints = getJasperPrint(jasperReports, parametersList);

			buildPdfDocument4(jasperPrints, outputPdfFilePath, password);
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF405", "PDF 파일 변환 중 오류 발생 [jasperReports:" + jasperReports
					+ ", outputPdfFilePath:" + outputPdfFilePath + ", parametersList:" + parametersList + ", password:"
					+ password + "]", e);
		}
	}

	/**
	 * JasperReport(컴파일된 XML 파일) 객체에 파라미터들을 매핑하여 PDF 파일로 변환한다.
	 * 
	 * @param jasperReport 입력 JasperReport 객체
	 * @param parameters 입력 파라미터들
	 * @param outputPdfFilePath 출력 PDF 파일경로
	 * @param password 암호화 패스워드
	 * 
	 * @throws PdfException
	 */
	public static void convertXmlToPdfList(JasperReport jasperReport, Map<String, Object> parameters,
			String outputPdfFilePath, String password) throws PdfException {
		try {
			parameters = convertJasperParameters(parameters);

			List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
			jasperPrints.add(getJasperPrint(jasperReport, parameters));

			buildPdfDocument4(jasperPrints, outputPdfFilePath, password);
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF406", "PDF 파일 변환 중 오류 발생 [jasperReport:" + jasperReport + ", parameters:"
					+ parameters + ", outputPdfFilePath:" + outputPdfFilePath + ", password:" + password + "]", e);
		}
	}

	/**
	 * XML 스트림 리스트에 파라미터들 리스트를 매핑하여 PDF 파일로 변환한다.
	 * 
	 * @param inputStreams 입력 XML 스트림 리스트
	 * @param jasperParametersList 입력 Jasper 파라미터들 리스트
	 * @param outputPdfFilePath 출력 PDF 파일경로
	 * @param password 암호화 패스워드
	 * 
	 * @throws PdfException
	 */
	public static void buildPdfDocument4(List<InputStream> inputStreams,
			List<Map<String, Object>> jasperParametersList, String outputPdfFilePath, String password)
			throws PdfException {
		if (inputStreams == null || jasperParametersList == null || outputPdfFilePath == null)
			return;

		try {
			List<JasperReport> jasperReports = new ArrayList<JasperReport>();
			for (InputStream inputStream : inputStreams)
				jasperReports.add(JasperCompileManager.compileReport(inputStream));

			List<JasperPrint> jasperPrints = getJasperPrint(jasperReports, jasperParametersList);

			buildPdfDocument4(jasperPrints, outputPdfFilePath, password);
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF407", "PDF 문서 생성 중 오류 발생 [inputStreams:" + inputStreams + ", outputPdfFilePath:"
					+ outputPdfFilePath + ", jasperParametersList:" + jasperParametersList + ", password:" + password
					+ "]", e);
		}
	}

	/**
	 * JasperPrint 객체들로 PDF 파일을 생성한다.
	 * 
	 * @param jasperPrints 입력 JasperPrint 객체들
	 * @param outputPdfFilePath 출력 PDF 파일경로
	 * @param password 암호화 패스워드
	 * 
	 * @throws PdfException
	 */
	public static void buildPdfDocument4(List<JasperPrint> jasperPrints, String outputPdfFilePath, String password)
			throws PdfException {
		if (jasperPrints == null || outputPdfFilePath == null)
			return;

		try {
			JRPdfExporter2 exporter = new JRPdfExporter2();

			exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrints));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputPdfFilePath));

			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			configuration.isCreatingBatchModeBookmarks();
			if (password != null) {
				configuration.setEncrypted(true);
				configuration.set128BitKey(true);
				configuration.setUserPassword(password);
				configuration.setOwnerPassword(password);
				configuration.setPermissions(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING);
			}
			exporter.setConfiguration(configuration);

			exporter.exportReport();
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF408", "PDF 문서 생성 중 오류 발생 [jasperPrints:" + jasperPrints + ", outputPdfFilePath:"
					+ outputPdfFilePath + ", password:" + password + "]", e);
		}
	}

	/**
	 * 파라미터들에 파라미터 컬럼명들을 추가한다.
	 * 
	 * @param parameterKey 파라미터키
	 * @param columnNames 컬럼명들
	 * @param parameters 파라미터들
	 * 
	 * @return Map
	 * 
	 * @throws PdfException
	 */
	public static Map<String, Object> addParameterColumnNames(String parameterKey, String[] columnNames,
			Map<String, Object> parameters) throws PdfException {
		if (parameterKey == null || columnNames == null || parameters == null)
			return parameters;

		parameters.put(parameterKey + COLUMN_NAMES, columnNames);

		return parameters;
	}

	public static List<JasperPrint> getJasperPrint(String[] inputXmlFilePaths,
			List<Map<String, Object>> jasperParametersList) throws PdfException {
		if (inputXmlFilePaths == null || jasperParametersList == null)
			return null;

		try {
			List<JasperReport> jasperReports = new ArrayList<JasperReport>();
			for (String inputXmlFilePath : inputXmlFilePaths)
				jasperReports.add(JasperCompileManager.compileReport(inputXmlFilePath));

			return getJasperPrint(jasperReports, jasperParametersList);
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF409", "PDF 문서 생성 중 오류 발생 [inputXmlFilePaths:"
					+ Arrays.toString(inputXmlFilePaths) + ", jasperParametersList:" + jasperParametersList + "]", e);
		}
	}

	/**
	 * XML 파일들에 파라미터들을 매핑하여 JasperPrint 객체를 생성한다.
	 * 
	 * @param inputXmlFilePath XML 파일
	 * @param jasperParameters 입력 Jasper 파라미터들
	 * 
	 * @return JasperPrint 객체
	 * 
	 * @throws PdfException
	 */
	public static JasperPrint getJasperPrint(String inputXmlFilePath, Map<String, Object> jasperParameters)
			throws PdfException {
		if (inputXmlFilePath == null || jasperParameters == null)
			return null;

		try {
			JasperReport jasperReport = JasperCompileManager.compileReport(inputXmlFilePath);

			return getJasperPrint(jasperReport, jasperParameters);
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF410", "PDF 문서 생성 중 오류 발생 [inputXmlFilePath:" + inputXmlFilePath
					+ ", jasperParameters:" + jasperParameters + "]", e);
		}
	}

	public static List<JasperPrint> getJasperPrint(List<JasperReport> jasperReports,
			List<Map<String, Object>> jasperParametersList) throws PdfException {
		if (jasperReports == null || jasperParametersList == null)
			return null;

		try {
			List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();

			for (int i = 0; i < jasperParametersList.size(); i++)
				jasperPrints.add(getJasperPrint(jasperReports.get(i), jasperParametersList.get(i)));

			return jasperPrints;
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF410", "PDF 문서 생성 중 오류 발생 [jasperReports:" + jasperReports
					+ ", jasperParametersList:" + jasperParametersList + "]", e);
		}
	}

	/**
	 * JasperReport(컴파일된 XML 파일) 객체에 파라미터들을 매핑하여 JasperPrint 객체를 생성한다.
	 * 
	 * @param jasperReport 입력 JasperReport 객체
	 * @param jasperParameters 입력 Jasper 파라미터들
	 * 
	 * @return JasperPrint 객체
	 * 
	 * @throws PdfException
	 */
	public static JasperPrint getJasperPrint(JasperReport jasperReport, Map<String, Object> jasperParameters)
			throws PdfException {
		if (jasperReport == null || jasperParameters == null)
			return null;

		try {
			JasperPrint jasperPrint = null;
			if (jasperParameters.containsKey(MAIN_DATA_SOURCE))
				jasperPrint = JasperFillManager.fillReport(jasperReport, jasperParameters);
			else
				jasperPrint = JasperFillManager.fillReport(jasperReport, jasperParameters, JR_EMPTY_DATASOURCE);

			return jasperPrint;
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF412", "PDF 문서 생성 중 오류 발생 [jasperReport:" + jasperReport + ", jasperParameters:"
					+ jasperParameters + "]", e);
		}
	}

	public static List<Map<String, Object>> convertJasperParameters(List<Map<String, Object>> parametersList)
			throws PdfException {
		if (parametersList == null)
			return null;

		try {
			List<Map<String, Object>> jasperParametersList = new ArrayList<Map<String, Object>>();

			for (Map<String, Object> parameters : parametersList) {
				Map<String, Object> jasperParameters = convertJasperParameters(parameters);

				jasperParametersList.add(jasperParameters);
			}

			return jasperParametersList;
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF413", "파라미터들 리스트를 추가 중 오류 발생 [parametersList:" + parametersList + "]", e);
		}
	}

	/**
	 * 파라미터들을 Jasper 파라미터 형태로 변환한다.
	 * 
	 * <pre>
	 * @param parameters 파라미터들
	 * - Map<String, Object>, Map<String, List>, Map<String, ResultSet> 형태가 가능
	 * - Map<String, List> 를 추가할 경우 반드시 컬럼명 배열(String[])도 추가 (4단계 테스트 코드 참고)
	 * </pre>
	 * 
	 * @return Map
	 * 
	 * @throws PdfException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, Object> convertJasperParameters(Map<String, Object> parameters) throws PdfException {
		if (parameters == null)
			return null;

		try {
			Map<String, Object> jasperParameters = new HashMap<String, Object>();

			Object dataSource = null;
			for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
				if (parameter.getValue() instanceof List) {
					String[] columnNames = (String[]) parameters.get(parameter.getKey() + COLUMN_NAMES);
					dataSource = new ListOfArrayDataSource((List) parameter.getValue(), columnNames);
				} else if (parameter.getValue() instanceof ResultSet) {
					dataSource = new JRResultSetDataSource((ResultSet) parameter.getValue());
				} else {
					dataSource = parameter.getValue();
				}

				jasperParameters.put(parameter.getKey(), dataSource);
			}

			return jasperParameters;
		} catch (PdfException e) {
			throw e;
		} catch (Exception e) {
			throw new PdfException("PDF414", "파라미터들을 추가 중 오류 발생 [parameters:" + parameters + "]", e);
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 테스트
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static boolean test() throws Exception {
		// 이미지 가로 정렬
//		Document document = new Document(new Rectangle(Page.getWidth(), Page.getHeight()));
//		PdfWriter.getInstance(document, new FileOutputStream("results/pdf/test.pdf"));
//
//		document.open();
//
//		com.itextpdf.text.Image itextImage = com.itextpdf.text.Image.getInstance("resources/img/logo.jpg");
//		itextImage.setAlignment(com.itextpdf.text.Image.ALIGN_MIDDLE);
//		document.add(itextImage);
//
//		itextImage = com.itextpdf.text.Image.getInstance("resources/img/logo.jpg");
//		itextImage.setAbsolutePosition(100, 842 - (100 + 100));
//		document.add(itextImage);
//
//		document.close();

		// 디지털 서명
//		Map<String, String> textMap = new HashMap<String, String>();
//		textMap.put("V2OWNM", "홍길동");
//
//		createPdf(textMap, "resources/pdf/sample_서명샘플.pdf", "results/pdf/sample_서명샘플_1.pdf");
//
//		List<Page> pages = new ArrayList<Page>();
//
//		Pdf pdf = new Pdf("results/pdf/sample_서명샘플_2", pages, "resources/pdf/sample_서명샘플.pdf");
//
//		List<Text> texts = new ArrayList<Text>();
//		Text text = new Text("text1", "홍길동", 100, 100, 150, 20, "resources/fonts/windows/gulim.ttc,0",
//				Text.FONT_ENCODING_IDENTITY_H, 20);
//		texts.add(text);
//
//		Page page = new Page(1, texts);
//		pages.add(page);
//
//		createPdf2(pdf);

		// HTML
//		Pdf newPdf = new Pdf("results/pdf/01_무선_해지양도인", new String[] { "resources/xml/skt/무선/01_무선_해지양도인.html" },
//				new String[] { "resources/xml/skt/무선/static/css/style.css" });
//		convertHtmlToPdf(newPdf);
//
//		newPdf = new Pdf("results/pdf/02_무선", new String[] { "resources/xml/skt/무선/02_무선.html" },
//				new String[] { "resources/xml/skt/무선/static/css/style.css" });
//		convertHtmlToPdf(newPdf);

		// Hi Comms
//		String htmlContent = IOUtils.toString(new FileInputStream("resources/xml/skt/무선/01_무선_해지양도인.html"), "UTF-8");
//
//		HtmlConvertToPdfS htmlConvert = new HtmlConvertToPdfS();
//		htmlConvert.fontUpload("resources/fonts/");
////		htmlConvert.licenseUpload("resources/");
////		htmlConvert.resourcePath("resources/xml/");
//
//		htmlConvert.SetStringToByteArray(htmlContent, "1", "1");
//
//		String jobId = htmlConvert.Submit(true);
//
//		if (jobId != null) {
//			System.out.println(" [ SingleCode ] JobId : " + jobId);
//			System.out.println(" [ SingleCode ] Error : " + htmlConvert.getErrorCode(jobId));
//			System.out.println(" [ SingleCode ] Msg : " + htmlConvert.getMsg(jobId));
//			ByteArrayOutputStream tmp = htmlConvert.getByteArray(jobId);
//
//			tmp.writeTo(new FileOutputStream("results/pdf/01_무선_해지양도인.pdf"));
//		}

		return true; // 이 메소드만 실행시 true
	}

	public static void main(String[] args) throws Exception {
		if (test())
			return;

		// <테스트시 고려사항>
		// 1. 다양한 폰트, 스타일, PDF뷰어 적용
		// 2. 다양한 모바일 단말기(안드로이드, IOS)의 기본 PDF뷰어로 확인
		Page page = null;
		Image image = null;
		Shape shape = null;
		Text text = null;

		List<Page> pages = new ArrayList<Page>();
		List<Image> images = new ArrayList<Image>();
		List<Shape> shapes = new ArrayList<Shape>();
		List<Text> texts = new ArrayList<Text>();

		Pdf pdf = new Pdf("results/pdf/temp_", pages, "results/pdf/temp_.pdf");
		Pdf newPdf = new Pdf("newPdf");

		// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 간단한 예제
		// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

		// 페이지 크기 (A4는 생략 가능)
		// Page.setSize(new int[] { 595, 842 });
		Pdf postopia = new Pdf("results/pdf/postopia");

		// 1 페이지
		Page page1 = new Page(1);
		postopia.addPage(page1);

		// 이미지
		page1.addImage(new Image("image1", "resources/img/postopia.jpg"));

		// 도형
		page1.addShape(new Shape("shape1", 200, 100, 200, 100, Base.COLOR_RED, 0, 3, 3, TYPE.LINE));

		// 텍스트 (리눅스에서 폰트는 따로 복사해서 경로 지정)
		// page1.addText(new Text("text1", "홍길동", 165, 316, 400, 20, Text.ALIGN_LEFT, Text.ALIGN_TOP, 0, Base.COLOR_BLACK,
		// "C:/windows/fonts/gulim.ttc,0", Text.FONT_ENCODING_IDENTITY_H, Text.FONT_NOT_EMBEDDED, 15,
		// Text.FONT_STYLE_NORMAL, Base.COLOR_RED, 0.5f)); // 좌표 지정용
		page1.addText(new Text("text1", "홍길동", 165, 316, 400, 20, "C:/windows/fonts/gulim.ttc,0",
				Text.FONT_ENCODING_IDENTITY_H, 15));

		// PDF 생성
		createPdf2(postopia, "1");

		// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 1단계 테스트
		// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

		// 1. PDF 파일 생성
		// 이미지
		images = new ArrayList<Image>();
		image = new Image("image1", "resources/img/page1.jpg", 595, 842);
		images.add(image);

		image = new Image("image11", "resources/img/page1.jpg", 100, 100, 100, 100, 90, 20);
		images.add(image);

		// 도형
		shapes = new ArrayList<Shape>();
		shape = new Shape("shape1", 100, 300, 200, 100, Base.COLOR_GREEN, 0, Base.COLOR_RED, 3, 3, TYPE.ELLIPSE);
		shapes.add(shape);

		// 텍스트
		texts = new ArrayList<Text>();
		text = new Text("text1", 370, 613, 150, 20, Text.ALIGN_LEFT, 20);
		texts.add(text);

		// 1 페이지
		page = new Page(1, images, shapes, texts);
		pages.add(page);

		// 이미지
		images = new ArrayList<Image>();
		image = new Image("image2", "resources/img/page2.jpg", 595, 842);
		images.add(image);

		// 텍스트
		texts = new ArrayList<Text>();
		text = new Text("text2", 325, 470, 150, 20, Text.ALIGN_CENTER, Base.COLOR_BLUE, 20, Base.COLOR_BLACK, 0.5f);
		texts.add(text);

		// 2 페이지
		page = new Page(2, images, texts);
		pages.add(page);

		createPdfImage(pdf);

		pdf.setFilePath("results/pdf/form1.pdf");
		createPdfForm(pdf, true);

		// 2. 개인화 정보 매핑
		Map<String, String> textMap = new HashMap<String, String>();

		textMap.put("text1", "홍길동");
		textMap.put("text2", "秦王殘劍");

		System.out.println("1단계: " + pdf);
		System.out.println("1단계: " + textMap);
		createPdf(textMap, "results/pdf/form1.pdf", "results/pdf/result1.pdf");
		createPdf(textMap, "results/pdf/form1.pdf", "results/pdf/result1_1.pdf", "1");
		encryptPdf("results/pdf/result1.pdf", "results/pdf/result1_2.pdf", "1");

		// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 2단계 테스트
		// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

		pdf.setPdfFilePath("resources/pdf/다이렉트e패키지북1.pdf");

		// 1 페이지
		page = pdf.getPageByNo(1);

		// 이미지
		page.addImage(new Image("image1", "resources/img/page1.jpg", 100, 100, 100, 100, 90, 20));
		page.addImage(new Image("image2", "resources/img/logo.jpg", 102, 100, Image.ALIGN_RIGHT, 0, 200));

		// 도형
		shape = new Shape("shape1", 100, 300, 200, 100, Base.COLOR_RED, 0, 3, 3, TYPE.LINE);
		page.addShape(shape);
		shape = new Shape("shape2", 100, 300, 200, 100, Base.COLOR_RED, 3, 3, TYPE.RECTANGLE);
		page.addShape(shape);
		shape = new Shape("shape3", 100, 300, 200, 100, Base.COLOR_RED, 3, 3, TYPE.ELLIPSE);
		page.addShape(shape);
		shape = new Shape("shape4", 100, 450, 200, 100, Base.COLOR_BLUE, 0, TYPE.RECTANGLE);
		page.addShape(shape);
		shape = new Shape("shape5", 100, 450, 200, 100, Base.COLOR_GREEN, 0, TYPE.ELLIPSE);
		page.addShape(shape);
		shape = new Shape("shape6", 100, 600, 200, 100, Base.COLOR_BLUE, 0, Base.COLOR_RED, 3, 3, TYPE.RECTANGLE);
		page.addShape(shape);
		shape = new Shape("shape7", 100, 600, 200, 100, Base.COLOR_GREEN, 0, Base.COLOR_RED, 3, 3, TYPE.ELLIPSE);
		page.addShape(shape);

		// 텍스트
		text = new Text("text1", "홍길동2", 370, 613, 150, 150, Text.ALIGN_CENTER, Text.ALIGN_TOP, 20);
		page.setText("text1", text);

		text = new Text("text3", "손오공2", 325, 470, 150, 150, "C:/windows/fonts/gulim.ttc,0",
				Text.FONT_ENCODING_IDENTITY_H, 20);
		page.addText(text);

		// 2 페이지
		page = pdf.getPageByNo(2);

		// 텍스트
		text = new Text("text2", "秦王殘劍2", 370, 613, 150, 150, Text.ALIGN_LEFT, Text.ALIGN_TOP, 20);
		page.setText("text2", text);

		text = new Text("text4", "秦王殘劍2", 325, 470, 150, 150, Text.ALIGN_RIGHT, Text.ALIGN_MIDDLE, 0, Base.COLOR_BLUE,
		// Text.FONT_NAME_HYGOTHIC_MEDIUM, Text.FONT_ENCODING_UNIKS_UCS2_H,
				"C:/windows/fonts/gulim.ttc,0", Text.FONT_ENCODING_IDENTITY_H, // 굴림
				// "C:/windows/fonts/malgun.ttf", Text.FONT_ENCODING_IDENTITY_H, // 맑은고딕
				Text.FONT_EMBEDDED, 30, Text.FONT_STYLE_BOLD | Text.FONT_STYLE_ITALIC, Base.COLOR_BLUE, 0.5f);
		page.addText(text);

		pdf.setFilePath("results/pdf/result2.pdf");
		System.out.println("2단계: " + pdf);
		createPdf2(pdf);

		pdf.setFilePath("results/pdf/result2_1");
		createPdf2(pdf, "1");

		encryptPdf("results/pdf/result2.pdf", "results/pdf/result2_2.pdf", "1");

		// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 3단계 테스트
		// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

		newPdf = new Pdf("results/pdf/test01", new String[] { "resources/xml/test01.html" });
		convertHtmlToPdf(newPdf, "1");

		// CSS
		newPdf = new Pdf("results/pdf/style_inline", new String[] { "resources/xml/style_inline.html" });
		convertHtmlToPdf(newPdf);

		newPdf = new Pdf("results/pdf/style_outline", new String[] { "resources/xml/style_outline.html" });
		convertHtmlToPdf(newPdf);

		newPdf = new Pdf("results/pdf/style_link", new String[] { "resources/xml/style_link.html" });
		convertHtmlToPdf(newPdf);

		newPdf = new Pdf("results/pdf/style_no", new String[] { "resources/xml/style_no.html" });
		// newPdf.setCssFilePaths(new String[] { Pdf.DEFAULT_CSS });
		newPdf.setCssFilePaths("resources/xml/style.css");
		convertHtmlToPdf(newPdf);

		newPdf = new Pdf("results/pdf/walden2", new String[] { "resources/xml/walden2.html" },
				new String[] { "resources/xml/walden.css" });
		convertHtmlToPdf(newPdf);

		// 폰트
		Map<String, String> inputFontMap = new HashMap<String, String>();
		inputFontMap.put("MS Mincho", "resources/fonts/cfmingeb.ttf");
		inputFontMap.put("Serif", "resources/fonts/PT_Serif-Web-Regular.ttf");
		newPdf = new Pdf("results/pdf/hero", new String[] { "resources/xml/hero.html" }, inputFontMap);
		convertHtmlToPdf(newPdf);

		// 샘플 01
		newPdf = new Pdf("results/pdf/신청안내페이지", new String[] { "resources/xml/신청안내페이지.html" });
		convertHtmlToPdf(newPdf);

		// 샘플 02
		newPdf = new Pdf("results/pdf/layout_school", new String[] { "resources/xml/layout_school.html" });
		convertHtmlToPdf(newPdf);

		pdf.setFilePath("results/pdf/layout_school");
		pdf.setHtmlFilePaths("resources/xml/layout_school.html");
		System.out.println("3단계: " + pdf);
		convertHtmlToPdf(pdf);

		// HTML String
		String html = IOUtils.toString(new FileInputStream("resources/xml/layout_school.html"), "UTF-8");
		newPdf = new Pdf("results/pdf/layout_school-1", IOUtils.toInputStream(html));
		convertHtmlToPdf(newPdf);

		// 샘플 03
		newPdf = new Pdf("results/pdf/c_mileage", new String[] { "resources/xml/c_mileage.html" });
		convertHtmlToPdf(newPdf); // 스타일 깨짐

		// 샘플 04
		newPdf = new Pdf("results/pdf/1", new String[] { "resources/xml/1.html" });
		convertHtmlToPdf(newPdf);

		// 샘플 05
		newPdf = new Pdf("results/pdf/headers", new String[] { "resources/xml/headers.html" });
		convertHtmlToPdf(newPdf);

		// 샘플 06
		newPdf = new Pdf("results/pdf/various_styles", new String[] { "resources/xml/various_styles.html" });
		convertHtmlToPdf(newPdf);

		// 샘플 07
		Page.setSize(new int[] { 708, 1000 }); // B4
		newPdf = new Pdf("results/pdf/some_tables", new String[] { "resources/xml/some_tables.html" });
		newPdf.setCssFilePaths(new String[] { Pdf.DEFAULT_CSS });
		convertHtmlToPdf(newPdf);

		// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// 4단계 테스트
		// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

		Map<String, Object> parameters = new HashMap<String, Object>();

		// 1. Parameter > String 추가
		parameters.put("Parameter1", "파라미터1");

		// 2. Main DataSource(1개) > ResultSet 추가
		// 주의) ResultSet 은 반복 사용 불가
		// PreparedStatement pstmt = null;
		// ResultSet rs = null;
		// Class.forName("oracle.jdbc.OracleDriver");
		// Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "hr", "hr");
		// try {
		// pstmt = conn.prepareStatement("SELECT username FROM userinfo");
		// rs = pstmt.executeQuery();
		// parameters.put(MAIN_DATA_SOURCE, rs);
		// // } catch (Exception e) {
		// // conn.close();
		// // }

		// // 3. Sub DataSource(n개) > ResultSet 추가
		// // Class.forName("oracle.jdbc.OracleDriver");
		// // conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "hr", "hr");
		// // try {
		// pstmt = conn.prepareStatement("SELECT userid, point FROM userinfo WHERE point = 2000");
		// rs = pstmt.executeQuery();
		// parameters.put("DataSource1", rs);
		// } catch (Exception e) {
		// conn.close();
		// e.printStackTrace();
		// }

		// 2-1. Main DataSource(1개) > List 추가
		List<Object[]> list = new ArrayList<Object[]>();
		for (int i = 1; i <= 20; i++)
			list.add(new Object[] { "회원" + i });
		parameters.put(MAIN_DATA_SOURCE, list);
		addParameterColumnNames(MAIN_DATA_SOURCE, new String[] { "USERNAME" }, parameters);

		// 3-1. Sub DataSource(n개) > List 추가
		list = new ArrayList<Object[]>();
		list.add(new Object[] { "회원3", 1000 });
		list.add(new Object[] { "회원4", 2000 });
		parameters.put("DataSource1", list);
		addParameterColumnNames("DataSource1", new String[] { "USERID", "POINT" }, parameters);

		list = new ArrayList<Object[]>();
		list.add(new Object[] { "Member5", 3000 });
		list.add(new Object[] { "회원6", 4000 });
		parameters.put("DataSource2", list);
		addParameterColumnNames("DataSource2", new String[] { "USERID", "POINT" }, parameters);

		// 4. PDF 변환(1개)
		System.out.println("4단계: " + parameters);
		convertXmlToPdf("resources/xml/Blank_A4.jrxml", parameters, "results/pdf/Blank_A4.pdf");

		// 4-1. PDF 변환(n개)
		List<Map<String, Object>> parametersList = new ArrayList<Map<String, Object>>();
		parametersList.add(parameters);
		parametersList.add(parameters);
		convertXmlToPdf(new String[] { "resources/xml/Blank_A4.jrxml", "resources/xml/Blank_A4.jrxml" },
				parametersList, "results/pdf/Blank_A4_1.pdf", "1");

		// 4-2. PDF 변환(String 입력)
		String xml = IOUtils.toString(new FileInputStream("resources/xml/Blank_A4.jrxml"), "UTF-8");
		InputStream xmlStream = IOUtils.toInputStream(xml);
		convertXmlToPdfStream(xmlStream, parameters, "results/pdf/Blank_A4_2.pdf", "1");
	}
}
