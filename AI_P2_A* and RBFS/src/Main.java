import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    // تبدیل آرایه دوبعدی به رشته
    public static String arrayToStr(char[][] arr, int puzzleLength) {
        StringBuilder arrNum = new StringBuilder();
        for (int i = 0; i < puzzleLength; i++) {
            for (int j = 0; j < puzzleLength; j++) {
                arrNum.append(arr[i][j]);

            }
        }
        return arrNum.toString();
    }

    /*
    گسترش وضعیت جاری به تمام حالات ممکن
    * در این تابع با بررسی وجود خانه های 4 طرف ممکن
    * تمام حالات ممکن بعدب را می سازد
    */
    public static void extend(int puzzleLength, int h) {
        char[][] array = new char[puzzleLength][puzzleLength];
        for (int i = 0; i < puzzleLength; i++) {
            for (int j = 0; j < puzzleLength; j++) {
                array[i][j] = nodPros.puzzle.array2D[i][j];
            }
        }
        if (nodPros.puzzle.zeroRow > 0) {
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn] = array[nodPros.puzzle.zeroRow - 1][nodPros.puzzle.zeroColumn];
            array[nodPros.puzzle.zeroRow - 1][nodPros.puzzle.zeroColumn] = '0';

            nodPros.nextPNum = arrayToStr(array, puzzleLength);
            Tree child = new Tree(nodPros.nextPNum, nodPros, puzzleLength, h);
            nodPros.pointer.add(child);

            array[nodPros.puzzle.zeroRow - 1][nodPros.puzzle.zeroColumn] = array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn];
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn] = '0';
        }
        if (nodPros.puzzle.zeroRow < puzzleLength - 1) {
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn] = array[nodPros.puzzle.zeroRow + 1][nodPros.puzzle.zeroColumn];
            array[nodPros.puzzle.zeroRow + 1][nodPros.puzzle.zeroColumn] = '0';

            nodPros.nextPNum = arrayToStr(array, puzzleLength);
            Tree child = new Tree(nodPros.nextPNum, nodPros, puzzleLength, h);
            nodPros.pointer.add(child);

            array[nodPros.puzzle.zeroRow + 1][nodPros.puzzle.zeroColumn] = array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn];
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn] = '0';
        }
        if (nodPros.puzzle.zeroColumn > 0) {
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn] = array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn - 1];
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn - 1] = '0';

            nodPros.nextPNum = arrayToStr(array, puzzleLength);
            Tree child = new Tree(nodPros.nextPNum, nodPros, puzzleLength, h);
            nodPros.pointer.add(child);

            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn - 1] = array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn];
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn] = '0';
        }
        if (nodPros.puzzle.zeroColumn < puzzleLength - 1) {
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn] = array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn + 1];
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn + 1] = '0';

            nodPros.nextPNum = arrayToStr(array, puzzleLength);
            Tree child = new Tree(nodPros.nextPNum, nodPros, puzzleLength, h);
            nodPros.pointer.add(child);

            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn + 1] = array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn];
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn] = '0';
        }
    }

    //بررسی اینکه ایا به جواب رسیده ایم یا نه
    public static boolean tst(int dimensions) {
        if (dimensions == 3) {
            return nodPros.pNum.equals("012345678");
        } else return (nodPros.pNum.equals("0123456789ABCDEF"));
    }

    static Tree nodPros;//اشاره گری برای پیمایش نود در حال پردازش
    static Tree treePuzzle;//درخت در حال پیمایش

    public static ArrayList findPath() {
        ArrayList<Tree> path = new ArrayList<>();
        while (nodPros.father != null) {
            path.add(nodPros);
            nodPros = nodPros.father;
        }
        path.add(nodPros);
        return path;
    }

    //تابع اصلی برنامه
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String firstPosition = input.nextLine();//دریافت حالت اولیه از کاربر

        int numberOfEveryNodExtend = 1;
        int dimensions = (int) (Math.sqrt(firstPosition.length()));//محاسبه سطر و ستون مورد نیاز برای پازل
        int depth = 0;//عمقی که جواب در ان می باشد
        ArrayList<Tree> path = new ArrayList<>(); // ذخیره مسیر از ریشه تا هدف
        treePuzzle = new Tree(firstPosition, null, dimensions, 1);//ساخت اولین نود درخت (ریشه)
        ArrayList<Tree> frantier = new ArrayList<>();// ذخیره پیشگامانی که استخراج می کنیم ( ذخیره نسل بعدی برای پردازش)

        frantier.add(treePuzzle);// اضافه کردن ریشه به لیست پیشگامان برای شروع پردازش
        nodPros = treePuzzle;

        //A* with h1
        long start = System.nanoTime();
        while (!tst(dimensions)) {
            extend(dimensions, 1);
            numberOfEveryNodExtend += nodPros.pointer.size();
            frantier.addAll(nodPros.pointer);
            frantier.remove(nodPros);
            nodPros = frantier.get(0);
            for (int i = 0; i < frantier.size(); i++) {
                if (nodPros.fN > frantier.get(i).fN)
                    nodPros = frantier.get(i);
            }
        }
        long end = System.nanoTime();

        //بعد از یافتن جواب باید مسیر را ذخیره کنیم

        // به علت اضافه نشدن نود ریشه باید ریشه را از اخر به صورت دستی اضافه بکنیم

        path = findPath();

        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("we find answer !");
        for (int i = path.size(); i > 0; i--) {
            nodPros = path.get(i - 1);
            nodPros.puzzle.printArray2D();
            System.out.println("------------");
        }
        depth = path.size();
        System.out.print("depth : ");
        System.out.println(depth - 1);
        System.out.print("every Nods extend : ");
        System.out.println(numberOfEveryNodExtend);
        System.out.println("A* with h1 end after :");
        System.out.println(end - start + " nano second");

        //A* with h2
        path.clear();
        frantier.clear();
        treePuzzle = new Tree(firstPosition, null, dimensions, 2);
        frantier.add(treePuzzle);
        nodPros = treePuzzle;
        numberOfEveryNodExtend = 1;
        start = System.nanoTime();
        while (!tst(dimensions)) {
            extend(dimensions, 2);
            numberOfEveryNodExtend += nodPros.pointer.size();
            frantier.addAll(nodPros.pointer);
            frantier.remove(nodPros);
            nodPros = frantier.get(0);
            for (int i = 0; i < frantier.size(); i++) {
                if (nodPros.fN > frantier.get(i).fN)
                    nodPros = frantier.get(i);
            }
        }
        end = System.nanoTime();

        //بعد از یافتن جواب باید مسیر را ذخیره کنیم
        path = findPath();

        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("we find answer !");
        for (int i = path.size(); i > 0; i--) {
            nodPros = path.get(i - 1);
            nodPros.puzzle.printArray2D();
            System.out.println("------------");
        }
        depth = path.size();
        System.out.print("depth : ");
        System.out.println(depth - 1);
        System.out.print("every Nods extend : ");
        System.out.println(numberOfEveryNodExtend);
        System.out.println("A* with h2 end after :");
        System.out.println(end - start + " nano second");

        //////RBFS
