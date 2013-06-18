package util.querytemplate;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import freemarker.template.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Позволяет определять параметризованные шаблоны DQL-запросов, в которые впоследствии можно подставлять конкретные
 * значения параметров. В качестве языка для описания шаблонов используется библиотека <a
 * href="http://freemarker.sourceforge.net/docs/index.html">Freemarker</a>.
 * <p/>
 * <h2>Применение шаблонов DQL-запросов</h2>
 * <p/>
 * Прежде всего, рекомендуется импортировать статическую метод-фабрику queryTemplate для создания шаблонов:
 * <p/>
 * <pre>
 *     import static ru.iteco.mtk.commonlib.dql.QueryTemplate.queryTemplate;
 * </pre>
 * <p/>
 * Затем в нашем классе создаем статические члены для каждого шаблона запроса:
 * <p/>
 * <pre>{@literal
 *     class DdtDocument {
 *         private static QueryTemplate documentsByObjectName = queryTemplate(
 *             "SELECT ${r_object_id}                      "
 *             "  FROM ${ddt_document}                     " +
 *             " WHERE ${object_name} LIKE '%${terms}%'    ");
 * <p/>
 *         // ...
 *     }
 * }</pre>
 * <p/>
 * И в нужный момент подставляем в шаблон значения параметров:
 * <p/>
 * <pre>{@literal
 *         // внутри DdtDocument
 * <p/>
 *         public static List<DdtDocument> byObjectName(String terms) {
 *             String query = documentsByObjectName.substitute(
 *                 "r_object_id", IDdtDocument.R_OBJECT_ID,
 *                 "ddt_document", IDdtDocument.NAME,
 *                 "object_name", IDdtDocument.OBJECT_NAME,
 *                 "terms", terms);
 *             // ...
 *         }
 * }</pre>
 * <p/>
 * <h2>Написание шаблонов</h2>
 * <p/>
 * При написании шаблонов доступен полный набор директив и конструкция встроенного языка Freemarker. См. соответствующее
 * руководство: http://freemarker.sourceforge.net/docs/index.html. В данном разделе собраны советы по тому, как
 * применять Freemarker специально для описания шаблонов DQL запросов.
 * <p/>
 * <p>Чтобы вставить в шаблон переменную типа Boolean, используйте следующий синтаксис:
 * <pre>{@literal
 *     queryTemplate("${flag?string}").substitute("flag", true); // вернет строку "true"
 *     queryTemplate("${flag?string('OK', 'NOT OK'}").substitute("flag", true); // вернет строку "OK"
 * }</pre>
 * <p/>
 * <h2>Имплементация</h2>
 * <p/>
 * При доработке класса и добавлении новых возможностей следует прогонять набор тестов
 * ru.iteco.mtk.commonlib.ru.iteco.commonlib.dql.QueryTemplateTest (в дереве исходников для тестов) во избежание регрессий. ОЧЕНЬ
 * желательно для новых возможностей и исправляемых багов добавлять соответствующие тесты в набор.
 *
 * @author Nikolay Artamonov (n.artamonov@i-teco.ru)
 * @since 03.2012
 */
public class QueryTemplate {
    /**
     * Метод-фабрика для создания шаблонов DQL-запросов {@link QueryTemplate}.
     * <p/>
     * <p>Рекомендуется импортировать данный метод, чтобы обращаться к нему напрямую:
     * <pre>
     *     import static ru.iteco.mtk.commonlib.dql.QueryTemplate.queryTemplate;
     *
     *     class MyClass {
     *         private static QueryTemplate myQuery = queryTemplate("SELECT ${r_object_id} FROM ${ddt_document}");
     *         // ...
     *     }
     * </pre>
     *
     * @param templateText текст шаблона
     * @param needStringMove флаг, указывающий необходимость перевода строки.
     *                       Это сделано для удобства отображения строки запроса при падении. Но, т.к.,
     *                       SQL не понимает символ "\n", введен такой флаг.
     * @return скомпилированный шаблон
     * @throws QueryTemplateException
     */
    public static QueryTemplate queryTemplate(String templateText, Boolean needStringMove) {
        // Предваряющий перевод строки пригодится, если запрос окажется некорректным с точки зрения DQL,
        // и DFC сгенерирует исключение с текстом запроса. Тогда он будет выведен целиком на новой строке,
        // и первоначальное форматирование запроса не будет испорчено.
        return new QueryTemplate((needStringMove ? (templateText.startsWith("\n") ? "" : "\n") : "") + templateText);
    }


