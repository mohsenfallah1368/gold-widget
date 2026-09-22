package com.mohsenfallah.goldwidget;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.*;

public class TgjuRepository {
 private static final String[] HOSTS={
   "https://gem.tgju.org/profile/",
   "https://www.tgju.org/profile/"
 };
 private static final Pattern CURRENT=Pattern.compile("نرخ\\s*فعلی\\s*::?\\s*([0-9٠-٩۰-۹,٬]+(?:\\.[0-9٠-٩۰-۹]+)?)");
 private static final Pattern RIAL=Pattern.compile("قیمت\\s*ریالی\\s*:?\\s*([0-9٠-٩۰-۹,٬]+)");
 public static class Data{double gold18,gold24,coin,dollar,tether,ounce,silver;}

 public static Data fetch() throws Exception{
  Data d=new Data();
  d.gold18=parse("geram18");
  d.gold24=parse("geram24");
  d.coin=parse("sekee");
  d.dollar=parse("price_dollar_rl");
  d.ounce=parse("ons");
  d.silver=parse("silver");
  try{
   Matcher m=RIAL.matcher(strip(get("crypto-tether")));
   if(m.find()) d.tether=num(m.group(1));
   else d.tether=d.dollar;
  }catch(Exception ignored){
   d.tether=d.dollar;
  }
  d.gold18/=10;
  d.gold24/=10;
  d.coin/=10;
  d.dollar/=10;
  d.tether/=10;
  return d;
 }

 private static double parse(String p)throws Exception{
  String h=strip(get(p));
  Matcher m=CURRENT.matcher(h);
  if(!m.find()){
   throw new IllegalStateException("Price not found: "+p);
  }
  return num(m.group(1));
 }

 private static String get(String p)throws Exception{
  Exception last=null;
  for(String host:HOSTS){
   try{return request(host+p);}
   catch(Exception e){last=e;}
  }
  throw last==null?new IOException("network"):last;
 }

 private static String request(String u)throws Exception{
  HttpURLConnection c=(HttpURLConnection)new URL(u).openConnection();
  c.setInstanceFollowRedirects(true);
  c.setConnectTimeout(20000);
  c.setReadTimeout(20000);
  c.setRequestMethod("GET");
  c.setUseCaches(false);
  c.setRequestProperty("User-Agent","Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 Chrome/120 Mobile Safari/537.36");
  c.setRequestProperty("Accept","text/html,application/xhtml+xml");
  c.setRequestProperty("Accept-Language","fa-IR,fa;q=0.9,en;q=0.8");
  c.setRequestProperty("Accept-Encoding","identity");
  c.setRequestProperty("Cache-Control","no-cache");
  c.setRequestProperty("Connection","close");
  c.setRequestProperty("Referer","https://www.tgju.org/");
  try{
   int code=c.getResponseCode();
   if(code<200||code>=300)throw new IOException("HTTP "+code);
   BufferedReader b=new BufferedReader(new InputStreamReader(c.getInputStream(),StandardCharsets.UTF_8));
   StringBuilder s=new StringBuilder();
   String l;
   while((l=b.readLine())!=null)s.append(l).append('\\n');
   b.close();
   return s.toString();
  }finally{c.disconnect();}
 }

 private static String strip(String h){
  return h.replaceAll("(?is)<script.*?</script>"," ")
   .replaceAll("(?is)<style.*?</style>"," ")
   .replaceAll("(?s)<[^>]*>"," ")
   .replace("&nbsp;"," ")
   .replace("&#160;"," ")
   .replace("٬",",")
   .replace("۰","0").replace("۱","1").replace("۲","2").replace("۳","3").replace("۴","4")
   .replace("۵","5").replace("۶","6").replace("۷","7").replace("۸","8").replace("۹","9")
   .replace("٠","0").replace("١","1").replace("٢","2").replace("٣","3").replace("٤","4")
   .replace("٥","5").replace("٦","6").replace("٧","7").replace("٨","8").replace("٩","9")
   .replaceAll("\\s+"," ").trim();
 }

 private static double num(String s){
  return Double.parseDouble(s.replace(",","").replace("٬","").trim()
   .replace("۰","0").replace("۱","1").replace("۲","2").replace("۳","3").replace("۴","4")
   .replace("۵","5").replace("۶","6").replace("۷","7").replace("۸","8").replace("۹","9")
   .replace("٠","0").replace("١","1").replace("٢","2").replace("٣","3").replace("٤","4")
   .replace("٥","5").replace("٦","6").replace("٧","7").replace("٨","8").replace("٩","9"));
 }
}
