// File reading code from https://howtodoinjava.com/java/io/java-read-file-to-string-examples/
import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MarkdownParse {
    public static Map<String, List<String>> getLinks(File dirOrFile) throws IOException {
        Map<String, List<String>> result = new HashMap<>();
        if(dirOrFile.isDirectory()) {
            for(File f: dirOrFile.listFiles()) {
                result.putAll(getLinks(f));
            }
            return result;
        }
        else {
            Path p = dirOrFile.toPath();
            int lastDot = p.toString().lastIndexOf(".");
            if(lastDot == -1 || !p.toString().substring(lastDot).equals(".md")) {
                return result;
            }
            ArrayList<String> links = getLinks(Files.readString(p));
            result.put(dirOrFile.getPath(), links);
            return result;
        }
    }
    public static ArrayList<String> getLinks(String markdown) {
        ArrayList<String> toReturn = new ArrayList<>();
        // find the next [, then find the ], then find the (, then take up to
        // the next )
        ArrayList<Integer> periodList = new ArrayList<Integer>();
        int currentIndex = 0;
        while(currentIndex < markdown.length()) {
            int nextPeriodIndex = markdown.indexOf(".", currentIndex + 1);
            if(nextPeriodIndex != -1) {
                periodList.add(nextPeriodIndex);
                currentIndex = nextPeriodIndex;
            } else {
                break;
            }
        }
        for(int periodIndex: periodList) {
            int startIndex = periodIndex;
            int endIndex = periodIndex;
            ArrayList<Character> stopCharacters = new ArrayList<Character>();
            stopCharacters.add('(');
            stopCharacters.add(')');
            stopCharacters.add('[');
            stopCharacters.add(']');
            stopCharacters.add('\n');
            stopCharacters.add(' ');
            while(startIndex >= 0) {
                if(stopCharacters.contains(markdown.charAt(startIndex))) {
                    startIndex++;
                    break;
                }
                startIndex--;
            }
            while(endIndex < markdown.length()) {
                if(stopCharacters.contains(markdown.charAt(endIndex))) {
                    endIndex--;
                    break;
                }
                endIndex++;
            }
            if(endIndex == markdown.length()) {
                endIndex--;
            }
            toReturn.add(markdown.substring(startIndex, endIndex + 1));
        }
        return toReturn;
    }
    public static void main(String[] args) throws IOException {
		Path fileName = Path.of(args[0]);
        File file = new File(fileName.toString());
        Map<String, List<String>> links = getLinks(file);
        System.out.println(links);
    }
}