    /**
     * Объявлен как package-private, для создания объектов QueryTemplate используйте статический метод-фабрику {@link
     * QueryTemplate#queryTemplate(String, Boolean)}. Обычная практика - импортировать статический метод:
     * <pre>
     *     import static ru.iteco.mtk.commonlib.dql.QueryTemplate.queryTemplate;
     * </pre>
     *
     * @throws QueryTemplateException
     */
    QueryTemplate(String templateText) {
        try {
            compiledTemplate = new Template(genTemplateName(), new StringReader(templateText), freemarkerCfg);
        } catch (IOException e) {
            throw new QueryTemplateException("Can't compile DQL query template.", templateText, e);
        }
    }

    /**
     * Копирующий конструктор, используется при частичной подстановке аргументов в шаблон, см. {@link
     * #substitutePartially(Object...)}.
     */
    QueryTemplate(Template compiledTemplate, Map<String, Object> partialArgs) {
        this.compiledTemplate = compiledTemplate;
        this.partialArgs = Optional.of(partialArgs);
    }

    /**
     * Подставляет значения параметров в шаблон. Параметры и их имена перечисляются последовательно, в строгом
     * соответствии друг другу, к примеру:
     * <pre>
     *     queryTemplate("SELECT ${dss_name} FROM ${ddt_doc}").substitute(
     *         "dss_name", IDdtDoc.DSS_NAME,
     *         "doc", IDdtDoc.NAME);
     * </pre>
     *
     * @throws IllegalArgumentException список параметров и их значений переданы некорректно, к примеру, на месте имени
     *                                  параметра передан объект не-строка, или для параметра не указано соответствующее
     *                                  значение
     * @throws QueryTemplateException   если при подстановке значений параметров возникли проблемы, к примеру, не для
     *                                  всех параметров шаблона предоставлены значения
     */
    public String substitute(Object... namesAndArgs) {
        return substitute(argListToNamedArgs(namesAndArgs));
    }

    /**
     * Подставляет значения параметров в шаблон.
     *
     * @throws QueryTemplateException если при подстановке значений параметров возникли проблемы, к примеру, не для всех
     *                                параметров шаблона предоставлены значения
     */
    public String substitute(Map<String, Object> namedArgs) {
        StringWriter result = new StringWriter();
        try {
            Map<String, Object> allArgs = mergeArgs(
                    partialArgs.or(Collections.<String, Object>emptyMap()),
                    namedArgs);
            compiledTemplate.process(allArgs, result);
        } catch (Exception e) {
            throw new QueryTemplateException("Error while substituting values in template.", getTemplateText(), e);
        }
        return result.toString();
    }

    /**
     * Выполняет частичную подстановку параметров в шаблон. Возвращает <i>новый</i> шаблон, не изменяя при этом тот, к
     * которому применялся. Частичную подстановку можно разложить в цепочку вызовов, к примеру:
     * <pre>
     *     queryTemplate("${a} < ${b} < ${c} < ${d}")
     *       .substitutePartially("a", 10)
     *       .substitutePartially("b", 20)
     *       .substitutePartially("c", 30)
     *       .substitute("d", 40); // вернет "10 < 20 < 30 < 40"
     * </pre>
     * <p/>
     * <p>Подстановка значения для уже заданного параметра в частично-заполненном щаблоне замещает старое значение:
     * <pre>
     *     queryTemplate("${a}")
     *       .substitutePartially("a", 10")
     *       .substitute("a", 20); // вернет "20"
     * </pre>
     *
     * @throws IllegalArgumentException список параметров и их значений переданы некорректно, к примеру, на месте имени
     *                                  параметра передан объект не-строка, или для параметра не указано соответствующее
     *                                  значение
     */
    public QueryTemplate substitutePartially(Object... namesAndArgs) {
        Map<String, Object> namedArgs = argListToNamedArgs(namesAndArgs);
        return new QueryTemplate(compiledTemplate,
                mergeArgs(partialArgs.or(Collections.<String, Object>emptyMap()),
                        namedArgs));
    }

