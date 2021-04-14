package tr.com.base.util;

import tr.com.base.model.GlobalStats;
import tr.com.base.model.WordCount;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class ThreadImpl implements Runnable {
    private List<String> sentenceList;
    private int threadId;

    public ThreadImpl(){
        sentenceList = new ArrayList<>();
    }

    public ThreadImpl(List<String> sentenceList,int threadId){
        this.sentenceList = sentenceList;
        this.threadId = threadId;
    }

    public void addToList(String sentence){
        this.sentenceList.add(sentence);
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    @Override
    public void run() {
        try {
            if(!sentenceList.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sentenceList.forEach(s -> sb.append(s).append(" "));
                String complete = sb.toString().trim();
                String[] arr = complete.split(" ");
                Map<String, List<String>> grouped = Arrays.stream(arr)
                        .collect(Collectors.groupingBy(String::intern));
                List<WordCount> wordCounts = new ArrayList<>();
                grouped.forEach((k, v) -> {
                    wordCounts.add(new WordCount(k, v.size()));
                });
                GlobalStats.addToList(wordCounts,threadId,sentenceList.size());
            }else{
                GlobalStats.addToList(new ArrayList<>(),threadId,0);
            }
        }catch (Throwable t){
            t.printStackTrace();
        }
    }
}
