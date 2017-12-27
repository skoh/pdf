package org.oh.pdf.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oh.pdf.Base;
import org.oh.pdf.Image;
import org.oh.pdf.Page;
import org.oh.pdf.Pdf;
import org.oh.pdf.PdfUtils;
import org.oh.pdf.Shape;
import org.oh.pdf.Text;
import org.oh.pdf.Shape.TYPE;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * Spring Framework PDF Controller
 * 
 * <pre>
 * <strong>- jdk1.6 이상 호환</strong>
 * 
 * <strong>- 문서</strong>
 *   <a href="http://121.78.144.107:8010/pdf">PDF 저작기</a>
 * 
 * <strong>- 단계별 샘플 코드</strong>
 * {@code
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * // 1단계 테스트
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * List<Page> pages = new ArrayList<Page>();
 * 
 * // PDF
 * Pdf pdf = new Pdf("pdf1", pages, "resources/pdf/form1.pdf");
 * 
 * // 텍스트
 * List<Text> texts = new ArrayList<Text>();
 * Text text = new Text("text1", "홍길동", 370, 613, 150, 20, Text.ALIGN_CENTER, Text.ALIGN_TOP, 20);
 * texts.add(text);
 * 
 * // 페이지
 * Page page = new Page(1, texts);
 * pages.add(page);
 * 
 * PdfUtils.buildPdfStamper(pdf, stamper);
 * 
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * // 2단계 테스트
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * List<Page> pages = new ArrayList<Page>();
 * 
 * // PDF
 * Pdf pdf = new Pdf("pdf2", pages);
 * 
 * // 이미지
 * List<Image> images = new ArrayList<Image>();
 * Image image = new Image("image1", "resources/img/page1.jpg", 595, 842);
 * images.add(image);
 * image = new Image("image2", "resources/img/page1.jpg", 100, 100, 100, 100, 90, 20);
 * images.add(image);
 * 
 * // 도형
 * List<Shape> shapes = new ArrayList<Shape>();
 * Shape shape = new Shape("shape1", 100, 300, 200, 100, Base.COLOR_GREEN, 0, Base.COLOR_RED, 3, 3, TYPE.ELLIPSE);
 * shapes.add(shape);
 * 
 * // 텍스트
 * List<Text> texts = new ArrayList<Text>();
 * Text text = new Text("text1", "홍길동", 370, 613, 150, 20, "resources/fonts/windows/gulim.ttc,0", Text.FONT_ENCODING_IDENTITY_H,
 * 		20);
 * texts.add(text);
 * 
 * // 페이지
 * Page page = new Page(1, images, shapes, texts);
 * pages.add(page);
 * 
 * PdfUtils.buildPdfDocument2(pdf, document, writer);
 * 
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * // 3단계 테스트
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * // HTML
 * String html = "resources/xml/layout_school1.html";
 * 
 * // PDF
 * Pdf pdf = new Pdf("pdf3", html);
 * 
 * PdfUtils.buildPdfDocument3(pdf, document, writer);
 * 
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * // 4단계 테스트
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * 
 * ModelAndView mav = new ModelAndView();
 * 
 * // XMl
 * PdfView4 view = new PdfView4();
 * view.setUrl("file:resources/xml/Blank_A4.jrxml");
 * view.setApplicationContext(context);
 * mav.setView(view);
 * 
 * // 파라미터들
 * Map<String, Object> parameters = new HashMap<String, Object>();
 * 
 * // 1. Parameter > String
 * parameters.put("Parameter1", "파라미터1");
 * 
 * // 2. Main DataSource(1개) > List 추가
 * List<Object[]> list = new ArrayList<Object[]>();
 * for (int i = 1; i <= 20; i++)
 * 	list.add(new Object[] { "회원" + i });
 * parameters.put(PdfUtils.MAIN_DATA_SOURCE, list);
 * PdfUtils.addParameterColumnNames(PdfUtils.MAIN_DATA_SOURCE, new String[] { "USERNAME" }, parameters);
 * 
 * // 3-1. Sub DataSource(n개) > List 추가
 * list = new ArrayList<Object[]>();
 * list.add(new Object[] { "회원3", 1000 });
 * list.add(new Object[] { "회원4", 2000 });
 * parameters.put("DataSource1", list);
 * PdfUtils.addParameterColumnNames("DataSource1", new String[] { "USERID", "POINT" }, parameters);
 * 
 * list = new ArrayList<Object[]>();
 * list.add(new Object[] { "Member5", 3000 });
 * list.add(new Object[] { "회원6", 4000 });
 * parameters.put("DataSource2", list);
 * PdfUtils.addParameterColumnNames("DataSource2", new String[] { "USERID", "POINT" }, parameters);
 * 
 * mav.addAllObjects(parameters);
 * 
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * // 1+2단계 테스트
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * List<Page> pages = new ArrayList<Page>();
 * 
 * // PDF
 * Pdf pdf = new Pdf("pdf12", pages, "resources/pdf/form1.pdf");
 * 
 * // 이미지
 * List<Image> images = new ArrayList<Image>();
 * Image image = new Image("image2", "resources/img/page1.jpg", 100, 100, 100, 100, 90, 20);
 * images.add(image);
 * 
 * // 도형
 * List<Shape> shapes = new ArrayList<Shape>();
 * Shape shape = new Shape("shape1", 100, 300, 200, 100, Base.COLOR_GREEN, 0, Base.COLOR_RED, 3, 3, TYPE.ELLIPSE);
 * shapes.add(shape);
 * 
 * // 텍스트
 * List<Text> texts = new ArrayList<Text>();
 * Text text = new Text("text1", "홍길동", 370, 613, 150, 20, "resources/fonts/windows/gulim.ttc,0", Text.FONT_ENCODING_IDENTITY_H,
 * 		20);
 * texts.add(text);
 * 
 * // 페이지
 * Page page = new Page(1, images, shapes, texts);
 * pages.add(page);
 * 
 * PdfUtils.buildPdfDocument2(pdf, document, writer);
 * 
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * // 2+3단계 테스트
 * // ////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * List<Page> pages = new ArrayList<Page>();
 * 
 * // PDF
 * Pdf pdf = new Pdf("pdf23", pages);
 * 
 * // 이미지
 * List<Image> images = new ArrayList<Image>();
 * Image image = new Image("image1", "resources/img/page1.jpg", 595, 842);
 * images.add(image);
 * image = new Image("image2", "resources/img/page1.jpg", 100, 100, 100, 100, 90, 20);
 * images.add(image);
 * 
 * // 텍스트
 * List<Text> texts = new ArrayList<Text>();
 * Text text = new Text("text1", "홍길동", 370, 613, 150, 20, "resources/fonts/windows/gulim.ttc,0", Text.FONT_ENCODING_IDENTITY_H,
 * 		20);
 * texts.add(text);
 * 
 * // 페이지
 * Page page = new Page(1, images, texts);
 * pages.add(page);
 * 
 * // HTML
 * String html = "resources/xml/layout_school1.html";
 * 
 * pdf.setHtmlFilePaths(html);
 * 
 * PdfUtils.buildPdfDocument3(pdf, document, writer);
 * }
 * </pre>
 * 
 * @author skoh
 */
