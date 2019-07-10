import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


//������� ���Ϸ�
//�׽�Ʈ �Ϸ�
class DBMatchTable{
	
	String driver = "org.mariadb.jdbc.Driver";
	//DB=����ȣ��Ʈ�� ����. �ڵ� ���� �� IP�ּ� �ٲ� ��
    String dbUrl="jdbc:mariadb://127.0.0.1:3306/formlive_formdead";
	String dbId="fsfd";
	String dbPw="1234";	
	Connection con=null;
	Statement smt=null;
    //PreparedStatement pstmt=null;
    ResultSet rs=null;
    
	
	String getClothes(int temp, boolean page) throws SQLException{
		
		//���� �� ���� �õ� ���� �� ����Ʈ���� Connection Failed�� �״�� ���ϵ�
		//���� �� ���� �õ� ���� �� ���������� ����� �ǻ� ���� ����
		String returnValue="Connection Failed";
		int success=1;
		
		//�µ� 
		int categorizedTemp=categorizeTemp(temp);
		
		//���� �� ���� �õ�
		try {
			//������ DB ����
            Class.forName(driver);
            con = DriverManager.getConnection(dbUrl,dbId,dbPw); //������ DB ����
            if( con != null ) {
                System.out.println("DB ���� ����");
            }//if 
            smt=con.createStatement();
            
            //���� �Է�
            String sql="select * from matchtable where temp="+categorizedTemp+";";
            //��� �ε� �� ����
			ResultSet rs=smt.executeQuery(sql);
			while(rs.next()){
				int t =rs.getInt("temp");
				String top=rs.getString("top");
				String bottoms=rs.getString("bottoms");
				String outerClothing=rs.getString("outerClothing");
				//���� �� ��� �ε� ���� �� returnValue���� ���������� ����
				if(page==true) { //����;����;����; ��ȯ
					//��������::����->��, Ȩ ȭ�� �ε�(DB)::����;����;����;
					returnValue=top+";"+bottoms+";"+outerClothing+";";
				}
				else { //����;����; ��ȯ
					returnValue=top+";"+outerClothing+";";
				}
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
			if(success==1) {
				 //*****MatchTable ������� ����Լ�
				if(page==true) { //����;����;����; ��ȯ
					//��������::����->��, Ȩ ȭ�� �ε�(DB)::����;����;����;
					returnValue=getClothesFailedHome(categorizedTemp);
				}
				else { //����;����; ��ȯ
					returnValue=getClothesFailedSecond(categorizedTemp);
				}
	            return returnValue;
			}
		}//finally

		return returnValue;
		
	}//getClothes
	
	int categorizeTemp(int temp) {
		
		if(temp>=28) {
			return 28;
		}
		else if(temp>=23) {
			return 23;
		}
		else if(temp>=20) {
			return 20;
		}
		else if(temp>=17) {
			return 17;
		}
		else if(temp>=12) {
			return 12;
		}
		else if(temp>=9) {
			return 9;
		}
		else if(temp>=5) {
			return 5;
		}
		else {
			return 4;
		}
	}//categorizeTemp
	
	String getClothesFailedHome(int temp) {
		String cloth;
		//��������::����->��, Ȩ ȭ�� �ε�(DB)::����;����;����;
		switch (temp){
		case 4:
			cloth="��� ������, ��Ʈ;������;�е�, �β��� ��Ʈ;";
			break;
		case 5:
			cloth="��Ʈ��, ��Ʈ;���뽺;��Ʈ, ��������;";
			break;
		case 9:
			cloth="��Ʈ;û����, ��Ÿŷ;����, Ʈ��ġ��Ʈ, �߻�;";
			break;
		case 12:
			cloth="������;û����, �����;����, �����, �߻�;";
			break;
		case 17:
			cloth="���� ��Ʈ, ������;û����;�����;";
			break;
		case 20:
			cloth="����;�����, û����;���� �����;";
			break;
		case 23:
			cloth="����, ���� ����;�ݹ���, �����;����;";
			break;
		case 28:
			cloth="�μҸ�, ����;�ݹ���;����;";
			break;
		default:
			System.out.println("�߸��� �µ���");
			cloth="";
			break;		
		}
		
		return cloth;
	}//getClothesFailed
	
	String getClothesFailedSecond(int temp) {
		String cloth;
		//��������::����->��, Ȩ ȭ�� �ε�(DB)::����;����;����;
		switch (temp){
		case 4:
			cloth="��� ������, ��Ʈ;�е�, �β��� ��Ʈ;";
			break;
		case 5:
			cloth="��Ʈ��, ��Ʈ;��Ʈ, ��������;";
			break;
		case 9:
			cloth="��Ʈ;����, Ʈ��ġ��Ʈ, �߻�;";
			break;
		case 12:
			cloth="������;����, �����, �߻�;";
			break;
		case 17:
			cloth="���� ��Ʈ, ������;�����;";
			break;
		case 20:
			cloth="����;���� �����;";
			break;
		case 23:
			cloth="����, ���� ����;����;";
			break;
		case 28:
			cloth="�μҸ�, ����;����;";
			break;
		default:
			System.out.println("�߸��� �µ���");
			cloth="";
			break;		
		}
		
		return cloth;
	}//getClothesFailed
	

}//DBMatchTable