package tr.com.base;

import tr.com.base.model.GlobalStats;
import tr.com.base.util.ThreadImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("enter thread count :");
        int threadCount = Integer.parseInt(bufferedReader.readLine().trim());
        System.out.println("enter file path :");
        String filePathStr = bufferedReader.readLine().trim();
        Path filePath = Path.of(filePathStr);
        String content = Files.readString(filePath, StandardCharsets.UTF_8).replaceAll("\n"," ");
        bufferedReader.close();
        String[] sentenceArr = content.split("\\?|\\.|!");
        List<ThreadImpl> threads = new ArrayList<>();
        for(int i=0;i<threadCount;i++){
            ThreadImpl t = new ThreadImpl(new ArrayList<>(),i);
            threads.add(t);
        }
        for(int i=0;i<sentenceArr.length;i++){
            threads.get(i%threadCount).addToList(sentenceArr[i].trim());
        }
        ExecutorService es = Executors.newFixedThreadPool(threadCount);
        threads.forEach(es::submit);
        es.shutdown();
        System.out.println("------submitted tasks----");
        es.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("----------------------------");
        GlobalStats.printStats();
    }
}