@Controller
@RequestMapping("/pdf")
public class TestController implements ApplicationContextAware {
	public static final String PASSWORD = "1";

	protected static Log log = LogFactory.getLog(TestController.class);

	protected ApplicationContext context = null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

	@RequestMapping("/test")
	public ModelAndView test(HttpServletRequest request, HttpServletResponse response, ModelAndView mav)
			throws Exception {
		log.info("TestController 테스트");

		mav.setViewName("test");

		return mav;
	}

	@RequestMapping("/error")
	public ModelAndView error(HttpServletRequest request, HttpServletResponse response, ModelAndView mav)
			throws Exception {
		int i = 1 / 0;

		return null;
	}

	/**
	 * 1단계 PDF 데이타
	 * 
	 * @return Pdf
	 */
	@ModelAttribute("pdf1")
	public Pdf getPdf1() {
		List<Page> pages = new ArrayList<Page>();

		// PDF
		Pdf pdf = new Pdf("pdf1", pages, "D:/dev/workspace/workspace_common/pdf/resources/pdf/form1.pdf");

		// 텍스트
		List<Text> texts = new ArrayList<Text>();
		Text text = new Text("text1", "홍길동", 370, 613, 150, 20, Text.ALIGN_CENTER, Text.ALIGN_TOP, 20);
		texts.add(text);

		// 페이지
		Page page = new Page(1, texts);
		pages.add(page);

		return pdf;
	}

