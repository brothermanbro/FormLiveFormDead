import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

class DBShare{
	
	//String IMAGEPATH="";
	
	String driver = "org.mariadb.jdbc.Driver";
	//DB=����ȣ��Ʈ�� ����. �ڵ� ���� �� IP�ּ� �ٲ� ��
    String dbUrl="jdbc:mariadb://127.0.0.1:3306/formlive_formdead";
	String dbId="fsfd";
	String dbPw="1234";	
	Connection con=null;
	Statement smt=null;
    //PreparedStatement pstmt=null;
    ResultSet rs=null;
	int loadPageFailed;
	int uploadFailedNum;
	
	DBShare(){
		uploadFailedNum=0;
	}
    
	
	Page[] loadPage() throws SQLException{
		
		Page [] pa=new Page[3];
		File i;
		String t;
		String p;
		String w;
		String ip;
	    int success=1;
		
		try {
			//������ DB ����
            Class.forName(driver);
            con = DriverManager.getConnection(dbUrl,dbId,dbPw); //������ DB ����
            if( con != null ) {
                System.out.println("DB ���� ����");
            }
            smt=con.createStatement();
            
            //���� �Է�
            String sql="select * from sharepage_all;";
            //��� �ε� �� ���
			ResultSet rs=smt.executeQuery(sql);
			while(rs.next()){
				int n=rs.getInt("number");
				t=rs.getString("time");
				p=rs.getString("place");
				w=rs.getString("weather");
				ip=rs.getString("imagePath")+n+".png";
				
				//System.out.println("Test load::::: "+n+" | "+t+" | "+p+" | "+w+" | "+ip);

				pa[n-1]=new Page(n+"",t,p,w);
				
				//System.out.println("Test load::::: "+pa[n-1].time+" | "+pa[n-1].place+" | "+pa[n-1].weather);
				
			}//while  
			success=0;//��������
			this.loadPageFailed=1;//loadPage
        }//try
		catch (ClassNotFoundException e) { 
            System.out.println("����̹� �ε� ����");
        }//catch 
		catch (SQLException e) {
            System.out.println("DB ���� ����");
            //e.printStackTrace();
        }//catch
		finally {
			//*****loadPage ������� ����Լ�
			if(success==1) {
				this.loadPageFailed=0;//loadPage����; ������ ���� �� ������Ʈ ���
				pa=loadPageFailed();
			}
			
		}
		return pa;
	}//loadPage
	
	