    /**
     * Выполняет частичную подстановку параметров в шаблон. Возвращает <i>новый</i> шаблон, не изменяя при этом тот, к
     * которому применялся.
     * Перегружает метод {@link #substitutePartially}.
     *
     * @param namesAndArgsMap - мап параметров (key = имя параметра, value = значение параметра)
     * @return - объект QueryTemplate шаблоном и переданными параметрами
     * @throws IllegalArgumentException список параметров и их значений переданы некорректно, к примеру, на месте имени
     *                                  параметра передан объект не-строка, или для параметра не указано соответствующее
     *                                  значение
     */

    public QueryTemplate substitutePartially(Map<String, Object> namesAndArgsMap) {
        return new QueryTemplate(compiledTemplate,
                mergeArgs(partialArgs.or(Collections.<String, Object>emptyMap()),
                        namesAndArgsMap));
    }

    /**
     * @throws IllegalArgumentException
     */
    private Map<String, Object> argListToNamedArgs(Object[] argList) {
        HashMap<String, Object> namedArgs = Maps.newHashMap();
        for (int i = 0; i < argList.length; i += 2) {
            String argName;
            try {
                argName = (String) argList[i];
            } catch (ClassCastException e) {
                throw new IllegalArgumentException(
                        String.format("Argument name of type String expected, got: %s of type %s.",
                                argList[i].getClass(), argList[i]));
            }
            if (i + 1 >= argList.length)
                throw new IllegalArgumentException(
                        String.format("Absents value for argument '%s'.", argList[i]));
            Object arg = argList[i + 1];
            namedArgs.put(argName, arg);
        }
        return namedArgs;
    }

    private Map<String, Object> mergeArgs(Map<String, Object> thisArgs,
                                          Map<String, Object> withThatArgs) {
        Map<String, Object> merged = Maps.newHashMap(thisArgs);
        merged.putAll(withThatArgs);
        return merged;
    }

    private String getTemplateText() {
        return compiledTemplate.getRootTreeNode().getSource();
    }

    /**
     * TODO (n.artamonov): придумать более полезную схему именования, возможно, включающую имя вызывающего класса Для
     * этого следует понять, где и как пользователю выводится имя шаблона (судя по всему, в сообщениях об ошибках)
     */
    private synchronized String genTemplateName() {
        return "DQL-Query-Template-" + templateCounter++;
    }

    /**
     * Основан на ограничивающей семантике объекта SimpleObjectWrapper: использует только обертки SimpleXXX; если
     * какой-то объект не может быть обернут в какой-либо объект SimpleXXX, то генерируется исключение.
     * <p/>
     * TODO (n.artamonov): решить, в каком виде будут проставляться даты (java.util.Date) в DQL-запросы
     */
    private static class DQLAwareObjectWrapper extends SimpleObjectWrapper {
        @Override
        public TemplateModel wrap(Object obj) throws TemplateModelException {
            return super.wrap(obj);
        }
    }

    private static int templateCounter = 1;

    protected static Configuration freemarkerCfg = new Configuration();

    static {
        freemarkerCfg.setObjectWrapper(new DQLAwareObjectWrapper());
    }

    private Template compiledTemplate;
    private Optional<Map<String, Object>> partialArgs = Optional.absent();
}
