package org.oh.pdf;

/**
 * PDF 예외
 * 
 * @author skoh
 */
public class PdfException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * 에러코드
	 */
	protected String errorCode = "";

	public PdfException(String errorMessage) {
		this("", errorMessage);
	}

	public PdfException(String errorCode, String errorMessage) {
		this(errorCode, errorMessage, null);
	}

	public PdfException(Throwable cause) {
		this(cause.getMessage(), cause);
	}

	public PdfException(String errorMessage, Throwable cause) {
		this("", errorMessage, cause);
	}

	public PdfException(String errorCode, String errorMessage, Throwable cause) {
		super(errorMessage, cause);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return "PdfException [errorCode=" + errorCode + "] " + super.toString();
	}
}