package fr.istic.project.view;

public enum Theme {
    DARK("dark"), LIGHT("light");

    private final String cssSelector;

    Theme(String cssSelector) {
        this.cssSelector = cssSelector;
    }

    public String getCssSelector() {
        return cssSelector;
    }

}
