package org.oh.pdf;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PDF
 * 
 * @author skoh
 */
public class Pdf {
	/**
	 * 내장 기본 CSS
	 */
	public static final String DEFAULT_CSS = "DEFAULT_CSS";

	/**
	 * 파일경로(확장자 제외) - 공통
	 */
	protected String filePath = null;

	/**
	 * 페이지 정보들 - 공통
	 */
	protected List<Page> pages = new ArrayList<Page>();

	/**
	 * PDF 파일경로(PDF양식 파일) -> 1단계
	 */
	protected String pdfFilePath = null;

	/**
	 * HTML 파일경로들 -> 3단계
	 */
	protected String[] htmlFilePaths = new String[0];

	/**
	 * HTML 파일스트림(선택사항) -> 3단계
	 */
	protected InputStream htmlInputStream = null;

	/**
	 * CSS 파일경로들(선택사항) -> 3단계
	 */
	protected String[] cssFilePaths = new String[0];

	/**
	 * 폰트 파일경로들(선택사항) -> 3단계
	 * 
	 * <pre>
	 * - 형식 : Map<폰트명, 파일경로>
	 * </pre>
	 */
	protected Map<String, String> fontMap = new HashMap<String, String>();

	public Pdf() {
	}

	public Pdf(String filePath) {
		this();

		setFilePath(filePath);
	}

	public Pdf(String filePath, String pdfFilePath) {
		this();

		setFilePath(filePath);
		setPdfFilePath(pdfFilePath);
	}

	public Pdf(String filePath, List<Page> pages) {
		this();

		setFilePath(filePath);
		setPages(pages);
	}

	public Pdf(String filePath, List<Page> pages, String pdfFilePath) {
		this();

		setFilePath(filePath);
		setPages(pages);
		setPdfFilePath(pdfFilePath);
	}

	public Pdf(String filePath, String[] htmlFilePaths) {
		this();

		setFilePath(filePath);
		setHtmlFilePaths(htmlFilePaths);
	}

	public Pdf(String filePath, List<Page> pages, String[] htmlFilePaths) {
		this();

		setFilePath(filePath);
		setPages(pages);
		setHtmlFilePaths(htmlFilePaths);
	}

	public Pdf(String filePath, InputStream htmlInputStream) {
		this();

		setFilePath(filePath);
		setHtmlInputStream(htmlInputStream);
	}

	public Pdf(String filePath, List<Page> pages, InputStream htmlInputStream) {
		this();

		setFilePath(filePath);
		setPages(pages);
		setHtmlInputStream(htmlInputStream);
	}

	public Pdf(String filePath, String[] htmlFilePaths, String[] cssFilePaths) {
		this();

		setFilePath(filePath);
		setHtmlFilePaths(htmlFilePaths);
		setCssFilePaths(cssFilePaths);
	}

	public Pdf(String filePath, String[] htmlFilePaths, Map<String, String> fontMap) {
		this();

		setFilePath(filePath);
		setHtmlFilePaths(htmlFilePaths);
		setFontMap(fontMap);
	}

	public Pdf(String filePath, String[] htmlFilePaths, String[] cssFilePaths, Map<String, String> fontMap) {
		this();

		setFilePath(filePath);
		setHtmlFilePaths(htmlFilePaths);
		setCssFilePaths(cssFilePaths);
		setFontMap(fontMap);
	}

	public Pdf(String filePath, String[] htmlFilePaths, String[] cssFilePaths, Map<String, String> fontMap,
			List<Page> pages) {
		this();

		setFilePath(filePath);
		setPages(pages);
		setHtmlFilePaths(htmlFilePaths);
		setCssFilePaths(cssFilePaths);
		setFontMap(fontMap);
	}

	public Pdf(String filePath, List<Page> pages, String pdfFilePath, String[] htmlFilePaths,
			InputStream htmlInputStream, String[] cssFilePaths, Map<String, String> fontMap) {
		this();

		setFilePath(filePath);
		setPages(pages);
		setPdfFilePath(pdfFilePath);
		setHtmlFilePaths(htmlFilePaths);
		setHtmlInputStream(htmlInputStream);
		setCssFilePaths(cssFilePaths);
		setFontMap(fontMap);
	}

	public Page getPage(int index) {
		return pages.get(index);
	}

	public void setPage(int index, Page page) {
		pages.set(index, page);
	}

	public Page getPageByNo(int no) {
		for (Page page : getPages()) {
			if (page.getNo() == no)
				return page;
		}
		return null;
	}

	public void setPageByNo(int no, Page page) {
		for (int i = 0; i < pages.size(); i++) {
			Page page1 = pages.get(i);
			if (page1.getNo() == no)
				pages.set(i, page);
		}
	}

	public void addPage(Page page) {
		pages.add(page);
	}

	public int getPageSize() {
		return (pages == null) ? 0 : pages.size();
	}

	public int getHtmlFilePathSize() {
		return (htmlFilePaths == null) ? 0 : htmlFilePaths.length;
	}

	public int getCssFilePathSize() {
		return (cssFilePaths == null) ? 0 : cssFilePaths.length;
	}

	public int getFontMapSize() {
		return (fontMap == null) ? 0 : fontMap.size();
	}

	public String getFilePath() {
		return filePath + ".pdf";
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<Page> getPages() {
		return pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public String getPdfFilePath() {
		return pdfFilePath;
	}

	public void setPdfFilePath(String pdfFilePath) {
		this.pdfFilePath = pdfFilePath;
	}

	public String[] getHtmlFilePaths() {
		return htmlFilePaths;
	}

	public void setHtmlFilePaths(String... htmlFilePaths) {
		this.htmlFilePaths = htmlFilePaths;
	}

	public InputStream getHtmlInputStream() {
		return htmlInputStream;
	}

	public void setHtmlInputStream(InputStream htmlInputStream) {
		this.htmlInputStream = htmlInputStream;
	}

	public String[] getCssFilePaths() {
		return cssFilePaths;
	}

	public void setCssFilePaths(String... cssFilePaths) {
		this.cssFilePaths = cssFilePaths;
	}

	public Map<String, String> getFontMap() {
		return fontMap;
	}

	public void setFontMap(Map<String, String> fontMap) {
		this.fontMap = fontMap;
	}

	@Override
	public String toString() {
		return "Pdf [filePath=" + filePath + ", pages=" + pages + ", pdfFilePath=" + pdfFilePath + ", htmlFilePaths="
				+ Arrays.toString(htmlFilePaths) + ", htmlInputStream=" + htmlInputStream + ", cssFilePaths="
				+ Arrays.toString(cssFilePaths) + ", fontMap=" + fontMap + "]";
	}
}
