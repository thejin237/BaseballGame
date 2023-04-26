package BaseballGame;

import java.util.*;
import java.util.regex.Pattern;

public class BaseballProcess {
	Scanner scanner = new Scanner(System.in);
	int[] targetA = new int[3];
	int[] targetB = new int[3];
	int[][] baseballA = new int[100][3];
	int[][] baseballB = new int[100][3];
	List<Integer> caseListA = new ArrayList<Integer>();
	List<Integer> caseListB = new ArrayList<Integer>();
	int countA = 0;
	int countB = 0;

	void printMenu() {
		System.out.println("==Baseball Game==");
		System.out.println("**[1] 혼자하기**");
		System.out.println("**[2] 같이하기**");
		System.out.println("**[3] 게임방법**");
		System.out.println("**[4] 게임종료**");
		System.out.print("**선택[ 1 ~ 4 ]:");
	}// printMenu end

	void selectMenu() {
		while (true) {
			printMenu();

			try {
				String menu = scanner.nextLine();
				if (Pattern.matches("\\d{1}", menu)) {
					switch (menu) {
					case "1":
						// Single Game
						System.out.println("혼자하기 모드");
						randomTarget(targetA);
						caseNum(caseListA);
						singlePlay();

						break;
					case "2":
						// Duel Game
						System.out.println("같이하기 모드");
						duelTarget();
						caseNum(caseListA);
						caseNum(caseListB);
						duelPlay();

						break;
					case "3":
						// Help
						printHelp();
						break;
					case "4":
						// Exit Game
						System.out.println("게임종료");
						System.exit(0);

					default:
						throw new Exception();
					}
				} else {
					throw new Exception();
				}
			} catch (Exception e) {
				System.out.println("입력 오류");
				continue;
			} // catch end
		} // while end
	}// selectMenu end

	void randomTarget(int[] target) {
		for (int i = 0; i < target.length; i++) {
			target[i] = (int) (Math.random() * 9) + 1; // To get random number 1 to 9
		}
		if (target[0] == target[1] || target[1] == target[2] || target[0] == target[2]) {
			randomTarget(target);
		}
	}// randomTarget end

	void singlePlay() {
		while (countA < 100) {
			System.out.print("Player Hit:");
			countA = play(baseballA, targetA, caseListA, countA);
		} // while end
		scanner.close();
	}// singlePlay end

	int checkInput(int[][] baseball, int[] target, List<Integer> caseList) {
		int intInput = 0;
		while (true) {
			try {
				String strInput = scanner.nextLine();
				if (!Pattern.matches("\\d{3}", strInput)) {
					throw new Exception();
				} else {
					int[] num = transArr(Integer.parseInt(strInput));
					if (num[0] == 0 || num[1] == 0 || num[2] == 0) {
						System.out.println("숫자 0은 입력할 수 없습니다.");
						System.out.print("다시입력:");
						continue;
					} else if (num[0] == num[1] || num[1] == num[2] || num[2] == num[0]) {
						System.out.println("같은 숫자가 입력되었습니다.");
						System.out.print("다시입력:");
						continue;
					} else {
						intInput = Integer.parseInt(strInput);
						break;
					}
				}
			} catch (Exception e) {
				System.out.println("Input Error");
				System.out.print("다시입력:");
				continue;
			}
		} // while end
		return intInput;
	}// checkInpu end

	int[] transArr(int num) {
		int[] arr = new int[3];
		arr[0] = num / 100;
		arr[1] = num / 10 % 10;
		arr[2] = num % 10;

		return arr;
	}// transArr end

	void duelTarget() {
		System.out.print("Player A 질문숫자입력:");
		int inputA = checkInput(baseballA, targetA, caseListA);
		targetA = transArr(inputA);
		System.out.print("Player B 질문숫자입력:");
		int inputB = checkInput(baseballB, targetB, caseListB);
		targetB = transArr(inputB);
	}// duelTarget end

	void duelPlay() {
		boolean flag = true;
		while (countA < 100 || countB < 100) {
			if (flag) {
				System.out.print("A player Hit:");
				countB = play(baseballB, targetB, caseListB, countB);
				flag = false;
				continue;
			} else {
				System.out.print("B player Hit:");
				countA = play(baseballA, targetA, caseListA, countA);
				flag = true;
				continue;
			}
		} // while end
		scanner.close();
	}// duelPlay end

