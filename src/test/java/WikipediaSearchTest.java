import Data.Locale;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class WikipediaSearchTest {

    @BeforeAll
    static void setUp() {
        Configuration.browserSize = "1920x1080";
        Configuration.holdBrowserOpen = true;
    }

    @ValueSource(strings = {"Selenide", "Тесты"})
    @ParameterizedTest(name = "Проверка числа результатов поиска в википедии для запроса {0}")
    void wikipediaSearchTest(String searchQuery) {
        open("https://ru.wikipedia.org/");
        $("#searchInput[name='search']").setValue(searchQuery);
        $("#searchButton").click();
        $$(".mw-search-result-heading").shouldHave(CollectionCondition.sizeGreaterThan(10));
    }

    @CsvSource(value = {
            "Автоматизированное тестирование, часть процесса тестирования на этапе контроля качества в процессе разработки программного обеспечения",
            "Selenium, это инструмент для автоматизации действий веб-браузера"
    })
    @ParameterizedTest(name = "Проверка поиска страницы {0} в википедии")
    void wikipediaSearchPagesTest(String searchQuery, String expectedPageDescription) {
        open("https://ru.wikipedia.org/");
        $("#searchInput[name='search']").setValue(searchQuery);
        $("#searchButton").click();
        $("#bodyContent").shouldHave(text(expectedPageDescription));
    }

    static Stream<Arguments> wikipediaSisterProjectsTest() {
        return Stream.of(
                Arguments.of(Locale.English, List.of("Wikimedia Commons", "MediaWiki", "Meta-Wiki", "Multilingual Wikisource", "Wikispecies",
                        "Wikibooks", "Wikidata", "Wikimania", "Wikinews", "Wikiquote", "Wikisource", "Wikiversity", "Wikivoyage", "Wiktionary")),
                Arguments.of(Locale.Русский, List.of("Викисклад", "Медиавики", "Мета-вики", "Многоязычная Викитека", "Викивиды",
                        "Викиучебник", "Викиданные", "Викимания", "Викиновости", "Викицитатник", "Викитека", "Викиверситет", "Викигид",
                        "Викисловарь", "Элемент Викиданных"))
        );
    }

    @MethodSource("wikipediaSisterProjectsTest")
    @ParameterizedTest(name = "Проверка списка других проектов википедии для локали: {0}")
    void wikipediaSisterProjectsTest(Locale language, List<String> project) {
        open("https://de.wikipedia.org/");
        $$(".interlanguage-link-target").find(text(language.name())).click();
        $$(".wb-otherproject-link").filter(visible)
                .shouldHave(CollectionCondition.texts(project));
    }
}
