package ru.job4j;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 *Класс поиска по предикату
 */
public class SearchFiles extends SimpleFileVisitor<Path> {
    private List<Path> paths = new ArrayList<>();
    private Predicate<Path> condition;
    public SearchFiles(Predicate<Path> condition) {
        this.condition = condition;
    }

    /**
     * Метод реализует предикат и добавляет результат в список List
     * @param file
     * @param attrs
     * @return
     * @throws IOException
     */
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (condition.test(file)) {
            paths.add(file);
        }
        return FileVisitResult.CONTINUE;
    }

    /**
     * Метод возвращает готовый список
     * @return
     */
    public List<Path> getPaths() {
        return paths;
    }
}