	void upload(Page newPage) throws SQLException, IOException{ //upload manager
		
		
		if(uploadFailedNum>0) {
			//2,3 ���� ����
			Page[] pa=new Page[2];
			String info="";
			try {
				File f=new File("C:\\flfdServerDB\\temporaryDBUpload\\upload.txt");
				Scanner s=new Scanner(f);
				while(s.hasNextLine()) {
					info=s.nextLine();
				}
			}
			catch(FileNotFoundException e) {
			System.out.println("error:���� ����");	
			}
			String i[]=info.split(";");
			pa[0]=new Page("1","C:\\flfdServerDB\\temporaryDBUpload",i[1],i[2],i[3]);
			pa[1]=new Page("2","C:\\flfdServerDB\\temporaryDBUpload",i[5],i[6],i[7]);
			newUpload(pa[0],true);
			newUpload(pa[1],true);
			uploadFailedNum=0;
			
		}
		
		newUpload(newPage,false);
		//*****�ε� ���� �� ���� �ؽ�Ʈ ���� ����
		
	}//upload
	
	
void newUpload(Page newPage, boolean update) throws SQLException, IOException{ //upload
		
		String nTime=newPage.time;
		String nPlace=newPage.place;
		String nWeather=newPage.weather;
		StringBuffer sb=new StringBuffer("insert into sharepage_all(number,time,place,weather) values(1,'");
		sb.append(nTime);
		sb.append("','");
		sb.append(nPlace);
		sb.append("','");
		sb.append(nWeather);
		sb.append("');");
		String all=sb.toString();
		int success=1;
		
		try {
			//������ DB ����
            Class.forName(driver);
            con = DriverManager.getConnection(dbUrl,dbId,dbPw); //������ DB ����
            if( con != null ) {
                System.out.println("DB ���� ����");
            }
            smt=con.createStatement();
            
            //���� �Է�+//��� �ε� �� ���
            //������ 2, 1�� �ѹ��� ��ĭ�� �̷�
            String sql="update sharepage_all set number=number+1;";
            smt.executeUpdate(sql);
            //���ο� ������ 1�� ������Ʈ
            smt.executeUpdate(all);
            //������ 3(���� ������ ������) ����
            sql="delete from sharepage_all where number=4;";
            smt.executeUpdate(sql);
            
            //Ȯ�ο� ��� �ڵ�
            ResultSet rs=smt.executeQuery("select * from sharepage_all;");
			while(rs.next()){
				int n=rs.getInt("number");
				String t=rs.getString("time");
				String p=rs.getString("place");
				String w=rs.getString("weather");
				String ip=rs.getString("imagePath");
				System.out.println("Test load::::: "+n+" | "+t+" | "+p+" | "+w+" | "+ip);
			}//while
			success=0;//���� ����
        }//try 
		catch (ClassNotFoundException e) { 
            System.out.println("����̹� �ε� ����");
        }//catch 
		catch (SQLException e) {
            System.out.println("DB ���� ����");
            //e.printStackTrace();    
        }//catch
		finally {
			//*****upload ������� �� �ӽ����� ����Լ�
			//*****loadPage ������� ����Լ�
			if(success==1) {
				uploadFailed(newPage);
			}
			else if(success==0||update==false) { //���� ���� �� ���ε�� ���� ������ ���� ����
				//3��° ���� ����
				File f3=new File("C:\\flfdServerDB\\3.png");
				f3.delete();
				//1,2��° ���� �̸� �ϳ��� �̷��
				File f2=new File("C:\\flfdServerDB\\2.png");
				File f1=new File("C:\\flfdServerDB\\1.png");
				setImagePath(f2,"C:\\flfdServerDB\\3.png");
				setImagePath(f1,"C:\\flfdServerDB\\2.png");
				//�ֱ� ������ 1��° ���Ϸ� �߰�
				File nf=new File("C:\\flfdServerDB\\1.png");
				copyImage(newPage.image,nf);
			}
			else if(success==0||update==true) {
				
				//*************���� �պ�����
				//3��° ���� ����
				File f3=new File("C:\\flfdServerDB\\3.png");
				f3.delete();
				//1,2��° ���� �̸� �ϳ��� �̷��
				File f2=new File("C:\\flfdServerDB\\2.png");
				File f1=new File("C:\\flfdServerDB\\1.png");
				setImagePath(f2,"C:\\flfdServerDB\\3.png");
				setImagePath(f1,"C:\\flfdServerDB\\2.png");
				//�ֱ� ������ 1��° ���Ϸ� �߰�
				File nf=new File("C:\\flfdServerDB\\1.png");
				copyImage(newPage.image,nf);
			}
			
		}//finally

	}//upload
	
	
	void copyImage(File src, File dest) {
		try(BufferedInputStream in = new BufferedInputStream(new FileInputStream(src)) ;
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dest))) {
			int data;
			while(true) {
				data = in.read();
				if(data == -1)
					break;
				out.write(data);
			}
			in.close();
			out.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	void setImagePath(File f, String newPath) {
		File dest=new File(newPath);
		if(f.exists()) {
			 f.renameTo( dest );
		}
		return;
	}
	
	
	Page[] loadPageFailed() {
		//�ε��� �ӽ�����ҿ� ����Ǿ��ִ� ���� 3�� ���� �� ��� ���� �õ�(������ 2��)
		//C:\flfdServerDB\temporaryDBLoad
		Page[] pa=new Page[3];
		String t;
		String p;
		String w;
		int n;
		try {
		       // ����Ʈ ������ �����б�
			String filePath = "C:/flfdServerDB/temporaryDBLoad/load.txt"; // ��� ����
			FileInputStream fileStream = null; // ���� ��Ʈ��
		        
		    fileStream = new FileInputStream( filePath );// ���� ��Ʈ�� ����
		        //���� ����
		    byte[ ] readBuffer = new byte[fileStream.available()];
		    while (fileStream.read( readBuffer ) != -1){}
		    String s=new String(readBuffer);
		    String[] parse=s.split(";");
		    //�ð�;��ġ;����;
		    for(int i=0, j=0;i<3;i++,j+=3) {
		    	t=parse[j];
			    p=parse[j+1];
			    w=parse[j+2];
			    n=i+1;
			    pa[i]=new Page("C:\\flfdServerDB\\"+n,t,p,w);
		    }
		    
		    fileStream.close(); //��Ʈ�� �ݱ�
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		
		return pa;
	}
	
	
	void uploadFailed(Page newPage) throws IOException {
		//�ð�;���;����;
		//���ε�� �ӽ�����ҿ� ���ε����� ����
		//C:\flfdServerDB\temporaryDBUpload
		uploadFailedNum++;
		StringBuffer sb= new StringBuffer(""+uploadFailedNum);
		sb.append(";");
		sb.append(newPage.time);
		sb.append(";");
		sb.append(newPage.place);
		sb.append(";");
		sb.append(newPage.weather);
		sb.append(";");
		
		String txt=sb.toString();
		
		System.out.println(txt);
		
		
		//���Ͽ� ��Ʈ�� ���� �� �����ؾ��� �Ƚ���...�����
		
        FileWriter writer = null;
        File f = new File("C:/flfdServerDB/temporaryDBUpload/upload.txt");
		try {
            // ���� ������ ���뿡 �̾ ������ true��, ���� ������ ���ְ� ���� ������ false�� �����Ѵ�.
			
            writer = new FileWriter(f, true);
            writer.write(txt);
            writer.flush();
            
            System.out.println("DONE");
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null) writer.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
		
		//�̹����ű��
		File n=new File("C:\\flfdServerDB\\temporaryDBUpload\\"+uploadFailedNum+".png");
		copyImage(newPage.image,n);
		
	}//uploadFailed
	
}//DBShare
