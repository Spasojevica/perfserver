package java.net.inemar.perfserver;

import java.util.Random;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.sun.net.httpserver.*;



 class Resp {
    static   void sendResponse200(HttpExchange httpExchange,String data) throws IOException {

        httpExchange.sendResponseHeaders(200,0);
        OutputStream outputStream = httpExchange.getResponseBody();
                
                outputStream.write(data.getBytes());
                outputStream.flush();
                outputStream.close();
        
    
    
    }

    static   void sendError(HttpExchange httpExchange,int error,String data) throws IOException {

        httpExchange.sendResponseHeaders(error,0);
        OutputStream outputStream = httpExchange.getResponseBody();
                
                outputStream.write(data.getBytes());
                outputStream.flush();
                outputStream.close();
        
    
    
    }

    
    static Random random=new Random(System.currentTimeMillis());


    static String[] vals= new String[] { "CPU","Power","attempts","rulesmatched","Germany","hungary","Linkload","memory","CPU1","CPU2","CPU3","CPU4","CPU5","CPU6","CPU7","CPU0"};

}


final class MyHelloHandler implements HttpHandler {    
      @Override    
      public void handle(HttpExchange httpExchange) throws IOException {
         System.out.println("Got "+httpExchange.getRequestMethod()); 
        String method=httpExchange.getRequestMethod();
        if("GET".equals(method)) { 
           Resp.sendResponse200(httpExchange, "Hello World!");
         }else  { 
             Resp.sendError(httpExchange, 400, "Only GET Method is allowed- Got: "+method);
          }  
      }
    }



final class GetDataHandler implements HttpHandler {    
        @Override    
        public void handle(HttpExchange httpExchange) throws IOException {
          String method=httpExchange.getRequestMethod();
          String query=httpExchange.getRequestURI().getQuery();
          if("GET".equals(method)) { 
              if (query.startsWith("time=")) {
                  long time=Long.parseLong(query.substring(5).toString());
                  if (time % (5*60) != 0 ) {
                    Resp.sendError(httpExchange, 400, "time is not at 5 minutes interval  time= "+time);
                  };
                  time=time*1000;
                  if (time > System.currentTimeMillis()-(5*60*1000) ) {
                    Resp.sendError(httpExchange, 400, "time is in the future   time= "+time);
                  };

                  if (time < System.currentTimeMillis()-(3*24*60*60*1000) ) {
                    Resp.sendError(httpExchange, 400, "time is too much in the past more than 3 days  time= "+time);
                  };


                  int val=Math.abs(Resp.random.nextInt(Resp.vals.length-5))+5;
                  String resp="Timestamp|Duration|Key";                  
                  for(int i=0;i<val;i++) {
                      resp=resp+"|"+Resp.vals[i];
                  };
                  resp=resp+"|\n";
                  int lines=Math.abs(Resp.random.nextInt(40))+10;
                    for (int i=0;i<lines;i++) {
                      resp=resp+""+(time*1000)+"|5|mykey"+Resp.random.nextInt(30000);

                      for(int j=0;j<val;j++) {
                        resp=resp+"|"+Resp.random.nextDouble();                          
                      }
                      resp=resp+"\n";
                    }
                    Resp.sendResponse200(httpExchange,resp);

              };
              Resp.sendError(httpExchange, 400, "the time paramter is missing or wrong  query= "+query);
           }else  { 
               Resp.sendError(httpExchange, 400, "Only GET Method is allowed- Got: "+method);
            }  
        }
      }
  

/**
 * Hello world!
 *
 */

public class App 
{
    public static void main( String[] args ) throws IOException 
    {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);


        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
        server.createContext("/hello", new  MyHelloHandler());
        server.createContext("/getdata", new  GetDataHandler());
        server.setExecutor(threadPoolExecutor);
        server.start();

        System.out.println(" Server started on port 8001");
        
    }
}
