//   **************************************************************************
//   *                                                                        *
//   *  This program is free software: you can redistribute it and/or modify  *
//   *  it under the terms of the GNU General Public License as published by  *
//   *  the Free Software Foundation, either version 3 of the License, or     *
//   * (at your option) any later version.                                    *
//   *                                                                        *
//   *  This program is distributed in the hope that it will be useful,       *  
//   *  but WITHOUT ANY WARRANTY; without even the implied warranty of        *
//   *  MERCHANTABILITY || FITNESS FOR A PARTICULAR PURPOSE.  See the         *
//   *  GNU General Public License for more details.                          *
//   *                                                                        *
//   *  You should have received a copy of the GNU General Public License     *
//   *  along with this program.  If not, see <http://www.gnu.org/licenses/>. *
//   *                                                                        *
//   *                   (C) Arvind Kumar 2011 .                              *
//   **************************************************************************


import java.io.*; 
import java.net.*; 
import java.util.*; 
import java.util.concurrent.*; 

public class ChessServer implements Runnable{ 
	
	private static Socket socket; 
    private int player;
    private String nick;
	public static final Object synch = new Object(); 
	public static Map<String, ChessServer> connectionMap = new HashMap<String, ChessServer>();  
	
	public ChessServer(Socket socket){ 
        this.socket = socket;
    }
	
	public static void main(String args[]){
		BufferedReader b=new BufferedReader(new InputStreamReader(System.in));
		System.out.print("PORT ( default - 1000 ): "); 
		int port=1000;
		try{
			port=Integer.parseInt(b.readLine()); 
		}
		catch(Exception e){
			System.out.println("Wrong format. Using 1000 instead"); 
			port=1000;
		}
		System.out.println("Server Started. Waiting for connections");
		try{
			ServerSocket listener=new ServerSocket(port);
			while(true){
				Socket server=listener.accept();
				ChessServer connection=new ChessServer(server); 
				Thread t=new Thread(connection); 
				t.start();
			}
		}
		catch(Exception e){ e.printStackTrace();
			System.out.println("Error : Cannot start server on port - "+port+" Quiting."); 
		}
	}
	
	public void run(){
		try{
			startServ(); 
		}
		catch(Exception e){ 
			if (nick!=null&&connectionMap.get(nick)==this){ 
                try{
					sendQuit("Leaving");
				}
				catch(Exception ex){ }
            }
			connectionMap.remove(nick);
			System.out.println(nick+" closed the connection");
			e.printStackTrace();
		}
	}
	
	public void startServ()throws Exception{
        checkSendLoop.start();
        String comm;
		OutputStream out=socket.getOutputStream();
		
		BufferedReader reading=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while((comm=reading.readLine())!=null){ 
            evaluate(comm);
        }
    }
	
	LinkedBlockingQueue<String> outQueue=new LinkedBlockingQueue<String>(1000);
	
	public Thread checkSendLoop=new Thread(){ 
        public void run(){
            try{
				OutputStream out=socket.getOutputStream();
                while (true){
                    String mes=outQueue.take();
                    mes=mes.replace("\n", "").replace("\r", ""); 
                    mes=mes+"\r\n"; 
                    out.write(mes.getBytes());
					out.flush();
                }
            }
            catch (Exception e){
                System.out.println("FATAL : Data to a client will not be sent anymore."); 
                outQueue.clear();
                outQueue=null;
            }
        }
    };
	
	public void evaluate(String line)throws Exception{
        System.out.println("Evaluating - "+nick+": "+line);
        String prefix="";
        String[] split=line.split(" ", 2); 
        String command=split[0]; 
		if (split.length>1)
			line=split[1]; 
		else
			line="";
        String[] arguments=line.split(" "); 
        Command commandObject=null; 
        try{
            commandObject=Command.valueOf(command.toUpperCase()); 
        }
        catch (Exception e){}
        if (commandObject==null){
            sendSelfNotice("Invalid Command or Not Supported - "+command); 
            return;
        }
        if (arguments.length<commandObject.getMin()||arguments.length>commandObject.getMax()){
            sendSelfNotice("Invalid Arguments"); 
            return;
        }
		ChessServer testcon=connectionMap.get(nick); 
		if (testcon!=this&&(!command.toUpperCase().equals("AUTH")))
			sendSelfNotice("You are not authenticated");
		else
			commandObject.run(this, prefix, arguments); 
    }
	
	public void sendQuit(String mess)throws Exception{ 
        synchronized (synch){
            for (String nicka : new ArrayList<String>(connectionMap.keySet())){
                ChessServer channel=connectionMap.get(nicka);
                allsend("LOGOUT "+nick); 
				connectionMap.remove(nick);
				socket.close();
            }
        }
    }
	
	public void sendSelfNotice(String string){
        send("NOTICE "+string.replace("%","%25").replace(" ","%20"));
    }
	
	public void send(String s){
        Queue<String> testQueue = outQueue;
		s=s+" "+getUnixTime();
        if (testQueue!=null){
            System.out.println("SENDING "+nick+" - "+s);
            testQueue.add(s); 
        }
    }
	
