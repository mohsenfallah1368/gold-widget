package com.mohsenfallah.goldwidget;
import java.io.*; import java.net.*; import java.nio.charset.StandardCharsets; import java.util.regex.*;
public class TgjuRepository {
 private static final String BASE="https://www.tgju.org/profile/";
 private static final Pattern CURRENT=Pattern.compile("نرخ\\s*فعلی\\s*::?\\s*([0-9,]+(?:\\.[0-9]+)?)");
 private static final Pattern RIAL=Pattern.compile("قیمت\\s*ریالی\\s*([0-9,]+)");
 public static class Data{double gold18,gold24,coin,dollar,tether,ounce,silver;}
 public static Data fetch() throws Exception{
  Data d=new Data();
  d.gold18=parse(BASE+"geram18"); d.gold24=parse(BASE+"geram24"); d.coin=parse(BASE+"sekee");
  d.dollar=parse(BASE+"price_dollar_rl"); d.ounce=parse(BASE+"ons"); d.silver=parse(BASE+"silver");
  Matcher m=RIAL.matcher(strip(get(BASE+"crypto-tether"))); if(!m.find()) throw new IllegalStateException("Tether not found"); d.tether=num(m.group(1));
  d.gold18/=10; d.gold24/=10; d.coin/=10; d.dollar/=10; d.tether/=10; return d;
 }
 private static double parse(String u)throws Exception{Matcher m=CURRENT.matcher(strip(get(u))); if(!m.find()) throw new IllegalStateException("Price not found"); return num(m.group(1));}
 private static String get(String u)throws Exception{HttpURLConnection c=(HttpURLConnection)new URL(u).openConnection(); c.setConnectTimeout(12000); c.setReadTimeout(12000); c.setRequestProperty("User-Agent","Mozilla/5.0 (Android) GoldWidget/1.0"); try{if(c.getResponseCode()!=200)throw new IllegalStateException("HTTP "+c.getResponseCode()); BufferedReader b=new BufferedReader(new InputStreamReader(c.getInputStream(),StandardCharsets.UTF_8)); StringBuilder s=new StringBuilder(); String l; while((l=b.readLine())!=null)s.append(l).append('\n'); b.close(); return s.toString();}finally{c.disconnect();}}
 private static String strip(String h){return h.replaceAll("(?is)<script.*?</script>"," ").replaceAll("(?is)<style.*?</style>"," ").replaceAll("(?s)<[^>]*>"," ").replace("&nbsp;"," ").replaceAll("\\s+"," ").trim();}
 private static double num(String s){return Double.parseDouble(s.replace(",",""));}
}