//        everyAlternative = new ArrayList();
        path.clear();
        frantier.clear();
        treePuzzle = new Tree(firstPosition, null, dimensions, 2);
        frantier.add(treePuzzle);
        nodPros = treePuzzle;
        numberOfEveryNodExtend = 1;
        while (!tst(dimensions)) {
            extend(dimensions, 2);
            numberOfEveryNodExtend += nodPros.pointer.size();
            frantier.remove(nodPros);
            frantier.addAll(nodPros.pointer);
            Tree bestChild;
            Tree lastChild;
            lastChild = nodPros.pointer.get(0);
            for (int i = 1; i < nodPros.pointer.size(); i++) { // پیدا کردن بهترین فرزند نسل تولید شده
                bestChild = nodPros.pointer.get(i);
                if (lastChild.fN > bestChild.fN) {
                    lastChild = bestChild;
                }
            }
            nodPros = lastChild;
            Tree alternative =nodPros;
            int newF=nodPros.fN;
            for (int i = 0; i < frantier.size(); i++) {
//                if (nodPros.fN > frantier.get(i).fN) {
                    if ( alternative.fN > frantier.get(i).fN) {
//                    newF = nodPros.fN;
                    newF = frantier.get(i).fN;
                    alternative=frantier.get(i);

                }
            }
            while (nodPros.gN != alternative.gN) {
                nodPros = nodPros.father;
                numberOfEveryNodExtend-=nodPros.pointer.size();
                for (int j = 0; j < nodPros.pointer.size(); j++)
                    frantier.remove(nodPros.pointer.get(j));
            }
            nodPros.fN = newF;
            nodPros.pointer.clear();
            nodPros = alternative;
        }

        //بعد از یافتن جواب باید مسیر را ذخیره کنیم

        path = findPath();

        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("we find answer !");
        for (int i = path.size(); i > 0; i--) {
            nodPros = path.get(i - 1);
            nodPros.puzzle.printArray2D();
            System.out.println("------------");
        }
        depth = path.size();
        System.out.print("depth : ");
        System.out.println(depth - 1);
        System.out.print("every Nods extend : ");
        System.out.println(numberOfEveryNodExtend);

    }

    //تابع بازگشتی که ناقصه
    static ArrayList<Tree> everyAlternative;

    public static void rbfs(int length) {
        if (tst(length)) {

        } else {
            extend(length, 2);
            ArrayList<Tree> bestAndAlternative = selectBestChild(nodPros.pointer);
            inputAlternative(bestAndAlternative.get(1));
            if (bestAndAlternative.get(0).fN>everyAlternative.get(0).fN){
                // باید تکمیل بشه
            }else {
                nodPros=bestAndAlternative.get(0);
                rbfs(length);
            }
        }
    }

    public static void inputAlternative(Tree alternative) {
        for (int i = 0; i < everyAlternative.size(); i++) {
            if (everyAlternative.get(i).fN > alternative.fN) everyAlternative.add(i,alternative);
            break;
        }
    }

    public static ArrayList<Tree> selectBestChild(ArrayList<Tree> list) {
        Tree best = list.get(0);
        Tree alternative = list.get(1);
        for (int i = 0; i < list.size(); i++) {
            if (best.fN > list.get(i).fN && alternative.fN > list.get(i).fN) {
                alternative = best;
                best = list.get(0);
            } else if (alternative.fN > list.get(i).fN) {
                alternative = list.get(i);
            }
        }
        ArrayList<Tree> arrayList = new ArrayList<>();
        arrayList.add(best);
        arrayList.add(alternative);
        return arrayList;
    }

}

