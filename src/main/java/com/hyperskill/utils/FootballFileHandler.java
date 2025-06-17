package com.hyperskill.utils;

public class FootballFileHandler implements FileHandler {
    private final FootballDataLoader loader;
    private final FootballDataSaver saver;
    
    /**
     * Create a new FootballFileHandler with default loader and saver
     */
    public FootballFileHandler() {
        this.loader = new FootballDataLoader();
        this.saver = new FootballDataSaver();
    }
    
    @Override
    public void loadData(String filename) {
        loader.loadData(filename);
    }
    
    @Override
    public void saveData(String filename) {
        saver.saveData(filename);
    }
}