package ru.job4j;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Класс проверки ключей и записи их в мапу.
 *
 * @author Mikhail Popov (m.popov83@gmail.com)
 * @version 0.1
 * @since 01/10/2025
 */

public class ArgsName {
    /**
     * Мапа для хранения пароы ключ-значение
     */
    private final Map<String, String> values = new HashMap<>();

    /**
     * Метод проверяет наличие переданного ключа в хэшМап
     * и выбрасывает исключение, если значение не найдено
     * @param key принимает значение ключа
     * @return возвращает значение присвоенное ключу
     */
    public String get(String key) {
        if (!values.containsKey(key)) {
            throw new IllegalArgumentException("This key: '%s' is missing".formatted(key));
        }
        return values.get(key);
    }

    /**
     * метод разбивает пару ключ-значение из массива на два значения и записывает их в мапу
     * @param args принимает массив параметров
     */
    private void parse(String[] args) {
        for (String vol : args) {
            String[] tmp = vol.substring(1).split("=", 2);
            values.put(tmp[0], tmp[1]);
        }
    }

    /**
     * проверяет массив параметров на валидность
     * создает объект класа и запускает метод  parse
     * @param args массив параметров
     * @return возвращает ссылку на объект
     */
    public static ArgsName of(String[] args) {
        check(args);
        ArgsName names = new ArgsName();
        names.parse(args);
        return names;
    }

    /**
     * Метод проверяет на валидность массив с ключами и значениями
     * @param args массив с параметрами
     */
    private static void check(String[] args) {
        if (args.length < 3) {
            throw new IllegalArgumentException("Отсутствуют параметры. Используйте 3 параметра.");
        }
        for (String vol : args) {
            if (!vol.contains("=")) {
                throw new IllegalArgumentException("Error: This argument '%s' does not contain an equal sign".formatted(vol));
            }
            if (!vol.startsWith("-")) {
                throw new IllegalArgumentException("Error: This argument '%s' does not start with a '-' character".formatted(vol));
            }
            String[] temp =  vol.substring(1).split("=");
            if (!Pattern.compile("^[dnto]$").matcher(temp[0]).matches()) {
                throw new IllegalArgumentException("Error: This argument '%s' does not contain a key".formatted(vol));
            }
            if (temp.length == 1) {
                throw new IllegalArgumentException("Error: This argument '%s' does not contain a value".formatted(vol));
            }
            if ("d".equals(temp[0])) {
                File file = new File(temp[1]);
                if (!file.isDirectory()) {
                    throw new IllegalArgumentException(String.format("Not directory %s", file.getAbsoluteFile()));
                }
            }
            if ("t".equals(temp[0]) && !Pattern.compile("^(mask|name|regex)$").matcher(temp[1]).matches()) {
                throw new IllegalArgumentException("Error: This argument '%s' does not contain a rigth command. Please use one of next command: mask, name, regex".formatted(vol));
            }
        }
    }
}