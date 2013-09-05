package transaction.holder;

import com.documentum.fc.client.IDfLocalTransaction;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;

/**
 * Класс, упрощающий работу с вложенными транзакциями Documentum (посредством методов IDfSession#beginTransEx(),
 * IDfSession#commitTransEx() и IDfSession#abortTransEx()).
 *
 * <p>Обычно код, который нужно выполнить во вложенной транзакции, оборачивается в длинный и неприглядный блок
 * try-catch:
 *
 * <pre>
 *     IDfLocalTransaction tx = session.beginTransEx();
 *     try {
 *         // Вызов методов, которые в совокупности могут генерировать некоторые
 *         // "проверяемые" исключения, к примеру, ObjectRelationMappingException и DfException.
 *         // ....
 *
 *         session.commitTransEx(tx); // Коммитим транзакцию.
 *     }
 *     // Нам необходимо отлавливать все "проверяемые" исключения, чтобы абортировать транзакцию,
 *     // а сами исключения перенаправить далее по стеку.
 *     catch (ObjectRelationMappingException e) {
 *         session.abortTransEx(tx);
 *         throw e;
 *     }
 *     catch (DfException e) {
 *         session.abortTransEx(tx);
 *         throw e;
 *     }
 *     // Здесь отлавливаем все run-time исключения, а также ошибки виртуальной машины;
 *     // абортируем транзакцию а исключение оборачиваем в одно из проверяемых.
 *     catch (Throwable e) {
 *         session.abortTransEx(tx);
 *         throw new DfException;
 *     }
 * </pre>
 *
 * <p>Такой код захламляет бизнес-логику метода, делает его трудным для сопровождения, содержит дублирование
 * кода, и вообще уродлив. Данный класс позволяет переписать его следующим образом:
 *
 * <pre>
 *     NestedTx tx = beginTx(session);
 *     try {
 *         // Вызов методов, которые в совокупности могут генерировать любые "проверяемые" исключения,
 *         // как станет видно далее, нам нет нужды заботиться о них.
 *
 *         tx.okToCommit(); // Помечаем, что транзакция может быть закоммичена.
 *     }
 *     // Откаты и коммиты производятся в блоке finally одним единственным методом.
 *     // Все исключения автоматически направляются вверх по стеку, без необходимости
 *     // их явного перехвата.
 *     finally {
 *         tx.commitOrAbort();
 *     }
 * </pre>
 *
 * <p>Еще одним преимуществом в наличии абстракции над механизмом транзакций заключается в том, что в будущем
 * мы сможем инкапсулировать ряд других стратегий работы с транзакциями Documentum, при этом изменения в
 * унаследованный код, использующий данный класс, будут минимальны и осуществимы автоматическим рефакторингом.
 *
 * <p>К примеру, данный класс легко специализируется для транзакционной стратегии, когда все вложенные
 * транзакции на самом деле выполняются в одной реальной транзакции Documentum, с помощью проверок
 * {@link com.documentum.fc.client.IDfSession#isTransactionActive()}.
 *
 * @author Nikolay Artamonov (n.artamonov@i-teco.ru)
 * @version 12.05.12
 */
public class NestedTx {
    private boolean isOkToCommit = false;
    private IDfSession session;
    private IDfLocalTransaction tx;

    public static NestedTx beginTx(IDfSession session) throws DfException {
        return new NestedTx(session);
    }

    private NestedTx(IDfSession session) throws DfException {
        this.session = session;
        this.tx = session.beginTransEx();
    }

    public void okToCommit() {
        isOkToCommit = true;
    }

    public void commitOrAbort() throws DfException {
        if (isOkToCommit)
            session.commitTransEx(tx);
        else
            session.abortTransEx(tx);
    }
}
