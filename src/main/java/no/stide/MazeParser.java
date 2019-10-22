package no.stide;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MazeParser {

    private String filePath;
    private String wallPattern;
    private String pathPattern;
    private LineNumberReader sourceFile;
    private int[] entrance;
    private int[] exit;
    private ArrayList<ArrayList<CellForm>> mazeForm = new ArrayList<>();


    public MazeParser(String path, String wallPattern, String pathPattern) {
        this.filePath = path;
        this.wallPattern = wallPattern;
        this.pathPattern = pathPattern;
    }

    public Maze parse() {
        try {
            FileInputStream fileStream = new FileInputStream(filePath);
            this.sourceFile = new LineNumberReader(new InputStreamReader(fileStream, StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            System.err.println("Could not read file " + filePath);
            System.err.println(e.getMessage());
            return null;
        }

        try {
            boolean mazeFound = false;
            Pattern pattern = Pattern.compile("(" + wallPattern + "|" + pathPattern + ")");
            Matcher matcher;
            String line;
            while ((line = sourceFile.readLine()) != null) {
                if (!mazeFound) {
                    if (entrance == null) {
                        this.findEntrance(line);
                    }
                    if (exit == null) {
                        this.findExit(line);
                    }
                    mazeFound = findMaze(line);
                    if (!mazeFound) continue;
                }

                ArrayList<CellForm> row = new ArrayList<>();
                int x = 0;
                matcher = pattern.matcher(line);
                while (matcher.find()) {
                    x++;
                    if (matcher.group(0).equals(wallPattern)) {
                        row.add(CellForm.WALL);
                    }
                    else {
                        row.add(CellForm.PATH);
                    }
                }
                if (x == 0) {
                    break;
                }
                mazeForm.add(row);
            }

            int width = mazeForm.get(0).size();
            int height = mazeForm.size();
            Maze maze = new Maze(width, height);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    maze.getCell(x, y).setForm(mazeForm.get(y).get(x));
                }
            }

            if (entrance != null) {
                maze.setEntrance(entrance[0], entrance[1]);
            }
            if (exit != null) {
                maze.setExit(exit[0], exit[1]);
            }

            return maze;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void findEntrance(String line) {
        final Pattern pattern = Pattern.compile("^(?:enter:\\s*)(\\d+\\s*,\\s*\\d+)");
        final Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            String match = matcher.group(1);
            String[] nums = match.split("\\s*,\\s*");
            this.entrance = new int[]{Integer.parseInt(nums[0]), Integer.parseInt(nums[1])};
        }
    }

    private void findExit(String line) {
        final Pattern pattern = Pattern.compile("^(?:exit:\\s*)(\\d+\\s*,\\s*\\d+)");
        final Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            String match = matcher.group(1);
            String[] nums = match.split("\\s*,\\s*");
            this.exit = new int[]{Integer.parseInt(nums[0]), Integer.parseInt(nums[1])};
        }
    }

    private boolean findMaze(String line) {
        final Pattern pattern = Pattern.compile("^" + wallPattern);
        final Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

}
