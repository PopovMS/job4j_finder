package ru.job4j;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс условий.
 *
 * @author Mikhail Popov (m.popov83@gmail.com)
 * @version 0.1
 * @since 01/10/2025
 */
public class Condition {
    public static final String NAME_TYPE = "name";
    public static final String MASK_TYPE = "mask";
    public static final String REGEX_TYPE = "regex";

    public Predicate<Path> getPredicate(String name, String type) {
        switch (type) {
            case NAME_TYPE:
                return getName(name);
            case MASK_TYPE:
                return getMask(name);
            case REGEX_TYPE:
                return getRegex(name);
            default:
                throw new IllegalArgumentException("неизвестный тип поиска");
        }
    }
    /**
     * Метод возвращает предикат, при типе поиска, по регулярному выражению.
     * @param name Имя файла поиска.
     * @return Предикат поиска файла по регулярному выражению.
     */
    private Predicate<Path> getRegex(String name) {
        return p -> {
            Pattern pattern = Pattern.compile(name);
            Matcher matcher = pattern.matcher(p.toFile().getName());
            return matcher.find();
        };
    }

    /**
     * Метод возвращает предикат, при типе поиска по маске.
     * @param name Имя файла поиска.
     * @return Предикат поиска файла по маске.
     */
    private Predicate<Path> getMask(String name) {
        return path -> {
            Pattern pattern = Pattern.compile(name.replace("?", "\\S{1}").replace("*", "\\S*"));
            return pattern.matcher(path.getFileName().toString()).matches();
        };
    }

    /**
     * Метод возвращает предикат, при типе поиска, по имени файла.
     * @param name Имя файла поиска.
     * @return Предикат поиска файла по имени.
     */
    private Predicate<Path> getName(String name) {
        return path -> path.toFile().getName().equals(name);
    }
}
