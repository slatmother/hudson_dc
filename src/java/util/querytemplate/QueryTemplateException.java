package util.querytemplate;

/**
 * Исключение, генерируемое объектами {@link QueryTemplate}. Может представлять различные ошибочные ситуации,
 * возникающие при компиляции шаблона, и его последующей интерполяции. Объявлено как исключение времени выполнения, т.к.
 * по сути, возникновение данного исключения означает баг в программе, который должен быть исправлен.
 */
public class QueryTemplateException extends RuntimeException {
    /** 14.03.2012   */
	private static final long serialVersionUID = 5040785248021517177L;

	public QueryTemplateException(String msg, String templateText, Throwable cause) {
        super(msg + " Template text:\n" + templateText, cause);
    }

    public QueryTemplateException(String msg, String templateText) {
        this(msg, templateText, null);
    }
}
