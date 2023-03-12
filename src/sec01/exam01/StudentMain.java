package sec01.exam01;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentMain {

	public static Scanner sc = new Scanner(System.in);
	public static final int INPUT = 1, PRINT = 2, ANLYZE = 3, SEARCH = 4, UPDATE = 5, SORT = 6, DELETE = 7, EXIT = 8;

	public static void main(String[] args) {
		// 지역변수선언
		boolean run = true;
		int no = 0;
		ArrayList<Student> list = new ArrayList<>();
		DBConnection dbCon = new DBConnection();
		// 무한루트
		while (run) {
			System.out.println("============================================================");
			System.out.println("1.입력 | 2.출력 | 3.분석 | 4.검색 | 5.수정 | 6.정렬 | 7.삭제 | 8.종료");
			System.out.println("============================================================");
			System.out.print(">>");
			no = Integer.parseInt(sc.nextLine());
			switch (no) {
			case INPUT:
				// 생성자를 선택 해야됨. 이름,나이,국어,영어,수학
				Student student = inputDataStudent();
				// 데이터베이스 입력
				int rValue = dbCon.insert(student);
				if (rValue == 1) {
					System.out.println("삽입 성공");
				} else {
					System.out.println("삽입 실패");
				}
				break;
			case PRINT:
				ArrayList<Student> list2 = dbCon.select();
				if (list == null) {
					System.out.println("select 실패");
				} else {
					printStudent(list2);
				}
				break;
			case ANLYZE:
				// 분석: 이름, 나이, 총점, 평균, 등급
				ArrayList<Student> list3 = dbCon.analyzeSelect();
				printAnalyze(list3);
				break;
			case SEARCH:
				// 학생검색 이름받기
				String dataName = searchStudent();
				ArrayList<Student> list4 = dbCon.nameSearchSelect(dataName);
				if (list4.size() >= 1) {
					System.out.println(list4);
				} else {
					System.out.println("학생 검색 결과 오류입니다.");
				}
				break;
			case UPDATE:
				// 학생검색 점수를 수정해서 저장
				int updateReturnValue = 0;
				int id = inputId();
				Student stu = dbCon.selectId(id);
				if (stu == null) {
					System.out.println("수정 오류 발생 ");
				} else {
					Student updateStudent = updateStudent(stu);
					updateReturnValue = dbCon.update(updateStudent);
				}
				if (updateReturnValue == 1) {
					System.out.println("update 완료");
				} else {
					System.out.println("update 실패");
				}
				break;
			case SORT:
				ArrayList<Student> list5 = dbCon.selectSort();
				if (list5 == null) {
					System.out.println("정렬 실패");
				} else {
					printStudent(list5);
				}
//				Collections.sort(list, Collections.reverseOrder());
//				sortStudent(list);
				break;
			case DELETE:
				// 학번(id) 검색
				int deleteId = inputId();
				int deleteReturnValue = dbCon.delete(deleteId);
				if (deleteReturnValue == 1) {
					System.out.println("삭제 성공");
				} else {
					System.out.println("삭제 실패");
				}
				break;
			case EXIT:
				run = false;
				break;
			}
		} // end of while

		System.out.println("The End");
	}// end of main
		// id 입력

	private static int inputId() {
		boolean run = true;
		int id = 0;
		System.out.print("Id 입력(숫자) : ");
		while (run) {
			try {
				id = Integer.parseInt(sc.nextLine());
				if (id > 0 && id < Integer.MAX_VALUE) {
					run = false;
				}
			} catch (Exception e) {
				System.out.println("id 입력 오류 : " + e.getMessage());
			}

		}
		return id;
	}
	// 학생 정보 수정

	private static Student updateStudent(Student stu) {
		int kor = inputScoreSubject(stu.getName(), stu.getKor(), "국어");
		stu.setKor(kor);
		int eng = inputScoreSubject(stu.getName(), stu.getEng(), "영어");
		stu.setEng(eng);
		int math = inputScoreSubject(stu.getName(), stu.getMath(), "수학");
		stu.setMath(math);

		stu.calTotal();
		stu.calAvg();
		stu.calGrade();
		System.out.println(stu);
		return stu;
	}
	// 과목별 성적 수정

	private static int inputScoreSubject(String name, int score, String subject) {
		boolean run = true;
		int data = 0;
		while (run) {
			System.out.print(name + " " + subject + " " + score + ">>");
			try {
				data = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(data));
				if (matcher.find() && data > 0 && data <= 100) {
					run = false;
				} else {
					System.out.println("입력값의 범위를 벗어났습니다.");
				}
			} catch (Exception e) {
				System.out.println("점수 입력에 오류 방생");
				data = 0;
			}
		}
		return data;
	}
	// 이름 입력 패턴 검색

	private static String matchingNamePattern() {
		String name = null;
		while (true) {

			try {
				System.out.print("검색할 학생이름: ");
				name = sc.nextLine();
				Pattern pattern = Pattern.compile("^[가-힣]{2,4}$");
				Matcher matcher = pattern.matcher(name);
				if (!matcher.find()) {
					System.out.println("이름 입력에 오류가 발생했습니다. 다시재입력요청합니다.");
				} else {
					break;
				}
			} catch (Exception e) {
				System.out.println("입력에서 오류가 발생했습니다.");
				break;
			}
		}
		return name;
	}
	// 학생 검색

	private static String searchStudent() {
		String name = null;
		boolean flag = true;
		name = matchingNamePattern();
		while (flag) {
			System.out.print("검색한 학생이름: ");

			Pattern pattern = Pattern.compile("^[가-힣]{2,4}$");
			Matcher matcher = pattern.matcher(name);
			if (!matcher.find()) {
				System.out.println("이름입력오류발생 다시재입력요청합니다.");
			} else {
				flag = false;
				break;
			}
		}
		return name;
	}
	// 분석

	private static void printAnalyze(ArrayList<Student> list) {
		for (Student data : list) {
			System.out.println(data.getId() + "\t" + data.getName() + "\t" + data.getAge() + "\t" + data.getTotal()
					+ "\t" + String.format("%.2f", data.getAvg()) + "\t" + data.getGrade());
		}
	}
	// 학생 정보 출력

	private static void printStudent(ArrayList<Student> list) {
		System.out.println("번호 \t 이름 \t나이\t국어\t영어\t수학\t총점\t평균\t등급");
		for (Student data : list) {
			System.out.println(data);
		}
	}
	// 학생 정보 입력

	private static Student inputDataStudent() {
		String name = inputName();
		int age = inputAge();
		int kor = inputScore("국어");
		int eng = inputScore("영어");
		int math = inputScore("수학");
		Student student = new Student(name, age, kor, eng, math);
		student.calTotal();
		student.calAvg();
		student.calGrade();
		return student;
	}
	// 이름 입력

	private static String inputName() {
		String name = null;
		while (true) {
			try {
				System.out.println("이름 :");
				name = sc.nextLine();
				Pattern pattern = Pattern.compile("^[가-힣]{2,4}$");
				Matcher matcher = pattern.matcher(String.valueOf(name));
				if (matcher.find()) {
					break;
				} else {
					System.out.println("이름를 잘못입력하였습니다. 재입력요청");
				}
			} catch (NumberFormatException e) {
				System.out.println("입력오류발생했습니다.");
				name = null;
				break;
			}

		}
		return name;
	}
	// 과목별 점수 입력

	private static int inputScore(String string) {
		int score = 0;
		while (true) {
			try {
				System.out.println(string + "점수 :");
				score = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(score));
				if (matcher.find() && score <= 100) {
					break;
				} else {
					System.out.println("점수를 잘못입력하였습니다. 재입력요청");
				}

			} catch (NumberFormatException e) {
				System.out.println("입력오류발생했습니다.");
				score = 0;
				break;
			}

		}
		return score;
	}
	// 나이 입력

	private static int inputAge() {
		int age = 0;
		while (true) {
			try {
				System.out.println("나이 : ");
				age = Integer.parseInt(sc.nextLine());
				Pattern pattern = Pattern.compile("^[0-9]{1,3}$");
				Matcher matcher = pattern.matcher(String.valueOf(age));
				if (matcher.find() && age <= 100) {
					break;
				} else {
					System.out.println("나이를 잘못입력하였습니다. 재입력요청");
				}

			} catch (NumberFormatException e) {
				System.out.println("나이입력 오류 발생");
				age = 0;
				break;
			}
		}
		return age;
	}

}
