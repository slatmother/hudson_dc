package util;

import java.io.File;

/**
 * @author a.dunaev (a.dunaev@i-teco.ru)
 *         Copyright iTeco, CJSK, (c) 2012
 *         Company: iTeco
 *         Date: 15.02.2012
 * @version 1.0
 *          <p/>
 *          Description:
 *          Класс со статическими методами обеспечивающие быструю проверку входящих параметров, внутренних переменных
 *          на null, пустоту и прочее
 */
public class Checker {

    /**
     * @param name  имя переменной или параметра для рапорта об ошибке
     * @param val   проверяемое значение
     * @param isVar true, рапортовать будет как об переменной с именем paramName
     *              false -- как о параметре
     * @date 08.02.2012 @author a.dunaev (a.dunaev@i-teco.ru)
     * Проверяет на пустоту переданный объект. Т.е. проверяет val,
     * а в случаее его пустоты сообщает о недопустимом значении переменной varName
     * (val == null || val.length() == 0)
     */
    public static void checkStringForEmpty(String name, String val, boolean isVar) {
        if (val == null || val.length() == 0)
            throw new IllegalArgumentException(
                    String.format("Illegal %s %s value - empty", isVar ? "variable" : "parameter", name));
    }

    /**
     * Проверка значения на пустую строку.
     *
     * @param val - строка
     * @return true - если строка не пустая, false -  если пустая
     */
    public static boolean isStringNotEmpty(String val) {
        return (val != null && val.trim().length() > 0);
    }

    /**
     * @param name  имя переменной или параметра для рапорта об ошибке
     * @param val   проверяемое значение
     * @param isVar true - рапортовать будет как об переменной с именем paramName
     *              false - как о параметре
     * @date 08.02.2012 @author a.dunaev (a.dunaev@i-teco.ru)
     * Проверяет переданный объект на null. Т.е. проверяет val,
     * а в случаее если он содержит null сообщает о нехватки параметра с именем name
     * (val == null)
     */
    public static void checkObjectForNull(String name, Object val, boolean isVar) {
        if (val == null)
            throw new IllegalArgumentException(
                    String.format("%s %s is null", isVar ? "Variable" : "Parameter", name));
    }

    /**
     * Проверяет, что переданный id корректен и не пуст !=null && len==16 &&
     * !='0000000000000000'
     *
     * @date 24.02.2012
     * @author a.dunaev (a.dunaev@i-teco.ru)
     */
    public static void checkFileExistsOrIsFile(File file) {
        if (!file.exists() || !file.isFile())
            throw new IllegalArgumentException(String.format("File %s %s", file.exists() ? "" : "does not exists", file.isFile() ? "" : "and is not a file"));
    }



}