	/** Game play method */
	int play(int[][] baseball, int[] target, List<Integer> caseList, int count) {
		int hit = checkInput(baseball, target, caseList);

		baseball[count][0] = hit;
		baseball[count][1] = checkStrike(target, hit);
		if (baseball[count][1] == 3) {
			System.out.println("Victory!!");
			selectMenu();
		}
		baseball[count][2] = checkBall(target, hit);
		count++;
		printBaseball(baseball, caseList);
		return count;
	}// play end

	int checkStrike(int[] target, int num) {
		int strike = 0;
		if (target[0] == num / 100) {
			strike++;
		}
		if (target[1] == (num / 10) % 10) {
			strike++;
		}
		if (target[2] == num % 10) {
			strike++;
		}
		return strike;
	}// checkStrike end

	int checkBall(int[] target, int num) {
		int ball = 0;
		int[] numArr = transArr(num);
		if (target[0] == numArr[1] || target[0] == numArr[2]) {
			ball++;
		}
		if (target[1] == numArr[0] || target[1] == numArr[2]) {
			ball++;
		}
		if (target[2] == numArr[0] || target[2] == numArr[1]) {
			ball++;
		}
		return ball;
	}// checkBall end

	/** To print Baseball results */
	void printBaseball(int[][] baseball, List<Integer> caseList) {
		System.out.println("Baseball [Hit, Strike, Ball], [Return]");
		for (int i = 0; i < baseball.length; i++) {
			if (baseball[i][0] == 0) {
				break;
			} else {
				if (i % 4 == 0) {
					System.out.print("[");
				}
				System.out.print("[" + baseball[i][0] + ", ");
				System.out.print(baseball[i][1] + ", ");
				System.out.print(baseball[i][2] + "]");
				if ((i + 1) % 4 == 0) {
					System.out.print("]");
					System.out.println();
				} else {
					System.out.print(", ");
				}
			} // else end
		} // for(i) end
		int result = checkResult(baseball, caseList);
		System.out.print("[" + result + "]");
		System.out.println();
	}// printBaseball end

	void caseNum(List<Integer> caseList) {
		for (int n = 123; n <= 987; n++) {
			int[] num = transArr(n);
			if (num[0] == num[1] || num[1] == num[2] || num[2] == num[0]) {
				continue;
			} else if (num[1] == 0 || num[2] == 0) {
				continue;
			}
			caseList.add(n);
		} // for end
	}// caseNum end

	int checkResult(int[][] baseball, List<Integer> caseList) {
		for (int i = (caseList.size() - 1); i >= 0; i--) {
			int[] caseNum = transArr(caseList.get(i));

			for (int j = 0; j < baseball.length; j++) {
				int strike = 0;
				int ball = 0;
				int[] baseNum = transArr(baseball[j][0]);
				for (int a = 0; a < 3; a++) {
					for (int b = 0; b < 3; b++) {
						if (a == b && caseNum[a] == baseNum[b]) {
							strike++;
							break;
						}
						if (a != b && caseNum[a] == baseNum[b]) {
							ball++;
							break;
						}
					}
				} // for(a) end
				if (baseball[j][1] != strike || baseball[j][2] != ball) {
					caseList.remove(i);
				} // if end
			} // for(j) end
		} // for(i) end
		return caseList.size();
	}// checkResult end

	void printHelp() {
		System.out.println("#게임설명#");
		System.out.println("각자 서로 다른 1~9까지 3자리 임의의 숫자를 정합니다.");
		System.out.println("교대로 서로에게 3자리의 숫자를 불러서 결과를 확인합니다.");
		System.out.println("그리고 그 결과를 토대로 상대가 정한 숫자를 예상한 뒤 맞힙니다.");
		System.out.println("숫자와 위치가 모두 맞을 때는 스트라이크");
		System.out.println("숫자는 맞지만, 위치가 틀렸을 때는 볼");
		System.out.println("숫자와 위치가 모두 틀렸을 때는 아웃");
		System.out.println("모든 자리의 숫자를 먼저 맞히는 사람이 승리하는 게임입니다.");
	}// help end

}
