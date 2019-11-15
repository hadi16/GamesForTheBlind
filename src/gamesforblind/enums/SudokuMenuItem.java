package gamesforblind.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum SudokuMenuItem {
    HINT,
    INSTRUCTIONS,
    LANGUAGE,
    RESTART,
    RETURN_TO_MAIN_MENU;

    /**
     * @return
     */
    @Override
    public String toString() {
        List<String> lowercaseWordList = Arrays.asList(this.name().toLowerCase().split("_"));
        var capitalizedWordList = lowercaseWordList.stream().map(StringUtils::capitalize).collect(Collectors.toList());
        return String.join(" ", capitalizedWordList);
    }
}