//کلاس درخت برای ساخت درخت با تمام ویژگی های مورد نیاز
class Tree {
    final Tree father;
    ArrayList<Tree> pointer;
    String pNum;
    String nextPNum;
    Puzzle puzzle;
    int gN;
    int fN;
    int hN;
    Tree findFather;

    public void calculateCost() {
        gN = 0;
        if (findFather != null) {
            for (int i = 1; findFather != null; i++) {
                gN++;
                findFather = findFather.father;
            }
        }
    }

    Tree(String position, Tree lastNode, int puzzleLength, int h) {
        pNum = position;
        father = lastNode;
        findFather = lastNode;
        puzzle = new Puzzle(pNum, puzzleLength);
        pointer = new ArrayList<>();
        calculateCost();
        if (h == 1) hN = puzzle.calculateMovesWithH1();
        else if (h == 2) hN = puzzle.calculateMovesWithH2();
        fN = gN + hN;
    }
}

// کلاس پازل برای ذخیره و پیمایش هر حالت پازل
class Puzzle {
    public char[][] array2D;//for puzzle shape
    public String puzzleNum;//for puzzle value
    public int zeroRow;//for zero row
    public int zeroColumn;//for zero column
    int puzzleLength;// برای اینکه بفهمیم پازل را باید 3در 3 تولید کنیم یا 4 در 4