	public String getUnixTime(){
		return ""+(System.currentTimeMillis()/1000L);
	}
	
	public static void allsend(String toSend){
        synchronized (synch){
            for (Map.Entry<String , ChessServer> con : connectionMap.entrySet()){
                con.getValue().send(toSend);
            }
        }
    }
	
	public enum Command{
		AUTH(1, 1){
			public void run(ChessServer con, String prefix, String[] arguments)throws Exception{
				String urln="http://enkrypt.in/abstergo/det.php?id="+arguments[0]+"&t="+Math.random(); //setting up stuff for MySQL connection
				String name="";
				int userid=0,access=0;
				try{
					URL url=new URL(urln);
					BufferedReader rea=new BufferedReader(new InputStreamReader(url.openStream()));
					String lin="",cres="";
					while ((lin=rea.readLine())!=null){
						cres+=lin;
					}
					rea.close();
					String[] cren=cres.split(" ");
					userid=Integer.parseInt(cren[0]);
					name=cren[1];
					access=Integer.parseInt(cren[2]);
				}
				catch(Exception e){ e.printStackTrace();
					userid=0;
					name="";
					access=0;
				}
				if (name.equals("")){ //somethings gone really wrong - client isnt supposed to send invalid session
					userid=0;
					name="Guest";
					access=0;
					con.send("AUTH Guest");
					con.send("LOGOUT Bye");
					con.sendQuit("Invalid");
					connectionMap.remove(con.nick);
				}
				else{
					con.nick=name;
					synchronized (synch){
						ChessServer testconn=connectionMap.get(con.nick); //test if theres already a similar connection
						if (testconn!=null){
							con.sendSelfNotice("Apparently your client got screwed up cause you like closing the browser to quit chat. Next time , type '/LOGOUT'. For now , just refresh the page and try again.");
							testconn.sendSelfNotice("Apparently your client got screwed up cause you like closing the browser to quit chat. Next time , type '/LOGOUT'. For now , just refresh the page and try again.");
							con.send("LOGOUT Bye");
							testconn.send("LOGOUT Bye");
							testconn.sendQuit("Invalid");
							connectionMap.remove(testconn.nick);
							ChessServer testlol=null;
							while((testlol=connectionMap.get(con.nick))!=null){
								testlol.send("LOGOUT Bye");
								testlol.sendQuit("Invalid");
								connectionMap.remove(testlol.nick);
								System.out.println("Removing extra connections for "+con.nick);
							}
						}
						else{
							connectionMap.put(con.nick, con); //add the connection
							con.send("AUTH "+con.nick); //send AUTH back to client
							con.sendSelfNotice("Looked up Session. Received User"); //Send Welcome messages and Stuff
							con.sendSelfNotice("**Connection Authenticated and Established**");
							con.sendSelfNotice("*");
							con.sendSelfNotice("Welcome to Abstergo's Chat Server. "+con.nick);
							con.sendSelfNotice("Written by EnKrypt");
							con.sendSelfNotice("*");
							con.sendSelfNotice("Type '/JOIN <channel>' to join a channel");
							con.sendSelfNotice("Deafult channel is #ac. Type '/JOIN #ac' to join that channel");
						}
					}
				}
			}
		},
		USERS(1, 1){ //get list of users in that channel
			public void run(ChessServer con, String prefix, String[] arguments)throws Exception{
				String listmem="USERS";
				for (Map.Entry<String , ChessServer> member : connectionMap.entrySet()){ //give list of members
					listmem+=" "+member.getValue().nick;
				}
				con.send(listmem);
            }
		},
		LOGOUT(1, 1){
			public void run(ChessServer con, String prefix, String[] arguments)throws Exception{
				con.send("LOGOUT Bye"); //client will understand this as end of communication
                con.sendQuit("");
            }
		},
		SEND(1,1){
			public void run(ChessServer con, String prefix, String[] arguments)throws Exception{
				allsend("SEND "+arguments[0]+" "+con.nick); //Send message to all members including sender
			}
		},
		ME(2,2){ //same as SEND , will be interpreted differently by Client
			public void run(ChessServer con, String prefix, String[] arguments)throws Exception{
				allsend("ME "+arguments[0]+" "+arguments[1]+" "+con.nick);
			}
		},
		PING(1, 1){
            public void run(ChessServer con, String prefix, String[] arguments)throws Exception{
                con.send("PONG "+arguments[0]); //return same argument
            }
        };
		int minArgumentCount; //minimum number of arguments for the command
        int maxArgumentCount; //maximum number of arguments for the command
        
        Command(int min, int max){
            minArgumentCount = min;
            maxArgumentCount = max;
        }
        
        public int getMin(){
            return minArgumentCount;
        }
        
        public int getMax(){
            return maxArgumentCount;
        }
		
		public abstract void run(ChessServer con, String prefix, String[] arguments) throws Exception; //runs command
	}
}