	/**
	 * 2단계 PDF 데이타
	 * 
	 * @return Pdf
	 */
	@ModelAttribute("pdf2")
	public Pdf getPdf2() {
		List<Page> pages = new ArrayList<Page>();

		// PDF
		Pdf pdf = new Pdf("pdf2", pages);

		// 이미지
		List<Image> images = new ArrayList<Image>();
		Image image = new Image("image1", "resources/img/page1.jpg", 595, 842);
		images.add(image);
		image = new Image("image2", "resources/img/page1.jpg", 100, 100, 100, 100, 90, 20);
		images.add(image);

		// 도형
		List<Shape> shapes = new ArrayList<Shape>();
		Shape shape = new Shape("shape1", 100, 300, 200, 100, Base.COLOR_GREEN, 0, Base.COLOR_RED, 3, 3, TYPE.ELLIPSE);
		shapes.add(shape);

		// 텍스트
		List<Text> texts = new ArrayList<Text>();
		Text text = new Text("text1", "홍길동", 370, 613, 150, 20, "resources/fonts/windows/gulim.ttc,0",
				Text.FONT_ENCODING_IDENTITY_H, 20);
		texts.add(text);

		// 페이지
		Page page = new Page(1, images, shapes, texts);
		pages.add(page);

		return pdf;
	}

	/**
	 * 1+2단계 PDF 데이타
	 * 
	 * @return Pdf
	 */
	@ModelAttribute("pdf12")
	public Pdf getPdf12() {
		List<Page> pages = new ArrayList<Page>();

		// PDF
		Pdf pdf = new Pdf("pdf12", pages, "resources/pdf/form1.pdf");

		// 이미지
		List<Image> images = new ArrayList<Image>();
		Image image = new Image("image2", "resources/img/page1.jpg", 100, 100, 100, 100, 90, 20);
		images.add(image);

		// 도형
		List<Shape> shapes = new ArrayList<Shape>();
		Shape shape = new Shape("shape1", 100, 300, 200, 100, Base.COLOR_GREEN, 0, Base.COLOR_RED, 3, 3, TYPE.ELLIPSE);
		shapes.add(shape);

		// 텍스트
		List<Text> texts = new ArrayList<Text>();
		Text text = new Text("text1", "홍길동", 370, 613, 150, 20, "resources/fonts/windows/gulim.ttc,0",
				Text.FONT_ENCODING_IDENTITY_H, 20);
		texts.add(text);

		// 페이지
		Page page = new Page(1, images, shapes, texts);
		pages.add(page);

		return pdf;
	}

	/**
	 * 3단계 HTML 파일
	 */
	@ModelAttribute("html")
	public String getHtml() {
		return "resources/xml/layout_school1.html";
	}

	/**
	 * 4단계 XML 파일
	 */
	@ModelAttribute("xml")
	public String getXml() {
		return "resources/xml/Blank_A4.jrxml";
	}

	/**
	 * 4단계 파라미터들
	 */
	@ModelAttribute("parameters")
	public Map<String, Object> getParameters() {
		Map<String, Object> parameters = new HashMap<String, Object>();

		// 1. Parameter > String
		parameters.put("Parameter1", "파라미터1");

		// 2. Main DataSource(1개) > List 추가
		List<Object[]> list = new ArrayList<Object[]>();
		for (int i = 1; i <= 20; i++)
			list.add(new Object[] { "회원" + i });
		parameters.put(PdfUtils.MAIN_DATA_SOURCE, list);
		PdfUtils.addParameterColumnNames(PdfUtils.MAIN_DATA_SOURCE, new String[] { "USERNAME" }, parameters);

		// 3-1. Sub DataSource(n개) > List 추가
		list = new ArrayList<Object[]>();
		list.add(new Object[] { "회원3", 1000 });
		list.add(new Object[] { "회원4", 2000 });
		parameters.put("DataSource1", list);
		PdfUtils.addParameterColumnNames("DataSource1", new String[] { "USERID", "POINT" }, parameters);

		list = new ArrayList<Object[]>();
		list.add(new Object[] { "Member5", 3000 });
		list.add(new Object[] { "회원6", 4000 });
		parameters.put("DataSource2", list);
		PdfUtils.addParameterColumnNames("DataSource2", new String[] { "USERID", "POINT" }, parameters);

		return parameters;
	}

	/**
	 * 1단계 테스트
	 * 
	 * @param pdf
	 * @param mav
	 * 
	 * @return ModelAndView
	 * 
	 * @throws Exception
	 */
	@RequestMapping("/view1")
	public ModelAndView view1(@ModelAttribute("pdf1") Pdf pdf, ModelAndView mav) throws Exception {
		pdf.setFilePath("view1");

		mav.setViewName("pdfView1");
		mav.addObject("pdf", pdf);
		mav.addObject("password", PASSWORD);

		return mav;
	}

