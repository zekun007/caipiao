package com.example.caipiao.thread;

import com.example.caipiao.dao.ProcessDao;
import com.example.caipiao.model.DoubleChromosphereInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class RequestDataTimer {

    private static final String url = "http://datachart.500.com/ssq/history/newinc/history.php?start=<code>000&end=<code>200";
    @Autowired
    private ProcessDao dao;

    @Scheduled(fixedDelay = 5000)
    public void accessDataTimer() {
        for (int i=3; i < 20; i++){
            try {
                String finalUrl = "";
                if (i < 10) {
                    finalUrl = url.replaceAll("<code>", ("0" + i));
                } else {
                    finalUrl = url.replaceAll("<code>", String.valueOf(i));
                }
                List<DoubleChromosphereInfo> list = analysisData(finalUrl);
                dao.insertBatch(list);
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public List<DoubleChromosphereInfo> analysisData(String url){
        System.out.println("url:" + url);
        List<DoubleChromosphereInfo> list = new ArrayList<DoubleChromosphereInfo>();
        try {
            Document document = Jsoup.connect(url).get();
            //像js一样，通过id 获取文章列表元素对象
            Element postList = document.getElementById("tdata");
            //像js一样，通过class 获取列表下的所有节点
            Elements postItems = postList.getElementsByClass("t_tr1");
            for (Element tr : postItems) {
                //从tr中获取td
               Elements tds = tr.select("td");
               DoubleChromosphereInfo doubleChromosphereInfo = new DoubleChromosphereInfo();
                for (Element td : tds) {
                    analysisTd(doubleChromosphereInfo, td);
                }
                list.add(doubleChromosphereInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void analysisTd(DoubleChromosphereInfo doubleChromosphereInfo, Element td) {
        int index = td.siblingIndex();
        switch(index){
            case 1:
                doubleChromosphereInfo.setWinningCode(td.text());
                break;
            case 2:
                doubleChromosphereInfo.setRed1(Integer.parseInt(td.text()));
                break;
            case 3:
                doubleChromosphereInfo.setRed2(Integer.parseInt(td.text()));
                break;
            case 4:
                doubleChromosphereInfo.setRed3(Integer.parseInt(td.text()));
                break;
            case 5:
                doubleChromosphereInfo.setRed4(Integer.parseInt(td.text()));
                break;
            case 6:
                doubleChromosphereInfo.setRed5(Integer.parseInt(td.text()));
                break;
            case 7:
                doubleChromosphereInfo.setRed6(Integer.parseInt(td.text()));
                break;
            case 8:
                doubleChromosphereInfo.setBlue(Integer.parseInt(td.text()));
                break;
            case 16:
                doubleChromosphereInfo.setWinningDate(td.text());
                doubleChromosphereInfo.setWinningWeek(dateToWeek(td.text()));
                break;
        }
    }

    public String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
}