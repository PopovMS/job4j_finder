package ru.job4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

/**
 *Клас осуществляет обход дерева файлов и каталогов и ищет файлы по условию в предикате
 *
 * @author Mikhail Popov (m.popov83@gmail.com)
 * @version 0.1
 * @since 01/10/2025
 */
public class Search {

    private static final Logger LOG = LoggerFactory.getLogger(Search.class.getName());

    /**
     *Метод запуска программы
     * @param args - аргументы запуска
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        LOG.info("The Search program has started ");
        ArgsName argsName = ArgsName.of(args);
        LOG.info("Arguments successfully validated: {}", (Object) args);
        try {
            Predicate<Path> condition = new Condition().getPredicate(
                    argsName.get("n"),
                    argsName.get("t"));
            List<Path> result = new Search().search(Paths.get(argsName.get("d")), condition);
            LOG.info("Files found: {}", result.size());
            Path fileLog = Path.of(argsName.get("o"));
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileLog.toFile(), true)))) {
                for (Path file : result) {
                    out.println(file.toString());
                }
            }
            LOG.info("Results successfully written.");
        } catch (IOException e) {
            LOG.error("Error writing to file: {}", e.getMessage(), e);
        } finally {
            LOG.info("The Search program has finished working");
        }
    }
    /**
     *Метод принимает путь к файлу и предикат
     * @param root - Путь начала поиска
     * @param condition предикат - условие поиска
     * @return Возвращает результат поиска ввиде списка
     * @throws IOException
     */
    public static List<Path> search(Path root, Predicate<Path> condition) throws IOException {
        SearchFiles searcher = new SearchFiles(condition);
        try {
            Files.walkFileTree(root, searcher);
            LOG.info("Search in directory {} completed successfully", root);
        } catch (IOException e) {
            LOG.error("Error traversing the file system: {}", e.getMessage(), e);
            throw new RuntimeException("Error searching for files", e);
        }
        return searcher.getPaths();
    }
}