	/**
	 * 2단계 테스트
	 * 
	 * @param pdf
	 * @param mav
	 * 
	 * @return ModelAndView
	 * 
	 * @throws Exception
	 */
	@RequestMapping("/view2")
	public ModelAndView view2(@ModelAttribute("pdf2") Pdf pdf, ModelAndView mav) throws Exception {
		pdf.setFilePath("view2");

		mav.setViewName("pdfView2");
		mav.addObject("pdf", pdf);
		mav.addObject("password", PASSWORD);

		return mav;
	}

	/**
	 * 3단계 테스트
	 * 
	 * @param html
	 * @param mav
	 * 
	 * @return ModelAndView
	 * 
	 * @throws Exception
	 */
	@RequestMapping("/view3")
	public ModelAndView view3(@ModelAttribute("html") String html, ModelAndView mav) throws Exception {
		Pdf pdf = new Pdf("view3", new String[] { html });

		mav.setViewName("pdfView3");
		mav.addObject("pdf", pdf);
		mav.addObject("password", PASSWORD);

		return mav;
	}

	/**
	 * 3-1단계 테스트
	 * 
	 * @param req
	 * @param mav
	 * 
	 * @return ModelAndView
	 * 
	 * @throws Exception
	 */
	@RequestMapping("/view31")
	public ModelAndView view31(MultipartHttpServletRequest req, ModelAndView mav) throws Exception {
		MultipartFile file = req.getFile("file");
		Pdf pdf = new Pdf("view31", file.getInputStream());

		mav.setViewName("pdfView3");
		mav.addObject("pdf", pdf);
		mav.addObject("password", PASSWORD);

		return mav;
	}

	/**
	 * 4단계 테스트
	 * 
	 * @param xml
	 * @param parameters
	 * @param request
	 * @param response
	 * @param mav
	 * 
	 * @return ModelAndView
	 * 
	 * @throws Exception
	 */
	@RequestMapping("/view4")
	public ModelAndView view4(@ModelAttribute("xml") String xml,
			@ModelAttribute("parameters") Map<String, Object> parameters, HttpServletRequest request,
			HttpServletResponse response, ModelAndView mav) throws Exception {
		parameters = PdfUtils.convertJasperParameters(parameters);

		if ("true".equals(request.getParameter("down"))) {
			JasperPrint jasperPrint = PdfUtils.getJasperPrint(xml, parameters);

			response.setHeader("Content-Disposition", "attachment; filename=\"" + "Blank_A4.pdf" + "\"");

			JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

			return null;
		} else {
			// 순서 주의
			PdfView4 view = new PdfView4();
			view.setUrl("file:" + xml);
			view.setApplicationContext(context);
			mav.setView(view);

			mav.addAllObjects(parameters);

			return mav;
		}
	}

	/**
	 * 1+2단계 테스트
	 * 
	 * @param pdf
	 * @param mav
	 * 
	 * @return ModelAndView
	 * 
	 * @throws Exception
	 */
	@RequestMapping("/view12")
	public ModelAndView view12(@ModelAttribute("pdf12") Pdf pdf, ModelAndView mav) throws Exception {
		pdf.setFilePath("view12");

		mav.setViewName("pdfView2");
		mav.addObject("pdf", pdf);
		mav.addObject("password", PASSWORD);

		return mav;
	}

	/**
	 * 2+3단계 테스트
	 * 
	 * @param pdf
	 * @param html
	 * @param mav
	 * 
	 * @return ModelAndView
	 * 
	 * @throws Exception
	 */
	@RequestMapping("/view23")
	public ModelAndView view23(@ModelAttribute("pdf2") Pdf pdf, @ModelAttribute("html") String html, ModelAndView mav)
			throws Exception {
		pdf.setFilePath("view23");
		pdf.setHtmlFilePaths(html);

		mav.setViewName("pdfView3");
		mav.addObject("pdf", pdf);
		mav.addObject("password", PASSWORD);

		return mav;
	}

	@RequestMapping("/download/{file:.+}")
	public ModelAndView download1(@PathVariable String file, ModelAndView mav) throws Exception {
		mav.setViewName("downloadView");
		mav.addObject("file", ResourceUtils.getFile("target/" + file));
//		mav.addObject("file", context.getServletContext().getRealPath("target/" + file));

		return mav;
	}
}
