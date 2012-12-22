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
//   *                   (C) Arvind Kumar 2013 .                              *
//   **************************************************************************


import java.io.*; 
import java.net.*; 
import java.util.*; 
import java.util.concurrent.*; 

public class ChessServer implements Runnable{ 
	
	private static Socket socket; 
    private int player;
    private String nick;
	private int time;
	public static final Object synch = new Object(); 
	public static Map<String, ChessServer> connectionMap = new HashMap<String, ChessServer>();  
	public static Grid[][] grid=new Grid[8][8];
	public static int turn=1;
	private int ping=100000;
	public static int timer=0;
	
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
		for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                grid[i][j]=new Grid(j*50,i*50);
            }
        }
		pingPoll.start();
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
					sendQuit("Something went wrong");
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
	public static Thread pingPoll=new Thread(){ 
        public void run(){
			while(true){
				try{
					Thread.sleep(1000);
				}
				catch (Exception e){
				}
				timer++;
				for (Map.Entry<String , ChessServer> ocon : connectionMap.entrySet()){
					if (ocon.getValue().ping!=100000&&timer%10==0){
						ocon.getValue().send("PING "+timer);
					}
				}
				for (Map.Entry<String , ChessServer> ocon : connectionMap.entrySet()){
					if (ocon.getValue().ping!=100000&&timer-ocon.getValue().ping>50){
						ocon.getValue().send("LOGOUT Ping Timout");
						ocon.getValue().sendQuit("Ping Timeout");
					}
				}
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
			mess=mess.replace("%","%25").replace(" ","%20");
            allsend("LOGOUT "+nick+" "+mess); 
			connectionMap.remove(nick);
			for (Map.Entry<String , ChessServer> ocon : connectionMap.entrySet()){
				if (player>ocon.getValue().player){
					ocon.getValue().player-=1;
					allsend("PLAYER "+ocon.getValue().player+" "+ocon.getValue().nick);
				}
			}
			boolean onec=false,twoc=false;
			for (Map.Entry<String , ChessServer> ocon : connectionMap.entrySet()){
				if (ocon.getValue().player==1){
					onec=true;
				}
			}
			for (Map.Entry<String , ChessServer> ocon : connectionMap.entrySet()){
				if (ocon.getValue().player==2){
					twoc=true;
				}
			}
			if (player==1||player==2){
				if (onec&&twoc){
					allsend("START");
					startboard();
				}
				if (onec&&(!twoc)){
					allsend("WAIT");
				}
			}
			socket.close();
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
	
	public static void startboard(){
		for (int i=0;i<8;i++){
            for (int j=0;j<8;j++){
                grid[i][j]=new Grid(j*50,i*50);
            }
        }
		for (int i=0;i<8;i++){
            (grid[1][i]).changePiece(Pawn.value);
            (grid[6][i]).changePiece(Pawn.value);
            (grid[6][i]).changeOwner(true);
        }
        grid[0][1].changePiece(Knight.value);
        grid[0][6].changePiece(Knight.value);
        grid[7][1].changePiece(Knight.value);
        grid[7][6].changePiece(Knight.value);
        grid[7][1].changeOwner(true);
        grid[7][6].changeOwner(true);
        grid[0][2].changePiece(Bishop.value);
        grid[0][5].changePiece(Bishop.value);
        grid[7][2].changePiece(Bishop.value);
        grid[7][5].changePiece(Bishop.value);
        grid[7][2].changeOwner(true);
        grid[7][5].changeOwner(true);
        grid[0][0].changePiece(Rook.value);
        grid[0][7].changePiece(Rook.value);
        grid[7][0].changePiece(Rook.value);
        grid[7][7].changePiece(Rook.value);
        grid[7][0].changeOwner(true);
        grid[7][7].changeOwner(true);
        grid[0][3].changePiece(Queen.value);
        grid[0][4].changePiece(King.value);
        grid[7][3].changePiece(Queen.value);
        grid[7][4].changePiece(King.value);
        grid[7][3].changeOwner(true);
        grid[7][4].changeOwner(true);
	}
	
	public static void checkmate(){
		//TODO - FINISH CHECKING MECHANISM
	}
	
	public enum Command{
		AUTH(1, 1){
			public void run(ChessServer con, String prefix, String[] arguments)throws Exception{
				ChessServer testcon=connectionMap.get(arguments[0]);
				if (arguments[0].equals("")){ 
					con.send("LOGOUT Bye");
					con.sendQuit("Authenticated with Invalid Username.");
				}
				else if (testcon!=null){
					con.send("LOGOUT Username alreadu exists");
					con.sendQuit("Authenticated using duplicate username.");
				}
				else{
					con.nick=arguments[0];
					synchronized (synch){
						con.player=connectionMap.size()+1;
						connectionMap.put(con.nick, con);
						allsend("AUTH "+con.player+" "+con.nick);
						con.sendSelfNotice("*************************************");
						con.sendSelfNotice("Welcome to JChess Server, "+con.nick);
						con.sendSelfNotice("       Developed by EnKrypt");
						con.sendSelfNotice("*************************************");
						con.ping=timer;
						allsend("PLAYER "+con.player+" "+con.nick);
						String listmem="USERS";
						for (Map.Entry<String , ChessServer> member : connectionMap.entrySet()){
							listmem+=" "+member.getValue().nick;
						}
						con.send(listmem);
						if (con.player==1){
							con.send("WAIT");
						}
						else if (con.player>2){
							con.send("SPECTATE");
							String allpiece="ALLPIECE";
							for (int i=0;i<8;i++){
								for (int j=0;j<8;j++){
									if (grid[i][j].piece!=100){
										allpiece+=" "+i+"~"+j+"~"+grid[i][j].piece+"~"+grid[i][j].owner;
									}
								}
							}
						}
						else if (con.player==2){
							allsend("START");
							startboard();
						}
					}
				}
			}
		},
		MOVE(4,4){
			public void run(ChessServer con, String prefix, String[] arguments)throws Exception{
				if (turn==con.player){
					try{
						int a1=Integer.parseInt(arguments[0]);
						int a2=Integer.parseInt(arguments[1]);
						int b1=Integer.parseInt(arguments[2]);
						int b2=Integer.parseInt(arguments[3]);
						if (grid[a1][a2].piece!=100){
							if(Piece.parse(grid[a1][a2].piece).placeMoves(grid)[b1][b2]){
								grid[b1][b2].changePiece(grid[a1][a2].piece);
								grid[a1][a2].changePiece(100);
								grid[b1][b2].changeOwner(grid[a1][a2].owner);
								grid[a1][a2].changeOwner(false);
								allsend("MOVE "+arguments[0]+" "+arguments[1]+" "+arguments[2]+" "+arguments[3]+" "+con.nick);
								int checkwin=0;
								if ((checkwin=checkmate())>0){
									allsend("WIN "+checkwin);
									if (checkwin==1){
										allsend("LOSE 2");
									}
									else if (checkwin==2){
										allsend("LOSE 1");
									}
									int cursize=connectionMap.size();
									for (Map.Entry<String , ChessServer> member : connectionMap.entrySet()){
										if (member.getValue().player==2){
											member.getValue().player=cursize+1;
										}
										if (member.getValue().player==1){
											member.getValue().player=cursize+2;
										}
									}
									for (Map.Entry<String , ChessServer> ocon : connectionMap.entrySet()){
										ocon.getValue().player-=2;
										allsend("PLAYER "+ocon.getValue().player+" "+ocon.getValue().nick);
									}
									allsend("START");
								}
							}
							else{
								con.sendSelfNotice("That piece does not have the ability to move to that grid");
							}
                        }
						else{
							con.sendSelfNotice("You cannot move an empty grid");
						}
					}
					catch(Exception e){
						con.sendSelfNotice("Invalid Grid co-ordinates to move");
					}
				}
				else{
					con.sendSelfNotice("It is not your turn to play");
				}
			}
		},
		CHECKMATE(1,1){
			public void run(ChessServer con, String prefix, String[] arguments)throws Exception{
				int mep=Integer.parseInt(arguments[0]);
				int yop=1;
				if (mep==1){
					yop=2;
				}
				for (Map.Entry<String , ChessServer> ocon : connectionMap.entrySet()){
					if (ocon.getValue().player==mep){
						ocon.getValue().send("WIN");
					}
					else if (ocon.getValue().player==yop){
						ocon.getValue().send("LOSE");
					}
					else{
						ocon.getValue().send("WIN "+con.nick);
					}
					
				}
				allsend("SEND "+arguments[0]+" "+con.nick);
			}
		},
		USERS(1, 1){ 
			public void run(ChessServer con, String prefix, String[] arguments)throws Exception{
				String listmem="USERS";
				for (Map.Entry<String , ChessServer> member : connectionMap.entrySet()){
					listmem+=" "+member.getValue().nick;
				}
				con.send(listmem);
            }
		},
		LOGOUT(1, 1){
			public void run(ChessServer con, String prefix, String[] arguments)throws Exception{
				con.send("LOGOUT Bye");
                con.sendQuit("Manual Disconnect");
            }
		},
		PONG(1, 1){
			public void run(ChessServer con, String prefix, String[] arguments)throws Exception{
				con.ping=timer;
            }
		},
		SEND(1,1){
			public void run(ChessServer con, String prefix, String[] arguments)throws Exception{
				allsend("SEND "+arguments[0]+" "+con.nick);
			}
		},
		PING(1, 1){
            public void run(ChessServer con, String prefix, String[] arguments)throws Exception{
                con.send("PONG "+arguments[0]);
            }
        };
		int minArgumentCount; 
        int maxArgumentCount; 
        
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
		
		public abstract void run(ChessServer con, String prefix, String[] arguments) throws Exception;
	}
}