    //پیدا کردن مختصات عدد 0 یا در واقع همان خانه خالی
    public void findZero() {
        for (int i = 0; i < puzzleLength; i++) {
            for (int j = 0; j < puzzleLength; j++) {
                if (array2D[i][j] == '0') {
                    zeroRow = i;
                    zeroColumn = j;
                }
            }
        }
    }

    public int calculateMovesWithH1() {
        int moves = 0;
        char[][] targetArray1 = {
                {'0', '1', '2'},
                {'3', '4', '5'},
                {'6', '7', '8'}
        };
        char[][] targetArray2 = {
                {'0', '1', '2', '3'},
                {'4', '5', '6', '7'},
                {'8', '9', 'A', 'B'},
                {'D', 'E', 'E', 'F'}
        };
        if (puzzleLength == 3) {
            for (int i = 0; i < puzzleLength; i++) {
                for (int j = 0; j < puzzleLength; j++) {
                    if (array2D[i][j] != targetArray1[i][j]) moves++;
                }
            }
        } else if (puzzleLength == 4) {
            for (int i = 0; i < puzzleLength; i++) {
                for (int j = 0; j < puzzleLength; j++) {
                    if (array2D[i][j] != targetArray2[i][j]) moves++;
                }
            }
        }
        return moves;
    }

    public int calculateMovesWithH2() {
        int moves = 0;

        // ارایه مورد نظر
        char[][] targetArray1 = {
                {'0', '1', '2'},
                {'3', '4', '5'},
                {'6', '7', '8'}
        };
        char[][] targetArray2 = {
                {'0', '1', '2', '3'},
                {'4', '5', '6', '7'},
                {'8', '9', 'A', 'B'},
                {'D', 'E', 'E', 'F'}
        };
        if (puzzleLength == 3) {
            for (int i = 0; i < puzzleLength; i++) {
                for (int j = 0; j < puzzleLength; j++) {
                    char currentNumber = array2D[i][j];

                    // یافتن موقعیت مطابق در ارایه هدف
                    for (int x = 0; x < puzzleLength; x++) {
                        for (int y = 0; y < puzzleLength; y++) {
                            if (targetArray1[x][y] == '0') continue;
                            if (currentNumber == targetArray1[x][y]) {
                                // محاسبه فاصله مطلق بین موقعیت فعلی و موقعیت هدف
                                moves += Math.abs(i - x) + Math.abs(j - y);
                            }
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < puzzleLength; i++) {
                for (int j = 0; j < puzzleLength; j++) {
                    char currentNumber = array2D[i][j];

                    // یافتن موقعیت مطابق در ارایه هدف
                    for (int x = 0; x < puzzleLength; x++) {
                        for (int y = 0; y < puzzleLength; y++) {
                            if (targetArray2[x][y] == '0') continue;
                            if (currentNumber == targetArray2[x][y]) {
                                // محاسبه فاصله مطلق بین موقعیت فعلی و موقعیت هدف
                                moves += Math.abs(i - x) + Math.abs(j - y);
                            }
                        }
                    }
                }
            }
        }

        return moves;
    }

    public Puzzle(String number, int puzzleLength) {
        puzzleNum = number;
        this.puzzleLength = puzzleLength;
        array2D = new char[puzzleLength][puzzleLength];
        int index = 0;
        for (int i = 0; i < puzzleLength; i++) {
            for (int j = 0; j < puzzleLength; j++) {
                array2D[i][j] = number.charAt(index);
                index++;
            }
        }
        findZero();
    }

    //چاپ کردن پازل
    public void printArray2D() {
        for (int i = 0; i < puzzleLength; i++) {
            for (int j = 0; j < puzzleLength; j++) {
                System.out.print(array2D[i][j] + " ");
            }
            System.out.println();
        }
    }
}