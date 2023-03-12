package sec01.exam01;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class DBConnection {
	// 멤버 변수
	private Connection connection = null;

	// 생성자 : 디폴트
	// 멤버 함수
	public void connect() {
		// 1. 외부에서 데이터베이스를 접속 할 수 있도록 설정
		Properties properties = new Properties();
		FileInputStream fis = null;
		// 2. db.properties 파일 로드
		try {
			fis = new FileInputStream("C:/202302javaWorkspace/Student/src/sec01/exam01/db.properties");
			properties.load(fis);
		} catch (FileNotFoundException e) {
			System.out.println("FileInputStream error : " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Properties.load error : " + e.getMessage());
		}
		// 2. 내부적으로 JDBC DriverManager 통해서 DB와 연결을 가져온다.
		try {
			// 1. jdbc 클래스 로드
			Class.forName(properties.getProperty("driverName"));
			// 2. mysql DB 연결
			connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"),
					properties.getProperty("password"));
		} catch (ClassNotFoundException e) {
			System.out.println("데이터베이스 로드오류" + e.getStackTrace());
		} catch (SQLException e) {
			System.out.println("데이터베이스 연결오류" + e.getStackTrace());
		}
	}

	public int insert(Student s) {
		this.connect();
		PreparedStatement ps = null;
		int returnValue = -1;
		String query = "insert into studentTBL values(null,?,?,?,?,?,?,?,?)";

		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, s.getName());
			ps.setInt(2, s.getAge());
			ps.setInt(3, s.getKor());
			ps.setInt(4, s.getEng());
			ps.setInt(5, s.getMath());
			ps.setInt(6, s.getTotal());
			ps.setDouble(7, s.getAvg());
			ps.setString(8, s.getGrade());
			// 삽입 성공하면 1을 리턴해준다.
			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("insert 오류 발생 : " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류 : " + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				System.out.println(" 오류 : " + e.getMessage());
			}
		}
		return returnValue;
	}

	// 내부클래스, 정적 내부 클래스, 로컬 클래스
	// 선택 select statement
	public ArrayList<Student> select() {
		ArrayList<Student> list = new ArrayList<>();
		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from studentTBL;";

		try {
			ps = connection.prepareStatement(query);
			// 삽입 성공하면 ResultSet를 리턴, 실패하면 null 리턴.
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avg = rs.getDouble("avg");
				String grade = rs.getString("grade");
				list.add(new Student(id, name, age, kor, eng, math, total, avg, grade));
			}
		} catch (Exception e) {
			System.out.println("select 오류 발생 : " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류 : " + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				System.out.println(" 오류 : " + e.getMessage());
			}
		}
		return list;
	}

	// 분석 선택
	public ArrayList<Student> analyzeSelect() {
		ArrayList<Student> list = new ArrayList<>();
		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select id,name,age,total,avg,grade from studentTBL;";

		try {
			ps = connection.prepareStatement(query);
			// 삽입 성공하면 ResultSet를 리턴, 실패하면 null 리턴.
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				int total = rs.getInt("total");
				double avg = rs.getDouble("avg");
				String grade = rs.getString("grade");
				list.add(new Student(id, name, age, 0, 0, 0, total, avg, grade));
			}
		} catch (Exception e) {
			System.out.println("select 오류 발생 : " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류 : " + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				System.out.println(" 오류 : " + e.getMessage());
			}
		}
		return list;
	}

	// 이름 검색 선택
	public ArrayList<Student> nameSearchSelect(String dataName) {
		ArrayList<Student> list = new ArrayList<>();
		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from studentTBL where name like ?";

		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, "%" + dataName + "%");
			// 삽입 성공하면 ResultSet를 리턴, 실패하면 null 리턴.
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avg = rs.getDouble("avg");
				String grade = rs.getString("grade");
				list.add(new Student(id, name, age, kor, eng, math, total, avg, grade));
			}
		} catch (Exception e) {
			System.out.println("select 오류 발생 : " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류 : " + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				System.out.println(" 오류 : " + e.getMessage());
			}
		}
		return list;
	}

	// select
	public Student selectId(int dataId) {
		Student student = null;
		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from studentTBL where id = ?;";

		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, dataId);
			// 삽입 성공하면 ResultSet를 리턴, 실패하면 null 리턴.
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			if (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avg = rs.getDouble("avg");
				String grade = rs.getString("grade");
				student = new Student(id, name, age, kor, eng, math, total, avg, grade);
			}
		} catch (Exception e) {
			System.out.println("select 오류 발생 : " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류 : " + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				System.out.println(" 오류 : " + e.getMessage());
			}
		}
		return student;
	}

	// update
	public int update(Student s) {
		this.connect();
		PreparedStatement ps = null;
		int returnValue = -1;
		String query = "update studentTBL set kor= ?, eng = ?, math = ?, total = ?, avg = ?, grade = ? where id = ?;";

		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, s.getKor());
			ps.setInt(2, s.getEng());
			ps.setInt(3, s.getMath());
			ps.setInt(4, s.getTotal());
			ps.setDouble(5, s.getAvg());
			ps.setString(6, s.getGrade());
			ps.setInt(7, s.getId());
			// 삽입 성공하면 1을 리턴해준다.
			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("insert 오류 발생 : " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류 : " + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				System.out.println(" 오류 : " + e.getMessage());
			}
		}
		return returnValue;
	}

	public ArrayList<Student> selectSort() {
		ArrayList<Student> list = new ArrayList<>();
		this.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "select * from studentTBL order by total desc;";

		try {
			ps = connection.prepareStatement(query);
			// 삽입 성공하면 ResultSet를 리턴, 실패하면 null 리턴.
			rs = ps.executeQuery();
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				int kor = rs.getInt("kor");
				int eng = rs.getInt("eng");
				int math = rs.getInt("math");
				int total = rs.getInt("total");
				double avg = rs.getDouble("avg");
				String grade = rs.getString("grade");
				list.add(new Student(id, name, age, kor, eng, math, total, avg, grade));
			}
		} catch (Exception e) {
			System.out.println("select 오류 발생 : " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류 : " + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				System.out.println(" 오류 : " + e.getMessage());
			}
		}
		return list;
	}

	public int delete(int deleteId) {
		this.connect();
		PreparedStatement ps = null;
		int returnValue = -1;
		String query = "delete from studentTBL where id = ?;";

		try {
			ps = connection.prepareStatement(query);
			ps.setInt(1, deleteId);
			// 삽입 성공하면 1을 리턴해준다.
			returnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("insert 오류 발생 : " + e.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("ps close 오류 : " + e.getMessage());
			}
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				System.out.println(" 오류 : " + e.getMessage());
			}
		}
		return returnValue;
	}
}
