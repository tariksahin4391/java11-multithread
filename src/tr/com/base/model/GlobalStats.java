package tr.com.base.model;

import java.awt.desktop.SystemSleepEvent;
import java.util.*;

public class GlobalStats {
    private static final Map<String,Integer> wordMap = new HashMap<>();
    private static int totalSentence = 0;
    private static int totalWordCount = 0;

    //syncronized yapılmazsa race condition oluşur
    public static synchronized void addToList(List<WordCount> add,int threadId,int sentenceCount){
        System.out.println("-----analyzing stats for thread : "+threadId+" - processed sentence count : "+sentenceCount);
        totalSentence+=sentenceCount;
        add.forEach((w)->{
            if(!wordMap.containsKey(w.getWord()))
                wordMap.put(w.getWord(),w.getCount());
            else
                wordMap.replace(w.getWord(),wordMap.get(w.getWord())+w.getCount());
        });
    }

    public static void printStats(){
        List<WordCount> wordCounts = new ArrayList<>();
        wordMap.forEach((w,c)->{
            wordCounts.add(new WordCount(w,c));
            totalWordCount+=c;
        });
        float avg = (float)(totalWordCount/totalSentence);
        System.out.println("total sentence :"+totalSentence);
        System.out.println("Avg Word Count :"+avg);
        wordCounts.sort(Comparator.comparing(w -> w.getCount() * -1));
        wordCounts.forEach(w->System.out.println(w.getWord()+" : "+w.getCount()));
    }
}
