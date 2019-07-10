import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class Page{
	File image;
	String time;
	String place;
	String weather;
	
	Page(){
		image=new File("C:\\flfdServerDB\\default.png");
		time=":";
		place="��ġ ������ �����ϴ�.";
		weather="���� ������ �����ϴ�.";
	}
	
	Page(String imageName, String t, String p, String w){
		String imagePath="C:\\flfdServerDB\\"+imageName+".png";
		image=new File(imagePath);
		time=t;
		place=p;
		weather=w;
	}
	
	Page(String imageName, String imagePath, String t, String p, String w){
		String iPath=imagePath+imageName+".png";
		image=new File(iPath);
		time=t;
		place=p;
		weather=w;
	}
	
	Page(String imageName, String imagePath, String time, String minutes, String p, String w){
		String iPath=imagePath+imageName+".png";
		image=new File(iPath);
		time=time+":"+minutes;
		place=p;
		weather=w;
	}
	
	File getImage(){	return image;	} //��ü image�� �����Ѵ�
	void setImage(File src) {
		try(BufferedInputStream in = new BufferedInputStream(new FileInputStream(src)) ;
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(this.image))) {
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
	}//��ü image�� ���� �Է��Ѵ�. 
	
	String getTime() {	return time;	}//���� time�� �����Ѵ�.
	String getPlace() {	return place;	}//���� place�� �����Ѵ�. 
	String getWeather() {	return weather;	}//���� weather�� �����Ѵ�. 
	void setTime(String t) {	this.time=t;	}//���� time�� ���� �Է��Ѵ�.
	void setPlace(String p) {	this.place=p;	}//���� place�� ���� �Է��Ѵ�. 
	void setWeather(String w) {	this.weather=w;	}//���� weather�� ���� �Է��Ѵ�.
}